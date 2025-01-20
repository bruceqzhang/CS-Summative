package GameObjects;

import java.io.IOException;
import java.io.InputStream;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * Represents a Farmer in the game.
 */
public class Farmer extends Hooman {
    
    private int stabDistance;
    private Timer stabTimer;
    private boolean isStabbing;
    
    // Global Constants for statistics and info
    private static final String NAME = "Farmer";
    private static final Image SPRITE = importSprites()[0];
    private static final Image WEAPON_SPRITE = importSprites()[1];
    private static final int WEAPON_SIZE = 128;
    private static final int EVOLUTION_INDEX = 1;
    private static final int DAMAGE = 30; // Balanced damage
    private static final int RANGE = 70; // Balanced range
    private static final int SPLASH = 15; // Balanced splash damage radius
    private static final int RELOAD_SPEED = 1000; // Balanced reload speed
    private static final int COST = 40; // Balanced cost

    /**
     * Constructor for the Farmer class.
     * 
     * @param position The position of the Farmer.
     * @param isActive Whether the Farmer is active.
     * @param isVisible Whether the Farmer is visible.
     */
    public Farmer(Point position, boolean isActive, boolean isVisible) {
        super(NAME, SPRITE, position, isActive, isVisible,
              EVOLUTION_INDEX, DAMAGE, RANGE, SPLASH, RELOAD_SPEED, COST);
        stabDistance = 0;
    }

    /**
     * Imports the sprites for the Farmer.
     * 
     * @return An array of Images representing the Farmer's sprites.
     */
    private static Image[] importSprites() {
        Image[] sprites = new Image[2];
        try {
            InputStream inputStream = Hooman.class.getResourceAsStream("/Resources/farmer.png");
            sprites[0] = ImageIO.read(inputStream);
            inputStream = Hooman.class.getResourceAsStream("/Resources/farmerPitchfork.png");
            sprites[1] = ImageIO.read(inputStream).getScaledInstance(WEAPON_SIZE, WEAPON_SIZE, Image.SCALE_AREA_AVERAGING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sprites;
    }

    /**
     * Animates the Farmer's attack.
     */
    @Override
    public void animateAttack() {
        if (getTargetAliens().isEmpty()) {
            return;
        }
        isStabbing = true;

        stabTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Increment stab distance until max distance is reached
                if (stabDistance <= RANGE && isStabbing) { 
                    stabDistance += 5; // Increment stab distance
                }
                // Decrement stab distance once max distance is reached
                else if (stabDistance >= 0) {
                    isStabbing = false; // Stop incrementation 
                    stabDistance -= 5; // Decrement stab distance
                }
                // Stop animation once it's done
                else {
                    stabDistance = 0;
                    stabTimer.stop();
                }
            }
        });
        stabTimer.start();
    }

    /**
     * Draws the Farmer and its attack animation.
     * 
     * @param g The Graphics object used for drawing.
     */
    @Override
    public void draw(Graphics g) {
        // Draws the Farmer
        g.drawImage(SPRITE, (int) getPosition().getX(), (int) getPosition().getY(), getSize(), getSize(), null);

        // Draw the pitchfork stab
        if (stabDistance > 0) {
            Graphics2D g2d = (Graphics2D) g.create();
            double rotateAngle;
            if (!getTargetAliens().isEmpty()) {
                double deltaX = getTargetAliens().get(0).getPosition().getX() - getPosition().getX();
                double deltaY = getTargetAliens().get(0).getPosition().getY() - getPosition().getY();
                rotateAngle = Math.atan2(deltaY, deltaX);
            } else {
                stabDistance = 0;
                return;
            }

            int startX = (int) (getPosition().getX() + getSize() / 2.0);
            int startY = (int) (getPosition().getY() + getSize() / 2.0);

            int currentX = startX + (int) (stabDistance * Math.cos(rotateAngle));
            int currentY = startY + (int) (stabDistance * Math.sin(rotateAngle));

            // Translate the current X and Y coordinates of the weapon
            g2d.translate(currentX, currentY);
            // Rotate pitchfork to face the alien
            g2d.rotate(rotateAngle + Math.PI / 2);

            // Draw the attack weapon (offset it by negative currentX and currentY after translation)
            g2d.drawImage(WEAPON_SPRITE, -WEAPON_SPRITE.getWidth(null) / 2, -WEAPON_SPRITE.getHeight(null) / 2, null);

            g2d.dispose();
        }
    }
     
    /**
     * Gets the stab timer.
     * 
     * @return The stab timer.
     */
    @Override
    public Timer getTimer() {
        return stabTimer;
    }
}