import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;

import org.w3c.dom.css.Rect;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JButton;
import java.awt.image.BufferedImage;

public class Upgrade extends JPanel{
    private static Rectangle upgradeBounds = new Rectangle(0, (int)(3/4.0 * getHeight()), getWidth(),(int)(1/4.0*getHeight()))
    private BufferedImage background;
    private Hooman hooman;

    // Constructor
    public Upgrade(BufferedImage background/* , Hooman hooman*/){
        this.background = background;
        this.hooman = hooman;
        JButton closeButton = new JButton("Back");
        closeButton.setBounds(0,0,(int)(0.075*getWidth()),(int)(0.075*getHeight()));
        closeButton.setVisible(true);
        add(closeButton);
    }
    
    // Method called by default by repaint() that will essentially repaint the shop 
   @Override
    public void paintComponent(Graphics g){
        // Drawing all child components of this
        super.paintComponent(g);
        // Drawing the background of the shop
        g.drawImage(background.getScaledInstance(getWidth(),getHeight(), Image.SCALE_SMOOTH ), 0, 0, getWidth(), getHeight(), null);
        //g.drawImage(hooman.getSprite(), (int)(1/4.0*getWidth()), (int)(1/2.0*getHeight()),  null);
    }


    

    // Toggles the visibility of the shop based on whether its already visibleß
    public void toggleVisibility() {//Chatgpt
        setVisible(!this.isVisible());
        revalidate();
        repaint();
    }
    
}

