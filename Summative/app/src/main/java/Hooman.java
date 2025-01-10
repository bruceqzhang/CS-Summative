import java.awt.image.BufferedImage;
import java.awt.Point;
import java.io.*;
import java.util.ArrayList;



public abstract class Hooman extends GameObject implements Upgradable, Placeable{
    private static ArrayList<Hooman> activeHoomans; 
    private Hooman[] sortedHoomans;
    private static int currentSortType;
    private final int EVOLUTION_ORDER;
    private final int[] DAMAGE_PER_LEVEL, RANGE_PER_LEVEL, SPLASH_PER_LEVEL, ATTACK_SPEED_PER_LEVEL, COST_PER_LEVEL;
    private ArrayList<Alien> targetAliens;

    //Constructor
    public Hooman (String name, int level, BufferedImage[] SPRITE_PER_LEVEL, Point position, boolean isActive, boolean isVisible,
                   int EVOLUTION_ORDER, int[] DAMAGE_PER_LEVEL, int[] RANGE_PER_LEVEL, int[] SPLASH_PER_LEVEL, int[] ATTACK_SPEED_PER_LEVEL, int[] COST_PER_LEVEL){
        //Call to parent GameObject constructor
        super(name,level, SPRITE_PER_LEVEL, position, isActive, isVisible);

        this.EVOLUTION_ORDER = EVOLUTION_ORDER;
        this.DAMAGE_PER_LEVEL = DAMAGE_PER_LEVEL;
        this.RANGE_PER_LEVEL = RANGE_PER_LEVEL;
        this.SPLASH_PER_LEVEL = SPLASH_PER_LEVEL;
        this.ATTACK_SPEED_PER_LEVEL = ATTACK_SPEED_PER_LEVEL;
        this.COST_PER_LEVEL = COST_PER_LEVEL;

        //Adding this object to a static arrayList for all Hooman objects
        activeHoomans.add(this);
    }


    //Accessor Methods

    public int getEvolutionOrder(){
        return EVOLUTION_ORDER;
    }

    public int getDamage(){
        return DAMAGE_PER_LEVEL[getLevel()];
    }

    public int getNextDamage(){
        return DAMAGE_PER_LEVEL[getLevel()+1];
    }

    public int getRange(){
        return RANGE_PER_LEVEL[getLevel()];
    }

    public int getNextRange(){
        return RANGE_PER_LEVEL[getLevel()+1];
    }

    public int getSplash(){
        return SPLASH_PER_LEVEL[getLevel()];
    }

    public int getNextSplash(){
        return SPLASH_PER_LEVEL[getLevel()+1];
    }

    public int getAttackSpeed(){
        return ATTACK_SPEED_PER_LEVEL[getLevel()];
    }

    public int getNextAttackSpeed(){
        return ATTACK_SPEED_PER_LEVEL[getLevel()+1];
    }

    public int getCost(){
        return COST_PER_LEVEL[getLevel()];
    }

    public int getNextCost(){
        return COST_PER_LEVEL[getLevel()+1];
    }

    public ArrayList<Alien> getTargetAliens(){
        return targetAliens;
    }

    private int compareToAZ(Hooman hooman){
        return getName().compareTo(hooman.getName());
    }

    private int compareToCost(Hooman hooman){
        return getCost()-hooman.getCost();
    }

    private int compareToEvolution(Hooman hooman){
        return getEvolutionOrder() - hooman.getEvolutionOrder();
    }

    public void findTargetAliens(){
        Alien furthestAlien = null;
        double furthestAlienWaypoint = 0, deltaX, deltaY, distance;
        for (Alien alien : Alien.getAliens()){
            deltaX = alien.getPosition().getX()-getPosition().getX();
            deltaY = alien.getPosition().getY()-getPosition().getY();
            distance = Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));
            if (distance<=getRange() && alien.getCurrentWaypointIndex()>furthestAlienWaypoint){
                furthestAlien = alien;
                furthestAlienWaypoint = alien.getCurrentWaypointIndex();
            }
        }
        for (Alien alien : Alien.getAliens()){
            if (Math.abs(alien.getPosition().getX()-furthestAlien.getPosition().getX())<=getSplash()
             && alien.getPosition().getY()-furthestAlien.getPosition().getY()<=getSplash()){
                targetAliens.add(alien);
            } 
        }

    }

    public abstract void attack();


    //Placeable method
    //Placing the game object onto the game field
    @Override
    public void place(Point position) {
        setActivity(true);
        setVisibility(true);
        setPosition(position);
    }


    //Upgradable method
    //Changing levels
    @Override
    public void upgrade() {
        setLevel(getLevel()+1);
    }

    public static int getCurrentSortType(){
        return currentSortType;
    }


    public static void sort(){
        switch (currentSortType){
            case 0 -> {
                sortAZ();
                currentSortType++;
            }
            case 1 -> {
                sortCost();
                currentSortType++;
            }
            case 2 -> {
                sortEvolution();
                currentSortType = 0;
            }
        }
    }

    private static void sortAZ(){}

    private static void sortCost(){}

    private static void sortEvolution(){}


    //public static ArrayList<Hooman> getHoomans(){}

    public static void addHooman(Hooman hooman){}

    public static void setHooman(ArrayList<Hooman> hoomans){}


    /**Hooman extends GameObject implements Upgradable implements Placeable
    - activeHoomans: ArrayList<Hooman>
    - sortedHoomans: Hooman[]
    - currentSortType: int
    - EVOLUTION_ORDER: int
    - DAMAGE_PER_LEVEL: int[]
    - RANGE_PER_LEVEL: int[]
    - SPLASH_PER_LEVEL: int[]
    - ATTACK_SPEED_PER_LEVEL: int[]
    - COST_PER_LEVEL: int[]
    + Hooman(String name, int level, BufferedImage[] SPRITE_PER_LEVEL, Point position, boolean isActive, boolean isVisible, int EVOLUTION_ORDER, int[] DAMAGE_PER_LEVEL, int[] RANGE_PER_LEVEL, int[] SPLASH_PER_LEVEL, int[] ATTACK_SPEED_PER_LEVEL, int[] COST_PER_LEVEL)
    + getDamage(): int
    + getRange(): int
    + getSplash(): int
    + getAttackSpeed(): int
    + getCost(): int
    + Attack(): void
    + Upgrade(): void
    + Place(Point position): void
    + sort(): void
    - sortAZ(): void
    - sortCost(): void
    - sortEvolution(): void
    - switchSortType(): void
    + getHoomans(): ArrayList<Hooman>
    + addHooman(Hooman hooman): void
    + setHooman(ArrayList<Hooman> hoomans): void
    
    */
}
