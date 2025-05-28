package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

class RoundedPanel extends JPanel {
    private int cornerRadius;

    public RoundedPanel(int radius) {
        this.cornerRadius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.dispose();
    }
}

class RoundButton extends JButton {
    public RoundButton(Color color) {
        setPreferredSize(new Dimension(14, 14));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setBackground(color);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
        g2.dispose();
    }
}

class IconButton extends JButton {
    private Color defaultBg = new Color(30, 30, 30);
    private Color hoverBg = new Color(60, 60, 60);

    public IconButton(String text) {
        super(text);
        setFocusPainted(false);
        setBackground(defaultBg);
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(hoverBg);
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(defaultBg);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.dispose();
        super.paintComponent(g);
    }
}

public class SpotifyCloneUI extends JFrame {

    public SpotifyCloneUI() {
        setTitle("Spotify - Clone");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUndecorated(true);

        Color background = new Color(25, 25, 25);
        Color panelColor = new Color(35, 35, 35);
        Color textColor = Color.WHITE;

        getContentPane().setBackground(background);

        // Título estilo Mac
        RoundedPanel titleBar = new RoundedPanel(20);
        titleBar.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 7));
        titleBar.setBackground(panelColor);
        titleBar.setPreferredSize(new Dimension(1280, 30));

        RoundButton closeBtn = new RoundButton(new Color(255, 95, 86));
        closeBtn.addActionListener(e -> System.exit(0));
        RoundButton minBtn = new RoundButton(new Color(255, 189, 46));
        minBtn.addActionListener(e -> setState(Frame.ICONIFIED));
        RoundButton maxBtn = new RoundButton(new Color(39, 201, 63));
        maxBtn.addActionListener(e -> {
            if (getExtendedState() == Frame.MAXIMIZED_BOTH) {
                setExtendedState(Frame.NORMAL);
            } else {
                setExtendedState(Frame.MAXIMIZED_BOTH);
            }
        });

        titleBar.add(closeBtn);
        titleBar.add(minBtn);
        titleBar.add(maxBtn);

        // Menu Bar Container
        RoundedPanel menuBarContainer = new RoundedPanel(40);
        menuBarContainer.setLayout(new BorderLayout());
        menuBarContainer.setBackground(new Color(25, 25, 25));
        menuBarContainer.setPreferredSize(new Dimension(1200, 60));

        // Logo Apple Music a la izquierda
        JLabel logo = new JLabel(new ImageIcon("resources/logo_music_resized.png"));

        logo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        menuBarContainer.add(logo, BorderLayout.WEST);

        // --- CENTRAR CON GRIDBAG ---
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        String[] items = { "For You", "Browse", "Videos", "Radio", "Library", "Now Playing" };
        ButtonGroup navGroup = new ButtonGroup();
        Color defaultBg = new Color(30, 30, 30);
        Color selectedBg = new Color(90, 90, 90);
        Color hoverBg = new Color(60, 60, 60);

        JPanel buttonGroupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        buttonGroupPanel.setOpaque(false);

        for (String item : items) {
            JToggleButton button = new JToggleButton(item) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getModel().isSelected() ? selectedBg : getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            button.setFocusPainted(false);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("SansSerif", Font.BOLD, 13));
            button.setContentAreaFilled(false);
            button.setOpaque(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
            button.setBackground(item.equals("For You") ? selectedBg : defaultBg);
            button.setSelected(item.equals("For You"));

            button.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (!button.isSelected())
                        button.setBackground(hoverBg);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (!button.isSelected())
                        button.setBackground(defaultBg);
                }
            });

            button.addActionListener(e -> {
                for (Component comp : buttonGroupPanel.getComponents()) {
                    if (comp instanceof JToggleButton btn) {
                        btn.setBackground(btn.isSelected() ? selectedBg : defaultBg);
                    }
                }
            });

            navGroup.add(button);
            buttonGroupPanel.add(button);
        }

        centerWrapper.add(buttonGroupPanel);

        // Botones a la derecha
        IconButton searchBtn = new IconButton("🔍");
        IconButton settingsBtn = new IconButton("⚙");

        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        iconPanel.setOpaque(false);
        iconPanel.add(searchBtn);
        iconPanel.add(settingsBtn);

        menuBarContainer.add(centerWrapper, BorderLayout.CENTER);
        menuBarContainer.add(iconPanel, BorderLayout.EAST);

        // TopPanel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(panelColor);
        topPanel.add(titleBar, BorderLayout.NORTH);
        topPanel.add(menuBarContainer, BorderLayout.SOUTH);

        // Panel Central
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        centerPanel.setBackground(background);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (int i = 0; i < 4; i++) {
            RoundedPanel mixPanel = new RoundedPanel(30); // 30 = radio de redondeo
            mixPanel.setLayout(new BorderLayout());
            mixPanel.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));

            mixPanel.setBackground(panelColor);
            mixPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel title = new JLabel("Mix " + (i + 1));
            title.setForeground(textColor);
            title.setFont(new Font("SansSerif", Font.BOLD, 16));

            JLabel subtitle = new JLabel("Updated Today");
            subtitle.setForeground(Color.GRAY);
            subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
            RoundedPanel header = new RoundedPanel(20); // Radio menor para este header
            header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
            header.setBackground(new Color(40, 40, 40)); // Ligeramente más claro para destacar
            header.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Espaciado interno
            header.add(title);
            header.add(subtitle);
            

            JPanel coverPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            coverPanel.setBackground(panelColor);

            for (int j = 0; j < 4; j++) {
                JLabel cover = new JLabel();
                cover.setOpaque(true);
                cover.setBackground(Color.DARK_GRAY);
                cover.setPreferredSize(new Dimension(100, 100));
                ImageIcon icon = new ImageIcon(getClass().getResource("/resources/icono_spotify.png"));
                Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                cover.setIcon(new ImageIcon(scaled));

                coverPanel.add(cover);
            }

            mixPanel.add(header, BorderLayout.NORTH);
            mixPanel.add(coverPanel, BorderLayout.CENTER);
            centerPanel.add(mixPanel);
        }

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
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