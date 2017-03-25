/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.UserInterface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import javax.swing.JPanel;

/**
 *
 * @author Nikhil
 */
public class CircularProgressBar extends JPanel {

    int progress = 0;
    String text = "";

    public void updateProgress(int progress_value) {
        progress = progress_value;
        repaint();
    }

    public void updateProgress(String progress_value) {
        text = progress_value;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(this.getWidth() / 2, this.getHeight() / 2);
        g2.rotate(Math.toRadians(270));
        Arc2D.Float arc = new Arc2D.Float(Arc2D.PIE);
        Ellipse2D circle = new Ellipse2D.Float(0, 0, 110, 110);
        arc.setFrameFromCenter(new Point(0, 0), new Point(120, 120));
        circle.setFrameFromCenter(new Point(0, 0), new Point(110, 110));
        arc.setAngleStart(1);
        arc.setAngleExtent(-progress * 3.6);
        g2.setColor(Color.red);
        g2.draw(arc);
        g2.fill(arc);
        g2.setColor(Color.white);
        g2.draw(circle);
        g2.fill(circle);
        g2.setColor(Color.red);
        g2.rotate(Math.toRadians(90));
        g.setFont(new Font("Verdana", Font.PLAIN, 50));
        FontMetrics fm = g2.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(progress + "%", g);
        int x = (0 - (int) r.getWidth() / 2);
        int y = (0 - (int) r.getHeight() / 2 + fm.getAscent());
        g2.drawString(progress + "%", x, y);

    }

    //<editor-fold defaultstate="collapsed" desc=" Alternate Paint Function ">
//    public void paintComponent(Graphics g) {
//        AffineTransform at = new AffineTransform();
//        super.paintComponent(g);
//        //g.setColor(new Color(new Random().nextInt(255),new Random().nextInt(255),new Random().nextInt(255)));
//        //g.fillRect(0,0, this.getWidth(), this.getHeight());
//        Graphics2D g2 = (Graphics2D) g;
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        AffineTransform save_ctx = g2.getTransform();
//        AffineTransform current_ctx = new AffineTransform();
//        current_ctx.concatenate(at);
//        current_ctx.translate(this.getWidth() / 2, this.getHeight() / 2);
//        current_ctx.rotate(Math.toRadians(270));
//        g2.transform(current_ctx);
//        g2.setColor(Color.gray);
//        g2.setStroke(new BasicStroke(12.0F,BasicStroke.CAP_ROUND,BasicStroke.CAP_ROUND));
//        g2.drawArc(-(getHeight() - 40 / 2)/2, -(getHeight() - 40 / 2)/2, getHeight() - 20, getHeight() - 20, 0, -360);
//        g2.setColor(Color.red);
//        g2.drawArc(-(getHeight() - 40 / 2)/2, -(getHeight() - 40 / 2)/2, getHeight() - 20, getHeight() - 20, 0, -(int) (progress * 3.6));
//        
//        AffineTransform current_ctx2 = new AffineTransform();
//        current_ctx2.rotate(Math.toRadians(90));
//        g2.transform(current_ctx2);
//        g.setFont(new Font("Verdana",Font.PLAIN, 50));
//        FontMetrics fm = g2.getFontMetrics();
//        Rectangle2D r = fm.getStringBounds(progress + "%", g);
//        int x = (0- (int)r.getWidth()/2);
//        int y = (0- (int)r.getHeight()/2 + fm.getAscent());
//        g2.drawString(progress + "%", x, y);
//        g2.transform(save_ctx);
//    }
    //</editor-fold>
}
