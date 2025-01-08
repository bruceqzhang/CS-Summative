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
public class Shop extends JPanel{
    private static int coins;
    private BufferedImage background;

    // Constructor
    public Shop(BufferedImage background){
        this.background = background;
        coins = 50;
        JButton sortButton = new JButton("A-Z");
    }
    
    // Method called by default by repaint() that will essentially repaint the shop 
   @Override
    public void paintComponent(Graphics g){
        // Drawing all child components of this
        super.paintComponent(g);
        // Drawing the background of the shop
        g.drawImage(background.getScaledInstance(getWidth(),getHeight(), Image.SCALE_SMOOTH ), 0, 0, getWidth(), getHeight(), null);
    }


    

    public void arrangeAZ(){}
    public void arrangeCost(){}

    // Toggles the visibility of the shop based on whether its already visibleß
    public void toggleVisibility() {//Chatgpt
        setVisible(!this.isVisible());
        revalidate();
        repaint();
    }
    
}
