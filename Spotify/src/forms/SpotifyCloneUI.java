package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

class RoundedPanel extends JPanel {
    private int cornerRadius;

    public RoundedPanel(int radius) {
        this.cornerRadius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.dispose();
    }
}

class RoundButton extends JButton {
    public RoundButton(Color color) {
        setPreferredSize(new Dimension(14, 14));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setBackground(color);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
        g2.dispose();
    }
}

class IconButton extends JButton {
    private Color defaultBg = new Color(30, 30, 30);
    private Color hoverBg = new Color(60, 60, 60);

    public IconButton(String text) {
        super(text);
        setFocusPainted(false);
        setBackground(defaultBg);
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(hoverBg);
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(defaultBg);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.dispose();
        super.paintComponent(g);
    }
}

// Clase mejorada para imágenes adaptables con esquinas redondeadas
class AdaptiveImagePanel extends JPanel {
    private Image image;
    private int cornerRadius;
    private Color backgroundColor;
    
    public AdaptiveImagePanel(String imagePath, int cornerRadius) {
        this.cornerRadius = cornerRadius;
        this.backgroundColor = Color.DARK_GRAY;
        setOpaque(false);
        loadImage(imagePath);
    }
    
    public AdaptiveImagePanel(String imagePath) {
        this(imagePath, 8); // Radio por defecto
    }
    
    private void loadImage(String imagePath) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            this.image = icon.getImage();
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen: " + imagePath);
            this.image = null;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Configurar antialiasing para mejor calidad
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        if (width <= 0 || height <= 0) {
            g2d.dispose();
            return;
        }
        
        // Crear una máscara con esquinas redondeadas
        Shape roundedRect = new java.awt.geom.RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius);
        g2d.setClip(roundedRect);
        
        if (image != null) {
            // Calcular dimensiones para mantener proporción y llenar el área
            int imgWidth = image.getWidth(null);
            int imgHeight = image.getHeight(null);
            
            if (imgWidth > 0 && imgHeight > 0) {
                double scaleX = (double) width / imgWidth;
                double scaleY = (double) height / imgHeight;
                double scale = Math.max(scaleX, scaleY); // Usar el mayor para llenar completamente
                
                int scaledWidth = (int) (imgWidth * scale);
                int scaledHeight = (int) (imgHeight * scale);
                
                // Centrar la imagen
                int x = (width - scaledWidth) / 2;
                int y = (height - scaledHeight) / 2;
                
                g2d.drawImage(image, x, y, scaledWidth, scaledHeight, this);
            }
        } else {
            // Dibujar fondo si no hay imagen
            g2d.setColor(backgroundColor);
            g2d.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
        }
        
        g2d.dispose();
    }
    
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }
    
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }
}

// Panel de imagen con placeholder personalizable
class ImageCoverPanel extends JPanel {
    private String placeholderText;
    private Color placeholderBg;
    private Font placeholderFont;
    
    public ImageCoverPanel(String imagePath, String placeholder, Color bgColor) {
        setLayout(new BorderLayout());
        this.placeholderText = placeholder;
        this.placeholderBg = bgColor;
        this.placeholderFont = new Font("SansSerif", Font.BOLD, 24);
        
        try {
            AdaptiveImagePanel imagePanel = new AdaptiveImagePanel(imagePath, 8);
            add(imagePanel, BorderLayout.CENTER);
        } catch (Exception e) {
            // Crear placeholder si falla la carga de imagen
            createPlaceholder();
        }
    }
    
    private void createPlaceholder() {
        JLabel placeholder = new JLabel(placeholderText, SwingConstants.CENTER);
        placeholder.setOpaque(true);
        placeholder.setBackground(placeholderBg);
        placeholder.setForeground(Color.WHITE);
        placeholder.setFont(placeholderFont);
        add(placeholder, BorderLayout.CENTER);
    }
}

public class SpotifyCloneUI extends JFrame {

