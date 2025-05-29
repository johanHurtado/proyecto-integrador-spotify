package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class SpotifyPremiumPlansUI extends JFrame {
    private Point initialClick;

    public SpotifyPremiumPlansUI() {
        setTitle("Spotify Premium Plans");
        setSize(800, 900);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // apply rounded corners initially
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));

        // update shape on resize / maximize
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (getExtendedState() == Frame.MAXIMIZED_BOTH) {
                    setShape(null); // remove shape when maximized
                } else {
                    setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
                }
            }
        });

        setLayout(new BorderLayout());

        // title bar
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 7));
        titleBar.setBackground(new Color(35, 35, 35));
        titleBar.setPreferredSize(new Dimension(800, 30));

        JButton closeBtn = createMacCircle(Color.RED);
        closeBtn.addActionListener(e -> System.exit(0));

        JButton minimizeBtn = createMacCircle(Color.ORANGE);
        minimizeBtn.addActionListener(e -> setState(Frame.ICONIFIED));

        JButton maximizeBtn = createMacCircle(Color.GREEN);
        maximizeBtn.addActionListener(e -> {
            if (getExtendedState() != Frame.MAXIMIZED_BOTH) {
                setExtendedState(Frame.MAXIMIZED_BOTH);
            } else {
                setExtendedState(Frame.NORMAL);
            }
        });

        titleBar.add(closeBtn);
        titleBar.add(minimizeBtn);
        titleBar.add(maximizeBtn);

        // allow dragging from titleBar
        titleBar.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseDragged(MouseEvent e) {
                int dx = e.getX() - initialClick.x;
                int dy = e.getY() - initialClick.y;
                setLocation(getX() + dx, getY() + dy);
            }
        });

        add(titleBar, BorderLayout.NORTH);

        // main scrollable content
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(18, 18, 18));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel heading = new JLabel("Elige tu plan");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(Color.WHITE);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(heading);
        mainPanel.add(Box.createVerticalStrut(15));

        JLabel subheading = new JLabel("Escucha música sin anuncios, descarga tus canciones favoritas y más.");
        subheading.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subheading.setForeground(Color.LIGHT_GRAY);
        subheading.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(subheading);
        mainPanel.add(Box.createVerticalStrut(30));

        mainPanel.add(createFreePlanPanel(
            "Free", "COP 0 al mes",
            new String[]{
                "1 cuenta Free",
                "Salto de canciones con límites",
                "Cambio de canciones solo hacia adelante",
                "Reproducción bajo demanda"
            },
            new Color(30, 215, 96)
        ));
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(createPremiumPlanPanel(
            "Premium", "COP 16,900 al mes",
            new String[]{
                "1 cuenta Premium",
                "Música sin anuncios",
                "Escucha en cualquier lugar, incluso sin conexión",
                "Reproducción bajo demanda"
            },
            new Color(245, 155, 35)
        ));
        mainPanel.add(Box.createVerticalStrut(30));

        JLabel legalInfo = new JLabel(
            "<html><center>Se aplican términos y condiciones. La disponibilidad puede variar según el plan y la región.</center></html>"
        );
        legalInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        legalInfo.setForeground(Color.LIGHT_GRAY);
        legalInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(legalInfo);
        mainPanel.add(Box.createVerticalStrut(20));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createFreePlanPanel(String title, String price, String[] features, Color accent) {
        JPanel panel = createBasePlanPanel(title, price, features, accent);

        JButton btn = new JButton("Siguiente");
        btn.setBackground(accent);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setMaximumSize(new Dimension(180, 35));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.addActionListener(e -> {
            FrameRegistroPaso4 paso4 = new FrameRegistroPaso4();
            paso4.setVisible(true);
            dispose();
        });

        panel.add(Box.createVerticalStrut(10));
        panel.add(btn);
        return panel;
    }

    private JPanel createPremiumPlanPanel(String title, String price, String[] features, Color accent) {
        JPanel panel = createBasePlanPanel(title, price, features, accent);

        JButton btn = new JButton("Siguiente");
        btn.setBackground(accent);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setMaximumSize(new Dimension(180, 35));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.addActionListener(e -> {
            FramePagoPlan pago = new FramePagoPlan();
            pago.setVisible(true);
            dispose();
        });

        panel.add(Box.createVerticalStrut(10));
        panel.add(btn);
        return panel;
    }

    private JPanel createBasePlanPanel(String title, String price, String[] features, Color accent) {
        JPanel panel = new RoundedPanel(15);
        panel.setBackground(new Color(24, 24, 24));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accent, 2),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
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
            JLabel lf = new JLabel("• " + feat);
            lf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lf.setForeground(Color.LIGHT_GRAY);
            lf.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(lf);
            panel.add(Box.createVerticalStrut(4));
        }

        return panel;
    }

    private JButton createMacCircle(Color color) {
        return new JButton() {
            {
                setPreferredSize(new Dimension(12, 12));
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
        SwingUtilities.invokeLater(SpotifyPremiumPlansUI::new);
    }
}
