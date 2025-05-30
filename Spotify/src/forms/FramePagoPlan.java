package forms;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.text.ParseException;

public class FramePagoPlan extends JFrame {

    private Point initialClick;

    /* Campos de tarjeta */
    private JFormattedTextField numero;
    private JFormattedTextField fecha;
    private JFormattedTextField cvv;

    public FramePagoPlan() {

        /* ------------- ventana ------------- */
        setUndecorated(true);
        setSize(380, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),30,30));
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e){
                setShape(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),30,30));
            }
        });

        Color bg     = new Color(18,18,18);
        Color text   = Color.WHITE;
        Color accent = new Color(30,215,96);

        /* ------------- title bar ------------- */
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setPreferredSize(new Dimension(380,30));
        titleBar.setBackground(new Color(35,35,35));

        // circulitos mac
        JPanel mac = new JPanel(new FlowLayout(FlowLayout.LEFT,6,7));
        mac.setOpaque(false);
        JButton c = createMacCircle(Color.RED), o = createMacCircle(Color.ORANGE),
                g = createMacCircle(Color.GREEN);
        c.addActionListener(e -> System.exit(0));
        o.addActionListener(e -> setState(Frame.ICONIFIED));
        g.addActionListener(e -> setExtendedState(
                getExtendedState()==Frame.MAXIMIZED_BOTH? Frame.NORMAL:Frame.MAXIMIZED_BOTH));
        mac.add(c); mac.add(o); mac.add(g);
        titleBar.add(mac, BorderLayout.WEST);

        // botón atrás
        JButton back = new JButton("<");
        back.setForeground(text);
        back.setFont(new Font("Arial",Font.BOLD,16));
        back.setContentAreaFilled(false);
        back.setBorderPainted(false);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addActionListener(e -> {
            dispose();
            new SpotifyPremiumPlansUI(null);            // ⚠  abre pantalla anterior
        });
        JPanel backP = new JPanel(new FlowLayout(FlowLayout.RIGHT,6,7));
        backP.setOpaque(false); backP.add(back);
        titleBar.add(backP, BorderLayout.EAST);

        // draggable
        titleBar.addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e){
            initialClick=e.getPoint(); }});
        titleBar.addMouseMotionListener(new MouseMotionAdapter(){ public void mouseDragged(MouseEvent e){
            int dx=e.getX()-initialClick.x, dy=e.getY()-initialClick.y;
            setLocation(getX()+dx,getY()+dy);
        }});

        /* ------------- contenido ------------- */
        JPanel main = new JPanel();
        main.setBackground(bg);
        main.setLayout(new BoxLayout(main,BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(15,20,15,20));

        JLabel lblPlan = new JLabel("Premium Individual");
        lblPlan.setForeground(text);
        lblPlan.setFont(new Font("Arial",Font.BOLD,18));
        lblPlan.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblPrecio = new JLabel("Pago único $16,900 COP / mes");
        lblPrecio.setForeground(Color.LIGHT_GRAY);
        lblPrecio.setFont(new Font("Arial",Font.PLAIN,12));
        lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.add(Box.createVerticalStrut(10));
        main.add(lblPlan); main.add(Box.createVerticalStrut(5)); main.add(lblPrecio);
        main.add(Box.createVerticalStrut(20));

        /* ---- panel tarjeta ---- */
        JPanel tarjeta = createRoundedPanel();
        tarjeta.setBackground(new Color(28,28,28));
        tarjeta.setLayout(new BoxLayout(tarjeta,BoxLayout.Y_AXIS));
        tarjeta.setBorder(new EmptyBorder(15,15,15,15));
        tarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblMetodo = new JLabel("Tarjeta de crédito o débito");
        lblMetodo.setForeground(text); lblMetodo.setFont(new Font("Arial",Font.BOLD,13));
        lblMetodo.setAlignmentX(Component.CENTER_ALIGNMENT);

        numero = makeMasked("#### #### #### ####", " ");
        fecha  = makeMasked("##/##", "_"); fecha.setMaximumSize(new Dimension(80,40));
        cvv    = makeMasked("###", "_");    cvv  .setMaximumSize(new Dimension(60,40));

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        fila.setBackground(tarjeta.getBackground());
        fila.add(fecha); fila.add(cvv);

        tarjeta.add(lblMetodo); tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(numero);    tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(fila);

        main.add(tarjeta); main.add(Box.createVerticalStrut(20));

        /* ---- botón comprar ---- */
        JButton pagar = new JButton("Comprar ahora"){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0,0,getWidth(),getHeight(),30,30);
                super.paintComponent(g); g2.dispose();
            }
        };
        pagar.setBackground(accent); pagar.setForeground(Color.BLACK);
        pagar.setFont(new Font("Arial",Font.BOLD,14));
        pagar.setFocusPainted(false); pagar.setContentAreaFilled(false); pagar.setOpaque(false);
        pagar.setAlignmentX(Component.CENTER_ALIGNMENT);
        pagar.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));

        pagar.addActionListener(e -> {
            String cn = numero.getText().replace(" ", ""),
                   ex = fecha.getText(),
                   cv = cvv.getText();
            if(!cn.matches("\\d{16}") || !ex.matches("(0[1-9]|1[0-2])/\\d{2}") || !cv.matches("\\d{3}")){
                showDialog("Tarjeta inválida.","Error");
            }else{
                showDialog("Pago realizado con éxito.","Listo");
                new FrameRegistro4(null);            // ⚠ abre siguiente paso
                dispose();
            }
        });
        main.add(pagar); main.add(Box.createVerticalStrut(20));

        /* ------------- ensamblar ------------- */
        JPanel cont=new JPanel(new BorderLayout());
        cont.add(titleBar,BorderLayout.NORTH);
        cont.add(main,    BorderLayout.CENTER);
        setContentPane(cont);
        setVisible(true);
    }

    /* === helpers === */
    private JFormattedTextField makeMasked(String mask, String place){
        try{
            MaskFormatter fmt=new MaskFormatter(mask); fmt.setPlaceholderCharacter(place.charAt(0));
            JFormattedTextField f=new JFormattedTextField(fmt); styleField(f); return f;
        }catch(ParseException e){ JFormattedTextField f=new JFormattedTextField(); styleField(f); return f; }
    }
    private void styleField(JTextComponent f){
        f.setFont(new Font("Arial",Font.PLAIN,14));
        f.setForeground(Color.WHITE); f.setBackground(Color.BLACK); f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
        f.setUI(new BasicTextFieldUI(){
            @Override protected void paintSafely(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(f.getBackground());
                g2.fillRoundRect(0,0,f.getWidth(),f.getHeight(),30,30);
                g2.dispose(); super.paintSafely(g);
            }
        });
    }
    private void showDialog(String msg,String title){
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
    private JPanel createRoundedPanel(){
        return new JPanel(){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
                super.paintComponent(g); g2.dispose();
            }
        };
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
        SwingUtilities.invokeLater(FramePagoPlan::new);
    }

    /* panel redondeado para title bar */
    static class RoundedPanel extends JPanel{
        private final int r;
        RoundedPanel(int r){ this.r=r; setOpaque(false);}
        @Override protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0,0,getWidth(),getHeight(),r,r);
            g2.dispose(); super.paintComponent(g);
        }
    }
}
