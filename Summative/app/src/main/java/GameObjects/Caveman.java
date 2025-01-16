package GameObjects;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;

public class Caveman extends Hooman{
    
    private Timer swingTimer;
    private JPanel gameScreen;
    private int swingAngle;
    
    private static final String NAME = "Caveman";
    private static final Image SPRITE = importSprites()[0];
    private static final Image WEAPON_SPRITE = importSprites()[1];
    private static final int EVOLUTION_INDEX = 0;
    private static final int DAMAGE = 20;
    private static final int RANGE = 100;
    private static final int SPLASH = 100;
    private static final int RELOAD_SPEED = 1000;
    private static final int COST = 30;
    

    public Caveman(Point position, boolean isActive, boolean isVisible, JPanel gameScreen){
        super(NAME, SPRITE, position, isActive, isVisible,
        EVOLUTION_INDEX, DAMAGE, RANGE, SPLASH, RELOAD_SPEED, COST);


        // Panel where the game is rendered
        this.gameScreen = gameScreen; 
        swingAngle = 0;
    }

    
    private static Image[] importSprites() {
        Image[] sprites = new Image[2];
        try{
            InputStream inputStream = Caveman.class.getResourceAsStream("/Resources/caveman.png");
            sprites[0] = ImageIO.read(inputStream);
            inputStream = Caveman.class.getResourceAsStream("/Resources/cavemanClub.png");
            sprites[1] = ImageIO.read(inputStream).getScaledInstance(getSize(),getSize(),Image.SCALE_AREA_AVERAGING);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return sprites;
    }


    @Override
    public void animateAttack() {
        swingTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findTargetAliens();
                if (getTargetAliens().isEmpty()) {
                    swingAngle = 0;
                    gameScreen.repaint();
                    return;
                }
                swingAngle += 15; // Increment swing angle
                if (swingAngle > 360) { // Reset after full swing
                    swingAngle = 0;
                    swingTimer.stop();
                }
                gameScreen.repaint(); // Trigger re-drawing of the panel
            }
        });
        swingTimer.start();
        
    }

    @Override
    // Draw the Caveman and its attack animation
    public void draw(Graphics g) {
        // Draw the Caveman
        g.drawImage(SPRITE, (int)(getPosition().getX()), (int)getPosition().getY(), getSize(), getSize(), null);
        // Draw the club swing
        if (swingAngle > 0) {
            Graphics2D g2d = (Graphics2D) g.create();
            int startX = (int) (getPosition().getX() + getSize()/2.0);
            int startY = (int) (getPosition().getY() + getSize()/2.0);
            
            // Set the pivot point for rotation at the base of the caveman
            g2d.translate(startX, startY);
            // Rotate by the swing angle
            g2d.rotate(Math.toRadians(swingAngle));

            // Draw the attack weapon (offset it by negative startX and startY after translation)
            g2d.drawImage(WEAPON_SPRITE, -WEAPON_SPRITE.getWidth(null) / 2, -WEAPON_SPRITE.getHeight(null) ,null);

            // Reset the graphics transformations
            g2d.dispose();
        }
        
    }


    @Override
    public void reload() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reload'");
    }
    
}
