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

        // --- TÍTULO: Botones estilo Mac ---
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

        // --- BARRA DE NAVEGACIÓN SECUNDARIA (basada en imagen) ---
        // --- BARRA DE NAVEGACIÓN CENTRADA Y REDONDEADA ---
        RoundedPanel menuBarContainer = new RoundedPanel(40);
        menuBarContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        menuBarContainer.setBackground(new Color(25, 25, 25));
        menuBarContainer.setPreferredSize(new Dimension(1200, 60)); // altura total del contenedor

        RoundedPanel menuBar = new RoundedPanel(40);
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        menuBar.setBackground(new Color(30, 30, 30));
        menuBar.setPreferredSize(new Dimension(800, 40)); // ancho reducido y centrado visualmente
        menuBar.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        String[] items = { "For You", "Browse", "Videos", "Radio", "Library", "Now Playing" };
        ButtonGroup navGroup = new ButtonGroup();
Color defaultBg = new Color(30, 30, 30);
Color selectedBg = new Color(90, 90, 90);
Color hoverBg = new Color(60, 60, 60);

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
    button.setContentAreaFilled(false); // para evitar el fondo por defecto
    button.setOpaque(false);            // fondo lo pintamos nosotros
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    button.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
    button.setBackground(item.equals("For You") ? selectedBg : defaultBg);
    button.setSelected(item.equals("For You"));

    // Hover
    button.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            if (!button.isSelected()) {
                button.setBackground(hoverBg);
            }
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent evt) {
            if (!button.isSelected()) {
                button.setBackground(defaultBg);
            }
        }
    });

    // Cambio de estado dinámico
    button.addActionListener(e -> {
        for (Component comp : menuBar.getComponents()) {
            if (comp instanceof JToggleButton) {
                JToggleButton btn = (JToggleButton) comp;
                btn.setBackground(btn.isSelected() ? selectedBg : defaultBg);
            }
        }
    });

    navGroup.add(button);
    menuBar.add(button);
}

        
        JButton searchBtn = new JButton("🔍");
        JButton settingsBtn = new JButton("⚙");
        for (JButton iconBtn : new JButton[] { searchBtn, settingsBtn }) {
            iconBtn.setFocusPainted(false);
            iconBtn.setBackground(new Color(30, 30, 30));
            iconBtn.setForeground(Color.WHITE);
            iconBtn.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
            menuBar.add(iconBtn);
        }

        menuBarContainer.add(menuBar);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(panelColor);
        topPanel.add(titleBar, BorderLayout.NORTH);
        topPanel.add(menuBarContainer, BorderLayout.SOUTH);

        

        // --- PANEL CENTRAL: Álbumes ---
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(background);
        centerPanel.setLayout(new GridLayout(2, 2, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (int i = 0; i < 4; i++) {
            JPanel mixPanel = new JPanel();
            mixPanel.setBackground(panelColor);
            mixPanel.setLayout(new BorderLayout());
            mixPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel title = new JLabel("Mix " + (i + 1));
            title.setForeground(textColor);
            title.setFont(new Font("SansSerif", Font.BOLD, 16));

            JLabel subtitle = new JLabel("Updated Today");
            subtitle.setForeground(Color.GRAY);
            subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));

            JPanel header = new JPanel();
            header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
            header.setBackground(panelColor);
            header.add(title);
            header.add(subtitle);

            JPanel coverPanel = new JPanel();
            coverPanel.setLayout(new GridLayout(2, 2, 5, 5));
            coverPanel.setBackground(panelColor);

            for (int j = 0; j < 4; j++) {
                JLabel cover = new JLabel();
                cover.setOpaque(true);
                cover.setBackground(Color.DARK_GRAY);
                cover.setPreferredSize(new Dimension(100, 100));
                cover.setIcon(new ImageIcon("img/cover.png"));
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