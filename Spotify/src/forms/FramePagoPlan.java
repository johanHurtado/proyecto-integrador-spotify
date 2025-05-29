package forms;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import javax.swing.text.JTextComponent;
import javax.swing.plaf.basic.BasicTextFieldUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.text.ParseException;

public class FramePagoPlan extends JFrame {
    private Point initialClick;

    // Campos de tarjeta como variables de instancia
    private JFormattedTextField numero;
    private JFormattedTextField fecha;
    private JFormattedTextField cvv;

    public FramePagoPlan() {
        // ══ Ventana ═══════════════════════════════════════════════════════════
        setUndecorated(true);
        setSize(380, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            }
        });

        Color bg     = new Color(18, 18, 18);
        Color text   = Color.WHITE;
        Color accent = new Color(30, 215, 96);

        // ══ Title Bar ════════════════════════════════════════════════════════
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(35, 35, 35));
        titleBar.setPreferredSize(new Dimension(380, 30));

        // mac circles (WEST)
        JPanel macPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 7));
        macPanel.setOpaque(false);
        JButton closeBtn = createMacCircle(Color.RED);
        closeBtn.addActionListener(e -> System.exit(0));
        JButton minBtn = createMacCircle(Color.ORANGE);
        minBtn.addActionListener(e -> setState(Frame.ICONIFIED));
        JButton maxBtn = createMacCircle(Color.GREEN);
        maxBtn.addActionListener(e -> {
            if (getExtendedState() != Frame.MAXIMIZED_BOTH) setExtendedState(Frame.MAXIMIZED_BOTH);
            else setExtendedState(Frame.NORMAL);
        });
        macPanel.add(closeBtn);
        macPanel.add(minBtn);
        macPanel.add(maxBtn);
        titleBar.add(macPanel, BorderLayout.WEST);

        // back button (EAST)
        JButton backBtn = new JButton("<");
        backBtn.setFont(new Font("Arial", Font.BOLD, 16));
        backBtn.setForeground(text);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            dispose();
            new SpotifyPremiumPlansUI().setVisible(true);
        });
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 7));
        backPanel.setOpaque(false);
        backPanel.add(backBtn);
        titleBar.add(backPanel, BorderLayout.EAST);

        // draggable
        titleBar.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { initialClick = e.getPoint(); }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseDragged(MouseEvent e) {
                int dx = e.getX() - initialClick.x, dy = e.getY() - initialClick.y;
                setLocation(getX() + dx, getY() + dy);
            }
        });

        // ══ Contenido ════════════════════════════════════════════════════════
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(bg);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Plan info
        JLabel tituloPlan = new JLabel("Premium Individual");
        tituloPlan.setFont(new Font("Arial", Font.BOLD, 18));
        tituloPlan.setForeground(text);
        tituloPlan.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel precioPlan = new JLabel("Pago único $16,900 COP / mes");
        precioPlan.setFont(new Font("Arial", Font.PLAIN, 12));
        precioPlan.setForeground(Color.LIGHT_GRAY);
        precioPlan.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(tituloPlan);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(precioPlan);
        mainPanel.add(Box.createVerticalStrut(20));

        // ══ Tarjeta ═════════════════════════════════════════════════════════
        JPanel tarjetaPanel = createRoundedPanel();
        tarjetaPanel.setBackground(new Color(28, 28, 28));
        tarjetaPanel.setLayout(new BoxLayout(tarjetaPanel, BoxLayout.Y_AXIS));
        tarjetaPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        tarjetaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel metodo = new JLabel("Tarjeta de crédito o débito");
        metodo.setFont(new Font("Arial", Font.BOLD, 13));
        metodo.setForeground(text);
        metodo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Número de tarjeta
        try {
            MaskFormatter cardFmt = new MaskFormatter("#### #### #### ####");
            cardFmt.setPlaceholderCharacter(' ');
            numero = new JFormattedTextField(cardFmt);
        } catch (ParseException ex) {
            numero = new JFormattedTextField();
        }
        configureRoundedField(numero);

        // Fecha MM/AA
        try {
            MaskFormatter dateFmt = new MaskFormatter("##/##");
            dateFmt.setPlaceholderCharacter('_');
            fecha = new JFormattedTextField(dateFmt);
        } catch (ParseException ex) {
            fecha = new JFormattedTextField();
        }
        configureRoundedField(fecha);
        fecha.setMaximumSize(new Dimension(80, 40));

        // CVC 3 dígitos
        try {
            MaskFormatter cvvFmt = new MaskFormatter("###");
            cvvFmt.setPlaceholderCharacter('_');
            cvv = new JFormattedTextField(cvvFmt);
        } catch (ParseException ex) {
            cvv = new JFormattedTextField();
        }
        configureRoundedField(cvv);
        cvv.setMaximumSize(new Dimension(60, 40));

        JPanel fechaCvvPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        fechaCvvPanel.setBackground(tarjetaPanel.getBackground());
        fechaCvvPanel.add(fecha);
        fechaCvvPanel.add(cvv);

        tarjetaPanel.add(metodo);
        tarjetaPanel.add(Box.createVerticalStrut(10));
        tarjetaPanel.add(numero);
        tarjetaPanel.add(Box.createVerticalStrut(10));
        tarjetaPanel.add(fechaCvvPanel);

        mainPanel.add(tarjetaPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // ══ Comprar ahora ════════════════════════════════════════════════════
        JButton comprar = new JButton("Comprar ahora") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        comprar.setBackground(accent);
        comprar.setForeground(Color.BLACK);
        comprar.setFont(new Font("Arial", Font.BOLD, 14));
        comprar.setFocusPainted(false);
        comprar.setContentAreaFilled(false);
        comprar.setOpaque(false);
        comprar.setAlignmentX(Component.CENTER_ALIGNMENT);
        comprar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        comprar.addActionListener(e -> {
            String cn = numero.getText().replace(" ", ""),
                   ex = fecha.getText(),
                   cv = cvv.getText();
            if (!cn.matches("\\d{16}") ||
                !ex.matches("(0[1-9]|1[0-2])/\\d{2}") ||
                !cv.matches("\\d{3}")) {
                showDarkWarningDialog("Tarjeta inválida.", "Error");
            } else {
                showDarkWarningDialog("Pago realizado con éxito.", "¡Listo");
                new FrameRegistroPaso4();
                dispose();
            }
        });

        mainPanel.add(comprar);
        mainPanel.add(Box.createVerticalStrut(20));

        // ══ Ensamblar ════════════════════════════════════════════════════════
        JPanel container = new JPanel(new BorderLayout());
        container.add(titleBar, BorderLayout.NORTH);
        container.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        setContentPane(container);
        setVisible(true);
    }

    // Diálogo oscuro y redondeado
    private void showDarkWarningDialog(String msg, String title) {
        JDialog d = new JDialog(this, title, true);
        d.setUndecorated(true);
        d.setSize(280, 130);
        d.setLocationRelativeTo(this);
        d.setShape(new RoundRectangle2D.Double(0,0,d.getWidth(),d.getHeight(),20,20));
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(30,30,30));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JLabel l = new JLabel("<html><center>"+msg+"</center></html>", SwingConstants.CENTER);
        l.setForeground(Color.WHITE);
        p.add(l, BorderLayout.CENTER);
        JButton ok = new JButton("OK");
        ok.setBackground(new Color(80,80,80));
        ok.setForeground(Color.WHITE);
        ok.setFocusPainted(false);
        ok.addActionListener(e -> d.dispose());
        JPanel bp = new JPanel();
        bp.setOpaque(false);
        bp.add(ok);
        p.add(bp, BorderLayout.SOUTH);
        d.setContentPane(p);
        d.setVisible(true);
    }

    // Aplica estilo redondeado a un JTextComponent
    private void configureRoundedField(JTextComponent field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setForeground(Color.WHITE);
        field.setBackground(Color.BLACK);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        field.setOpaque(false);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setUI(new BasicTextFieldUI() {
            @Override protected void paintSafely(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(field.getBackground());
                g2.fillRoundRect(0, 0, field.getWidth(), field.getHeight(), 30, 30);
                g2.dispose();
                super.paintSafely(g);
            }
        });
    }

    // Panel con fondo redondeado
    private JPanel createRoundedPanel() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
                super.paintComponent(g);
                g2.dispose();
            }
        };
    }

    // Botón mac-style
    private JButton createMacCircle(Color color) {
        return new JButton() {
            {
                setPreferredSize(new Dimension(14,14));
                setContentAreaFilled(false);
                setFocusPainted(false);
                setBorderPainted(false);
                setOpaque(false);
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0,0,getWidth(),getHeight());
                g2.dispose();
            }
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FramePagoPlan::new);
    }
}
