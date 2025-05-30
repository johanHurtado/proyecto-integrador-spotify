package forms;

import entities.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Paso 1 del registro: introduce y valida la contraseña.
 */
public class FrameRegistroPassword extends JFrame {

    private Point initialClick;
    private final User user;                 // viene de FrameRegistro1
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private JLabel checkLetra, checkEspecial, checkLongitud;

    /*------------------------------------------------------------------
      Constructor recibe el User con el e-mail ya seteado
     -----------------------------------------------------------------*/
    public FrameRegistroPassword(User user) {
        this.user = user;

        /* ---------- Ventana ---------- */
        setUndecorated(true);
        setSize(500, 700);
        setMinimumSize(new Dimension(500,700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),40,40));
        addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e){
                setShape(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),40,40));
            }
        });

        /* ---------- Title bar ---------- */
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT,6,7));
        titleBar.setBackground(new Color(35,35,35));
        titleBar.setPreferredSize(new Dimension(500,30));
        titleBar.add(macCircle(Color.RED  , () -> System.exit(0)));
        titleBar.add(macCircle(Color.ORANGE, () -> setState(Frame.ICONIFIED)));
        titleBar.add(macCircle(Color.GREEN , () -> setExtendedState(
                getExtendedState()==Frame.MAXIMIZED_BOTH? Frame.NORMAL:Frame.MAXIMIZED_BOTH)));

        titleBar.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){ initialClick=e.getPoint(); }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseDragged(MouseEvent e){
                setLocation(getX()+e.getX()-initialClick.x,
                            getY()+e.getY()-initialClick.y);
            }
        });

        /* ---------- Contenido ---------- */
        JPanel content = new JPanel();
        content.setBackground(new Color(18,18,18));
        content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(30,40,30,40));

        /* Logo */
        JLabel logo = new JLabel();
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        try{
            BufferedImage img = ImageIO.read(getClass().getClassLoader()
                                             .getResource("resources/icono_spotify.png"));
            logo.setIcon(new ImageIcon(img.getScaledInstance(60,60,Image.SCALE_SMOOTH)));
        }catch(IOException ex){
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
        back.addActionListener(e -> { dispose(); new FrameRegistro1(); });
        JLabel lblPaso = new JLabel("Paso 1 de 4");
        lblPaso.setForeground(Color.LIGHT_GRAY);
        nav.add(back); nav.add(Box.createHorizontalStrut(10)); nav.add(lblPaso);

        /* Título */
        JLabel lblTitulo = new JLabel("Crea una contraseña");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial",Font.BOLD,18));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        /* Campo contraseña */
        JLabel lblPwd = new JLabel("Contraseña");
        lblPwd.setForeground(Color.WHITE); lblPwd.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = roundedPasswordField();
        passwordField.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){ validarRequisitos(); }
        });

        /* Etiquetas de requisitos */
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial",Font.PLAIN,12));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setVisible(false);

        JLabel lblReq = new JLabel("La contraseña debe tener al menos");
        lblReq.setForeground(Color.WHITE);
        lblReq.setFont(new Font("Arial",Font.BOLD,12));
        lblReq.setAlignmentX(Component.CENTER_ALIGNMENT);

        checkLetra     = requisito("● 1 letra");
        checkEspecial  = requisito("● 1 número o carácter especial (ej: #, ?, !, &)");
        checkLongitud  = requisito("● 10 caracteres");

        /* Botón Siguiente */
        JButton btnNext = roundedButton("Siguiente");
        btnNext.addActionListener(e -> onNext());

        /* Ensamblar */
        content.add(logo);
        content.add(Box.createVerticalStrut(20));
        content.add(nav);
        content.add(Box.createVerticalStrut(10));
        content.add(lblTitulo);
        content.add(Box.createVerticalStrut(30));
        content.add(lblPwd);
        content.add(passwordField);
        content.add(Box.createVerticalStrut(10));
        content.add(errorLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(lblReq);
        content.add(checkLetra);
        content.add(checkEspecial);
        content.add(checkLongitud);
        content.add(Box.createVerticalStrut(30));
        content.add(btnNext);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(18,18,18));
        root.add(titleBar,BorderLayout.NORTH);
        root.add(content ,BorderLayout.CENTER);
        setContentPane(root);
        setVisible(true);
    }

    /* ---------- validar y avanzar ---------- */
    private void onNext(){
        String pwd = String.valueOf(passwordField.getPassword());

        if(pwd.length()>=10 && pwd.matches(".*[a-zA-Z].*")
           && pwd.matches(".*[0-9!@#$%^&*()_+=?-].*")){
            errorLabel.setVisible(false);

            /* ↳ Guarda la contraseña en el objeto User
               Si vas a encriptar / hashear, hazlo aquí */
            user.setPassword(pwd);

            new FrameRegistro2(user);
            dispose();
        }else{
            errorLabel.setText("⚠ Contraseña inválida. Usa mínimo 10 caracteres, una letra y un símbolo o número.");
            errorLabel.setVisible(true);
        }
    }

    /* ---------- helpers UI ---------- */
    private void validarRequisitos(){
        String pwd = String.valueOf(passwordField.getPassword());
        Color ok = new Color(30,215,96), bad = Color.LIGHT_GRAY;
        checkLetra   .setForeground(pwd.matches(".*[a-zA-Z].*")                ? ok : bad);
        checkEspecial.setForeground(pwd.matches(".*[0-9!@#$%^&*()_+=?-].*")    ? ok : bad);
        checkLongitud.setForeground(pwd.length()>=10                           ? ok : bad);
    }
    private JPasswordField roundedPasswordField(){
        return new JPasswordField(){
            { setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
              setBackground(Color.BLACK); setForeground(Color.WHITE);
              setCaretColor(Color.WHITE);
              setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
              setOpaque(false); }
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                                 RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0,0,getWidth(),getHeight(),30,30);
                super.paintComponent(g); g2.dispose();
            }
        };
    }
    private JLabel requisito(String t){
        JLabel l=new JLabel(t);
        l.setFont(new Font("Arial",Font.PLAIN,13));
        l.setForeground(Color.LIGHT_GRAY);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }
    private JButton roundedButton(String text){
        JButton b=new JButton(text){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                                 RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0,0,getWidth(),getHeight(),30,30);
                super.paintComponent(g); g2.dispose();
            }
        };
        b.setBackground(new Color(30,215,96));
        b.setForeground(Color.BLACK);
        b.setFont(new Font("Arial",Font.BOLD,14));
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
        return b;
    }
    private JButton macCircle(Color color,Runnable action){
        return new JButton(){
            { setPreferredSize(new Dimension(14,14));
              setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
              addActionListener(e -> action.run()); }
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                                  RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color); g2.fillOval(0,0,getWidth(),getHeight());
            }
        };
    }

    /* ---------- main prueba ---------- */
    public static void main(String[] a){
        SwingUtilities.invokeLater(() -> {
            User u=new User(); u.setEmail("test@usantoto.edu.co");
            new FrameRegistroPassword(u);
        });
    }
}
