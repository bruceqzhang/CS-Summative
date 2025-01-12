package Main;
import javax.swing.JPanel;
import GameObjects.Caveman;
import GameObjects.Hooman;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.image.BufferedImage;
import java.awt.GridLayout;


public class ShopMenu extends JPanel{
    private int coins;
    private BufferedImage background;
    private Game game;
    

    // Constructor
    public ShopMenu(Game game, BufferedImage background){
        this.background = background;
        this.game = game;
        // Setting the shop bounds 
        this.setBounds(0, (int)(3/4.0 * game.getHeight()), game.getWidth(),(int)(1/4.0*game.getHeight()));
        // Adds the shop to the JFrame
        game.add(this);
        // Making the shop invisible at the start
        setVisible(false);

        // Adds a panel on the shop menu for displaying Hooman buttons
        JPanel hoomanPanel = new JPanel();
        hoomanPanel.setLayout(new GridLayout(2,6,0,0));

        for(Hooman hooman: Hooman.getSortedHoomans()){
            // Uses html tags to label the buttons with multiple lines of text
            JButton hoomanButton = new JButton("<html>"+hooman.getName()+"<br>"+hooman.getCost()+"</html>", new ImageIcon(hooman.getSprite().getScaledInstance(96,32, Image.SCALE_SMOOTH)));

            hoomanButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    game.startPlacementMode(hooman.getClass()); // Enable placement mode for Caveman
                }
            });

            hoomanPanel.add(hoomanButton);
        }

        JButton sortButton = new JButton("A-Z");

        add(hoomanPanel);

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
