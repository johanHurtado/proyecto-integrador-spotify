import javax.swing.*;
import java.awt.*;

public class SpotifyCloneUI extends JFrame {

    public SpotifyCloneUI() {
        setTitle("Spotify - Clone");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Colores base estilo Spotify
        Color background = new Color(18, 18, 18);
        Color panelColor = new Color(24, 24, 24);
        Color textColor = Color.WHITE;
        Color accentGreen = new Color(30, 215, 96);

        getContentPane().setBackground(background);

        // PANEL IZQUIERDO: Biblioteca
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(panelColor);
        leftPanel.setPreferredSize(new Dimension(220, getHeight()));

        JLabel libraryTitle = new JLabel("Tu biblioteca");
        libraryTitle.setForeground(textColor);
        libraryTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        libraryTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton createBtn = new JButton("+ Crear");
        createBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel libraryHeader = new JPanel();
        libraryHeader.setLayout(new BorderLayout());
        libraryHeader.setBackground(panelColor);
        libraryHeader.add(libraryTitle, BorderLayout.WEST);
        libraryHeader.add(createBtn, BorderLayout.EAST);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(panelColor);

        String[] items = {
                "Tus me gusta", "Blessd", "Anuel AA", "Ovy On The Drums",
                "GYM - TEMAZOS MOTIVACION 2025", "BlessDeluxury", "SI SABE",
                "Your Top Songs 2023", "Saber Amar"
        };

        for (String item : items) {
            JLabel label = new JLabel(item);
            label.setForeground(textColor);
            label.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
            listPanel.add(label);
        }

        JScrollPane scrollLeft = new JScrollPane(listPanel);
        scrollLeft.setBorder(null);
        scrollLeft.getViewport().setBackground(panelColor);

        leftPanel.add(libraryHeader);
        leftPanel.add(scrollLeft);

        // PANEL CENTRAL: Recomendado / Lanzamientos
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(background);

        JLabel featured = new JLabel("Best of Morat", JLabel.CENTER);
        featured.setForeground(textColor);
        featured.setFont(new Font("SansSerif", Font.BOLD, 24));
        featured.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton playBtn = new JButton("Reproducir");
        playBtn.setBackground(accentGreen);
        playBtn.setForeground(Color.BLACK);
        playBtn.setFocusPainted(false);

        JButton followBtn = new JButton("Seguir");

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(background);
        btnPanel.add(playBtn);
        btnPanel.add(followBtn);

        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(featured);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(btnPanel);

        // PANEL DERECHO: Info del álbum
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(panelColor);
        rightPanel.setPreferredSize(new Dimension(300, getHeight()));

        JLabel nowPlaying = new JLabel("Saber Amar", JLabel.CENTER);
        nowPlaying.setForeground(textColor);
        nowPlaying.setFont(new Font("SansSerif", Font.BOLD, 18));
        nowPlaying.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel artist = new JLabel("Penyair", JLabel.CENTER);
        artist.setForeground(textColor);

        rightPanel.add(nowPlaying);
        rightPanel.add(artist);

        // PANEL INFERIOR: Controles de reproducción
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(panelColor);
        bottomPanel.setPreferredSize(new Dimension(getWidth(), 60));

        JButton prevBtn = new JButton("⏮");
        JButton playPauseBtn = new JButton("▶");
        JButton nextBtn = new JButton("⏭");

        bottomPanel.add(prevBtn);
        bottomPanel.add(playPauseBtn);
        bottomPanel.add(nextBtn);

        // Añadir paneles al frame
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public static void mostrarInterfaz() {
        SwingUtilities.invokeLater(() -> {
            SpotifyCloneUI ui = new SpotifyCloneUI();
            ui.setVisible(true);
        });
    }

    public static void main(String[] args) {
        mostrarInterfaz();
    }
}
