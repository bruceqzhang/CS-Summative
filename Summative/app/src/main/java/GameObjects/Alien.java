package GameObjects;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Interfaces.Downgradable;
import Interfaces.Removable;

public class Alien extends GameObject implements Downgradable, Removable{
    private static ArrayList<Alien> activeAliens = new ArrayList<Alien>();
    private static final String[] NAME_PER_LEVEL = {};
    private static final BufferedImage[] SPRITE_PER_LEVEL ={};
    private static final int[] SPEED_PER_LEVEL ={5,7,10,15,15}, MAX_HEALTH_PER_LEVEL = {50,50,75,100,200};
    private static final Point[] WAYPOINTS = {};
    private int levelIndex;
    private int speed, maxHealth, currentWaypointIndex, currentHealth;

    //Constructor
    public Alien(String name, BufferedImage sprite, Point position, boolean isActive, boolean isVisible, int levelIndex, int currentWaypointIndex){
        super(NAME_PER_LEVEL[levelIndex], SPRITE_PER_LEVEL[levelIndex], position, isActive, isVisible);
        this.levelIndex = levelIndex;
        this.speed = SPEED_PER_LEVEL[levelIndex];
        this.maxHealth = MAX_HEALTH_PER_LEVEL[levelIndex];
        currentHealth = maxHealth;
        this.currentWaypointIndex = currentWaypointIndex;
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
        currentWaypointIndex--;
    }


    public void takeDamage(int damage) {
        currentHealth-=damage;
        if (currentHealth>=0){
            downgrade();
        }
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

    @Override
    public void draw(Graphics g) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }

    public BufferedImage[] importSprites() {
        BufferedImage[] sprites = new BufferedImage[5];

        return sprites;
    }

    


}
