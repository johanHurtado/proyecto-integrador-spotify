package forms;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import DAO.UserDAO;
import entities.User;

public class FrameInicioSesion extends JFrame {
    private JLabel errorMessage;
    private Point initialClick;

    public FrameInicioSesion() {
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

        JPanel content = new JPanel();
        content.setBackground(new Color(18, 18, 18));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

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

        JLabel heading = new JLabel("Inicia sesión en Spotify");
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setForeground(Color.WHITE);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(Box.createVerticalStrut(30));
        content.add(logoLabel);
        content.add(Box.createVerticalStrut(15));
        content.add(heading);
        content.add(Box.createVerticalStrut(30));

        JLabel userLabel = new JLabel("Email o nombre de usuario");
        userLabel.setForeground(Color.WHITE);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField userField = new JTextField();
        userField.setBackground(Color.BLACK);
        userField.setForeground(Color.WHITE);
        userField.setCaretColor(Color.WHITE);
        userField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        userField.setOpaque(true);

        JLabel passLabel = new JLabel("Contraseña");
        passLabel.setForeground(Color.WHITE);
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField passField = new JPasswordField();
        passField.setBackground(Color.BLACK);
        passField.setForeground(Color.WHITE);
        passField.setCaretColor(Color.WHITE);
        passField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passField.setOpaque(true);

        JButton loginButton = new JButton("Iniciar sesión");
        loginButton.setBackground(new Color(30, 215, 96));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        loginButton.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();

    // 👉 Creamos un nuevo usuario
            User nuevoUsuario = new User();
            nuevoUsuario.setNombreUsuario(user);
            nuevoUsuario.setCorreo(user); // Suponiendo que se usa el email como username
            nuevoUsuario.setClaveHash(pass);
            nuevoUsuario.setIdRol(2);    // ID 2 = Usuario normal (puedes ajustar según tus roles)

            boolean exito = new UserDAO().insert(nuevoUsuario);

            if (exito) {
            errorMessage.setVisible(false);
            dispose();

            new SpotifyCloneUI(); // Cambiar a FrameAdmin() si lo deseas
            } else {
            errorMessage.setText("⚠ No se pudo registrar el usuario.");
            errorMessage.setVisible(true);
            }

            });

        JLabel register = new JLabel("¿No tienes cuenta? ");
        register.setFont(new Font("Arial", Font.PLAIN, 13));
        register.setForeground(Color.LIGHT_GRAY);
        register.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel registerLink = new JLabel("Regístrate en Spotify");
        registerLink.setFont(new Font("Arial", Font.BOLD, 13));
        registerLink.setForeground(new Color(30, 215, 96));
        registerLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new FrameRegistro();
            }
        });

        content.add(userLabel);
        content.add(userField);
        content.add(Box.createVerticalStrut(10));
        content.add(passLabel);
        content.add(passField);
        content.add(Box.createVerticalStrut(20));
        content.add(loginButton);

        errorMessage = new JLabel("⚠ Nombre de usuario o contraseña incorrectos.");
        errorMessage.setForeground(Color.WHITE);
        errorMessage.setBackground(new Color(187, 0, 0));
        errorMessage.setOpaque(true);
        errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
        errorMessage.setFont(new Font("Arial", Font.BOLD, 14));
        errorMessage.setVisible(false);
        errorMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorMessage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        content.add(Box.createVerticalStrut(10));
        content.add(errorMessage);
        content.add(Box.createVerticalStrut(20));
        content.add(register);
        content.add(registerLink);

        add(titleBar, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
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
