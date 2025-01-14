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

public class Archer extends Hooman{
    
    private Timer shootTimer;
    private int projectileDistance;

    private JPanel gameScreen;
    
   
    
    // Global Constants for statistics and info
    private static final String NAME = "Archer";
    private static final Image SPRITE = importSprites()[0];
    private static final Image WEAPON_SPRITE = importSprites()[1];
    private static final int WEAPON_SIZE = 48;
    private static final Image PROJECTILE_SPRITE = importSprites()[2];
    private static final int PROJECTILE_SIZE = 64;
    private static final int EVOLUTION_INDEX = 3;
    private static final int DAMAGE = 40;
    private static final int RANGE = 500;
    private static final int SPLASH = 5;
    private static final int RELOAD_SPEED = 1000;
    private static final int COST = 100;
    

    // Constructor
    public Archer(Point position, boolean isActive, boolean isVisible, JPanel gameScreen){
        super(NAME, SPRITE, position, isActive, isVisible,
        EVOLUTION_INDEX, DAMAGE, RANGE, SPLASH, RELOAD_SPEED, COST);


        // Panel where the game is rendered
        this.gameScreen = gameScreen; 
        projectileDistance = 0;
    }

    
    private static Image[] importSprites() {
        Image[] sprites = new Image[3];
        try{
            InputStream inputStream = Archer.class.getResourceAsStream("/Resources/archer.png");
            sprites[0] = ImageIO.read(inputStream);

            inputStream = Archer.class.getResourceAsStream("/Resources/archerBow.png");
            sprites[1] = ImageIO.read(inputStream).getScaledInstance(WEAPON_SIZE,WEAPON_SIZE,Image.SCALE_AREA_AVERAGING);

            inputStream = Archer.class.getResourceAsStream("/Resources/archerArrow.png");
            sprites[2] = ImageIO.read(inputStream).getScaledInstance(PROJECTILE_SIZE,PROJECTILE_SIZE,Image.SCALE_AREA_AVERAGING);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return sprites;
    }


    @Override
    public void animateAttack() {
        shootTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // double deltaX = getTargetAliens().get(0).getPosition().getX() - getPosition().getX();
                // double deltaY = getTargetAliens().get(0).getPosition().getY() - getPosition().getY();
                // double totalDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
                double totalDistance = 200;
                // Increment projectile distance until max distance is reached
                if (projectileDistance <= totalDistance) { 
                    // Increment projectile distance
                    projectileDistance += 20; 
                }
                // Stop animation once its done
                else{
                    projectileDistance = 0;
                    shootTimer.stop();
                }
                // Trigger re-drawing of the panel
                gameScreen.repaint(); 
            }
        });
        shootTimer.start();
        
    }

    @Override
    // Draws the Archer and its attack animation
    public void draw(Graphics g) {

        // Draws the Archer
        g.drawImage(SPRITE, (int)(getPosition().getX()), (int)getPosition().getY(), getSize(), getSize(), null);
        
        Graphics2D g2dWeapon = (Graphics2D) g.create();

        int weaponX = (int) (getPosition().getX() + getSize() / 2.0);
        int weaponY = (int) (getPosition().getY()+ getSize() / 2.0);

        double rotateAngle = Math.PI/4;
        if(!getTargetAliens().isEmpty()){
            double deltaX = getTargetAliens().get(0).getPosition().getX() - getPosition().getX();
            double deltaY = getTargetAliens().get(0).getPosition().getY() - getPosition().getY();
            rotateAngle = Math.atan2(deltaY,deltaX);
        }
        else{
            //projectileDistance = 0;
        }

        g2dWeapon.translate(weaponX, weaponY);

        // Rotate bow to face the alien
        g2dWeapon.rotate(rotateAngle + Math.PI/2); 

        // Draw the attack weapon (offset it by negative currentX and currentY after translation)
        g2dWeapon.drawImage(WEAPON_SPRITE, -WEAPON_SPRITE.getWidth(null) / 2, -WEAPON_SPRITE.getHeight(null)/2 ,null);

        g2dWeapon.dispose();


        // Draw the projectile swing
        if (projectileDistance > 0) {
            Graphics2D g2dProjectile = (Graphics2D) g.create();
            

            int projectileX = weaponX + (int)(projectileDistance * Math.cos(rotateAngle));
            int projectileY = weaponY + (int)(projectileDistance * Math.sin(rotateAngle));
            
            g2dProjectile.translate(projectileX, projectileY);
            g2dProjectile.rotate(rotateAngle + Math.PI/2);
            
            
            g2dProjectile.drawImage(PROJECTILE_SPRITE, -PROJECTILE_SPRITE.getWidth(null) / 2, -PROJECTILE_SPRITE.getHeight(null) / 2, null);

            g2dProjectile.dispose();




        }
        
    }
    
}


