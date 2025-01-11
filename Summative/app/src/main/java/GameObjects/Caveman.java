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
    private static final int MAX_UPGRADE = 3;
    private static final String NAME = "Caveman";
    private static final BufferedImage[] SPRITE_PER_LEVEL = new BufferedImage[3];
    private static final int EVOLUTION_ORDER = 0;
    private static final int[] DAMAGE_PER_LEVEL = {10,15,25};
    private static final int[] RANGE_PER_LEVEL = {15,15,15};
    private static final int[] SPLASH_PER_LEVEL = {2,5,5};
    private static final int[] ATTACK_SPEED_PER_LEVEL = {1000,750,500};
    
    public Caveman(int level, Point Position, boolean isActive, boolean isVisible, JPanel gameScreen){
        importSprites();

    }

   

    private Caveman(String name, int level, BufferedImage[] SPRITE_PER_LEVEL, Point position, boolean isActive, boolean isVisible,
                   int EVOLUTION_ORDER, int[] DAMAGE_PER_LEVEL, int[] RANGE_PER_LEVEL, int[] SPLASH_PER_LEVEL, 
                   int[] ATTACK_SPEED_PER_LEVEL, int[] COST_PER_LEVEL, JPanel gameScreen) {

        super(name, level, SPRITE_PER_LEVEL, position, isActive, isVisible, EVOLUTION_ORDER, 
              DAMAGE_PER_LEVEL, RANGE_PER_LEVEL, SPLASH_PER_LEVEL, ATTACK_SPEED_PER_LEVEL, COST_PER_LEVEL);

        this.gameScreen = gameScreen; // Panel where the game is rendered

        swingAngle = 0;
    }

    private void importSprites() {
        try{
            InputStream inputStream = getClass().getResourceAsStream("/Resources/Caveman1.png");
            SPRITE_PER_LEVEL[0] = ImageIO.read(inputStream);
            inputStream = getClass().getResourceAsStream("/Resources/Caveman2.png");
            SPRITE_PER_LEVEL[1] = ImageIO.read(inputStream);
            inputStream = getClass().getResourceAsStream("/Resources/Caveman3.png");
            SPRITE_PER_LEVEL[2] = ImageIO.read(inputStream);
        }
        catch(IOException e){
            e.printStackTrace();
        }
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
    // Draw the tower and its attack animation
    public void draw(Graphics g) {
        // Draw the tower
        g.setColor(Color.BLUE);
        g.fillRect((int) getPosition().getX(), (int) getPosition().getY(), 20, 20);

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
