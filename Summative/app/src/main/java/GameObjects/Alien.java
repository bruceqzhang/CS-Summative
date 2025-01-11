package GameObjects;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Interfaces.Downgradable;
import Interfaces.Removable;

public class Alien extends GameObject implements Downgradable, Removable{
    private static ArrayList<Alien> activeAliens = new ArrayList<Alien>();
    private final int[] SPEED_PER_LEVEL, HEALTH_PER_LEVEL;
    private final Point[] WAYPOINTS;
    private int currentWaypointIndex, health;

    //Constructor
    public Alien(String name, int level, BufferedImage[] SPRITE_PER_LEVEL, Point position, boolean isActive, boolean isVisible, int[] SPEED_PER_LEVEL, int[] HEALTH_PER_LEVEL, Point[] WAYPOINTS, int currentWaypointIndex){
        super(name, level, SPRITE_PER_LEVEL, position, isActive, isVisible);
        this.SPEED_PER_LEVEL = SPEED_PER_LEVEL;
        this.HEALTH_PER_LEVEL = HEALTH_PER_LEVEL;
        health = HEALTH_PER_LEVEL[level];
        this.WAYPOINTS = WAYPOINTS;
        this.currentWaypointIndex = currentWaypointIndex;
    }

    public int getSpeed(){
        return SPEED_PER_LEVEL[getLevel()];
    }

    public int getMaxHealth(){
        return HEALTH_PER_LEVEL[getLevel()];
    }

    public int getCurrentWaypointIndex(){
        return currentWaypointIndex;
    }

    public void updateCurrentWaypointIndex(){
        currentWaypointIndex++;
    }

    public void takeDamage(int damage) {
        health-=damage;
        if (health>=0){
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
        setLevel(getLevel()-1);
        if (getLevel()<0){
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

    


}
