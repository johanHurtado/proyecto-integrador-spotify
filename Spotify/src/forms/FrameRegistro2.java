package forms;

import entities.User;                           // ajusta si tu paquete es otro

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Paso 2 del registro: nombre, fecha de nacimiento y género.
 */
public class FrameRegistro2 extends JFrame {

    private Point initialClick;
    private final User user;        // ← viene del paso anterior

    /* campos locales */
    private JTextField nombreField, diaField, anioField;
    private JComboBox<String> mesBox;
    private JRadioButton hombre, mujer, noBinario, otro, prefNo;

    public FrameRegistro2(User user) {
        this.user = user;

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
                getExtendedState()==Frame.MAXIMIZED_BOTH? Frame.NORMAL:Frame.MAXIMIZED_BOTH));

        titleBar.add(close); titleBar.add(min); titleBar.add(max);

        titleBar.addMouseListener(new MouseAdapter(){ public void mousePressed(MouseEvent e){
            initialClick=e.getPoint(); }});
        titleBar.addMouseMotionListener(new MouseMotionAdapter(){ public void mouseDragged(MouseEvent e){
            setLocation(getX()+e.getX()-initialClick.x, getY()+e.getY()-initialClick.y); }});

        /* ---------- contenido ---------- */
        JPanel content = new JPanel();
        content.setBackground(new Color(18,18,18));
        content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20,40,20,40));

        /* logo */
        JLabel logo = new JLabel();
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        try{
            BufferedImage img = ImageIO.read(getClass().getClassLoader()
                                             .getResource("resources/icono_spotify.png"));
            logo.setIcon(new ImageIcon(img.getScaledInstance(60,60,Image.SCALE_SMOOTH)));
        }catch(IOException ex){
            logo.setText("Spotify"); logo.setFont(new Font("Arial",Font.BOLD,30));
            logo.setForeground(Color.WHITE);
        }

        /* nav back */
        JButton btnBack = new JButton("<");
        btnBack.setFont(new Font("Arial",Font.BOLD,18));
        btnBack.setForeground(Color.WHITE);
        btnBack.setContentAreaFilled(false); btnBack.setBorderPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> { dispose();
            new FrameRegistroPassword(user).setVisible(true);        // ← volver con los datos
        });
        JPanel nav = new JPanel(); nav.setOpaque(false);
        nav.setLayout(new BoxLayout(nav,BoxLayout.X_AXIS));
        nav.add(btnBack);
        JLabel lblPaso = new JLabel("Paso 2 de 4");
        lblPaso.setForeground(Color.LIGHT_GRAY); nav.add(Box.createHorizontalStrut(10)); nav.add(lblPaso);

        /* título */
        JLabel lblTitulo = new JLabel("Cuéntanos sobre ti");
        lblTitulo.setFont(new Font("Arial",Font.BOLD,18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        /* nombre */
        nombreField = createRoundedTextField("");
        JLabel lblNombre = label("Nombre");

        /* fecha */
        JLabel lblFecha = label("Fecha de nacimiento");

        diaField  = createRoundedTextField("dd");
        anioField = createRoundedTextField("aaaa");

        mesBox = new JComboBox<>(new String[]{
            "Mes", "Enero","Febrero","Marzo","Abril","Mayo","Junio",
            "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"});
        mesBox.setMaximumSize(new Dimension(150,40));
        mesBox.setBackground(Color.BLACK);
        mesBox.setForeground(Color.WHITE);

        JPanel fechaP = new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        fechaP.setBackground(content.getBackground());
        fechaP.add(diaField); fechaP.add(mesBox); fechaP.add(anioField);

        /* género */
        JLabel lblGenero = label("Género");
        JPanel generoP = new JPanel(new GridLayout(2,3,10,5));
        generoP.setBackground(content.getBackground());
        hombre    = radio("Hombre"); mujer = radio("Mujer");
        noBinario = radio("No binario"); otro = radio("Otro");
        prefNo    = radio("Prefiero no aclararlo");
        ButtonGroup bg = new ButtonGroup();
        for(JRadioButton r: new JRadioButton[]{hombre,mujer,noBinario,otro,prefNo}) bg.add(r);
        generoP.add(hombre); generoP.add(mujer); generoP.add(noBinario);
        generoP.add(otro); generoP.add(prefNo);

        /* botón siguiente */
        JButton btnNext = new JButton("Siguiente"){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                                 RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0,0,getWidth(),getHeight(),30,30);
                super.paintComponent(g); g2.dispose();
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

        btnNext.addActionListener(e -> onNext());

        /* ensamblar */
        content.add(logo);            content.add(Box.createVerticalStrut(20));
        content.add(nav);             content.add(Box.createVerticalStrut(10));
        content.add(lblTitulo);       content.add(Box.createVerticalStrut(20));
        content.add(lblNombre);       content.add(nombreField);
        content.add(label("Este nombre aparecerá en tu perfil"));
        content.add(Box.createVerticalStrut(20));
        content.add(lblFecha);        content.add(fechaP);
        content.add(Box.createVerticalStrut(20));
        content.add(lblGenero);       content.add(generoP);
        content.add(Box.createVerticalStrut(30));
        content.add(btnNext);

        setLayout(new BorderLayout());
        add(titleBar,BorderLayout.NORTH);
        add(content ,BorderLayout.CENTER);
        setVisible(true);
    }

    /* ---------- Validar y avanzar ---------- */
    private void onNext(){
        String nombre = nombreField.getText().trim();
        String dia = diaField.getText().trim(), mes = (String)mesBox.getSelectedItem(),
               anio = anioField.getText().trim();

        boolean genOk = hombre.isSelected()||mujer.isSelected()||
                        noBinario.isSelected()||otro.isSelected()||prefNo.isSelected();

        /* validaciones simples */
        if(nombre.isEmpty() || !dia.matches("\\d{2}") || mesBox.getSelectedIndex()==0 ||
           !anio.matches("\\d{4}") || !genOk){
            showWarn("Por favor, completa todos los campos.");
            return;
        }
        int d=Integer.parseInt(dia), m=mesBox.getSelectedIndex(), y=Integer.parseInt(anio);
        if(d<1||d>31||y<1900||y>2100){
            showWarn("Fecha de nacimiento inválida.");
            return;
        }

        /* llenar objeto user */
        user.setUsername(nombre);
       

        /* avanzar al paso 3 */
        new SpotifyPremiumPlansUI(user);        // ⚠ siguiente frame
        dispose();
    }
    private String selectedGender(){
        if(hombre.isSelected())    return "Hombre";
        if(mujer.isSelected())     return "Mujer";
        if(noBinario.isSelected()) return "No binario";
        if(otro.isSelected())      return "Otro";
        return "Prefiero no aclararlo";
    }

    /* ---------- helpers UI ---------- */
    private JTextField createRoundedTextField(String ph){
        JTextField f = new JTextField(ph){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                                 RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0,0,getWidth(),getHeight(),30,30);
                super.paintComponent(g); g2.dispose();
            }
        };
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
        f.setBackground(Color.BLACK); f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        f.setOpaque(false);
        return f;
    }
    private JRadioButton radio(String t){
        JRadioButton r = new JRadioButton(t);
        r.setForeground(Color.WHITE); r.setBackground(new Color(18,18,18)); r.setFocusPainted(false);
        return r;
    }
    private JLabel label(String t){
        JLabel l=new JLabel(t); l.setForeground(Color.WHITE); l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setFont(new Font("Arial",Font.BOLD,13));
        return l;
    }
    private void showWarn(String msg){
        JOptionPane.showMessageDialog(this,msg,"Campos incompletos",
                                      JOptionPane.WARNING_MESSAGE);
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
        SwingUtilities.invokeLater(() -> new FrameRegistro2(new User()));
    }
}
