package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import entities.ArtistDAO;
import entities.GenderDAO;
import entities.Artist;
import entities.Gender;

public class FrameAdmin extends JFrame {

    public FrameAdmin() {
        setUndecorated(true); // Eliminar barra nativa

        setTitle("Admin Panel");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de botones estilo macOS
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topBar.setBackground(new Color(30, 30, 30));

        JButton btnClose = createMacControlButton(Color.RED);
        btnClose.addActionListener(e -> System.exit(0));

        JButton btnMinimize = createMacControlButton(Color.YELLOW);
        btnMinimize.addActionListener(e -> setState(JFrame.ICONIFIED));

        JButton btnMaximize = createMacControlButton(Color.GREEN);
        btnMaximize.addActionListener(e -> setExtendedState(JFrame.MAXIMIZED_BOTH));

        topBar.add(btnClose);
        topBar.add(btnMinimize);
        topBar.add(btnMaximize);

        add(topBar, BorderLayout.NORTH);

        // Panel lateral estilo Spotify
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(18, 18, 18));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));

        JLabel lblLogo = new JLabel("  \uD83C\uDFB5 Spotify", JLabel.LEFT);
        lblLogo.setForeground(new Color(30, 215, 96));
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLogo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebar.add(lblLogo);

        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(createSidebarButton("➕ Add Song"));
        sidebar.add(createSidebarButton("👤 Manage Users"));
        sidebar.add(createSidebarButton("📊 Reports"));

        add(sidebar, BorderLayout.WEST);

        // Panel central
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("Admin Panel", JLabel.CENTER);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));

        mainPanel.add(lblTitle);

        JPanel buttons = new JPanel(new GridLayout(2, 3, 20, 20));
        buttons.setBackground(Color.BLACK);
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 100, 10, 100));

        buttons.add(createMainButton("Add Song", e -> abrirAgregarCancion()));
        buttons.add(createMainButton("Read Songs", e -> JOptionPane.showMessageDialog(this, "Leer canciones...")));
        buttons.add(createMainButton("Update Song", e -> JOptionPane.showMessageDialog(this, "Actualizar canción...")));
        buttons.add(createMainButton("Delete Song", e -> JOptionPane.showMessageDialog(this, "Eliminar canción...")));
        buttons.add(createMainButton("Manage Users", e -> JOptionPane.showMessageDialog(this, "Función en desarrollo")));
        buttons.add(createMainButton("Reports", e -> JOptionPane.showMessageDialog(this, "Función en desarrollo")));

        mainPanel.add(buttons);

        add(mainPanel, BorderLayout.CENTER);
        getContentPane().setBackground(Color.BLACK);

        setVisible(true);
    }

    private JButton createMacControlButton(Color color) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(14, 14));
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(color.darker()));
        btn.setOpaque(true);
        return btn;
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setBackground(new Color(25, 25, 25));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        return btn;
    }

    private JButton createMainButton(String text, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setBackground(new Color(40, 40, 40));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(180, 60));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60), 1, true),
            BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));
        btn.addActionListener(action);
        return btn;
    }

    private void abrirAgregarCancion() {
        List<Artist> artistas = new ArtistDAO().getAllArtists();
        List<Gender> generos = new GenderDAO().getAllGenders();
        new FrameAddSong(artistas, generos);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FrameAdmin::new);
    }
}

