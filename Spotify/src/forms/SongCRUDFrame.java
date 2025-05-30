package forms;

// ─── Ajusta a tus paquetes reales ──────────────────────────────────
import DAO.ArtistDAO;
import DAO.GenderDAO;      // o GenreDAO
import DAO.SongDAO;

import entities.*;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ventana CRUD para canciones (MP3 + portada).
 */
public class SongCRUDFrame extends JFrame {

    // ─── Componentes ───────────────────────────────────────────────
    private JTextField  txtTitle, txtDuration;
    private JTextArea   txtDescription;
    private JComboBox<Artist> cbArtist;
    private JComboBox<Gender> cbGenre;
    private JLabel lblMp3Chosen, lblCoverChosen;
    private JTable table;

    // ─── Buffers temporales ────────────────────────────────────────
    private byte[] mp3Bytes;
    private byte[] coverBytes;

    // ─── DAO ───────────────────────────────────────────────────────
    private final SongDAO   songDao   = new SongDAO();
    private final ArtistDAO artistDao = new ArtistDAO();
    private final GenderDAO genreDao  = new GenderDAO();

    // ─── Mapas para mostrar nombres / obtener ids ──────────────────
    private final Map<Integer,String> artistMap = new HashMap<>();
    private final Map<Integer,String> genreMap  = new HashMap<>();

    // Recuerda la última carpeta que abrió JFileChooser
    private File lastDir = new File(System.getProperty("user.home"));

