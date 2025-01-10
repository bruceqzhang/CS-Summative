package Main;
import javax.swing.JPanel;

import GameObjects.Hooman;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JButton;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class UpgradeMenu extends JPanel{
    private BufferedImage background;
    private Hooman hooman;
    private Game game;
    private static ArrayList<UpgradeMenu> upgradeMenus;

    // Constructor
    public UpgradeMenu (Game game, BufferedImage background/* , Hooman hooman*/){
        this.background = background;
        this.hooman = hooman;
        this.game = game;
        upgradeMenus.add(this);

        this.setLayout(null);
        this.setBounds(0, (int)(3/4.0 * game.getHeight()), game.getWidth(),(int)(1/4.0*game.getHeight()));
        game.add(this);
        this.setVisible(true);
        
        JButton closeButton = new JButton("Back");
        closeButton.setBounds(0,0,(int)(0.075*game.getWidth()),(int)(0.075*game.getHeight()));
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


    
    public ArrayList<UpgradeMenu> getUpgradeMenus(){
        return upgradeMenus;
    }

    // Toggles the visibility of the shop based on whether its already visibleß
    public void close() {//Chatgpt
        setVisible(false);
        revalidate();
        repaint();
    }
    
}

