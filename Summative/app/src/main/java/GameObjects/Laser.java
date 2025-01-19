package GameObjects;

import java.io.IOException;
import java.io.InputStream;
import java.awt.Point;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Laser extends Hooman{
    
    private boolean isShooting;
    private Timer shootTimer;
    
    // Global Constants for statistics and info
    private static final String NAME = "Laser";
    private static final Image SPRITE = importSprites()[0];
    private static final int EVOLUTION_INDEX = 3;
    private static final int DAMAGE = 40;
    private static final int RANGE = 500;
    private static final int SPLASH = 5;
    private static final int RELOAD_SPEED = 400;
    private static final int COST = 100;
    

    // Constructor
    public Laser(Point position, boolean isActive, boolean isVisible){
        super(NAME, SPRITE, position, isActive, isVisible,
        EVOLUTION_INDEX, DAMAGE, RANGE, SPLASH, RELOAD_SPEED, COST);

    }

    
    private static Image[] importSprites() {
        Image[] sprites = new Image[3];
        try{
            InputStream inputStream = Archer.class.getResourceAsStream("/Resources/laser.png");
            sprites[0] = ImageIO.read(inputStream);

        }
        catch(IOException e){
            e.printStackTrace();
        }
        return sprites;
    }



    @Override
    public void animateAttack() {
        findTargetAliens();
        if (getTargetAliens().isEmpty()) {
            return;
        }

        isShooting = true;

        shootTimer = new Timer(100, new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e){
                // Increment projectile distance until max distance is reached
                isShooting = false;
                shootTimer.stop();
            }

        });
        shootTimer.setRepeats(false);
        shootTimer.start();
    }

    @Override
// Draws the Laser and its attack animation
public void draw(Graphics g) {

    // Draws the Laser
    g.drawImage(SPRITE, (int) (getPosition().getX()), (int) getPosition().getY(), getSize(), getSize(), null);


    if (!getTargetAliens().isEmpty()&&isShooting) {
        Graphics2D g2dProjectile = (Graphics2D) g.create();

        // Calculate the target position
        int targetX = (int) getTargetAliens().get(0).getPosition().getX() + getSize()/2;
        int targetY = (int) getTargetAliens().get(0).getPosition().getY() + getSize()/2;

        int startX = (int) (getPosition().getX() + getSize() / 2.0);
        int startY = (int) (getPosition().getY() + getSize()/4.0);

        // Draw the laser beam
        g2dProjectile.setColor(Color.RED); // Set the laser color
        g2dProjectile.setStroke(new BasicStroke(10,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND)); // Set the laser thickness
        g2dProjectile.drawLine(startX, startY, targetX, targetY);
        g2dProjectile.setStroke(new BasicStroke(4,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND)); 
        g2dProjectile.setColor(Color.WHITE);
        g2dProjectile.drawLine(startX, startY, targetX, targetY);
    }

}



    
}


