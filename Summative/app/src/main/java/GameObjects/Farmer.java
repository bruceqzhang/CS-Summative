package GameObjects;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Point;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;

public class Farmer extends Hooman{
    
    private int stabDistance;
   
    
    // Global Constants for statistics and info
    private static final String NAME = "Farmer";
    private static final Image SPRITE = importSprites()[0];
    private static final Image WEAPON_SPRITE = importSprites()[1];
    private static final int WEAPON_SIZE = 128;
    private static final int EVOLUTION_INDEX = 2;
    private static final int DAMAGE = 30;
    private static final int RANGE = 100;
    private static final int SPLASH = 5;
    private static final int RELOAD_SPEED = 500;
    private static final int COST = 100;
    

    // Constructor
    public Farmer(Point position, boolean isActive, boolean isVisible){
        super(NAME, SPRITE, position, isActive, isVisible,
        EVOLUTION_INDEX, DAMAGE, RANGE, SPLASH, RELOAD_SPEED, COST);

    }

    
    private static Image[] importSprites() {
        Image[] sprites = new Image[2];
        try{
            InputStream inputStream = Caveman.class.getResourceAsStream("/Resources/farmer.png");
            sprites[0] = ImageIO.read(inputStream);
            inputStream = Caveman.class.getResourceAsStream("/Resources/farmerPitchfork.png");
            sprites[1] = ImageIO.read(inputStream).getScaledInstance(WEAPON_SIZE,WEAPON_SIZE,Image.SCALE_AREA_AVERAGING);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return sprites;
    }


    @Override
    public void animateAttack() {
        new Thread( new Runnable() {
            
            @Override
            public void run() {
                while(true){
                    // findTargetAliens();
                    // if (getTargetAliens().isEmpty()) {
                    //     stabDistance = 0;
                    //     return;
                    // }
                    boolean isStabbing = true;
                    // Increment stab distance until max distance is reached
                    if (stabDistance <= RANGE && isStabbing) { 
                        // Increment stab distance
                        stabDistance += 5; 
                    }
                    // Decrement stab distance once max distance is reached
                    else if (stabDistance>=0){
                        // Stop incrementation 
                        isStabbing = false;
                        // Decrement stab distance
                        stabDistance -= 5;
                    }
                    // Stop animation once its done
                    else{
                        return;
                    }
                }

            }
        }).start();
        
    }

    @Override
    // Draws the Farmer and its attack animation
    public void draw(Graphics g) {

        // Draws the Farmer
        g.drawImage(SPRITE, (int)(getPosition().getX()), (int)getPosition().getY(), getSize(), getSize(), null);
        
    
        // Draw the club swing
        if (stabDistance > 0) {
            Graphics2D g2d = (Graphics2D) g.create();
            double deltaX = getTargetAliens().get(0).getPosition().getX() - getPosition().getX();
            double deltaY = getTargetAliens().get(0).getPosition().getY() - getPosition().getY();
            double rotateAngle = Math.atan2(deltaY,deltaX);
            


            int startX = (int) (getPosition().getX() + getSize() / 2.0);
            int startY = (int) (getPosition().getY()+ getSize() / 2.0);

            int currentX = startX + (int)(stabDistance * Math.cos(rotateAngle));
            int currentY = startY + (int)(stabDistance * Math.sin(rotateAngle));
            
            
            // Translate the current X and Y coordinates of the weapons
            g2d.translate(currentX, currentY);
            // Rotate pitchfork to face the alien
            g2d.rotate(rotateAngle + Math.PI/2); 
            

            // Draw the attack weapon (offset it by negative currentX and currentY after translation)
            g2d.drawImage(WEAPON_SPRITE, -WEAPON_SPRITE.getWidth(null) / 2, -WEAPON_SPRITE.getHeight(null)/2 ,null);

            g2d.dispose();
        }
        
    }



    
}
