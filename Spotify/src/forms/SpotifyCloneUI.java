package forms;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
            // Intentar cargar desde recursos
            java.net.URL imageURL = getClass().getResource(imagePath);
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                this.image = icon.getImage();
            } else {
                // Si no encuentra la imagen, intentar como archivo
                ImageIcon icon = new ImageIcon(imagePath);
                if (icon.getIconWidth() > 0) {
                    this.image = icon.getImage();
                } else {
                    System.err.println("No se pudo cargar la imagen: " + imagePath);
                    this.image = null;
                }
            }
        } catch (Exception e) {
            System.err.println("Error cargando imagen " + imagePath + ": " + e.getMessage());
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

// Nueva clase para el layout dinámico de imágenes estilo Discovery Mix
class DynamicMixLayout extends JPanel {
    public String[] getImages() {
        return images;
    }

    private String[] images;
    private Color placeholderColor;
    private String placeholderSymbol;
    private boolean initialized = false;

    public DynamicMixLayout(String[] images, Color placeholderColor, String placeholderSymbol) {
        this.images = images;
        this.placeholderColor = placeholderColor;
        this.placeholderSymbol = placeholderSymbol;
        setLayout(null);
        setOpaque(false);
    }

    @Override
    public void doLayout() {
        super.doLayout();
        if (!initialized || getWidth() > 0 && getHeight() > 0) {
            createLayout();
            initialized = true;
        }
    }

    private void createLayout() {
        removeAll();

        int width = getWidth();
        int height = getHeight();

        if (width <= 0 || height <= 0)
            return;

        // Imagen principal (grande, lado izquierdo) - 60% del ancho
        if (images.length > 0) {
            int mainWidth = (int) (width * 0.6);
            createImagePanel(0, 0, 0, mainWidth, height, 8);
        }

        // Panel derecho con imágenes más pequeñas
        int rightX = (int) (width * 0.62);
        int rightWidth = width - rightX;
        int smallHeight = (int) (height * 0.48);
        int gap = (int) (height * 0.04);

        // Imagen superior derecha
        if (images.length > 1) {
            createImagePanel(1, rightX, 0, rightWidth, smallHeight, 6);
        }

        // Imagen inferior derecha
        if (images.length > 2) {
            createImagePanel(2, rightX, smallHeight + gap, rightWidth, smallHeight, 6);
        }

        // Imagen superpuesta (más pequeña, centrada en el lado derecho)
        if (images.length > 3) {
            int overlaySize = Math.min(rightWidth / 2, smallHeight / 2);
            int overlayX = rightX + (rightWidth - overlaySize) / 2;
            int overlayY = (height - overlaySize) / 2;
            createImagePanel(3, overlayX, overlayY, overlaySize, overlaySize, 4);
        }

        revalidate();
        repaint();
    }

    private void createImagePanel(int index, int x, int y, int width, int height, int cornerRadius) {
        ImageCoverPanel imagePanel = new ImageCoverPanel(
                images[index],
                placeholderSymbol,
                placeholderColor);
        imagePanel.setBounds(x, y, width, height);
        add(imagePanel);
    }
}

// Botón de Mix mejorado con layout dinámico

// Clase para la flecha de navegación (AGREGAR ANTES DE LA CLASE SpotifyCloneUI)
class NavigationArrow extends JButton {
    private boolean isRight;
    private Color arrowColor = new Color(200, 200, 200);
    private Color hoverColor = Color.WHITE;
    private Color bgColor = new Color(0, 0, 0, 100);
    private Color hoverBgColor = new Color(0, 0, 0, 150);

    public NavigationArrow(boolean isRight) {
        this.isRight = isRight;
        setPreferredSize(new Dimension(50, 50));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo circular
        Color bg = getModel().isRollover() ? hoverBgColor : bgColor;
        g2.setColor(bg);
        g2.fillOval(5, 5, getWidth() - 10, getHeight() - 10);

        // Flecha
        Color arrow = getModel().isRollover() ? hoverColor : arrowColor;
        g2.setColor(arrow);
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int arrowSize = 8;

        if (isRight) {
            // Flecha derecha
            g2.drawLine(centerX - arrowSize / 2, centerY - arrowSize, centerX + arrowSize / 2, centerY);
            g2.drawLine(centerX + arrowSize / 2, centerY, centerX - arrowSize / 2, centerY + arrowSize);
        } else {
            // Flecha izquierda
            g2.drawLine(centerX + arrowSize / 2, centerY - arrowSize, centerX - arrowSize / 2, centerY);
            g2.drawLine(centerX - arrowSize / 2, centerY, centerX + arrowSize / 2, centerY + arrowSize);
        }

        g2.dispose();
    }
}

public class SpotifyCloneUI extends JFrame {

    // Variables para navegación (AGREGAR AL INICIO DE LA CLASE)
    private NowPlayingBar nowPlayingBar;
    private int currentMixSet = 0;
    private final int TOTAL_MIX_SETS = 2;
    private JPanel centerPanel;

    class MixButton extends JPanel {
        private Color defaultBg = new Color(35, 35, 35);
        private Color hoverBg = new Color(45, 45, 45);
        private Color pressedBg = new Color(55, 55, 55);
        private boolean isHovered = false;
        private boolean isPressed = false;
        private int cornerRadius = 12;
        private String mixName;
        private String artistName;
        private DynamicMixLayout mixLayout;
        private String audioPath;
        private Timer animationTimer;
        private float animationProgress = 0f;
        private final float ANIMATION_SPEED = 0.08f;

        public MixButton(String mixName, String artistName, String[] coverImages, Color placeholderColor,
                String placeholderSymbol, String audioPath) {
            this.mixName = mixName;
            this.artistName = artistName;
            this.audioPath = audioPath;

            setLayout(new BorderLayout(0, 8));
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

            mixLayout = new DynamicMixLayout(coverImages, placeholderColor, placeholderSymbol);
            mixLayout.setPreferredSize(new Dimension(280, 160));

            SwingUtilities.invokeLater(() -> {
                mixLayout.revalidate();
                mixLayout.repaint();
            });

            JPanel textPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    GradientPaint gradient = new GradientPaint(
                            0, 0, new Color(40, 40, 40, 200),
                            0, getHeight(), new Color(20, 20, 20, 220));
                    g2.setPaint(gradient);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.dispose();
                }
            };

            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false);
            textPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

            JLabel title = new JLabel(mixName);
            title.setForeground(Color.WHITE);
            title.setFont(new Font("SansSerif", Font.BOLD, 18));
            title.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel subtitle = new JLabel(artistName);
            subtitle.setForeground(new Color(180, 180, 180));
            subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
            subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

            textPanel.add(title);
            textPanel.add(Box.createVerticalStrut(2));
            textPanel.add(subtitle);

            add(mixLayout, BorderLayout.CENTER);
            add(textPanel, BorderLayout.SOUTH);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    isPressed = false;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    isPressed = true;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    isPressed = false;
                    repaint();
                    onMixClicked();
                }
            });

            animationTimer = new Timer(16, e -> {
                float target = isHovered ? 1f : 0f;
                if (Math.abs(animationProgress - target) > 0.01f) {
                    animationProgress += (target - animationProgress) * ANIMATION_SPEED;
                    repaint();
                }
            });
            animationTimer.start();

        }

        private void onMixClicked() {
            SpotifyCloneUI ui = (SpotifyCloneUI) SwingUtilities.getWindowAncestor(this);
            ui.updateNowPlayingBar(mixName, artistName, mixLayout.getImages()[0], audioPath);
        }
    }

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
        menuBarContainer.setBackground(new Color(25, 25, 25, 180)); // el último valor es la transparencia
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

        // DESDE AQUÍ REEMPLAZA TODO HASTA EL FINAL DEL CONSTRUCTOR

        // Panel principal que contendrá las flechas y el panel de mix
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(background);

        // Panel contenedor para los mix con flechas
        JPanel mixContainerPanel = new JPanel(new BorderLayout());
        mixContainerPanel.setBackground(background);
        mixContainerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel para los mix (GridLayout)
        centerPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        centerPanel.setBackground(background);

        // Crear flechas de navegación
        NavigationArrow leftArrow = new NavigationArrow(false);
        NavigationArrow rightArrow = new NavigationArrow(true);
        // Panel para la flecha izquierda
        JPanel leftArrowPanel = new JPanel(new GridBagLayout());
        leftArrowPanel.setBackground(background);
        leftArrowPanel.setPreferredSize(new Dimension(70, 0));
        leftArrowPanel.add(leftArrow);

        // Panel para la flecha derecha
        JPanel rightArrowPanel = new JPanel(new GridBagLayout());
        rightArrowPanel.setBackground(background);
        rightArrowPanel.setPreferredSize(new Dimension(70, 0));
        rightArrowPanel.add(rightArrow);

        // Eventos para las flechas
        leftArrow.addActionListener(e -> {
            if (currentMixSet > 0) {
                currentMixSet--;
                updateMixDisplay();
            }
        });

        rightArrow.addActionListener(e -> {
            if (currentMixSet < TOTAL_MIX_SETS - 1) {
                currentMixSet++;
                updateMixDisplay();
            }
        });

        // Ensamblar el panel de mix con flechas
        mixContainerPanel.add(leftArrowPanel, BorderLayout.WEST);
        mixContainerPanel.add(centerPanel, BorderLayout.CENTER);
        mixContainerPanel.add(rightArrowPanel, BorderLayout.EAST);

        // Inicializar con el primer conjunto de mix
        updateMixDisplay();

        add(topPanel, BorderLayout.NORTH);
        add(mixContainerPanel, BorderLayout.CENTER);
        nowPlayingBar = new NowPlayingBar();
        add(nowPlayingBar, BorderLayout.SOUTH);

    }

    // Método para actualizar los mix mostrados (AGREGAR ESTE MÉTODO)
    private void updateMixDisplay() {
        centerPanel.removeAll();

        // Diferentes conjuntos de imágenes para cada Mix
        String[][] allCoverImages = {
                // Mix 1 - Hip Hop/Rap
                { "/resources/cover1.png", "/resources/cover2.png", "/resources/cover3.png",
                        "/resources/cover.png" },
                // Mix 2 - Pop/Electronic
                { "/resources/Kris r1.png", "/resources/Kris r2.png", "/resources/Kris r3.png",
                        "/resources/Kris r4.png" },
                // Mix 3 - Rock/Alternative
                { "/resources/Vallenato1.png", "/resources/Vallenato2.png", "/resources/Vallenato3.png" },
                // Mix 4 - R&B/Soul
                { "/resources/salsa.png", "/resources/Salsa1.png", "/resources/Salsa2.png", "/resources/Salsa3.png" }

        };

        String[] mixNames = { "Discovery Mix", "Reggaeton Hits", "Vallenato Clásico", "Salsa Mix" };
        String[] audioFiles = {
                "resources/pirlo.wav",
                "resources/kris r.wav",
                "resources/sample.wav",
                "resources/sample.wav"
        };

        String[] mixSubtitles = { "Pirlo", "Kris R", "Binomio de Oro", "Frankie Ruiz" };

        Color[] placeholderColors = {
                new Color(255, 20, 147), // Rosa/magenta para Discovery
                new Color(0, 191, 255), // Azul cielo para Reggaeton
                new Color(255, 165, 0), // Naranja para Vallenato
                new Color(220, 20, 60) // Rojo para Salsa
        };
        String[] placeholderSymbols = { "🎵", "🔥", "🎸", "💃" };

        // Mix adicionales
        String[][] additionalCoverImages = {
                // Mix 5 - Merengue
                { "/resources/merengue1.png", "/resources/merengue2.png", "/resources/merengue3.png",
                        "/resources/merengue4.png" },
                // Mix 6 - Bachata
                { "/resources/bachata1.png", "/resources/bachata2.png", "/resources/bachata3.png",
                        "/resources/bachata4.png" },
                // Mix 7 - Cumbia
                { "/resources/cumbia1.png", "/resources/cumbia2.png", "/resources/cumbia3.png",
                        "/resources/cumbia4.png" },
                // Mix 8 - Tropical
                { "/resources/tropical1.png", "/resources/tropical2.png", "/resources/tropical3.png",
                        "/resources/tropical4.png" }
        };

        String[] additionalMixNames = { "Merengue Clásico", "Bachata Romántica", "Cumbia Colombiana",
                "Tropical Mix" };
        String[] additionalAudioFiles = {
                "resources/merengue.wav",
                "resources/bachata.wav",
                "resources/cumbia.wav",
                "resources/tropical.wav"
        };

        String[] additionalMixSubtitles = { "Juan Luis Guerra", "Romeo Santos", "Los Ángeles Azules",
                "Marc Anthony" };

        Color[] additionalPlaceholderColors = {
                new Color(50, 205, 50), // Verde para Merengue
                new Color(255, 69, 0), // Rojo-naranja para Bachata
                new Color(138, 43, 226), // Violeta para Cumbia
                new Color(255, 215, 0) // Dorado para Tropical
        };
        String[] additionalPlaceholderSymbols = { "🎺", "💕", "🪘", "🌴" };

        String[][] currentCoverImages;
        String[] currentMixNames;
        String[] currentMixSubtitles;
        Color[] currentPlaceholderColors;
        String[] currentPlaceholderSymbols;
        String[] currentAudioFiles;

        if (currentMixSet == 0) {
            currentCoverImages = allCoverImages;
            currentMixNames = mixNames;
            currentMixSubtitles = mixSubtitles;
            currentPlaceholderColors = placeholderColors;
            currentPlaceholderSymbols = placeholderSymbols;
            currentAudioFiles = audioFiles; // <-- esta línea es nueva
        } else {
            currentCoverImages = additionalCoverImages;
            currentMixNames = additionalMixNames;
            currentMixSubtitles = additionalMixSubtitles;
            currentPlaceholderColors = additionalPlaceholderColors;
            currentPlaceholderSymbols = additionalPlaceholderSymbols;
            currentAudioFiles = additionalAudioFiles; // <-- esta línea es nueva
        }

        // Crear los MixButtons
        for (int i = 0; i < 4; i++) {
            MixButton mixButton = new MixButton(
                    currentMixNames[i],
                    currentMixSubtitles[i],
                    currentCoverImages[i],
                    currentPlaceholderColors[i],
                    currentPlaceholderSymbols[i],
                    currentAudioFiles[i]);
            centerPanel.add(mixButton);
        }

        centerPanel.revalidate();
        centerPanel.repaint();
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