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
    //private BufferedImage shopImage;

    public Shop(BufferedImage background, Rectangle bounds){
        //this.tileSet = tileSet;
        this.background = background;
        setBounds(bounds);
        //setSize((int)getBounds().getWidth(), (int)getBounds().getHeight()); 
        //createShopImage();
        coins = 50;
        JButton sortButton = new JButton("A-Z");
    }
    
   @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        g.drawImage(background.getScaledInstance(getWidth(),getHeight(), Image.SCALE_SMOOTH ), 0, 0, getWidth(), getHeight(), null);
    }

    /**private void createShopImage(){
        shopImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = shopImage.createGraphics();

        for (int x = 0; x<getWidth(); x+=32){
            for (int y = 0; y<getHeight(); y+=32){
               g2d.drawImage(tileSet.getSubimage(14*16, 12*16, 16, 16), x,y,32,32,null);
        
            }
        }
  
    }*/

    

    public void arrangeAZ(){}
    public void arrangeCost(){}

    public void toggleVisibility() {//Chatgpt
        setVisible(!this.isVisible());
        revalidate();
        repaint();
    }
    
}
