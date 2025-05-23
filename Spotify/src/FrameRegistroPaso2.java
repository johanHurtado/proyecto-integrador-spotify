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
            new FrameRegistroPassword();
        });

        JLabel paso = new JLabel("Paso 2 de 3");
        paso.setFont(new Font("Arial", Font.PLAIN, 14));
        paso.setForeground(Color.LIGHT_GRAY);
        paso.setAlignmentX(Component.LEFT_ALIGNMENT);

        navPanel.add(backBtn);
        navPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        navPanel.add(paso);

        JLabel titulo = new JLabel("Cuéntanos sobre ti");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nombreLabel = new JLabel("Nombre");
        nombreLabel.setForeground(Color.WHITE);
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 13));
        nombreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField nombreField = createRoundedTextField("");

        JLabel subtitulo = new JLabel("Este nombre aparecerá en tu perfil");
        subtitulo.setForeground(Color.GRAY);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel fechaLabel = new JLabel("Fecha de nacimiento");
        fechaLabel.setForeground(Color.WHITE);
        fechaLabel.setFont(new Font("Arial", Font.BOLD, 13));
        fechaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel fechaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        fechaPanel.setBackground(new Color(18, 18, 18));

        JTextField diaField = createRoundedTextField("dd");
        diaField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (diaField.getText().equals("dd")) diaField.setText("");
            }
            public void focusLost(FocusEvent e) {
                if (diaField.getText().isEmpty()) diaField.setText("dd");
            }
        });

        JComboBox<String> mesBox = new JComboBox<>(new String[]{"Mes", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"});
        mesBox.setMaximumSize(new Dimension(150, 40));
        mesBox.setFont(new Font("Arial", Font.PLAIN, 13));
        mesBox.setBackground(Color.BLACK);
        mesBox.setForeground(Color.WHITE);

        JTextField anioField = createRoundedTextField("aaaa");
        anioField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (anioField.getText().equals("aaaa")) anioField.setText("");
            }
            public void focusLost(FocusEvent e) {
                if (anioField.getText().isEmpty()) anioField.setText("aaaa");
            }
        });

        fechaPanel.add(diaField);
        fechaPanel.add(mesBox);
        fechaPanel.add(anioField);

        JLabel generoLabel = new JLabel("Género");
        generoLabel.setForeground(Color.WHITE);
        generoLabel.setFont(new Font("Arial", Font.BOLD, 13));
        generoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel generoPanel = new JPanel(new GridLayout(2, 3, 10, 5));
        generoPanel.setBackground(new Color(18, 18, 18));

        JRadioButton hombre = createRadio("Hombre");
        JRadioButton mujer = createRadio("Mujer");
        JRadioButton noBinario = createRadio("No binario");
        JRadioButton otro = createRadio("Otro");
        JRadioButton prefieroNoDecir = createRadio("Prefiero no aclararlo");

        ButtonGroup generoGroup = new ButtonGroup();
        generoGroup.add(hombre);
        generoGroup.add(mujer);
        generoGroup.add(noBinario);
        generoGroup.add(otro);
        generoGroup.add(prefieroNoDecir);

        generoPanel.add(hombre);
        generoPanel.add(mujer);
        generoPanel.add(noBinario);
        generoPanel.add(otro);
        generoPanel.add(prefieroNoDecir);

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
            boolean generoSeleccionado = hombre.isSelected() || mujer.isSelected() || noBinario.isSelected() || otro.isSelected() || prefieroNoDecir.isSelected();
            if (!nombreField.getText().trim().isEmpty()
                && !diaField.getText().equals("dd") && !diaField.getText().trim().isEmpty()
                && mesBox.getSelectedIndex() != 0
                && !anioField.getText().equals("aaaa") && !anioField.getText().trim().isEmpty()
                && generoSeleccionado) {
                dispose();
                new FrameRegistroPaso3();
            } else {
                JOptionPane.showMessageDialog(this, "Por favor completa todos los campos para continuar.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            }
        });

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

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(18, 18, 18));
        mainPanel.add(titleBar, BorderLayout.NORTH);
        mainPanel.add(content, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private JTextField createRoundedTextField(String placeholder) {
        JTextField textField = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        textField.setBackground(Color.BLACK);
        textField.setForeground(Color.WHITE);
        textField.setCaretColor(Color.WHITE);
        textField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textField.setOpaque(false);
        return textField;
    }

    private JRadioButton createRadio(String text) {
        JRadioButton radio = new JRadioButton(text);
        radio.setForeground(Color.WHITE);
        radio.setBackground(new Color(18, 18, 18));
        radio.setFocusPainted(false);
        return radio;
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