    public SpotifyCloneUI() {
        setTitle("Spotify - Clone");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUndecorated(true);

        Color background = new Color(25, 25, 25);
        Color panelColor = new Color(35, 35, 35);
        Color textColor = Color.WHITE;

        getContentPane().setBackground(background);

        // Título estilo Mac
        RoundedPanel titleBar = new RoundedPanel(20);
        titleBar.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 7));
        titleBar.setBackground(panelColor);
        titleBar.setPreferredSize(new Dimension(1280, 30));

        RoundButton closeBtn = new RoundButton(new Color(255, 95, 86));
        closeBtn.addActionListener(e -> System.exit(0));
        RoundButton minBtn = new RoundButton(new Color(255, 189, 46));
        minBtn.addActionListener(e -> setState(Frame.ICONIFIED));
        RoundButton maxBtn = new RoundButton(new Color(39, 201, 63));
        maxBtn.addActionListener(e -> {
            if (getExtendedState() == Frame.MAXIMIZED_BOTH) {
                setExtendedState(Frame.NORMAL);
            } else {
                setExtendedState(Frame.MAXIMIZED_BOTH);
            }
        });

        titleBar.add(closeBtn);
        titleBar.add(minBtn);
        titleBar.add(maxBtn);

        // Menu Bar Container
        RoundedPanel menuBarContainer = new RoundedPanel(40);
        menuBarContainer.setLayout(new BorderLayout());
        menuBarContainer.setBackground(new Color(25, 25, 25));
        menuBarContainer.setPreferredSize(new Dimension(1200, 60));

        // Logo Apple Music a la izquierda
        JLabel logo = new JLabel(new ImageIcon("resources/logo_music_resized.png"));
        logo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        menuBarContainer.add(logo, BorderLayout.WEST);

        // --- CENTRAR CON GRIDBAG ---
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        String[] items = { "For You", "Browse", "Videos", "Radio", "Library", "Now Playing" };
        ButtonGroup navGroup = new ButtonGroup();
        Color defaultBg = new Color(30, 30, 30);
        Color selectedBg = new Color(90, 90, 90);
        Color hoverBg = new Color(60, 60, 60);

        JPanel buttonGroupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        buttonGroupPanel.setOpaque(false);

        for (String item : items) {
            JToggleButton button = new JToggleButton(item) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getModel().isSelected() ? selectedBg : getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            button.setFocusPainted(false);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("SansSerif", Font.BOLD, 13));
            button.setContentAreaFilled(false);
            button.setOpaque(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
            button.setBackground(item.equals("For You") ? selectedBg : defaultBg);
            button.setSelected(item.equals("For You"));

            button.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (!button.isSelected())
                        button.setBackground(hoverBg);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (!button.isSelected())
                        button.setBackground(defaultBg);
                }
            });

            button.addActionListener(e -> {
                for (Component comp : buttonGroupPanel.getComponents()) {
                    if (comp instanceof JToggleButton btn) {
                        btn.setBackground(btn.isSelected() ? selectedBg : defaultBg);
                    }
                }
            });

            navGroup.add(button);
            buttonGroupPanel.add(button);
        }

        centerWrapper.add(buttonGroupPanel);

        // Botones a la derecha
        IconButton searchBtn = new IconButton("🔍");
        IconButton settingsBtn = new IconButton("⚙");

        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        iconPanel.setOpaque(false);
        iconPanel.add(searchBtn);
        iconPanel.add(settingsBtn);

        menuBarContainer.add(centerWrapper, BorderLayout.CENTER);
        menuBarContainer.add(iconPanel, BorderLayout.EAST);

        // TopPanel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(panelColor);
        topPanel.add(titleBar, BorderLayout.NORTH);
        topPanel.add(menuBarContainer, BorderLayout.SOUTH);

        // Panel Central
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        centerPanel.setBackground(background);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Diferentes conjuntos de imágenes para cada Mix
        String[][] allCoverImages = {
            // Mix 1 - Hip Hop/Rap
            {"/resources/cover1.png", "/resources/cover2.png", "/resources/cover3.png", "/resources/cover.png"},
            // Mix 2 - Pop/Electronic
            {"/resources/Kris r1.png", "/resources/Kris r2.png", "/resources/Kris r3.png", "/resources/Kris r4.png"},
            // Mix 3 - Rock/Alternative
            {"/resources/Vallenato4.png", "/resources/Vallenato1.png", "/resources/Vallenato2.png", "/resources/Vallenato3.png"},
            // Mix 4 - R&B/Soul
            {"/resources/Salsa4.png", "/resources/Salsa1.png", "/resources/Salsa2.png", "/resources/Salsa3.png"}
        };

        String[] mixNames = {"TTrap Mix", "reggaeton Mix", "Vallenato Mix", "Salsa Mix"};
        String[] mixSubtitles = {"Pirlo", "Kris r", "Binomio de Oro", "Franki ruiz"};

        for (int i = 0; i < 4; i++) {
            RoundedPanel mixPanel = new RoundedPanel(30);
            mixPanel.setLayout(new BorderLayout());
            mixPanel.setBackground(panelColor);
            mixPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel title = new JLabel(mixNames[i]);
            title.setForeground(textColor);
            title.setFont(new Font("SansSerif", Font.BOLD, 16));

            JLabel subtitle = new JLabel(mixSubtitles[i]);
            subtitle.setForeground(Color.GRAY);
            subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
            
            RoundedPanel header = new RoundedPanel(20);
            header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
            header.setBackground(new Color(40, 40, 40));
            header.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            header.add(title);
            header.add(subtitle);

            // Panel de carátulas con GridLayout adaptable
            JPanel coverPanel = new JPanel(new GridLayout(2, 2, 3, 3));
            coverPanel.setBackground(panelColor);

            String[] coverImages = allCoverImages[i];
            Color[] placeholderColors = {
                new Color(180, 50, 50),   // Rojo para Hip Hop
                new Color(50, 150, 200),  // Azul para Pop
                new Color(120, 60, 180),  // Púrpura para Rock
                new Color(200, 120, 50)   // Naranja para R&B
            };
            String[] placeholderSymbols = {"🎤", "🎵", "🎸", "🎶"};

            for (int j = 0; j < coverImages.length; j++) {
                ImageCoverPanel cover = new ImageCoverPanel(
                    coverImages[j], 
                    placeholderSymbols[i], 
                    placeholderColors[i]
                );
                coverPanel.add(cover);
            }

            mixPanel.add(header, BorderLayout.NORTH);
            mixPanel.add(coverPanel, BorderLayout.CENTER);
            centerPanel.add(mixPanel);
        }

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    public static void mostrarInterfaz() {
        SwingUtilities.invokeLater(() -> {
            SpotifyCloneUI ui = new SpotifyCloneUI();
            ui.setVisible(true);
        });
    }

    public static void main(String[] args) {
        mostrarInterfaz();
    }
}