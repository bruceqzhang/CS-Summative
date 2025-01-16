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
import java.util.Currency;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.google.common.escape.ArrayBasedCharEscaper;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;

import Interfaces.Downgradable;
import Interfaces.Removable;

import java.io.BufferedReader;
import java.io.FileReader;

public class Alien extends GameObject implements Downgradable, Removable{
    private static ArrayList<Alien> activeAliens = new ArrayList<Alien>();
    private static final String[] NAME_PER_LEVEL = {"Hello"};
    private static final Image[] SPRITE_PER_LEVEL = importSprites();
    private static final int[] SPEED_PER_LEVEL ={5,7,10,15,15}, MAX_HEALTH_PER_LEVEL = {50,50,75,100,200};
    private static final ArrayList<Point> WAYPOINTS = new ArrayList<Point>();
    private static final Point STARTING_POINT = new Point(0,112);
    private Timer moveTimer;
    private JPanel gameScreen;
    private int levelIndex,speed, maxHealth, currentWaypointIndex, currentHealth;
    private final int delay; 

    private double progress;

    //Constructor
    public Alien(boolean isActive, boolean isVisible, int levelIndex, int delay, JPanel gameScreen){
        super(NAME_PER_LEVEL[levelIndex], SPRITE_PER_LEVEL[levelIndex], STARTING_POINT, isActive, isVisible);
        this.levelIndex = levelIndex;
        this.speed = SPEED_PER_LEVEL[levelIndex];
        this.maxHealth = MAX_HEALTH_PER_LEVEL[levelIndex];
        this.currentWaypointIndex = 0;
        this.gameScreen = gameScreen;
        currentHealth = maxHealth;
        
        progress = 0;
        if (isActive){
            addAlien(this);
        }
        this.delay = delay;
    }

    public Alien(int levelIndex, int delay, JPanel gameScreen){
        this(false,false,levelIndex,delay, gameScreen);
        
    }

    public void initAlien(){
        setActivity(true);
        setVisibility(true);
        //startMovement();
    }

    @Override
    public void setActivity(boolean isActive){
        super.setActivity(isActive);
        if (isActive&&!getAliens().contains(this)){
            addAlien(this);
        }
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

    public int getDelay(){
        return delay;
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

    // public void startMovement() {
    //     moveTimer = new Timer(16, new ActionListener() { // Approx. 60 FPS
    //         @Override
    //         public void actionPerformed(ActionEvent e) {
    //             move(); // Call your move method
    //             gameScreen.repaint(); // Trigger a redraw
    //         }
    //     });
    //     moveTimer.start();
    // }

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
            progress = Math.abs(deltaX); 
            setPosition(new Point(
                (int) (currentPosition.getX()+ Math.min(getSpeed(), progress)*Integer.signum(deltaX)),
                (int) currentPosition.getY() 
            ));
        }
        else if (deltaY!=0){
            progress = Math.abs(deltaY); 
            setPosition(new Point(
                (int) currentPosition.getX(),
                (int) (currentPosition.getY()+ Math.min(getSpeed(), progress)*Integer.signum(deltaY))
            ));
        }

        if(getPosition().equals(nextWaypoint)){
            updateCurrentWaypointIndex();
        }

    }

    public double getProgress(){
        return progress;
    }


    public void takeDamage(int damage) {
        currentHealth-=damage;
        if (currentHealth>=0){
            downgrade();
        }
    }

    private void reachGoal() {
        remove();
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
            sprites[0] = spriteSheet.getSubimage(24,24,128,128).getScaledInstance(32,32,Image.SCALE_AREA_AVERAGING);
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

    public static Alien[] getRoundAliens(int round, JPanel gameScreen){
        
        try {
            BufferedReader br = new BufferedReader(new FileReader("/TextFiles/Rounds.txt"));
            String line = "";
            boolean lineFound = false;
            while(line!=null){
                line = br.readLine();
                if (line.startsWith("Round " + round)){
                    lineFound = true;
                    break;
                }
            }
            if (lineFound){
                line = br.readLine();
                String[] alienText = line.split(",");
                Alien[] roundAliens = new Alien[alienText.length];
                for (int i = 0; i<alienText.length; i++){
                    roundAliens[i] = new Alien(Integer.parseInt(alienText[i].split(":")[0]), Integer.parseInt(alienText[i].split(":")[1]), gameScreen);
                }
                return roundAliens;
            }
            //Possible else statement here for random aliens?
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

        

    }


    public static void addWaypoint(int x, int y){
        WAYPOINTS.add(new Point(x,y));
    }
  

    


}
