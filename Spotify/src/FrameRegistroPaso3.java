import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FrameRegistroPaso3 extends JFrame {
    private Point initialClick;

    public FrameRegistroPaso3() {
        setUndecorated(true);
        setSize(500, 700);
        setMinimumSize(new Dimension(500, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
            }
        });

        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 7));
        titleBar.setBackground(new Color(35, 35, 35));
        titleBar.setPreferredSize(new Dimension(500, 30));

        JButton closeBtn = createMacCircle(Color.RED);
        closeBtn.addActionListener(e -> System.exit(0));
        JButton minimizeBtn = createMacCircle(Color.ORANGE);
        minimizeBtn.addActionListener(e -> setState(Frame.ICONIFIED));
        JButton maximizeBtn = createMacCircle(Color.GREEN);
        maximizeBtn.addActionListener(e -> setExtendedState(Frame.MAXIMIZED_BOTH));

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
                int thisX = getLocation().x;
                int thisY = getLocation().y;
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;
                setLocation(thisX + xMoved, thisY + yMoved);
            }
        });

        JPanel content = new JPanel();
        content.setBackground(new Color(18, 18, 18));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel logoLabel = new JLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            BufferedImage logo = ImageIO.read(getClass().getClassLoader().getResource("resources/icono_spotify.png"));
            logoLabel.setIcon(new ImageIcon(logo.getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        } catch (IOException ex) {
            logoLabel.setText("Spotify");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 30));
            logoLabel.setForeground(Color.WHITE);
        }

        JPanel navPanel = new JPanel();
        navPanel.setBackground(new Color(18, 18, 18));
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.X_AXIS));
        navPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton backBtn = new JButton("<");
        backBtn.setFont(new Font("Arial", Font.BOLD, 18));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBackground(new Color(18, 18, 18));
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            dispose();
            new FrameRegistroPaso2();
        });

        JLabel paso = new JLabel("Paso 3 de 3");
        paso.setFont(new Font("Arial", Font.PLAIN, 14));
        paso.setForeground(Color.LIGHT_GRAY);
        paso.setAlignmentX(Component.LEFT_ALIGNMENT);

        navPanel.add(backBtn);
        navPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        navPanel.add(paso);

        JLabel titulo = new JLabel("Términos y Condiciones");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JCheckBox publicidad = new JCheckBox("Prefiero no recibir publicidad de Spotify");
        JCheckBox compartirDatos = new JCheckBox("Compartir mis datos de registro con los proveedores de contenido de Spotify para fines de marketing.");
        for (JCheckBox cb : new JCheckBox[]{publicidad, compartirDatos}) {
            cb.setForeground(Color.WHITE);
            cb.setBackground(new Color(18, 18, 18));
            cb.setFocusPainted(false);
            cb.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        JLabel legal1 = new JLabel("<html><font color='white'>Al registrarte, aceptas los <a href='#'><font color='#1DB954'>Términos y Condiciones de Uso</font></a> de Spotify.</font></html>");
        JLabel legal2 = new JLabel("<html><font color='white'>Al registrarte, aceptas la <a href='#'><font color='#1DB954'>Política de Privacidad</font></a> de Spotify.</font></html>");
        legal1.setAlignmentX(Component.LEFT_ALIGNMENT);
        legal2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton registrarseBtn = new JButton("Registrarte") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        registrarseBtn.setBackground(new Color(30, 215, 96));
        registrarseBtn.setForeground(Color.BLACK);
        registrarseBtn.setFocusPainted(false);
        registrarseBtn.setFont(new Font("Arial", Font.BOLD, 14));
        registrarseBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registrarseBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        registrarseBtn.setContentAreaFilled(false);
        registrarseBtn.setOpaque(false);

        registrarseBtn.addActionListener(e -> {
            JFrame mensaje = new JFrame();
            JPanel panel = new JPanel();
            panel.setBackground(new Color(18, 18, 18));
            JLabel label = new JLabel("¡Registro completado exitosamente!");
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            panel.add(label);
            mensaje.getContentPane().add(panel);
            mensaje.setSize(300, 100);
            mensaje.setLocationRelativeTo(null);
            mensaje.setUndecorated(true);
            mensaje.setShape(new RoundRectangle2D.Double(0, 0, 300, 100, 20, 20));
            mensaje.setVisible(true);
            Timer timer = new Timer(1500, ev -> {
                mensaje.dispose();
                new FrameInicioSesion();
                dispose();
            });
            timer.setRepeats(false);
            timer.start();
        });

        content.add(logoLabel);
        content.add(Box.createVerticalStrut(20));
        content.add(navPanel);
        content.add(Box.createVerticalStrut(10));
        content.add(titulo);
        content.add(Box.createVerticalStrut(20));
        content.add(publicidad);
        content.add(Box.createVerticalStrut(10));
        content.add(compartirDatos);
        content.add(Box.createVerticalStrut(30));
        content.add(legal1);
        content.add(Box.createVerticalStrut(5));
        content.add(legal2);
        content.add(Box.createVerticalStrut(30));
        content.add(registrarseBtn);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(18, 18, 18));
        mainPanel.add(titleBar, BorderLayout.NORTH);
        mainPanel.add(content, BorderLayout.CENTER);

        setContentPane(mainPanel);
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
}
