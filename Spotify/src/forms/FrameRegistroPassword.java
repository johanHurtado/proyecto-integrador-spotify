package forms;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FrameRegistroPassword extends JFrame {
    private Point initialClick;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private JLabel checkLetra, checkEspecial, checkLongitud;

    public FrameRegistroPassword() {
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
            new FrameRegistro();
        });

        JLabel paso = new JLabel("Paso 1 de 3");
        paso.setFont(new Font("Arial", Font.PLAIN, 14));
        paso.setForeground(Color.LIGHT_GRAY);
        paso.setAlignmentX(Component.LEFT_ALIGNMENT);

        navPanel.add(backBtn);
        navPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        navPanel.add(paso);

        JLabel titulo = new JLabel("Crea una contraseña");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel contraseñaLabel = new JLabel("Contraseña");
        contraseñaLabel.setForeground(Color.WHITE);
        contraseñaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setBackground(Color.BLACK);
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        passwordField.setOpaque(false);

        passwordField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validarRequisitos();
            }
        });

        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setVisible(false);

        JLabel requisitos = new JLabel("La contraseña debe tener al menos");
        requisitos.setFont(new Font("Arial", Font.BOLD, 12));
        requisitos.setForeground(Color.WHITE);
        requisitos.setAlignmentX(Component.CENTER_ALIGNMENT);

        checkLetra = new JLabel("● 1 letra");
        checkEspecial = new JLabel("● 1 número o carácter especial (ejemplo: #, ?, ! o &)");
        checkLongitud = new JLabel("● 10 caracteres");

        for (JLabel label : new JLabel[]{checkLetra, checkEspecial, checkLongitud}) {
            label.setFont(new Font("Arial", Font.PLAIN, 13));
            label.setForeground(Color.LIGHT_GRAY);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

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
            String pwd = new String(passwordField.getPassword());
            if (pwd.length() >= 10 && pwd.matches(".*[a-zA-Z].*") && pwd.matches(".*[0-9!@#$%^&*()_+=?-].*")) {
                errorLabel.setVisible(false);
                new FrameRegistroPaso2();
                dispose();
            } else {
                errorLabel.setText("\u26A0 Contraseña inválida. Usa mínimo 10 caracteres, una letra y un símbolo o número.");
                errorLabel.setVisible(true);
            }
        });

        content.add(logoLabel);
        content.add(Box.createVerticalStrut(20));
        content.add(navPanel);
        content.add(Box.createVerticalStrut(10));
        content.add(titulo);
        content.add(Box.createVerticalStrut(30));
        content.add(contraseñaLabel);
        content.add(passwordField);
        content.add(Box.createVerticalStrut(10));
        content.add(errorLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(requisitos);
        content.add(checkLetra);
        content.add(checkEspecial);
        content.add(checkLongitud);
        content.add(Box.createVerticalStrut(30));
        content.add(siguienteBtn);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(18, 18, 18));
        mainPanel.add(titleBar, BorderLayout.NORTH);
        mainPanel.add(content, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private void validarRequisitos() {
        String pwd = new String(passwordField.getPassword());
        checkLetra.setForeground(pwd.matches(".*[a-zA-Z].*") ? new Color(30, 215, 96) : Color.LIGHT_GRAY);
        checkEspecial.setForeground(pwd.matches(".*[0-9!@#$%^&*()_+=?-].*") ? new Color(30, 215, 96) : Color.LIGHT_GRAY);
        checkLongitud.setForeground(pwd.length() >= 10 ? new Color(30, 215, 96) : Color.LIGHT_GRAY);
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
