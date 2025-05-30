package forms;

import DAO.UserDAO;
import entities.User;                  // ⚠ ajusta a tu paquete real

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FrameInicioSesion extends JFrame {

    private JLabel errorMessage;
    private Point initialClick;

    public FrameInicioSesion() {

        /* ---------- Ventana y barra mac-style ---------- */
        setUndecorated(true);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
            }
        });

        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 7));
        titleBar.setBackground(new Color(35, 35, 35));
        titleBar.setPreferredSize(new Dimension(500, 30));

        JButton closeBtn    = createMacCircle(Color.RED);
        JButton minimizeBtn = createMacCircle(Color.ORANGE);
        JButton maximizeBtn = createMacCircle(Color.GREEN);
        closeBtn   .addActionListener(e -> System.exit(0));
        minimizeBtn.addActionListener(e -> setState(Frame.ICONIFIED));
        maximizeBtn.addActionListener(e -> setExtendedState(Frame.MAXIMIZED_BOTH));

        titleBar.add(closeBtn); titleBar.add(minimizeBtn); titleBar.add(maximizeBtn);

        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { initialClick = e.getPoint(); }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int dx = e.getX() - initialClick.x;
                int dy = e.getY() - initialClick.y;
                setLocation(getX() + dx, getY() + dy);
            }
        });

        /* ---------- Panel principal ---------- */
        JPanel content = new JPanel();
        content.setBackground(new Color(18, 18, 18));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        /* Logo */
        JLabel logoLabel = new JLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            BufferedImage logo = ImageIO.read(getClass().getClassLoader()
                                              .getResource("resources/icono_spotify.png"));
            logoLabel.setIcon(new ImageIcon(logo.getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        } catch (IOException ex) {
            logoLabel.setText("Spotify");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 30));
            logoLabel.setForeground(Color.WHITE);
        }

        JLabel heading = new JLabel("Inicia sesión en Spotify");
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setForeground(Color.WHITE);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        /* ---------- Campos ---------- */
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        styleField(userField);  styleField(passField);

        /* ---------- Botón Login ---------- */
        JButton loginButton = new JButton("Iniciar sesión");
        loginButton.setBackground(new Color(30, 215, 96));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        /* ---------- mensaje de error ---------- */
        errorMessage = new JLabel("⚠ Correo o contraseña incorrectos.");
        errorMessage.setForeground(Color.WHITE);
        errorMessage.setBackground(new Color(187, 0, 0));
        errorMessage.setOpaque(true);
        errorMessage.setFont(new Font("Arial", Font.BOLD, 14));
        errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
        errorMessage.setVisible(false);
        errorMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

        /* ---------- Acción login ---------- */
        loginButton.addActionListener(e -> {
            String correo = userField.getText().trim();
            String clave  = new String(passField.getPassword()).trim();

            User u = new UserDAO().authenticate(correo, clave);   // ← método propuesto abajo
            if (u != null) {
                errorMessage.setVisible(false);
                dispose();
                new SpotifyCloneUI();     // o FrameAdmin() según rol
            } else {
                errorMessage.setVisible(true);
            }
        });

        /* ---------- Enlace registro ---------- */
        JLabel lblReg = new JLabel("¿No tienes cuenta? ");
        lblReg.setForeground(Color.LIGHT_GRAY);
        JLabel registro = new JLabel("Regístrate en Spotify");
        registro.setForeground(new Color(30, 215, 96));
        registro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registro.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { new FrameRegistro1(); }
        });

        /* ---------- Ensamblar ---------- */
        content.add(Box.createVerticalStrut(30));
        content.add(logoLabel);
        content.add(Box.createVerticalStrut(15));
        content.add(heading);
        content.add(Box.createVerticalStrut(30));

        content.add(makeLabel("Email o nombre de usuario"));
        content.add(userField);
        content.add(Box.createVerticalStrut(10));
        content.add(makeLabel("Contraseña"));
        content.add(passField);
        content.add(Box.createVerticalStrut(20));
        content.add(loginButton);
        content.add(Box.createVerticalStrut(10));
        content.add(errorMessage);
        content.add(Box.createVerticalStrut(30));

        JPanel regPanel = new JPanel();
        regPanel.setOpaque(false);
        regPanel.add(lblReg); regPanel.add(registro);
        regPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(regPanel);

        setLayout(new BorderLayout());
        add(titleBar, BorderLayout.NORTH);
        add(content , BorderLayout.CENTER);
        setVisible(true);
    }

    /* ---------- helpers de UI ---------- */
    private void styleField(JTextField f) {
        f.setBackground(Color.BLACK);
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }
    private JLabel makeLabel(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.WHITE);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }
   /* ---------- circulito estilo macOS ---------- */
private JButton createMacCircle(Color color) {
    return new JButton() {
        /* bloque de inicialización del botón */
        {
            setPreferredSize(new Dimension(14, 14));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
        }

        /* pintamos el círculo */
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillOval(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    };
}


    /* -------------------- main de prueba -------------------- */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FrameInicioSesion::new);
    }
}
