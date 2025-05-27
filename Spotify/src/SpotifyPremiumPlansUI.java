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
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
            }
        });

        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 7));
        titleBar.setBackground(new Color(35, 35, 35));
        titleBar.setPreferredSize(new Dimension(800, 30));

        JButton closeBtn = createMacCircle(Color.RED);
        closeBtn.addActionListener(e -> System.exit(0));
        JButton minimizeBtn = createMacCircle(Color.ORANGE);
        minimizeBtn.addActionListener(e -> setState(Frame.ICONIFIED));
        JButton maximizeBtn = createMacCircle(Color.GREEN);
        maximizeBtn.addActionListener(e -> setExtendedState(
                getExtendedState() == Frame.MAXIMIZED_BOTH ? Frame.NORMAL : Frame.MAXIMIZED_BOTH));

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

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(18, 18, 18));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel heading = new JLabel("Elige tu plan Premium");
        heading.setFont(new Font("Arial", Font.BOLD, 28));
        heading.setForeground(Color.WHITE);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subheading = new JLabel("Escucha música sin anuncios, descarga tus canciones favoritas y más.");
        subheading.setFont(new Font("Arial", Font.PLAIN, 16));
        subheading.setForeground(Color.LIGHT_GRAY);
        subheading.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(heading);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(subheading);
        mainPanel.add(Box.createVerticalStrut(30));

        // Paneles de planes
        mainPanel.add(createPlanPanel("Individual", "COP 16,900 al mes", new String[] {
                "1 cuenta Premium",
                "Música sin anuncios",
                "Escucha en cualquier lugar, incluso sin conexión",
                "Reproducción bajo demanda"
        }, new Color(30, 215, 96)));

        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(createPlanPanel("Duo", "COP 21,400 al mes", new String[] {
                "2 cuentas Premium",
                "Para parejas que viven juntas",
                "Música sin anuncios",
                "Duo Mix: una playlist para dos"
        }, new Color(245, 155, 35)));

        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(createPlanPanel("Familiar", "COP 26,400 al mes", new String[] {
                "Hasta 6 cuentas Premium",
                "Para familias que viven juntas",
                "Bloqueo de contenido explícito",
                "Family Mix: una playlist para la familia"
        }, new Color(83, 83, 255)));

        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(createPlanPanel("Estudiantes", "COP 9,300 al mes", new String[] {
                "1 cuenta Premium",
                "Descuento para estudiantes elegibles",
                "Música sin anuncios",
                "Escucha sin conexión"
        }, new Color(255, 83, 112)));

        mainPanel.add(Box.createVerticalStrut(30));

        // Comparativa de beneficios
        JLabel comparativaHeading = new JLabel("Comparativa de beneficios");
        comparativaHeading.setFont(new Font("Arial", Font.BOLD, 22));
        comparativaHeading.setForeground(Color.WHITE);
        comparativaHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(comparativaHeading);
        mainPanel.add(Box.createVerticalStrut(10));

        String[] beneficios = {
                "Música sin anuncios",
                "Descarga para escuchar sin conexión",
                "Reproducción en cualquier orden",
                "Calidad de audio alta",
                "Escucha con amigos en tiempo real",
                "Organiza tu cola de reproducción"
        };

        JPanel comparativaPanel = new JPanel();
        comparativaPanel.setLayout(new GridLayout(beneficios.length, 2, 10, 10));
        comparativaPanel.setBackground(new Color(18, 18, 18));

        for (String beneficio : beneficios) {
            JLabel beneficioLabel = new JLabel(beneficio);
            beneficioLabel.setForeground(Color.WHITE);
            beneficioLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            JLabel checkLabel = new JLabel("✔");
            checkLabel.setForeground(new Color(30, 215, 96));
            checkLabel.setFont(new Font("Arial", Font.BOLD, 16));

            comparativaPanel.add(beneficioLabel);
            comparativaPanel.add(checkLabel);
        }

        mainPanel.add(comparativaPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        // Información legal
        JLabel legalInfo = new JLabel(
                "<html><center>Se aplican términos y condiciones. La disponibilidad de las funciones puede variar según el plan y la región.</center></html>");
        legalInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        legalInfo.setForeground(Color.LIGHT_GRAY);
        legalInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(legalInfo);
        mainPanel.add(Box.createVerticalStrut(20));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, Integer.MAX_VALUE));
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(100, 100, 100);
                this.trackColor = new Color(30, 30, 30);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });

        add(titleBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createPlanPanel(String title, String price, String[] features, Color accentColor) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(24, 24, 24));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(accentColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel(price);
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(priceLabel);
        panel.add(Box.createVerticalStrut(10));

        for (String feature : features) {
            JLabel featureLabel = new JLabel("• " + feature);
            featureLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            featureLabel.setForeground(Color.LIGHT_GRAY);
            featureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(featureLabel);
            panel.add(Box.createVerticalStrut(5));
        }

        panel.add(Box.createVerticalStrut(10));

        JButton getPremiumButton = new JButton("Obtener Premium");
        getPremiumButton.setBackground(accentColor);
        getPremiumButton.setForeground(Color.BLACK);
        getPremiumButton.setFocusPainted(false);
        getPremiumButton.setFont(new Font("Arial", Font.BOLD, 14));
        getPremiumButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        getPremiumButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        getPremiumButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        panel.add(getPremiumButton);

        return panel;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SpotifyPremiumPlansUI());
    }
}
