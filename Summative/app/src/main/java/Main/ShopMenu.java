package Main;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import GameObjects.Caveman;
import GameObjects.Hooman;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.image.BufferedImage;
import java.awt.GridLayout;


public class ShopMenu extends JPanel{
    private int coins;
    private BufferedImage background;
    private Game game;
    private JButton sortButton;
    

    // Constructor
    public ShopMenu(Game game, BufferedImage background){
        this.background = background;
        this.game = game;
        // Setting the shop bounds 
        this.setBounds(0, (int)(3/4.0 * game.getHeight()), game.getWidth(),(int)(1/4.0*game.getHeight()));

        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        //setBorder(BorderFactory.createLineBorder(new Color(125,90,50),16));

        // Adds the shop to the JFrame
        game.add(this);
        // Making the shop invisible at the start
        setVisible(false);

        setPreferredSize(new Dimension(game.getWidth(), game.getHeight() / 4));

        // Adds a panel on the shop menu for displaying Hooman buttons
        setLayout(new GridLayout(2,7,32,32));
        setOpaque(false);
        sortButton = new JButton("A-Z");
        sortButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Hooman.sort();
                switch (Hooman.getCurrentSortType()){
                    case 0 -> sortButton.setText("A-Z");
                    case 1 -> sortButton.setText("Cost");
                    case 2 -> sortButton.setText("Evolution");
                }
                removeAll();
                displayButtons();
                
            }
            
        });
        displayButtons();

    }
    

    private void displayButtons(){
        add(new JLabel(""));
        for (int i = 0; i<Hooman.getSortedHoomans().length/2; i++){
            Hooman hooman = Hooman.getSortedHoomans()[i];
            // Uses html tags to label the buttons with multiple lines of text
            JButton hoomanButton = new JButton();
            configureHoomanButton(hoomanButton, hooman);
            add(hoomanButton);
        }
        add(sortButton);
        for (int i = Hooman.getSortedHoomans().length/2; i<Hooman.getSortedHoomans().length; i++){
            Hooman hooman = Hooman.getSortedHoomans()[i];
            JButton hoomanButton = new JButton();
            configureHoomanButton(hoomanButton, hooman);
            add(hoomanButton);
        }
    }

    private void configureHoomanButton(JButton hoomanButton, Hooman hooman) {
        hoomanButton.setPreferredSize(new Dimension(getWidth() / 7, getHeight() / 2));
        // Uses html tags to label the buttons with multiple lines of text
        hoomanButton.setText("<html>"+hooman.getName()+"<br>"+hooman.getCost()+" Coins</html>");
        hoomanButton.setIcon(new ImageIcon(hooman.getSprite().getScaledInstance(32,32, Image.SCALE_AREA_AVERAGING)));
        hoomanButton.setOpaque(true);
        hoomanButton.setBackground(new Color(175,130,100));
        hoomanButton.setForeground(new Color(250,250,250));
        hoomanButton.setMargin(new Insets(0, 0, 0, 0));
        hoomanButton.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED, new Color(175,130,100), new Color(100,75,50)));
        hoomanButton.setContentAreaFilled(true);
        hoomanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.startPlacementMode(hooman.getClass()); // Enable placement mode for Hooman
            }
        });
    }

    // Method called by default by repaint() that will essentially repaint the shop 
    @Override
    public void paintComponent(Graphics g){
        // Drawing all child components of this
        super.paintComponent(g);
        // Drawing the background of the shop
        g.drawImage(background.getScaledInstance(getWidth(),getHeight(), Image.SCALE_SMOOTH ), 0, 0, getWidth(), getHeight(), null);
    }

    // Toggles the visibility of the shop based on whether its already visibleß
    public void toggleVisibility() {//Chatgpt
        setVisible(!this.isVisible());
        revalidate();
        repaint();
    }
    
}
