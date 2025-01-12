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

public class Caveman extends Hooman{
    
    private Timer swingTimer;
    private JPanel gameScreen;
    private int swingAngle;
    
    private static final String NAME = "Caveman";
    private static final BufferedImage SPRITE = importSprites()[0];
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

    
    private static BufferedImage[] importSprites() {
        BufferedImage[] sprites = new BufferedImage[1];
        try{
            InputStream inputStream = Caveman.class.getResourceAsStream("/Resources/caveman.png");
            sprites[0] = ImageIO.read(inputStream);
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
                if (swingAngle > 90) { // Reset after full swing
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

            // Draw the sword swing (a simple line as an example)
            if (swingAngle > 0) {
                int x1 = (int) getPosition().getX() + 10;
                int y1 = (int) getPosition().getY() + 10;
                int x2 = x1 + (int) (50 * Math.cos(Math.toRadians(swingAngle)));
                int y2 = y1 - (int) (50 * Math.sin(Math.toRadians(swingAngle)));
                g.setColor(Color.RED);
                g.drawLine(x1, y1, x2, y2);
            }
        }
    }
    
}
