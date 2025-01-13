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
    private static final int RANGE = 20;
    private static final int SPLASH = 20;
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
            sprites[1] = ImageIO.read(inputStream).getScaledInstance(getSize()/2,getSize()/2,Image.SCALE_AREA_AVERAGING);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return sprites;
    }


    @Override
    public void animateAttack() {
        swingTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        if (getVisibility()){
            // Draw the Caveman
            g.drawImage(SPRITE, (int)(getPosition().getX()), (int)getPosition().getY(), getSize(), getSize(), null);
            Graphics2D g2d = (Graphics2D) g;
            // Draw the sword swing (a simple line as an example)
            if (swingAngle > 0) {
                int x1 = (int) getPosition().getX() + 30;
                int y1 = (int) getPosition().getY() + 30;
                
                // Set the pivot point for rotation at the base of the caveman
                g2d.translate(x1, y1);
                g2d.rotate(Math.toRadians(swingAngle)+Math.PI/2); // Rotate by the swing angle

                // Draw the attack sprite (offset it by negative x1 and y1 after translation)
                g2d.drawImage(WEAPON_SPRITE, WEAPON_SPRITE.getWidth(null) / 2, -WEAPON_SPRITE.getHeight(null) / 2 ,null);

                // Reset the graphics transformations
                g2d.rotate(-Math.toRadians(swingAngle));
                g2d.translate(-x1, -y1);
            }
        }
    }
    
}
