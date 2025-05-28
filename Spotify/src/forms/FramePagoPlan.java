package forms;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class FramePagoPlan extends JFrame {

    private Point initialClick;

    public FramePagoPlan() {
        setUndecorated(true);
        setSize(420, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
            }
        });

        Color bg = new Color(18, 18, 18);
        Color text = Color.WHITE;
        Color accent = new Color(30, 215, 96);

        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 7));
        titleBar.setBackground(new Color(35, 35, 35));
        titleBar.setPreferredSize(new Dimension(1000, 30));

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
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;
                setLocation(getX() + xMoved, getY() + yMoved);
            }
        });

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(bg);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel tituloPlan = new JLabel("Tu plan");
        tituloPlan.setForeground(text);
        tituloPlan.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel nombrePlan = new JLabel("Premium Individual");
        nombrePlan.setForeground(text);
        nombrePlan.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel precio = new JLabel("Un pago único de $19.900 COP por 1 mes");
        precio.setForeground(Color.LIGHT_GRAY);
        precio.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel tarjetaPanel = createRoundedPanel();
        tarjetaPanel.setLayout(new BoxLayout(tarjetaPanel, BoxLayout.Y_AXIS));
        tarjetaPanel.setBackground(new Color(28, 28, 28));
        tarjetaPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel metodo = new JLabel("Tarjeta de crédito o débito");
        metodo.setForeground(text);
        metodo.setFont(new Font("Arial", Font.BOLD, 13));

        JTextField numero = createField("Número de tarjeta");
        JTextField fecha = createField("MM/AA");
        JTextField cvv = createField("CVV");

        JPanel fechaCVV = new JPanel(new GridLayout(1, 2, 10, 0));
        fechaCVV.setBackground(tarjetaPanel.getBackground());
        fechaCVV.add(fecha);
        fechaCVV.add(cvv);

        JCheckBox guardarTarjeta = new JCheckBox("Guardar la tarjeta para futuras compras.");
        guardarTarjeta.setForeground(Color.GRAY);
        guardarTarjeta.setBackground(tarjetaPanel.getBackground());
        guardarTarjeta.setFont(new Font("Arial", Font.PLAIN, 11));

        tarjetaPanel.add(metodo);
        tarjetaPanel.add(Box.createVerticalStrut(10));
        tarjetaPanel.add(numero);
        tarjetaPanel.add(Box.createVerticalStrut(10));
        tarjetaPanel.add(fechaCVV);
        tarjetaPanel.add(Box.createVerticalStrut(10));
        tarjetaPanel.add(guardarTarjeta);

        JPanel opcionesPago = createRoundedPanel();
        opcionesPago.setBackground(new Color(28, 28, 28));
        opcionesPago.setLayout(new GridLayout(4, 1));
        opcionesPago.setBorder(new EmptyBorder(10, 15, 10, 15));

        String[] metodos = {"PayPal", "Efecty", "PSE"};
        for (String metodoPago : metodos) {
            JButton btn = new JButton(metodoPago);
            btn.setFocusPainted(false);
            btn.setBackground(bg);
            btn.setForeground(Color.CYAN);
            btn.setFont(new Font("Arial", Font.BOLD, 13));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            opcionesPago.add(btn);
        }

        JTextArea terminos = new JTextArea(
            "Al completar tu compra, aceptas los Términos de Spotify y autorizas el pago " +
            "automático con la forma de pago seleccionada. Puedes cancelar en cualquier momento " +
            "en tu cuenta de usuario."
        );
        terminos.setWrapStyleWord(true);
        terminos.setLineWrap(true);
        terminos.setEditable(false);
        terminos.setFocusable(false);
        terminos.setBackground(bg);
        terminos.setForeground(Color.LIGHT_GRAY);
        terminos.setFont(new Font("Arial", Font.PLAIN, 11));
        terminos.setBorder(null);

        JButton comprar = new JButton("Comprar ahora") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        comprar.setBackground(accent);
        comprar.setForeground(Color.BLACK);
        comprar.setFont(new Font("Arial", Font.BOLD, 14));
        comprar.setFocusPainted(false);
        comprar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        comprar.setContentAreaFilled(false);
        comprar.setOpaque(false);
        comprar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agregar a mainPanel
        mainPanel.add(tituloPlan);
        mainPanel.add(nombrePlan);
        mainPanel.add(precio);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(tarjetaPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(opcionesPago);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(terminos);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(comprar);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(bg);
        main.add(titleBar, BorderLayout.NORTH);
        main.add(mainPanel, BorderLayout.CENTER);

        setContentPane(main);
        setVisible(true);
    }

    private JTextField createField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setBackground(Color.BLACK);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        return field;
    }

    private JPanel createRoundedPanel() {
        return new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
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

            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0, 0, getWidth(), getHeight());
            }
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FramePagoPlan::new);
    }
}