package forms;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Pantalla inicial con fondo y botón que abre FrameInicioSesion.
 */
public class FrameLogin extends JFrame {

    private BufferedImage backgroundImage;
    private Point initialClick;

    public FrameLogin() {
        /* ---- Ventana sin bordes y redondeada ---- */
        setUndecorated(true);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
            }
        });

        /* ---- Imagen de fondo ---- */
        try {
            backgroundImage = ImageIO.read(getClass().getClassLoader()
                                           .getResource("resources/Spoti.jpg"));
        } catch (IOException ex) { ex.printStackTrace(); }

        JPanel fondo = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null)
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        fondo.setLayout(new BorderLayout());

        /* ---- Barra tipo mac ---- */
        RoundedPanel titleBar = new RoundedPanel(20);
        titleBar.setBackground(new Color(35, 35, 35));
        titleBar.setPreferredSize(new Dimension(1280, 30));
        titleBar.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 7));

        JButton btnClose = createMacCircle(Color.RED);
        JButton btnMin   = createMacCircle(Color.ORANGE);
        JButton btnMax   = createMacCircle(Color.GREEN);
        btnClose.addActionListener(e -> System.exit(0));
        btnMin  .addActionListener(e -> setState(Frame.ICONIFIED));
        btnMax  .addActionListener(e -> {
            setExtendedState(getExtendedState()==Frame.MAXIMIZED_BOTH? Frame.NORMAL : Frame.MAXIMIZED_BOTH);
        });
        titleBar.add(btnClose); titleBar.add(btnMin); titleBar.add(btnMax);

        /* arrastrar ventana */
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { initialClick = e.getPoint(); }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (getExtendedState()!=Frame.NORMAL) setExtendedState(Frame.NORMAL);
                setLocation(getX()+e.getX()-initialClick.x, getY()+e.getY()-initialClick.y);
            }
        });

        /* ---- Panel semitransparente + formulario simple ---- */
        JPanel overlay = new JPanel(new GridBagLayout());
        overlay.setOpaque(true);
        overlay.setBackground(new Color(0,0,0,160));

        RoundedPanel card = new RoundedPanel(20);
        card.setBackground(Color.BLACK);
        card.setBorder(BorderFactory.createEmptyBorder(40,40,40,40));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(400,300));

        /* logo + texto */
        JPanel logoP = new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        logoP.setOpaque(false);
        try{
            BufferedImage logo = ImageIO.read(getClass().getClassLoader()
                                              .getResource("resources/icono_spotify.png"));
            logoP.add(new JLabel(new ImageIcon(logo.getScaledInstance(40,40,Image.SCALE_SMOOTH))));
        }catch(IOException ex){ ex.printStackTrace();}
        JLabel logotxt = new JLabel("Spotify");
        logotxt.setFont(new Font("Arial",Font.BOLD,32));
        logotxt.setForeground(Color.WHITE);
        logoP.add(logotxt);

        JLabel slogan = new JLabel("<html><center>Millones de canciones.<br>Gratis en Spotify.</center></html>",
                                   JLabel.CENTER);
        slogan.setFont(new Font("Arial",Font.BOLD,20));
        slogan.setForeground(Color.WHITE);

        /* botón iniciar sesión */
        JButton btnLogin = new JButton("Iniciar sesión"){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                                  RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0,0,getWidth(),getHeight(),30,30);
                super.paintComponent(g);
            }
        };
        btnLogin.setBackground(new Color(30,215,96));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFont(new Font("Arial",Font.BOLD,16));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(12,25,12,25));
        btnLogin.setFocusPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setOpaque(false);
        btnLogin.addActionListener(e -> {
            new FrameInicioSesion();   // ← abre pantalla de login real
            dispose();
        });

        /* enlace registro */
        JLabel lblNew = new JLabel("¿Eres nuevo en Spotify?");
        lblNew.setForeground(Color.LIGHT_GRAY);
        JLabel linkReg = new JLabel("Registrarse gratis");
        linkReg.setForeground(new Color(30,215,96));
        linkReg.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        linkReg.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){ new FrameRegistro1(); dispose(); }
        });

        /* arma card */
        card.add(logoP);
        card.add(Box.createVerticalStrut(20));
        card.add(slogan);
        card.add(Box.createVerticalStrut(30));
        card.add(btnLogin);
        card.add(Box.createVerticalStrut(8));
        card.add(lblNew);
        card.add(linkReg);

        overlay.add(card);
        fondo.add(titleBar, BorderLayout.NORTH);
        fondo.add(overlay , BorderLayout.CENTER);

        setContentPane(fondo);
        setVisible(true);
    }

    /* circulitos mac */
    private JButton createMacCircle(Color c){
        return new JButton(){
            { setPreferredSize(new Dimension(14,14));
              setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);}
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                                  RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c); g2.fillOval(0,0,getWidth(),getHeight());
            }
        };
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(FrameLogin::new);
    }

    /* Panel con esquinas redondeadas */
    static class RoundedPanel extends JPanel{
        private final int radius;
        RoundedPanel(int r){ radius=r; setOpaque(false);}
        @Override protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                              RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0,0,getWidth(),getHeight(),radius,radius);
        }
    }
}
