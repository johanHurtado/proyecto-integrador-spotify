package forms;

import DAO.*;
import entities.entities.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;

/**
 * Swing frame that lets the user create, update and delete songs
 * (with MP3 stored as a BLOB).
 */
public class SongCRUDFrame extends JFrame {

    private JTextField  txtTitle, txtDuration;
    private JTextArea   txtDescription;
    private JLabel      lblMp3Chosen;
    private JTable      table;
    private byte[]      mp3Bytes;          // temporary buffer
    private final SongDAO dao = new SongDAO();

    public SongCRUDFrame() {
        setTitle("Song Management");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        loadTable();
    }

    private void initUI() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        txtTitle       = new JTextField(25);
        txtDuration    = new JTextField(10);
        txtDescription = new JTextArea(3,25);
        JScrollPane scrollDesc = new JScrollPane(txtDescription);

        lblMp3Chosen = new JLabel("No file selected");
        JButton btnChooseMp3 = new JButton("Choose MP3…");
        btnChooseMp3.addActionListener(e -> chooseMp3());

        // Row 0
        gbc.gridx=0; gbc.gridy=0; form.add(new JLabel("Title:"), gbc);
        gbc.gridx=1; form.add(txtTitle, gbc);
        // Row 1
        gbc.gridx=0; gbc.gridy=1; form.add(new JLabel("Duration (sec):"), gbc);
        gbc.gridx=1; form.add(txtDuration, gbc);
        // Row 2
        gbc.gridx=0; gbc.gridy=2; gbc.anchor = GridBagConstraints.NORTH;
        form.add(new JLabel("Description:"), gbc);
        gbc.gridx=1; gbc.anchor = GridBagConstraints.CENTER;
        form.add(scrollDesc, gbc);
        // Row 3
        gbc.gridx=0; gbc.gridy=3; form.add(new JLabel("MP3 file:"), gbc);
        gbc.gridx=1; form.add(btnChooseMp3, gbc);
        // Row 4
        gbc.gridy=4; form.add(lblMp3Chosen, gbc);

        // CRUD buttons
        JButton btnSave     = new JButton("Save");
        JButton btnUpdate   = new JButton("Update");
        JButton btnDelete   = new JButton("Delete");
        JButton btnClear    = new JButton("Clear");

        btnSave.addActionListener   (e -> saveSong());
        btnUpdate.addActionListener (e -> updateSong());
        btnDelete.addActionListener (e -> deleteSong());
        btnClear.addActionListener  (e -> clearForm());

        JPanel buttons = new JPanel();
        buttons.add(btnSave);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);
        buttons.add(btnClear);

        // Table
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) loadSelectedRow();
            }
        });
        JScrollPane scrollTable = new JScrollPane(table);

        // Layout
        setLayout(new BorderLayout(8,8));
        add(form,   BorderLayout.NORTH);
        add(scrollTable, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    /*=================== Action methods ===================*/

    private void chooseMp3() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                mp3Bytes = Files.readAllBytes(fc.getSelectedFile().toPath());
                lblMp3Chosen.setText(fc.getSelectedFile().getName());
            } catch (IOException ex) {
                showError("I/O error: " + ex.getMessage());
            }
        }
    }

    private void saveSong() {
        try {
            dao.insert(readForm(-1));
            loadTable();
            clearForm();
        } catch (SQLException ex) {
            showError(ex.getMessage());
        }
    }

    private void updateSong() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a row first.");
            return;
        }
        int id = (int) table.getValueAt(row, 0);
        try {
            dao.update(readForm(id));
            loadTable();
            clearForm();
        } catch (SQLException ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteSong() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a row first.");
            return;
        }
        int id = (int) table.getValueAt(row, 0);
        int op = JOptionPane.showConfirmDialog(this,
                "Delete selected song?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            try {
                dao.delete(id);
                loadTable();
                clearForm();
            } catch (SQLException ex) {
                showError(ex.getMessage());
            }
        }
    }

    private Song readForm(int id) {
        Song s = new Song();
        s.setId(id);
        s.setTitle(txtTitle.getText().trim());
        s.setDescription(txtDescription.getText().trim());
        s.setDuration(Double.parseDouble(txtDuration.getText().trim()));
        s.setArtistId(1); // ⚠️ Replace with a JComboBox later
        s.setGenreId (1); // ⚠️ Replace with a JComboBox later
        s.setCoverArt(null);          // Add cover upload if needed
        s.setMp3Bytes(mp3Bytes);
        return s;
    }

    private void loadSelectedRow() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        txtTitle.setText((String) table.getValueAt(row, 1));
        txtDescription.setText((String) table.getValueAt(row, 2));
        txtDuration.setText(String.valueOf(table.getValueAt(row, 3)));
        lblMp3Chosen.setText("(keeping existing MP3)");
        mp3Bytes = null; // leave null to keep current BLOB
    }

    private void clearForm() {
        txtTitle.setText("");
        txtDescription.setText("");
        txtDuration.setText("");
        lblMp3Chosen.setText("No file selected");
        mp3Bytes = null;
        table.clearSelection();
    }

    private void loadTable() {
        try {
            List<Song> list = dao.findAll();
            DefaultTableModel m = new DefaultTableModel(
                    new Object[]{"ID","Title","Description","Duration","Artist","Genre"}, 0);
            for (Song s : list) {
                m.addRow(new Object[]{
                        s.getId(),
                        s.getTitle(),
                        s.getDescription(),
                        s.getDuration(),
                        s.getArtistId(),
                        s.getGenreId()
                });
            }
            table.setModel(m);
        } catch (SQLException ex) {
            showError(ex.getMessage());
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /*========================= Test launcher =========================*/
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SongCRUDFrame().setVisible(true));
    }
}
