
package edu.usta.clases;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;




public class PanelDegradado extends JPanel{
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        Color colorInicio = new Color(0, 0, 0); // Azul
        Color colorFin = new Color(55, 55, 55);  // Blanco

        int width = getWidth();
        int height = getHeight();

        GradientPaint gp = new GradientPaint(0, 0, colorInicio, 0, height, colorFin);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
    }
    
}
