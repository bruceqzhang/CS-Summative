package GameObjects;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;

public class Knight extends Hooman{
    
    private int swingAngle;
    private Timer swingTimer;
    
    private static final String NAME = "Knight";
    private static final Image SPRITE = importSprites()[0];
    private static final Image WEAPON_SPRITE = importSprites()[1];
    private static final int EVOLUTION_INDEX = 0;
    private static final int DAMAGE = 20;
    private static final int RANGE = 100;
    private static final int SPLASH = 100;
    private static final int RELOAD_SPEED = 1000;
    private static final int COST = 30;
    

    public Knight(Point position, boolean isActive, boolean isVisible){
        super(NAME, SPRITE, position, isActive, isVisible,
        EVOLUTION_INDEX, DAMAGE, RANGE, SPLASH, RELOAD_SPEED, COST);


        swingAngle = 0;
    }

    
    private static Image[] importSprites() {
        Image[] sprites = new Image[2];
        try{
            InputStream inputStream = Hooman.class.getResourceAsStream("/Resources/knight.png");
            sprites[0] = ImageIO.read(inputStream);
            inputStream = Hooman.class.getResourceAsStream("/Resources/knightSword.png");
            sprites[1] = ImageIO.read(inputStream).getScaledInstance(getSize(),getSize(),Image.SCALE_AREA_AVERAGING);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return sprites;
    }


    @Override
    public void animateAttack() {
      
        if (getTargetAliens().isEmpty()) {
            return;
        }
        swingAngle = 0;

        swingTimer = new Timer(16, new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if(swingAngle<=360){
                    swingAngle += 40; // Increment swing angle
                }
                else{
                    swingAngle = 0;
                    swingTimer.stop();
                }
            }
        });
        swingTimer.start();
        
        
    }

    
    // Draw the Caveman and its attack animation
    @Override
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


    
}

