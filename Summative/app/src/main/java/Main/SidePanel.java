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
    private ImageIcon shopToggleButtonIcon, roundLabelIcon, livesLabelIcon, coinsLabelIcon;
    private Font font;
    private JButton shopToggleButton;
    private JLabel roundLabel, livesLabel, coinsLabel;
    private Game game;

    public SidePanel(Game game){

        this.game = game;
        this.setBounds(0,240,160,320);
        game.add(this);

        importResources();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setOpaque(false);

        setBackground(null);

        setFont(font);
          
        setForeground(Color.white);

        setBorder(BorderFactory.createEmptyBorder(5,10,5,0));
        
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
        g.fillRoundRect(0,0,160,240,16,16);
    }

    public JButton getShopToggleButton(){
        return shopToggleButton;
    }

    public void importResources(){
         // Scales the icon to match the size of the button
        shopToggleButtonIcon = new ImageIcon(new ImageIcon("Summative/app/src/main/java/Resources/shopButton.png").getImage().getScaledInstance(this.getWidth(),(int)(1/4.0*this.getHeight()), Image.SCALE_SMOOTH)); 
        roundLabelIcon = new ImageIcon(new ImageIcon("Summative/app/src/main/java/Resources/roundLabel.png").getImage().getScaledInstance((int)(0.5*this.getWidth()),(int)(1/4.0*this.getHeight()), Image.SCALE_SMOOTH));
        livesLabelIcon = new ImageIcon(new ImageIcon("Summative/app/src/main/java/Resources/livesLabel.png").getImage().getScaledInstance((int)(0.5*this.getWidth()),(int)(1/4.0*this.getHeight()), Image.SCALE_SMOOTH));
        coinsLabelIcon = new ImageIcon(new ImageIcon("Summative/app/src/main/java/Resources/coinsLabel.png").getImage().getScaledInstance((int)(0.5*this.getWidth()),(int)(1/4.0*this.getHeight()), Image.SCALE_SMOOTH));
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("Summative/app/src/main/java/Resources/PressStart2P-Regular.ttf")).deriveFont(Font.BOLD, 16);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
