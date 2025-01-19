package GameObjects;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.checkerframework.checker.units.qual.s;

import Interfaces.Downgradable;
import Interfaces.Removable;
import java.io.BufferedReader;
import java.io.FileReader;

public class Alien extends GameObject implements Downgradable, Removable{
    private static ArrayList<Alien> activeAliens = new ArrayList<Alien>();
    private static final ArrayList<Point> WAYPOINTS = new ArrayList<Point>();
    private static final Point STARTING_POINT = new Point(0,112);
    private static final String[] NAME_PER_LEVEL = {"Crawler","Stinger","Shell", "Gloop", "Bloble", "Glob", "Tentacule", "Garugant", "Droid"};
    private static final Image[] SPRITE_PER_LEVEL = importSprites();
    private static final int[] SPEED_PER_LEVEL ={2,3,2,4,4,5,5,7,7}, MAX_HEALTH_PER_LEVEL = {50,50,150,100,200,250,300,300,400};
    
    private boolean reachedGoal, isKilled, beingRemoved;
    private int levelIndex,speed, maxHealth, currentWaypointIndex, currentHealth, originalLevelIndex;
    private final int delay;
    private double progress;

    //Constructor
    public Alien(boolean isActive, boolean isVisible, int levelIndex, int delay){
        super(NAME_PER_LEVEL[levelIndex], SPRITE_PER_LEVEL[levelIndex], STARTING_POINT, isActive, isVisible);
        this.levelIndex = levelIndex;
        this.speed = SPEED_PER_LEVEL[levelIndex];
        this.maxHealth = MAX_HEALTH_PER_LEVEL[levelIndex];
        this.currentWaypointIndex = 0;
        this.delay = delay;
        progress = 0;
        currentHealth = maxHealth;
        originalLevelIndex = levelIndex;
        reachedGoal = false;
        isKilled = false;
        beingRemoved = false;
    
        if (isActive){
            addAlien(this);
        }
    }

    public Alien(int levelIndex, int delay){
        this(false,false,levelIndex,delay);
        
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


    public int getCurrentWaypointIndex(){
        return currentWaypointIndex;
    }

    public int getDelay(){
        return delay;
    }

    public double getProgress(){

        return progress;
    }

    public int getCurrentHealth(){
        return currentHealth;
    }

    public int getOriginalLevelIndex(){
        return originalLevelIndex;
    }
    
    public boolean getReachedGoal(){
        return reachedGoal;
    }

    public boolean getIsKilled(){
        return isKilled;
    }

    public boolean getBeingRemoved(){
        return beingRemoved;
    }
    
    @Override
    public void setActivity(boolean isActive){
        super.setActivity(isActive);
        if (isActive&&!getAliens().contains(this)){
            addAlien(this);
        }
    }

    public void updateLevelIndex(){
        levelIndex--;
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

    public void initAlien(){
        setActivity(true);
        setVisibility(true);
    }
    
    public void takeDamage(int damage) {
        currentHealth-=damage;
        if (currentHealth>=0){
            downgrade();
        }
    }

    private void reachGoal() {
        remove();
        reachedGoal = true;
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
            progress = WAYPOINTS.get(currentWaypointIndex).getX() - nextWaypoint.getX() - Math.abs(deltaX); 
            setPosition(new Point(
                (int) (currentPosition.getX()+ Math.min(getSpeed(), Math.abs(deltaX))*Integer.signum(deltaX)),
                (int) currentPosition.getY() 
            ));
        }
        else if (deltaY!=0){
            progress = WAYPOINTS.get(currentWaypointIndex).getY() - nextWaypoint.getY() - Math.abs(deltaY); 
            setPosition(new Point(
                (int) currentPosition.getX(),
                (int) (currentPosition.getY()+ Math.min(getSpeed(), Math.abs(deltaY))*Integer.signum(deltaY))
            ));
        }

        if(getPosition().equals(nextWaypoint)){
            updateCurrentWaypointIndex();
        }

    }



    @Override
    public void draw(Graphics g) {
        g.drawImage(getSprite(), (int)getPosition().getX(), (int)getPosition().getY(), null);
    }

    @Override
    public void remove() {
        setActivity(false);
        setVisibility(false);
        beingRemoved = true;
    }

    @Override
    public void downgrade() {
        if (getLevelIndex()>0){
            updateLevelIndex();
            updateSpeed();
            updateHealth();
            setName(NAME_PER_LEVEL[levelIndex]);
            setSprite(SPRITE_PER_LEVEL[levelIndex]);
        }
        else{
            isKilled = true;
            remove();
        }
    }   

    public static void addWaypoint(int x, int y){
        WAYPOINTS.add(new Point(x,y));
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

    public static Alien[] getRoundAliens(int round){
        
        try {
            BufferedReader br = new BufferedReader(new FileReader("/Users/brucezhang/Coding/CS-Summative/Summative/app/src/main/java/TextFiles/Rounds.txt"));
            String line = "";
            boolean lineFound = false;
            while(line!=null){
                if (line.startsWith("Round " + round)){
                    lineFound = true;
                    break;
                }
                line = br.readLine();
            }
            if (lineFound){
                line = br.readLine();

                String[] alienText = line.split(",");
                Alien[] roundAliens = new Alien[alienText.length];

                for (int i = 0; i<alienText.length; i++){
                    
                    roundAliens[i] = new Alien(Integer.parseInt(alienText[i].split(":")[0]), Integer.parseInt(alienText[i].split(":")[1]));
                    
                }
                
                br.close();
                return roundAliens;
            }
            else{
                br.close();
                return null;
            }
            //Possible else statement here for random aliens?
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        

    }

    public static Image[] importSprites() {
        Image[] sprites = new Image[9];
        BufferedImage spriteSheet;
        try{
            InputStream inputStream = Alien.class.getResourceAsStream("/Resources/alienSpriteSheet.png");
            spriteSheet = ImageIO.read(inputStream);
            sprites[0] = spriteSheet.getSubimage(24,24,128,128).getScaledInstance(getSize(),getSize(),Image.SCALE_AREA_AVERAGING);
            sprites[1] = spriteSheet.getSubimage(176,24,128,128).getScaledInstance(getSize(),getSize(),Image.SCALE_AREA_AVERAGING);
            sprites[2] = spriteSheet.getSubimage(24,176,128,128).getScaledInstance(getSize(),getSize(),Image.SCALE_AREA_AVERAGING);
            sprites[3] = spriteSheet.getSubimage(24,328,128,128).getScaledInstance(getSize(),getSize(),Image.SCALE_AREA_AVERAGING);
            sprites[4] = spriteSheet.getSubimage(328,328,128,128).getScaledInstance(getSize(), getSize(), Image.SCALE_AREA_AVERAGING);
            sprites[5] = spriteSheet.getSubimage(176,176,128,128).getScaledInstance(getSize(), getSize(), Image.SCALE_AREA_AVERAGING);
            sprites[6] = spriteSheet.getSubimage(328,176,128,128).getScaledInstance(getSize(), getSize(), Image.SCALE_AREA_AVERAGING);
            sprites[7] = spriteSheet.getSubimage(176,328,128,128).getScaledInstance(getSize(), getSize(), Image.SCALE_AREA_AVERAGING);
            sprites[8] = spriteSheet.getSubimage(328,24,128,128).getScaledInstance(getSize(), getSize(), Image.SCALE_AREA_AVERAGING);  
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return sprites;
    }
    
  

    


}
