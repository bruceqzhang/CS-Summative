import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Alien extends GameObject implements Downgradable, Removable{
    private static ArrayList<Alien> activeAliens;
    private final int[] SPEED_PER_LEVEL, HEALTH_PER_LEVEL;
    private final Point[] WAYPOINTS;
    private int currentWaypointIndex;

    //Constructor
    public Alien(String name, int level, BufferedImage[] SPRITE_PER_LEVEL, Point position, boolean isActive, boolean isVisible, int[] SPEED_PER_LEVEL, int[] HEALTH_PER_LEVEL, Point[] WAYPOINTS, int currentWaypointIndex){
        super(name, level, SPRITE_PER_LEVEL, position, isActive, isVisible);
        this.SPEED_PER_LEVEL = SPEED_PER_LEVEL;
        this.HEALTH_PER_LEVEL = HEALTH_PER_LEVEL;
        this.WAYPOINTS = WAYPOINTS;
        this.currentWaypointIndex = currentWaypointIndex;
    }

    public int getSpeed(){
        return SPEED_PER_LEVEL[getLevel()];
    }

    public int getHealth(){
        return HEALTH_PER_LEVEL[getLevel()];
    }

    public int getCurrentWaypointIndex(){
        return currentWaypointIndex;
    }

    public void updateCurrentWaypointIndex(){
        currentWaypointIndex++;
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


}
