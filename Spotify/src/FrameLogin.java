import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FrameLogin extends JFrame {
    private BufferedImage backgroundImage;
    private Point initialClick;

    public FrameLogin() {
        setUndecorated(true);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
            }
        });

        try {
            backgroundImage = ImageIO.read(getClass().getClassLoader().getResource("resources/Spoti.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        RoundedPanel titleBar = new RoundedPanel(20);
        titleBar.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 7));
        titleBar.setBackground(new Color(35, 35, 35));
        titleBar.setPreferredSize(new Dimension(1280, 30));

        JButton closeBtn = createMacCircle(Color.RED);
        closeBtn.addActionListener(e -> System.exit(0));

        JButton minimizeBtn = createMacCircle(Color.ORANGE);
        minimizeBtn.addActionListener(e -> setState(Frame.ICONIFIED));

        JButton maximizeBtn = createMacCircle(Color.GREEN);
        maximizeBtn.addActionListener(e -> {
            if (getExtendedState() == Frame.MAXIMIZED_BOTH) {
                setExtendedState(Frame.NORMAL);
            } else {
                setExtendedState(Frame.MAXIMIZED_BOTH);
            }
        });

        titleBar.add(closeBtn);
        titleBar.add(minimizeBtn);
        titleBar.add(maximizeBtn);

        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
        });

        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (getExtendedState() != Frame.NORMAL) setExtendedState(Frame.NORMAL);
                int thisX = getLocation().x;
                int thisY = getLocation().y;
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;
                setLocation(thisX + xMoved, thisY + yMoved);
            }
        });

        JPanel overlay = new JPanel(new GridBagLayout());
        overlay.setOpaque(true);
        overlay.setBackground(new Color(0, 0, 0, 160));

        RoundedPanel loginPanel = new RoundedPanel(20);
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(Color.BLACK);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        loginPanel.setPreferredSize(new Dimension(400, 300));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        logoPanel.setOpaque(false);

        try {
            BufferedImage logoImg = ImageIO.read(getClass().getClassLoader().getResource("resources/icono_spotify.png"));
            JLabel logoIcon = new JLabel(new ImageIcon(logoImg.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
            JLabel logoText = new JLabel("Spotify");
            logoText.setFont(new Font("Arial", Font.BOLD, 32));
            logoText.setForeground(Color.WHITE);
            logoPanel.add(logoIcon);
            logoPanel.add(logoText);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel texto = new JLabel("<html><center>Millones de canciones.<br>Gratis en Spotify.</center></html>", JLabel.CENTER);
        texto.setFont(new Font("Arial", Font.BOLD, 20));
        texto.setForeground(Color.WHITE);
        texto.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginBtn = new JButton("Iniciar sesión") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        loginBtn.setBackground(new Color(30, 215, 96));
        loginBtn.setForeground(Color.BLACK);
        loginBtn.setFocusPainted(false);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setOpaque(false);
        loginBtn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        loginBtn.setFont(new Font("Arial", Font.BOLD, 16));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> {
            new FrameInicioSesion();
            dispose();
        });

        JLabel eresNuevo = new JLabel("¿Eres nuevo en Spotify?");
        eresNuevo.setFont(new Font("Arial", Font.PLAIN, 13));
        eresNuevo.setForeground(Color.LIGHT_GRAY);
        eresNuevo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel registrarTexto = new JLabel("Registrarse gratis");
        registrarTexto.setFont(new Font("Arial", Font.BOLD, 13));
        registrarTexto.setForeground(new Color(30, 215, 96));
        registrarTexto.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registrarTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Efecto visual y acción para abrir FrameRegistro
        registrarTexto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registrarTexto.setFont(registrarTexto.getFont().deriveFont(Font.BOLD | Font.ITALIC));
                registrarTexto.setForeground(new Color(102, 255, 178)); // Color brillante
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registrarTexto.setFont(new Font("Arial", Font.BOLD, 13));
                registrarTexto.setForeground(new Color(30, 215, 96));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                new FrameRegistro(); // Abre el frame de registro
                dispose(); // Cierra el frame actual
            }
        });

        loginPanel.add(logoPanel);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(texto);
        loginPanel.add(Box.createVerticalStrut(30));
        loginPanel.add(loginBtn);
        loginPanel.add(Box.createVerticalStrut(5));
        loginPanel.add(eresNuevo);
        loginPanel.add(Box.createVerticalStrut(5));
        loginPanel.add(registrarTexto);

        overlay.add(loginPanel);
        backgroundPanel.add(titleBar, BorderLayout.NORTH);
        backgroundPanel.add(overlay, BorderLayout.CENTER);

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    private JButton createMacCircle(Color color) {
        return new JButton() {
            {
                setPreferredSize(new Dimension(14, 14));
                setContentAreaFilled(false);
                setFocusPainted(false);
                setBorderPainted(false);
                setOpaque(false);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0, 0, getWidth(), getHeight());
            }
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FrameLogin::new);
    }

    class RoundedPanel extends JPanel {
        private int cornerRadius;

        public RoundedPanel(int radius) {
            super();
            this.cornerRadius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcs.width, arcs.height);
        }
    }
}
