package forms;

import DAO.UserDAO;
import entities.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Paso 1 del registro – Introducir e-mail y validarlo contra la BD.
 */
public class FrameRegistro1 extends JFrame {

    private Point initialClick;
    private JLabel errorLabel;
    private JTextField emailField;

    public FrameRegistro1() {

        /* ---------- ventana ---------- */
        setUndecorated(true);
        setSize(500, 700);
        setMinimumSize(new Dimension(500, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),40,40));
        addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e){
                setShape(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),40,40));
            }
        });

        /* ---------- title bar ---------- */
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT,6,7));
        titleBar.setBackground(new Color(35,35,35));
        titleBar.setPreferredSize(new Dimension(500,30));

        JButton close = createMacCircle(Color.RED),
                min   = createMacCircle(Color.ORANGE),
                max   = createMacCircle(Color.GREEN);
        close.addActionListener(e -> System.exit(0));
        min  .addActionListener(e -> setState(Frame.ICONIFIED));
        max  .addActionListener(e -> setExtendedState(
                getExtendedState()==Frame.MAXIMIZED_VERT? Frame.NORMAL:Frame.MAXIMIZED_BOTH));
        titleBar.add(close); titleBar.add(min); titleBar.add(max);

        titleBar.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){ initialClick=e.getPoint(); }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseDragged(MouseEvent e){
                setLocation(getX()+e.getX()-initialClick.x, getY()+e.getY()-initialClick.y);
            }
        });

        /* ---------- contenido ---------- */
        JPanel content = new JPanel();
        content.setBackground(new Color(18,18,18));
        content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(30,40,30,40));

        // logo
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

        // botón atrás
        JButton back = new JButton("<");
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Arial",Font.BOLD,18));
        back.setContentAreaFilled(false); back.setBorderPainted(false);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addActionListener(e -> { dispose(); new FrameLogin(); });
        JPanel nav = new JPanel(); nav.setOpaque(false); nav.setLayout(new BoxLayout(nav,BoxLayout.X_AXIS));
        nav.add(back); nav.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));

        JLabel heading = new JLabel("<html><div style='text-align:center'>Regístrate<br>para empezar<br>a escuchar<br>contenido</div></html>",
                                    SwingConstants.CENTER);
        heading.setFont(new Font("Arial",Font.BOLD,24)); heading.setForeground(Color.WHITE);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblEmail = new JLabel("Dirección de email");
        lblEmail.setForeground(Color.WHITE); lblEmail.setAlignmentX(Component.CENTER_ALIGNMENT);

        /* ---- campo email ---- */
        emailField = new JTextField(){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                                 RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0,0,getWidth(),getHeight(),30,30);
                super.paintComponent(g);
            }
        };
        styleField(emailField);

        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial",Font.PLAIN,12));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setVisible(false);

        /* ---- botón siguiente ---- */
        JButton btnNext = new JButton("Siguiente"){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                                 RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0,0,getWidth(),getHeight(),30,30);
                super.paintComponent(g);
            }
        };
        btnNext.setBackground(new Color(30,215,96));
        btnNext.setForeground(Color.BLACK);
        btnNext.setFont(new Font("Arial",Font.BOLD,14));
        btnNext.setFocusPainted(false);
        btnNext.setContentAreaFilled(false);
        btnNext.setOpaque(false);
        btnNext.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNext.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));

        /* ---- acción siguiente ---- */
        btnNext.addActionListener(e -> {
            String email = emailField.getText().trim();

            if(!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")){
                showError("Formato de e-mail inválido.");
                return;
            }
            if(new UserDAO().emailExists(email)){
                showError("Ese correo ya está registrado.");
                return;
            }

            // ok: crear User parcial y avanzar
            User u = new User();
            u.setEmail(email);

            dispose();
            new FrameRegistroPassword(u);          // paso 2 con el User
        });

        /* ---- ensamblar ---- */
        content.add(logo);
        content.add(Box.createVerticalStrut(10));
        content.add(nav);
        content.add(Box.createVerticalStrut(10));
        content.add(heading);
        content.add(Box.createVerticalStrut(30));
        content.add(lblEmail);
        content.add(emailField);
        content.add(Box.createVerticalStrut(5));
        content.add(errorLabel);
        content.add(Box.createVerticalStrut(30));
        content.add(btnNext);

        setLayout(new BorderLayout());
        add(titleBar,BorderLayout.NORTH);
        add(content ,BorderLayout.CENTER);
        setVisible(true);
    }

    /* ---------- helpers ---------- */
    private void showError(String msg){
        emailField.setBorder(BorderFactory.createLineBorder(Color.RED));
        errorLabel.setText("⚠ "+msg);
        errorLabel.setVisible(true);
    }
    private void styleField(JTextComponent f){
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
        f.setBackground(Color.BLACK);
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        f.setOpaque(false);
        f.setUI(new BasicTextFieldUI(){
            @Override protected void paintSafely(Graphics g){
                Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                                  RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(f.getBackground());
                g2.fillRoundRect(0,0,f.getWidth(),f.getHeight(),30,30);
                super.paintSafely(g);
            }
        });
    }
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

    public static void main(String[] a){
        SwingUtilities.invokeLater(FrameRegistro1::new);
    }
}
