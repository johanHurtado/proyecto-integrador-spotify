import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FrameRegistro extends JFrame {
    private Point initialClick;
    private JLabel errorLabel;
    private JTextField emailField;

    public FrameRegistro() {
        setUndecorated(true);
        setSize(500, 700);
        setMinimumSize(new Dimension(500, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));

        addComponentListener(new ComponentAdapter() {
            @Override
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
        content.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel logoLabel = new JLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            BufferedImage logo = ImageIO.read(getClass().getClassLoader().getResource("resources/icono_spotify.png"));
            logoLabel.setIcon(new ImageIcon(logo.getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        } catch (IOException ex) {
            logoLabel.setText("Spotify");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 30));
            logoLabel.setForeground(Color.WHITE);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
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
            new FrameLogin(); // Suponiendo que este es el frame anterior
        });

        navPanel.add(backBtn);

        JLabel heading = new JLabel("<html><div style='text-align: center;'>Regístrate<br>para empezar<br>a escuchar<br>contenido</div></html>");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setForeground(Color.WHITE);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel emailLabel = new JLabel("Dirección de email");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        emailField.setBackground(Color.BLACK);
        emailField.setForeground(Color.WHITE);
        emailField.setCaretColor(Color.WHITE);
        emailField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        emailField.setOpaque(false);

        errorLabel = new JLabel();
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setVisible(false);

        JButton siguienteBtn = new JButton("Siguiente") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        siguienteBtn.setBackground(new Color(30, 215, 96));
        siguienteBtn.setForeground(Color.BLACK);
        siguienteBtn.setFocusPainted(false);
        siguienteBtn.setFont(new Font("Arial", Font.BOLD, 14));
        siguienteBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        siguienteBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        siguienteBtn.setContentAreaFilled(false);
        siguienteBtn.setOpaque(false);

        siguienteBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty() || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                emailField.setBorder(BorderFactory.createLineBorder(Color.RED));
                errorLabel.setText("\u26A0 Este email no es válido, debe tener un formato así ejemplo@email.com");
                errorLabel.setVisible(true);
            } else {
                emailField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                errorLabel.setVisible(false);
                dispose();
                new FrameRegistroPassword();
            }
        });

        content.add(logoLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(navPanel);
        content.add(Box.createVerticalStrut(10));
        content.add(heading);
        content.add(Box.createVerticalStrut(30));
        content.add(emailLabel);
        content.add(emailField);
        content.add(Box.createVerticalStrut(5));
        content.add(errorLabel);
        content.add(Box.createVerticalStrut(30));
        content.add(siguienteBtn);

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
