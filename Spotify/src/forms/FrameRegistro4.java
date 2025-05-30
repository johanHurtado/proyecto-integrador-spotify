package forms;

// Ajusta a tu estructura real
import DAO.UserDAO;
import entities.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Paso 4 del registro: acepta TyC y guarda el usuario.
 */
public class FrameRegistro4 extends JFrame {

    private Point initialClick;
    private final User user;
    private final UserDAO userDao = new UserDAO();

    public FrameRegistro4(User user) {
        this.user = user;

        /* ---------- Ventana ---------- */
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

        /* ---------- Barra estilo mac ---------- */
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 7));
        titleBar.setBackground(new Color(35, 35, 35));
        titleBar.setPreferredSize(new Dimension(500, 30));

        titleBar.add(macCircle(Color.RED  , () -> System.exit(0)));
        titleBar.add(macCircle(Color.ORANGE, () -> setState(Frame.ICONIFIED)));
        titleBar.add(macCircle(Color.GREEN , () -> setExtendedState(
                getExtendedState()==Frame.MAXIMIZED_BOTH? Frame.NORMAL:Frame.MAXIMIZED_BOTH)));

        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e){ initialClick = e.getPoint(); }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e){
                setLocation(getX()+e.getX()-initialClick.x,
                            getY()+e.getY()-initialClick.y);
            }
        });

        /* ---------- Contenido ---------- */
        JPanel content = new JPanel();
        content.setBackground(new Color(18,18,18));
        content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20,40,20,40));

        /* Logo */
        JLabel logo = new JLabel();
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            BufferedImage img = ImageIO.read(getClass().getClassLoader()
                                             .getResource("resources/icono_spotify.png"));
            logo.setIcon(new ImageIcon(img.getScaledInstance(60,60,Image.SCALE_SMOOTH)));
        } catch (IOException ex) {
            logo.setText("Spotify");
            logo.setFont(new Font("Arial",Font.BOLD,30));
            logo.setForeground(Color.WHITE);
        }

        /* Navegación */
        JPanel nav = new JPanel(); nav.setOpaque(false);
        nav.setLayout(new BoxLayout(nav,BoxLayout.X_AXIS));
        JButton back = new JButton("<");
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Arial",Font.BOLD,18));
        back.setContentAreaFilled(false);
        back.setBorderPainted(false);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addActionListener(e -> { dispose(); new SpotifyPremiumPlansUI(user); });
        nav.add(back); nav.add(Box.createHorizontalStrut(10));
        JLabel lblPaso = new JLabel("Paso 4 de 4");
        lblPaso.setForeground(Color.LIGHT_GRAY);
        nav.add(lblPaso);

        /* Texto legal */
        JLabel lblTitulo = label("Términos y Condiciones", 18);
        JCheckBox cbPub  = check("Prefiero no recibir publicidad de Spotify");
        JCheckBox cbData = check("Compartir mis datos con los proveedores de contenido "
                               + "de Spotify para fines de marketing.");
        JLabel legal1 = htmlLabel("Al registrarte, aceptas los <b>Términos y Condiciones</b>.");
        JLabel legal2 = htmlLabel("Al registrarte, aceptas la <b>Política de Privacidad</b>.");

        /* Botón registrarse */
        JButton btnReg = roundedButton("Registrarte");
        btnReg.addActionListener(e -> {
            boolean ok = userDao.insert(user);
            if (ok) showToast(true, null);
            else    showToast(false, "No se pudo insertar el usuario.");
        });

        /* Ensamblar */
        content.add(logo);          content.add(Box.createVerticalStrut(20));
        content.add(nav);           content.add(Box.createVerticalStrut(10));
        content.add(lblTitulo);     content.add(Box.createVerticalStrut(20));
        content.add(cbPub);         content.add(Box.createVerticalStrut(10));
        content.add(cbData);        content.add(Box.createVerticalStrut(30));
        content.add(legal1);        content.add(Box.createVerticalStrut(5));
        content.add(legal2);        content.add(Box.createVerticalStrut(30));
        content.add(btnReg);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(18,18,18));
        root.add(titleBar,BorderLayout.NORTH);
        root.add(content ,BorderLayout.CENTER);
        setContentPane(root);
        setVisible(true);
    }

    /* ---------- helpers UI ---------- */
    private JLabel label(String text,int size){
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial",Font.BOLD,size));
        l.setForeground(Color.WHITE);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }
    private JCheckBox check(String t){
        JCheckBox c=new JCheckBox(t);
        c.setForeground(Color.WHITE);
        c.setBackground(new Color(18,18,18));
        c.setFocusPainted(false);
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }
    private JLabel htmlLabel(String html){
        JLabel l=new JLabel("<html><font color='white'>"+html+"</font></html>");
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
    private JButton roundedButton(String text){
        return new JButton(text){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                                 RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0,0,getWidth(),getHeight(),30,30);
                super.paintComponent(g); g2.dispose();
            }
        };
    }
    /** Botón redondo estilo mac */
    private JButton macCircle(Color color,Runnable action){
        return new JButton(){
            { setPreferredSize(new Dimension(14,14));
              setContentAreaFilled(false);
              setBorderPainted(false);
              setFocusPainted(false);
              addActionListener(e -> action.run()); }
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                                  RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0,0,getWidth(),getHeight());
            }
        };
    }

    /* ---------- Toast ---------- */
    private void showToast(boolean ok,String err){
        JFrame t=new JFrame(); t.setUndecorated(true);
        t.setSize(330,110); t.setLocationRelativeTo(this);
        t.setShape(new RoundRectangle2D.Double(0,0,330,110,20,20));
        JPanel p=new JPanel(); p.setBackground(new Color(18,18,18));
        JLabel l=new JLabel(ok? "¡Registro completado!" :
                                 "<html><font color=red>Error: "+err+"</font></html>");
        l.setForeground(Color.WHITE); l.setFont(new Font("Arial",Font.BOLD,14));
        p.add(l); t.add(p); t.setVisible(true);
        new Timer(1700,e->{
            t.dispose();
            if(ok){ new FrameInicioSesion(); dispose(); }
        }){{ setRepeats(false); }}.start();
    }

    /* ---------- test rápido ---------- */
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            User u=new User();
            u.setUsername("prueba");
            u.setEmail("test@usantoto.edu.co");
            u.setPassword("1234");
            u.setRoleId(2);
            u.setSubscriptionId(1);
            new FrameRegistro4(u);
        });
    }
}
