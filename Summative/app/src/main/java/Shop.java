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
    private static Tower[] towers;
    private BufferedImage background;

    public Shop(BufferedImage background, Rectangle bounds){
        this.background = background;
        setBounds(bounds);
        coins = 50;
        JButton sortButton = new JButton("A-Z");
    }
    
   @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        g.drawImage(background.getScaledInstance(getWidth(),getHeight(), Image.SCALE_SMOOTH ), 0, 0, getWidth(), getHeight(), null);
    }


    

    public void arrangeAZ(){}
    public void arrangeCost(){}

    public void toggleVisibility() {//Chatgpt
        setVisible(!this.isVisible());
        revalidate();
        repaint();
    }
    
}
