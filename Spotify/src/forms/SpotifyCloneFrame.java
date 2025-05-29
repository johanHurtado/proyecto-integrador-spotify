package forms;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class SpotifyCloneFrame extends JFrame {
    private boolean isPlaying = false;
    private boolean isMaximized = false;
    private boolean shuffleOn = false;
    private boolean repeatAll = false;
    private boolean repeatOne = false;
    private JButton shuffleButton, prevButton, playPauseButton, nextButton, repeatButton, repeatOneButton;

    private static final Font SONG_FONT   = new Font("SansSerif", Font.BOLD, 14);
    private static final Font ARTIST_FONT = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font HEADER_FONT = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font TITLE_FONT  = new Font("SansSerif", Font.BOLD, 16);
    private static final Font INFO_FONT   = new Font("SansSerif", Font.PLAIN, 14);

    public SpotifyCloneFrame() {
        setUndecorated(true);
        setTitle("Spotify Clone");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        initUI();
        applyWindowShape();
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) { applyWindowShape(); }
        });
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // --- Top bar ---
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        titleBar.setBackground(new Color(24, 24, 24));
        titleBar.add(createWindowButton(new Color(237, 67, 69), () -> System.exit(0)));
        titleBar.add(createWindowButton(new Color(241, 196, 15), () -> setState(JFrame.ICONIFIED)));
        titleBar.add(createWindowButton(new Color(39, 174, 96), () -> toggleMaximize()));
        add(titleBar, BorderLayout.NORTH);

        // --- Playlist panel ---
        String[] cols = {"Canción", "Artista", "Duración"};
        Object[][] rows = {
            {"TU TU TU", "Clave Especial, Edgardo Núñez", "2:32"},
            {"Pareja Del Año", "Sebastián Yatra, Myke Towers", "3:15"},
            {"COQUETA", "Fuerza Regida, Grupo Frontera", "4:02"},
            {"Uno Se Cura", "Raulín Rosendo", "5:32"},
            {"Saber Amar", "Penyair", "4:10"},
            {"UNA NOCHE DE LOCURA", "Blessd", "2:44"},
            {"Ojalá", "Ryan Castro", "2:36"},
            {"lo que hay x aquí", "Rels B", "2:35"},
            {"TENGO FE", "Feid", "3:26"}
        };
        DefaultTableModel model = new DefaultTableModel(rows, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(SONG_FONT);
        table.setRowHeight(50);
        styleTable(table);
        // renderer columna 0 con icono + texto
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                                                          boolean sel, boolean foc,
                                                          int row, int col) {
                JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                p.setOpaque(true);
                p.setBackground(sel ? t.getSelectionBackground() : t.getBackground());
                String song = v.toString();
                JLabel ico = new JLabel(new ImageIcon("resources/" + song.toLowerCase().replace(' ', '_') + ".png"));
                JLabel lbl = new JLabel(song);
                lbl.setFont(SONG_FONT);
                lbl.setForeground(sel ? t.getSelectionForeground() : t.getForeground());
                p.add(ico);
                p.add(lbl);
                return p;
            }
        });
        JScrollPane scroll = new JScrollPane(table);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(new RoundedBorder(15));
        JPanel listWrapper = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(24, 24, 24));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        listWrapper.setOpaque(false);
        listWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        listWrapper.add(scroll, BorderLayout.CENTER);
        add(listWrapper, BorderLayout.CENTER);

        // --- Now Playing ---
        JPanel now = new JPanel(new BorderLayout());
        now.setBackground(new Color(18, 18, 18));
        now.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(40, 40, 40)));
        now.setPreferredSize(new Dimension(getWidth(), 100));

        // izquierda: cover + info
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        left.setBackground(now.getBackground());
        left.add(new JLabel(new ImageIcon("resources/cover.png")));
        JPanel text = new JPanel(new GridLayout(2, 1));
        text.setBackground(now.getBackground());
        JLabel title = new JLabel("Simples Corazones"); title.setFont(TITLE_FONT); title.setForeground(Color.WHITE);
        JLabel artist = new JLabel("Fonseca — Agustín"); artist.setFont(INFO_FONT); artist.setForeground(new Color(180, 180, 180));
        text.add(title);
        text.add(artist);
        left.add(text);
        now.add(left, BorderLayout.WEST);

                // centro: controles
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        controls.setBackground(now.getBackground());
        controls.setOpaque(true);
        controls.setPreferredSize(new Dimension(getWidth(), 60));
        // Carga absoluta de recursos para asegurar rutas
        String basePath = System.getProperty("user.dir") + "/resources/";
        shuffleButton = iconButton(basePath + "shuffle.png"); shuffleButton.addActionListener(e -> toggleShuffle());
        prevButton    = iconButton(basePath + "prev.png");    prevButton.addActionListener(e -> playPrevious());
        playPauseButton = iconButton(basePath + "play.png"); playPauseButton.addActionListener(e -> togglePlayPause());
        nextButton    = iconButton(basePath + "next.png");    nextButton.addActionListener(e -> playNext());
        repeatButton  = iconButton(basePath + "repeat.png");  repeatButton.addActionListener(e -> toggleRepeatAll());
        repeatOneButton = iconButton(basePath + "repeat_one.png"); repeatOneButton.addActionListener(e -> toggleRepeatOne());
        controls.add(shuffleButton);
        controls.add(prevButton);
        controls.add(playPauseButton);
        controls.add(nextButton);
        controls.add(repeatButton);
        controls.add(repeatOneButton);
        now.add(controls, BorderLayout.CENTER);


        // progreso
        JProgressBar prog = new JProgressBar(0, 100);
        prog.setValue(20);
        prog.setPreferredSize(new Dimension(getWidth(), 4));
        prog.setBackground(new Color(50, 50, 50));
        prog.setForeground(new Color(30, 215, 96));
        now.add(prog, BorderLayout.SOUTH);

        add(now, BorderLayout.SOUTH);
    }

    private void toggleMaximize() {
        if (!isMaximized) setExtendedState(JFrame.MAXIMIZED_BOTH);
        else setExtendedState(JFrame.NORMAL);
        isMaximized = !isMaximized;
    }

    private void applyWindowShape() {
        setShape(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
    }

    private JButton createWindowButton(Color c, Runnable action) {
        JButton b = new JButton() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
            public boolean contains(int x, int y) {
                return new Ellipse2D.Float(0, 0, getWidth(), getHeight()).contains(x, y);
            }
        };
        b.setPreferredSize(new Dimension(12, 12)); b.setOpaque(false);
        b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.addActionListener(e -> action.run());
        return b;
    }

    private JButton iconButton(String path) {
        JButton b = new JButton(new ImageIcon(path));
        b.setBorderPainted(false); b.setContentAreaFilled(false);
        return b;
    }

    private void styleTable(JTable table) {
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(new Color(24, 24, 24));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(50, 50, 50));
        table.setSelectionForeground(Color.WHITE);
        JTableHeader h = table.getTableHeader();
        h.setBackground(new Color(18, 18, 18));
        h.setForeground(Color.WHITE);
        h.setBorder(null);
        h.setFont(HEADER_FONT);
        DefaultTableCellRenderer hr = (DefaultTableCellRenderer) h.getDefaultRenderer();
        hr.setHorizontalAlignment(JLabel.CENTER);
        h.setDefaultRenderer(hr);
        h.setReorderingAllowed(false);
    }

    // stubs
    private void togglePlayPause() { isPlaying = !isPlaying; playPauseButton.setIcon(new ImageIcon(isPlaying?"resources/pause.png":"resources/play.png")); }
    private void toggleShuffle() { shuffleOn = !shuffleOn; }
    private void playPrevious() { }
    private void playNext() { }
    private void toggleRepeatAll() { repeatAll = !repeatAll; }
    private void toggleRepeatOne() { repeatOne = !repeatOne; }

    static class RoundedBorder implements Border {
        private final int r;
        public RoundedBorder(int radius) { this.r = radius; }
        public Insets getBorderInsets(Component c) { return new Insets(r, r, r, r); }
        public boolean isBorderOpaque() { return false; }
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getBackground());
            g2.drawRoundRect(x, y, w-1, h-1, r, r);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SpotifyCloneFrame().setVisible(true));
    }
}
