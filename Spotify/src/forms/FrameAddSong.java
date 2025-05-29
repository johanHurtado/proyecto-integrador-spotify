package forms;


import javax.swing.*;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import entities.*;


public class FrameAddSong extends JFrame {

    private JTextField txtTitle, txtDuration;
    private JTextArea txtDescription;
    private JComboBox<Artist> cbArtist;
    private JComboBox<Gender> cbGenre;
    private JLabel lblCoverPath;
    private byte[] coverBytes;

    public FrameAddSong(List<Artist> artistas, List<Gender> generos) {
        setTitle("Agregar Canción");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(0, 1));

        txtTitle = new JTextField();
        txtDuration = new JTextField();
        txtDescription = new JTextArea(3, 20);
        cbArtist = new JComboBox<>(artistas.toArray(new Artist[0]));
        cbGenre = new JComboBox<>(generos.toArray(new Gender[0]));
        lblCoverPath = new JLabel("Sin imagen seleccionada");

        JButton btnSelectImage = new JButton("Seleccionar portada");
        btnSelectImage.addActionListener(this::seleccionarImagen);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(this::guardarCancion);

        add(new JLabel("Título:"));
        add(txtTitle);
        add(new JLabel("Duración (minutos):"));
        add(txtDuration);
        add(new JLabel("Descripción:"));
        add(new JScrollPane(txtDescription));
        add(new JLabel("Artista:"));
        add(cbArtist);
        add(new JLabel("Género:"));
        add(cbGenre);
        add(btnSelectImage);
        add(lblCoverPath);
        add(btnGuardar);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void seleccionarImagen(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            lblCoverPath.setText(file.getName());
            coverBytes = new SongDAO().imageToByteArray(file);
        }
    }

    private void guardarCancion(ActionEvent e) {
        try {
            String titulo = txtTitle.getText();
            String descripcion = txtDescription.getText();
            double duracion = Double.parseDouble(txtDuration.getText());
            Artist artista = (Artist) cbArtist.getSelectedItem();
            Gender genero = (Gender) cbGenre.getSelectedItem();

            Song nueva = new Song(0, titulo, descripcion, duracion, artista, genero, coverBytes);
            boolean exito = new SongDAO().addSong(nueva);

            JOptionPane.showMessageDialog(this, exito ? "Canción guardada correctamente." : "Error al guardar la canción.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // Puedes probarlo así desde tu método main:
    public static void main(String[] args) {
        List<Artist> artistas = new ArtistDAO().getAllArtists();
        List<Gender> generos = new GenderDAO().getAllGenders();
        new FrameAddSong(artistas, generos);
    }
}
