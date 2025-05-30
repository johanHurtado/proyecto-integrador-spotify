package forms;

import entities.*;              // Ajusta si tu paquete es distinto
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Paso 3 del registro: selección de plan (Free / Premium).
 * Recibe el objeto User ya rellenado en pasos anteriores.
 */
public class SpotifyPremiumPlansUI extends JFrame {

    private Point initialClick;
    private final User user;   // guarda los datos del usuario hasta el paso 4

    /*------------------------------------------------------------------
      Constructor: recibe el usuario y construye la UI
     -----------------------------------------------------------------*/
    public SpotifyPremiumPlansUI(User user) {
        super("Elige tu plan");
        this.user = user;

        /* ---------- Ventana sin bordes y redondeada ---------- */
        setUndecorated(true);
        setSize(800, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
            }
        });

        /* ---------- Barra estilo macOS ---------- */
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        titleBar.setBackground(new Color(35, 35, 35));
        titleBar.setPreferredSize(new Dimension(800, 30));

        JButton closeBtn = createMacCircle(new Color(240, 80, 80));
        closeBtn.addActionListener(e -> System.exit(0));

        JButton minBtn = createMacCircle(new Color(245, 190, 80));
        minBtn.addActionListener(e -> setState(Frame.ICONIFIED));

        JButton maxBtn = createMacCircle(new Color(100, 210, 100));
        maxBtn.addActionListener(e -> {
            if ((getExtendedState() & Frame.MAXIMIZED_BOTH) != Frame.MAXIMIZED_BOTH) {
                setExtendedState(Frame.MAXIMIZED_BOTH);
            } else {
                setExtendedState(Frame.NORMAL);
            }
        });

        titleBar.add(closeBtn);
        titleBar.add(minBtn);
        titleBar.add(maxBtn);

        /* Permite arrastrar la ventana */
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

        add(titleBar, BorderLayout.NORTH);

        /* ---------- Contenido con scroll ---------- */
        JPanel content = new JPanel();
        content.setBackground(new Color(18, 18, 18));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel heading = new JLabel("Elige tu plan");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(Color.WHITE);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(heading);
        content.add(Box.createVerticalStrut(10));

        JLabel sub = new JLabel(
                "Escucha música sin anuncios, descarga tus canciones favoritas y más.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub.setForeground(Color.LIGHT_GRAY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(sub);
        content.add(Box.createVerticalStrut(30));

        /* ---------- Plan Free ---------- */
        content.add(createPlanPanel(
                "Free",
                "COP 0 al mes",
                new String[]{
                        "1 cuenta Free",
                        "Salto de canciones con límites",
                        "Cambio de canciones solo hacia adelante",
                        "Reproducción bajo demanda"
                },
                new Color(30, 215, 96),
                (ActionListener) e -> {
                    user.setSubscriptionId(1);                 // Free
                    new FrameRegistro4(user).setVisible(true);
                    dispose();
                }
        ));
        content.add(Box.createVerticalStrut(20));

        /* ---------- Plan Premium ---------- */
        content.add(createPlanPanel(
                "Premium",
                "COP 16,900 al mes",
                new String[]{
                        "1 cuenta Premium",
                        "Música sin anuncios",
                        "Escucha en cualquier lugar, incluso sin conexión",
                        "Reproducción bajo demanda"
                },
                new Color(245, 155, 35),
                (ActionListener) e -> {
                    user.setSubscriptionId(2);                 // Premium
                    new FramePagoPlan().setVisible(true);
                    dispose();
                }
        ));
        content.add(Box.createVerticalStrut(30));

        JLabel legal = new JLabel(
                "<html><center>Se aplican términos y condiciones. "
              + "La disponibilidad puede variar según el plan y la región.</center></html>");
        legal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        legal.setForeground(Color.LIGHT_GRAY);
        legal.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(legal);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        setVisible(true);
    }

    /*------------------------------------------------------------------
      Crea el panel visual para cada plan
     -----------------------------------------------------------------*/
    private JPanel createPlanPanel(String title,
                                   String price,
                                   String[] features,
                                   Color accent,
                                   ActionListener al) {

        JPanel panel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setBackground(new Color(24, 24, 24));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accent, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(accent);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitle);

        panel.add(Box.createVerticalStrut(5));
        JLabel lblPrice = new JLabel(price);
        lblPrice.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblPrice.setForeground(Color.WHITE);
        lblPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblPrice);

        panel.add(Box.createVerticalStrut(10));
        for (String feat : features) {
            JLabel f = new JLabel("• " + feat);
            f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            f.setForeground(Color.LIGHT_GRAY);
            f.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(f);
            panel.add(Box.createVerticalStrut(4));
        }

        panel.add(Box.createVerticalStrut(10));
        JButton btn = new JButton("Siguiente") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setBackground(accent);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setMaximumSize(new Dimension(180, 35));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(al);
        panel.add(btn);

        return panel;
    }

    /*------------------------------------------------------------------
      Crea los “círculos” de la barra de título estilo macOS
     -----------------------------------------------------------------*/
    private JButton createMacCircle(Color color) {
        JButton b = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setPreferredSize(new Dimension(12, 12));
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
        return b;
    }

    /*------------------------------------------------------------------
      Test rápido independiente
     -----------------------------------------------------------------*/
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User u = new User();
            u.setUsername("tmp");
            u.setEmail("tmp@usantoto.edu.co");
            u.setPhone("000");
            u.setPassword("pwd");
            u.setRoleId(2);          // espectador
            new SpotifyPremiumPlansUI(u);
        });
    }
}
