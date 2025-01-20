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
 * Represents a Soldier in the game.
 */
public class Soldier extends Hooman {
    
    private int projectileDistance;
    private Timer shootTimer;
    
    // Global Constants for statistics and info
    private static final String NAME = "Soldier";
    private static final Image SPRITE = importSprites()[0];
    private static final Image WEAPON_SPRITE = importSprites()[1];
    private static final int WEAPON_SIZE = 48;
    private static final Image PROJECTILE_SPRITE = importSprites()[2];
    private static final int PROJECTILE_SIZE = 64;
    private static final int EVOLUTION_INDEX = 9;
    private static final int DAMAGE = 20; // Balanced damage
    private static final int RANGE = 400; // Balanced range
    private static final int SPLASH = 5; // Balanced splash damage radius
    private static final int RELOAD_SPEED = 750; // Balanced reload speed
    private static final int COST = 150; // Balanced cost

    /**
     * Constructor for the Soldier class.
     * 
     * @param position The position of the Soldier.
     * @param isActive Whether the Soldier is active.
     * @param isVisible Whether the Soldier is visible.
     */
    public Soldier(Point position, boolean isActive, boolean isVisible) {
        super(NAME, SPRITE, position, isActive, isVisible,
              EVOLUTION_INDEX, DAMAGE, RANGE, SPLASH, RELOAD_SPEED, COST);
        projectileDistance = 0;
    }

    /**
     * Imports the sprites for the Soldier.
     * 
     * @return An array of Images representing the Soldier's sprites.
     */
    private static Image[] importSprites() {
        Image[] sprites = new Image[3];
        try {
            InputStream inputStream = Soldier.class.getResourceAsStream("/Resources/soldier.png");
            sprites[0] = ImageIO.read(inputStream);

            inputStream = Soldier.class.getResourceAsStream("/Resources/soldierGun.png");
            sprites[1] = ImageIO.read(inputStream).getScaledInstance(WEAPON_SIZE, WEAPON_SIZE * 3 / 2, Image.SCALE_AREA_AVERAGING);

            inputStream = Soldier.class.getResourceAsStream("/Resources/soldierBullet.png");
            sprites[2] = ImageIO.read(inputStream).getScaledInstance(PROJECTILE_SIZE, PROJECTILE_SIZE, Image.SCALE_AREA_AVERAGING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sprites;
    }

    /**
     * Animates the Soldier's attack.
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
     * Draws the Soldier and its attack animation.
     * 
     * @param g The Graphics object used for drawing.
     */
    @Override
    public void draw(Graphics g) {
        // Draws the Soldier
        g.drawImage(SPRITE, (int) getPosition().getX(), (int) getPosition().getY(), getSize(), getSize(), null);
        
        Graphics2D g2dWeapon = (Graphics2D) g.create();

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

        g2dWeapon.translate(weaponX, weaponY);

        // Rotate gun to face the alien
        g2dWeapon.rotate(rotateAngle + Math.PI / 2);


        if (rotateAngle < -Math.PI / 2 || rotateAngle > Math.PI / 2){
            g2dWeapon.scale(-1,1);
        }
        

        // Draw the attack weapon (offset it by negative currentX and currentY after translation)
        g2dWeapon.drawImage(WEAPON_SPRITE, -WEAPON_SPRITE.getWidth(null) / 2, -WEAPON_SPRITE.getHeight(null) / 2, null);
        g2dWeapon.dispose();

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