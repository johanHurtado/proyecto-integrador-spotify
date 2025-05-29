package forms;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FrameRegistroPaso2 extends JFrame {
    private Point initialClick;

    public FrameRegistroPaso2() {
        setUndecorated(true);
        setSize(500, 700);
        setMinimumSize(new Dimension(500, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));

        // Reajustar la forma al redimensionar
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
            }
        });

        // Barra de título
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 7));
        titleBar.setBackground(new Color(35, 35, 35));
        titleBar.setPreferredSize(new Dimension(500, 30));
        JButton closeBtn = createMacCircle(Color.RED);
        closeBtn.addActionListener(e -> System.exit(0));
        JButton minimizeBtn = createMacCircle(Color.ORANGE);
        minimizeBtn.addActionListener(e -> setState(Frame.ICONIFIED));
        JButton maximizeBtn = createMacCircle(Color.GREEN);
        maximizeBtn.addActionListener(e -> {
            int st = getExtendedState() == Frame.MAXIMIZED_BOTH ? Frame.NORMAL : Frame.MAXIMIZED_BOTH;
            setExtendedState(st);
        });
        titleBar.add(closeBtn);
        titleBar.add(minimizeBtn);
        titleBar.add(maximizeBtn);
        titleBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = e.getX() - initialClick.x;
                int dy = e.getY() - initialClick.y;
                setLocation(getX() + dx, getY() + dy);
            }
        });

        // Contenido principal
        JPanel content = new JPanel();
        content.setBackground(new Color(18, 18, 18));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Logo Spotify
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

        // Navegación paso
        JPanel navPanel = new JPanel();
        navPanel.setBackground(new Color(18, 18, 18));
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.X_AXIS));
        navPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JButton backBtn = new JButton("<");
        backBtn.setFont(new Font("Arial", Font.BOLD, 18));
        backBtn.setForeground(Color.WHITE);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            dispose();
            new FrameRegistroPassword().setVisible(true);
        });
        JLabel paso = new JLabel("Paso 2 de 4");
        paso.setFont(new Font("Arial", Font.PLAIN, 14));
        paso.setForeground(Color.LIGHT_GRAY);
        navPanel.add(backBtn);
        navPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        navPanel.add(paso);

        // Encabezados y campos
        JLabel titulo = new JLabel("Cuéntanos sobre ti");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nombreLabel = new JLabel("Nombre");
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 13));
        nombreLabel.setForeground(Color.WHITE);
        nombreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField nombreField = createRoundedTextField("");

        JLabel subtitulo = new JLabel("Este nombre aparecerá en tu perfil");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitulo.setForeground(Color.GRAY);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fecha de nacimiento
        JLabel fechaLabel = new JLabel("Fecha de nacimiento");
        fechaLabel.setFont(new Font("Arial", Font.BOLD, 13));
        fechaLabel.setForeground(Color.WHITE);
        fechaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel fechaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        fechaPanel.setBackground(new Color(18, 18, 18));
        JTextField diaField = createRoundedTextField("dd");
        diaField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (diaField.getText().equals("dd"))
                    diaField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (diaField.getText().isEmpty())
                    diaField.setText("dd");
            }
        });
        JComboBox<String> mesBox = new JComboBox<>(new String[] {
                "Mes", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        });
        mesBox.setMaximumSize(new Dimension(150, 40));
        mesBox.setFont(new Font("Arial", Font.PLAIN, 13));
        mesBox.setBackground(Color.BLACK);
        mesBox.setForeground(Color.WHITE);
        JTextField anioField = createRoundedTextField("aaaa");
        anioField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (anioField.getText().equals("aaaa"))
                    anioField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (anioField.getText().isEmpty())
                    anioField.setText("aaaa");
            }
        });
        fechaPanel.add(diaField);
        fechaPanel.add(mesBox);
        fechaPanel.add(anioField);

        // Género
        JLabel generoLabel = new JLabel("Género");
        generoLabel.setFont(new Font("Arial", Font.BOLD, 13));
        generoLabel.setForeground(Color.WHITE);
        generoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel generoPanel = new JPanel(new GridLayout(2, 3, 10, 5));
        generoPanel.setBackground(new Color(18, 18, 18));
        JRadioButton hombre = createRadio("Hombre");
        JRadioButton mujer = createRadio("Mujer");
        JRadioButton noBinario = createRadio("No binario");
        JRadioButton otro = createRadio("Otro");
        JRadioButton prefieroNo = createRadio("Prefiero no aclararlo");
        ButtonGroup bg = new ButtonGroup();
        bg.add(hombre);
        bg.add(mujer);
        bg.add(noBinario);
        bg.add(otro);
        bg.add(prefieroNo);
        generoPanel.add(hombre);
        generoPanel.add(mujer);
        generoPanel.add(noBinario);
        generoPanel.add(otro);
        generoPanel.add(prefieroNo);

        // Botón Siguiente
        JButton siguienteBtn = new JButton("Siguiente") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        siguienteBtn.setBackground(new Color(30, 215, 96));
        siguienteBtn.setForeground(Color.BLACK);
        siguienteBtn.setFocusPainted(false);
        siguienteBtn.setFont(new Font("Arial", Font.BOLD, 14));
        siguienteBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        siguienteBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        siguienteBtn.setContentAreaFilled(false);
        siguienteBtn.setOpaque(true);

        siguienteBtn.addActionListener(e -> {
            boolean genOk = hombre.isSelected() || mujer.isSelected()
                    || noBinario.isSelected() || otro.isSelected() || prefieroNo.isSelected();
            if (!nombreField.getText().trim().isEmpty()
                    && !diaField.getText().equals("dd")
                    && mesBox.getSelectedIndex() != 0
                    && !anioField.getText().equals("aaaa")
                    && genOk) {
                dispose();
                new SpotifyPremiumPlansUI().setVisible(true);
            } else {
                showDarkWarningDialog(
                        "<html><body style='text-align:center;'>Por favor, completa<br/>todos los campos antes de continuar.</body></html>",
                        "Campos incompletos");
            }
        });

        // Ensamblar
        content.add(logoLabel);
        content.add(Box.createVerticalStrut(20));
        content.add(navPanel);
        content.add(Box.createVerticalStrut(10));
        content.add(titulo);
        content.add(Box.createVerticalStrut(20));
        content.add(nombreLabel);
        content.add(nombreField);
        content.add(subtitulo);
        content.add(Box.createVerticalStrut(20));
        content.add(fechaLabel);
        content.add(fechaPanel);
        content.add(Box.createVerticalStrut(20));
        content.add(generoLabel);
        content.add(generoPanel);
        content.add(Box.createVerticalStrut(30));
        content.add(siguienteBtn);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(18, 18, 18));
        main.add(titleBar, BorderLayout.NORTH);
        main.add(content, BorderLayout.CENTER);
        setContentPane(main);
        setVisible(true);
    }

    private void showDarkWarningDialog(String message, String title) {
    // Diálogo sin bordes y redondeado
    JDialog dialog = new JDialog(this, title, true);
    dialog.setUndecorated(true);
    dialog.setSize(320, 160);
    dialog.setLocationRelativeTo(this);
    dialog.setShape(new RoundRectangle2D.Double(0, 0,
        dialog.getWidth(), dialog.getHeight(), 20, 20));

    // Panel principal oscuro
    JPanel dlg = new JPanel(new BorderLayout());
    dlg.setBackground(new Color(30, 30, 30));
    dlg.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    // Mensaje centrado
    JLabel lbl = new JLabel(message, SwingConstants.CENTER);
    lbl.setForeground(Color.WHITE);
    lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    dlg.add(lbl, BorderLayout.CENTER);

    // Botón OK redondeado con JButton local
    JButton ok = new JButton("OK") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            super.paintComponent(g);
            g2.dispose();
        }
    };
    ok.setBackground(new Color(80, 80, 80));
    ok.setForeground(Color.WHITE);
    ok.setFocusPainted(false);
    ok.setFont(new Font("Segoe UI", Font.BOLD, 12));
    ok.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
    ok.addActionListener(e -> dialog.dispose());

    JPanel btnP = new JPanel();
    btnP.setOpaque(false);
    btnP.add(ok);
    dlg.add(btnP, BorderLayout.SOUTH);

    dialog.setContentPane(dlg);
    dialog.setVisible(true);
}


    private JTextField createRoundedTextField(String placeholder) {
        JTextField tf = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tf.setBackground(Color.BLACK);
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tf.setOpaque(false);
        return tf;
    }

    private JRadioButton createRadio(String text) {
        JRadioButton rb = new JRadioButton(text);
        rb.setForeground(Color.WHITE);
        rb.setBackground(new Color(18, 18, 18));
        rb.setFocusPainted(false);
        return rb;
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
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FrameRegistroPaso2::new);
    }
}
