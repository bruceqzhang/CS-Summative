import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;

import org.w3c.dom.css.Rect;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JButton;
import java.awt.image.BufferedImage;
public class Shop extends JPanel{
    private static int coins;
    private static Tower[] towers;
    private BufferedImage tileSet;
    private BufferedImage shopImage;

    public Shop(BufferedImage tileSet, Rectangle bounds){
        this.tileSet = tileSet;
        setBounds(bounds);
        //setSize((int)getBounds().getWidth(), (int)getBounds().getHeight()); 
        createShopImage();
        coins = 50;
        JButton sortButton = new JButton("A-Z");
    }
    
   @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        //g.drawImage(shopImage, getX(), getY(), getWidth(), getHeight(), null);
        //g.drawImage(tileSet.getSubimage(14*16, 12*16, 16, 16), 0, 0, 32 ,32, null);
        
        //g.setColor(new Color(250,0,120));
        //g.fillRect(32, 32, 32, 32);
       
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        
    }

    private void createShopImage(){
        shopImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = shopImage.createGraphics();

        for (int x = (int)getX(); x<(int)getX()+getWidth(); x+=32){
            for (int y = (int)getY(); y<(int)getY()+getWidth(); y+=32){
               g2d.drawImage(tileSet.getSubimage(14*16, 12*16, 16, 16), x,y,32,32,null);
        
            }
        }
        g2d.dispose();
    }

    public void arrangeAZ(){}
    public void arrangeCost(){}

    public void toggleVisibility() {//Chatgpt
        setVisible(!this.isVisible());
        revalidate();
        repaint();
    }
    
}
