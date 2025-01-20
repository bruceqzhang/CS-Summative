package GameObjects;

import java.io.IOException;
import java.io.InputStream;
import java.awt.Point;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Represents a Bowler in the game.
 */
public class Bowler extends Hooman {
    
    private int projectileDistance;
    private Timer shootTimer;
    
    // Global Constants for statistics and info
    private static final String NAME = "Bowler";
    private static final Image SPRITE = importSprites()[0];
    private static final Image PROJECTILE_SPRITE = importSprites()[1];
    private static final int PROJECTILE_SIZE = 64;
    private static final int EVOLUTION_INDEX = 3;
    private static final int DAMAGE = 70; // Balanced damage
    private static final int RANGE = 200; // Balanced range
    private static final int SPLASH = 50; // Small splash damage radius
    private static final int RELOAD_SPEED = 2000; // Balanced reload speed
    private static final int COST = 100; // Balanced cost

    /**
     * Constructor for the Bowler class.
     * 
     * @param position The position of the Bowler.
     * @param isActive Whether the Bowler is active.
     * @param isVisible Whether the Bowler is visible.
     */
    public Bowler(Point position, boolean isActive, boolean isVisible) {
        super(NAME, SPRITE, position, isActive, isVisible,
              EVOLUTION_INDEX, DAMAGE, RANGE, SPLASH, RELOAD_SPEED, COST);
        projectileDistance = 0;
    }

    /**
     * Imports the sprites for the Bowler.
     * 
     * @return An array of Images representing the Bowler's sprites.
     */
    private static Image[] importSprites() {
        Image[] sprites = new Image[2];
        try {
            InputStream inputStream = Bowler.class.getResourceAsStream("/Resources/bowler.png");
            sprites[0] = ImageIO.read(inputStream).getScaledInstance(getSize(), getSize(), Image.SCALE_AREA_AVERAGING);

            inputStream = Bowler.class.getResourceAsStream("/Resources/bowlerRock.png");
            sprites[1] = ImageIO.read(inputStream).getScaledInstance(PROJECTILE_SIZE, PROJECTILE_SIZE, Image.SCALE_AREA_AVERAGING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sprites;
    }

    /**
     * Animates the Bowler's attack.
     */
    @Override
    public void animateAttack() {
        findTargetAliens();
        if (getTargetAliens().isEmpty()) {
            return;
        }

        projectileDistance = 0;

        double deltaX = getTargetAliens().get(0).getPosition().getX() - getPosition().getX();
        double deltaY = getTargetAliens().get(0).getPosition().getY() - getPosition().getY();
        double totalDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        shootTimer = new Timer(8, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Increment projectile distance until max distance is reached
                if (projectileDistance <= totalDistance) {
                    // Increment projectile distance
                    projectileDistance += 15;
                } else {
                    // Stop animation once it's done                 
                    projectileDistance = 0;
                    shootTimer.stop();
                }
            }
        });
        shootTimer.start();
    }

    /**
     * Draws the Bowler and its attack animation.
     * 
     * @param g The Graphics object used for drawing.
     */
    @Override
    public void draw(Graphics g) {
        // Draws the Bowler
        g.drawImage(SPRITE, (int) getPosition().getX(), (int) getPosition().getY(), getSize(), getSize(), null);
        
        int weaponX = (int) (getPosition().getX() + getSize() / 2.0);
        int weaponY = (int) (getPosition().getY() + getSize() / 2.0);

        double rotateAngle = Math.PI / 2;
        if (!getTargetAliens().isEmpty()) {
            double deltaX = getTargetAliens().get(0).getPosition().getX() - getPosition().getX();
            double deltaY = getTargetAliens().get(0).getPosition().getY() - getPosition().getY();
            rotateAngle = Math.atan2(deltaY, deltaX);
        } else {
            projectileDistance = 0;
        }

        // Draw the projectile
        if (projectileDistance > 0) {
            Graphics2D g2dProjectile = (Graphics2D) g.create();
            
            int projectileX = weaponX + (int) (projectileDistance * Math.cos(rotateAngle));
            int projectileY = weaponY + (int) (projectileDistance * Math.sin(rotateAngle));
            
            g2dProjectile.translate(projectileX, projectileY);
            g2dProjectile.rotate(rotateAngle + Math.PI / 2);
            
            g2dProjectile.drawImage(PROJECTILE_SPRITE, -PROJECTILE_SPRITE.getWidth(null) / 2, -PROJECTILE_SPRITE.getHeight(null) / 2, null);

            g2dProjectile.dispose();
        }
    }

    /**
     * Gets the shoot timer.
     * 
     * @return The shoot timer.
     */
    @Override
    public Timer getTimer() {
        return shootTimer;
    }
}