    public SongCRUDFrame() {
        setTitle("Gestión de Canciones");
        setSize(950, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        loadTable();
    }

    /* ------------------------------------------------------------------
       Construcción de la interfaz
     -----------------------------------------------------------------*/
    private void initUI() {

        // ── Listas desde la base de datos ──────────────────────────
        List<Artist> artistas = artistDao.getAllArtists();
        List<Gender> generos  = genreDao .getAllGenders();

        artistas.forEach(a -> artistMap.put(a.getId(),        a.getName()));        // ⚠ getId/getName
        generos .forEach(g -> genreMap .put(g.getIdGender(),  g.getNameGender()));  // ⚠ getters

        cbArtist = new JComboBox<>(artistas.toArray(new Artist[0]));
        cbGenre  = new JComboBox<>(generos .toArray(new Gender[0]));
        cbArtist.setRenderer(nameRenderer());
        cbGenre .setRenderer(nameRenderer());

        // ── Campos de texto ───────────────────────────────────────
        txtTitle       = new JTextField(25);
        txtDuration    = new JTextField(10);
        txtDescription = new JTextArea(3, 25);
        JScrollPane scrollDesc = new JScrollPane(txtDescription);

        // ── Selección de archivos ─────────────────────────────────
        lblMp3Chosen   = new JLabel("Sin MP3 seleccionado");
        JButton btnMp3 = new JButton("Elegir MP3…");
        btnMp3.addActionListener(e -> chooseFile(true));

        lblCoverChosen = new JLabel("Sin portada seleccionada");
        JButton btnImg = new JButton("Elegir portada…");
        btnImg.addActionListener(e -> chooseFile(false));

        // ── Formulario (GridBag) ──────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        int y = 0;
        addGB(form,new JLabel("Título:"),        gbc,0,y); addGB(form,txtTitle,        gbc,1,y++);
        addGB(form,new JLabel("Duración (seg):"),gbc,0,y); addGB(form,txtDuration,     gbc,1,y++);
        gbc.anchor=GridBagConstraints.NORTH;
        addGB(form,new JLabel("Descripción:"),   gbc,0,y); addGB(form,scrollDesc,      gbc,1,y++);
        gbc.anchor=GridBagConstraints.CENTER;
        addGB(form,new JLabel("Artista:"),       gbc,0,y); addGB(form,cbArtist,        gbc,1,y++);
        addGB(form,new JLabel("Género:"),        gbc,0,y); addGB(form,cbGenre,         gbc,1,y++);
        addGB(form,new JLabel("Archivo MP3:"),   gbc,0,y); addGB(form,btnMp3,          gbc,1,y++);
        addGB(form,lblMp3Chosen,                 gbc,1,y++);
        addGB(form,new JLabel("Portada:"),       gbc,0,y); addGB(form,btnImg,          gbc,1,y++);
        addGB(form,lblCoverChosen,               gbc,1,y++);

        // ── Botones CRUD ──────────────────────────────────────────
        JButton btnSave   = new JButton("Guardar");
        JButton btnUpdate = new JButton("Actualizar");
        JButton btnDelete = new JButton("Eliminar");
        JButton btnClear  = new JButton("Limpiar");

        btnSave  .addActionListener(e -> saveSong());
        btnUpdate.addActionListener(e -> updateSong());
        btnDelete.addActionListener(e -> deleteSong());
        btnClear .addActionListener(e -> clearForm());

        JPanel btns = new JPanel();
        btns.add(btnSave); btns.add(btnUpdate); btns.add(btnDelete); btns.add(btnClear);

        // ── Tabla ────────────────────────────────────────────────
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) loadSelectedRow();
            }
        });
        JScrollPane scrollTable = new JScrollPane(table);

        // ── Layout principal ─────────────────────────────────────
        setLayout(new BorderLayout(8,8));
        add(form,        BorderLayout.NORTH);
        add(scrollTable, BorderLayout.CENTER);
        add(btns,        BorderLayout.SOUTH);
    }

    /* ---------- Helper GridBag ---------- */
    private void addGB(JPanel p, Component c, GridBagConstraints gbc,int x,int y){
        gbc.gridx=x; gbc.gridy=y; p.add(c,gbc);
    }

    /* ---------- Renderer que muestra solo el nombre ---------- */
    private <T> DefaultListCellRenderer nameRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Artist a) setText(a.getName());
                if (value instanceof Gender g) setText(g.getNameGender()); // ⚠ getter
                return this;
            }
        };
    }

    /* =================== Selección de archivos =================== */
    private void chooseFile(boolean mp3) {
        JFileChooser fc = new JFileChooser(lastDir);               // abre en la última carpeta
        if (mp3) fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("MP3", "mp3"));
        else     fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "png","jpg","jpeg"));

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            lastDir = f.getParentFile();                           // guarda la carpeta
            try {
                byte[] bytes = Files.readAllBytes(f.toPath());
                if (mp3) { mp3Bytes   = bytes; lblMp3Chosen  .setText(f.getName()); }
                else     { coverBytes = bytes; lblCoverChosen.setText(f.getName()); }
            } catch (IOException ex) { showError("Error al leer archivo: " + ex.getMessage()); }
        }
    }

    /* =================== CRUD =================== */
    private void saveSong()   { try { songDao.insert(readForm(-1)); loadTable(); clearForm(); }
                               catch (Exception ex) { showError(ex.getMessage()); } }

    private void updateSong() {
        int row = table.getSelectedRow();
        if (row == -1) { showError("Selecciona una fila."); return; }
        int id = (int) table.getValueAt(row, 0);
        try { songDao.update(readForm(id)); loadTable(); clearForm(); }
        catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void deleteSong() {
        int row = table.getSelectedRow();
        if (row == -1) { showError("Selecciona una fila."); return; }
        int id = (int) table.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this,"¿Eliminar la canción?", "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try { songDao.delete(id); loadTable(); clearForm(); }
            catch (Exception ex) { showError(ex.getMessage()); }
        }
    }

    /* ---------- Convierte formulario a Song ---------- */
    private Song readForm(int id) {
        Song s = new Song();
        s.setId(id);
        s.setTitle(txtTitle.getText().trim());
        s.setDescription(txtDescription.getText().trim());
        s.setDuration(Double.parseDouble(txtDuration.getText().trim()));
        s.setArtistId(((Artist) cbArtist.getSelectedItem()).getId());
        s.setGenreId (((Gender) cbGenre .getSelectedItem()).getIdGender()); // ⚠ ajusta getter
        s.setCoverArt(coverBytes);
        s.setMp3Bytes(mp3Bytes);
        return s;
    }

    /* ---------- Rellena formulario al hacer doble clic ---------- */
    private void loadSelectedRow() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        txtTitle      .setText((String) table.getValueAt(row, 1));
        txtDescription.setText((String) table.getValueAt(row, 2));
        txtDuration   .setText(String.valueOf(table.getValueAt(row, 3)));

        String artistName = (String) table.getValueAt(row, 4);
        String genreName  = (String) table.getValueAt(row, 5);

        // Selecciona el índice que tenga ese nombre
        selectComboByName(cbArtist, artistName);
        selectComboByName(cbGenre,  genreName);

        lblMp3Chosen  .setText("(mantener MP3 actual)");
        lblCoverChosen.setText("(mantener portada actual)");
        mp3Bytes = null; coverBytes = null;
    }

    private <T> void selectComboByName(JComboBox<T> combo, String name) {
        for (int i=0;i<combo.getItemCount();i++) {
            T obj = combo.getItemAt(i);
            if (obj instanceof Artist a && a.getName().equals(name)) { combo.setSelectedIndex(i); break; }
            if (obj instanceof Gender g && g.getNameGender().equals(name)) { combo.setSelectedIndex(i); break; }
        }
    }

    /* ---------- Limpia formulario ---------- */
    private void clearForm() {
        txtTitle.setText(""); txtDescription.setText(""); txtDuration.setText("");
        cbArtist.setSelectedIndex(0); cbGenre.setSelectedIndex(0);
        lblMp3Chosen.setText("Sin MP3 seleccionado");
        lblCoverChosen.setText("Sin portada seleccionada");
        mp3Bytes = null; coverBytes = null;
        table.clearSelection();
    }

    /* ---------- Carga la tabla ---------- */
    private void loadTable() {
        try {
            List<Song> list = songDao.findAll();
            DefaultTableModel m = new DefaultTableModel(
                new Object[]{"ID","Título","Descripción","Duración","Artista","Género"},0);
            for (Song s : list) {
                m.addRow(new Object[]{
                    s.getId(), s.getTitle(), s.getDescription(), s.getDuration(),
                    artistMap.getOrDefault(s.getArtistId(),"—"),
                    genreMap .getOrDefault(s.getGenreId () ,"—")
                });
            }
            table.setModel(m);
        } catch (SQLException ex) { showError(ex.getMessage()); }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /* ---------- Main (prueba rápida) ---------- */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SongCRUDFrame().setVisible(true));
    }
}
