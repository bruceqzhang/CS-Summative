package GameObjects;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import Interfaces.Downgradable;
import Interfaces.Removable;

public class Alien extends GameObject implements Downgradable, Removable{
    private static ArrayList<Alien> activeAliens = new ArrayList<Alien>();
    private static final String[] NAME_PER_LEVEL = {};
    private static final Image[] SPRITE_PER_LEVEL = importSprites();
    private static final int[] SPEED_PER_LEVEL ={5,7,10,15,15}, MAX_HEALTH_PER_LEVEL = {50,50,75,100,200};
    private static final ArrayList<Point> WAYPOINTS = new ArrayList<Point>();
    private Timer moveTimer;
    private JPanel gameScreen;
    
    private int levelIndex;
    private int speed, maxHealth, currentWaypointIndex, currentHealth;

    //Constructor
    public Alien(String name, Image sprite, Point position, boolean isActive, boolean isVisible, int levelIndex, int currentWaypointIndex, JPanel gameScreen){
        super(NAME_PER_LEVEL[levelIndex], SPRITE_PER_LEVEL[levelIndex], position, isActive, isVisible);
        this.levelIndex = levelIndex;
        this.speed = SPEED_PER_LEVEL[levelIndex];
        this.maxHealth = MAX_HEALTH_PER_LEVEL[levelIndex];
        this.currentWaypointIndex = currentWaypointIndex;
        this.gameScreen = gameScreen;
        currentHealth = maxHealth;
    }

    public int getLevelIndex(){
        return levelIndex;
    }

    public int getSpeed(){
        return speed;
    }

    public int getMaxHealth(){
        return maxHealth;
    }

    public int getCurrentHealth(){
        return currentHealth;
    }

    public int getCurrentWaypointIndex(){
        return currentWaypointIndex;
    }

    public void decrementLevelIndex(){
        levelIndex++;
    }

    public void updateSpeed(){
        speed = SPEED_PER_LEVEL[getLevelIndex()];
    }

    public void updateHealth(){
        maxHealth = MAX_HEALTH_PER_LEVEL[getLevelIndex()];
        currentHealth = maxHealth;
    }

    public void updateCurrentWaypointIndex(){
        currentWaypointIndex++;
    }

    public static void drawWaypoints(Graphics g){
        for (Point waypoint:WAYPOINTS){
            g.drawRect(waypoint.x, waypoint.y, 10, 10);
        }
    }

    public void startMovement() {
        moveTimer = new Timer(16, new ActionListener() { // Approx. 60 FPS
            @Override
            public void actionPerformed(ActionEvent e) {
                move(); // Call your move method
                gameScreen.repaint(); // Trigger a redraw
            }
        });
        moveTimer.start();
    }

    public void move(){

        if (currentWaypointIndex<WAYPOINTS.size()-1){
            animateMovement();
        }  
        else{
            reachGoal();
        } 
    }

    public void animateMovement(){
        Point currentPosition = getPosition();
        Point nextWaypoint = WAYPOINTS.get(currentWaypointIndex+1);
        int deltaX = (int)(nextWaypoint.getX() - currentPosition.getX());
        int deltaY = (int)(nextWaypoint.getY() - currentPosition.getY());

        if (deltaX!=0){ 
            setPosition(new Point(
                (int) (currentPosition.getX()+ Math.min(getSpeed(), Math.abs(deltaX))*Integer.signum(deltaX)),
                (int) currentPosition.getY()
            ));
        }
        else if (deltaY!=0){
            setPosition(new Point(
                (int) currentPosition.getX(),
                (int) (currentPosition.getY()+ Math.min(getSpeed(), Math.abs(deltaY))*Integer.signum(deltaY))
            ));
        }

        if(getPosition().equals(nextWaypoint)){
            updateCurrentWaypointIndex();
        }

    }


    public void takeDamage(int damage) {
        currentHealth-=damage;
        if (currentHealth>=0){
            downgrade();
        }
    }

    private void reachGoal() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reachGoal'");
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(getSprite(), (int)getPosition().getX(), (int)getPosition().getY(), null);
    }

    @Override
    public void remove() {
        setActivity(false);
        setVisibility(false);
        removeAlien(this);
    }

    @Override
    public void downgrade() {
        if (getLevelIndex()>0){
            decrementLevelIndex();
            updateSpeed();
            updateHealth();
            setName(NAME_PER_LEVEL[levelIndex]);
            setSprite(SPRITE_PER_LEVEL[levelIndex]);
        }
        else{
            remove();
        }
    }   

    public static Image[] importSprites() {
        Image[] sprites = new Image[5];
        BufferedImage spriteSheet;
        try{
            InputStream inputStream = Alien.class.getResourceAsStream("/Resources/alienSpriteSheet.png");
            spriteSheet = ImageIO.read(inputStream);
            sprites[0] = spriteSheet.getSubimage(12,12,64,64);
            sprites[1] = spriteSheet.getSubimage(12,12,64,64);
            sprites[2] = spriteSheet.getSubimage(12,12,64,64);
            sprites[3] = spriteSheet.getSubimage(12,12,64,64);
            sprites[4] = spriteSheet.getSubimage(12,12,64,64);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return sprites;
    }

    public static ArrayList<Alien> getAliens(){
        return activeAliens;
    }

    public static void removeAlien(Alien alien){
        activeAliens.remove(alien);
    }

    public static void addAlien(Alien alien){
        activeAliens.add(alien);
    }

    public static void setAliens(ArrayList<Alien> aliens){
        activeAliens = aliens;
    }

    
    public static void addWaypoint(int x, int y){
        WAYPOINTS.add(new Point(x,y));
    }
  

    


}
