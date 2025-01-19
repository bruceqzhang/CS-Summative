package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SidePanel extends JPanel{
    private ImageIcon startLabelIcon, gameOverLabelIcon, shopToggleButtonIcon, roundLabelIcon, livesLabelIcon, coinsLabelIcon;
    private Font font;
    private JButton shopToggleButton, messageButton;
    private JLabel roundLabel, livesLabel, coinsLabel;
    private Game game;

    public SidePanel(Game game){

        this.game = game;
        // this.setBounds(0,240,160,320);
        // game.add(this);

        importResources();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setOpaque(false);

        setBackground(null);
          

        setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        
        messageButton = new JButton(startLabelIcon);
        messageButton.setBorder(null);
        add(messageButton);
        messageButton.setVisible(true);


        setFont(font);

        roundLabel = new JLabel(""+game.getRound(),roundLabelIcon,SwingConstants.LEFT);
        roundLabel.setFont(font);
        roundLabel.setForeground(Color.white);
        add(roundLabel);

        livesLabel = new JLabel(""+game.getLives(),livesLabelIcon,SwingConstants.LEFT);
        livesLabel.setFont(font);
        livesLabel.setForeground(Color.white);        
        add(livesLabel);

        coinsLabel = new JLabel(""+game.getCoins(),coinsLabelIcon,SwingConstants.LEFT);
        coinsLabel.setFont(font);
        coinsLabel.setForeground(Color.white);
        add(coinsLabel);

        shopToggleButton = new JButton(shopToggleButtonIcon);
        shopToggleButton.setBackground(null);
        shopToggleButton.setBorder(null);
        add(shopToggleButton);
        
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(new Color(0,0,0,127));
        messageButton.setFont(font);
        if (game.getRoundFinished()){
            g.fillRoundRect(0,getInsets().top,180,300+getInsets().top,16,16);
            messageButton.setIcon(startLabelIcon);
            messageButton.setText("");
        }
        else if (game.getGameOver()){
            g.fillRoundRect(0,getInsets().top,180,300+getInsets().top,16,16);
            messageButton.setIcon(gameOverLabelIcon);
            messageButton.setText("");
        }
        else{
            g.fillRoundRect(0,60+getInsets().top,180,240 + getInsets().top,16,16);
            messageButton.setIcon(null);
            messageButton.setFont(font.deriveFont(Font.BOLD,60));
            messageButton.setText(" ");
        }
        messageButton.setBorder(null);
        roundLabel.setText(""+game.getRound());
        livesLabel.setText(""+game.getLives());
        coinsLabel.setText(""+game.getCoins());
    }

    public JButton getShopToggleButton(){
        return shopToggleButton;
    }

    public JButton getMessageButton(){
        return messageButton;
    }

    public void importResources(){
        int width = (int)getWidth();
        int height = (int)getHeight();
        System.out.println(width + " " + height);

        // Scales the icon to match the size of the button

        startLabelIcon = new ImageIcon(new ImageIcon("Summative/app/src/main/java/Resources/startLabel.png").getImage().getScaledInstance((int)(160),(int)(1/4.0*240), Image.SCALE_SMOOTH));
        gameOverLabelIcon = new ImageIcon(new ImageIcon("Summative/app/src/main/java/Resources/gameOverLabel.png").getImage().getScaledInstance((int)(160),(int)(1/4.0*240), Image.SCALE_SMOOTH));
        shopToggleButtonIcon = new ImageIcon(new ImageIcon("Summative/app/src/main/java/Resources/shopButton.png").getImage().getScaledInstance(160,(int)(1/4.0*240), Image.SCALE_SMOOTH)); 
        roundLabelIcon = new ImageIcon(new ImageIcon("Summative/app/src/main/java/Resources/roundLabel.png").getImage().getScaledInstance((int)(0.5*160),(int)(1/4.0*240), Image.SCALE_SMOOTH));
        livesLabelIcon = new ImageIcon(new ImageIcon("Summative/app/src/main/java/Resources/livesLabel.png").getImage().getScaledInstance((int)(0.5*160),(int)(1/4.0*240), Image.SCALE_SMOOTH));
        coinsLabelIcon = new ImageIcon(new ImageIcon("Summative/app/src/main/java/Resources/coinsLabel.png").getImage().getScaledInstance((int)(0.5*160),(int)(1/4.0*240), Image.SCALE_SMOOTH)); 
       
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("Summative/app/src/main/java/Resources/PressStart2P-Regular.ttf")).deriveFont(Font.BOLD, 16);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
