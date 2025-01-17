package GameObjects;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.Graphics;
import Interfaces.Placeable;
import java.awt.Image;
import java.util.Arrays;

import com.google.common.escape.ArrayBasedCharEscaper;



public abstract class Hooman extends GameObject implements Placeable{
    private static ArrayList<Hooman> activeHoomans = new ArrayList<Hooman>(); 
    private static Hooman[] sortedHoomans = {new Caveman(new Point(), false, false), new Archer(new Point(), false, false), new Farmer(new Point(), false, false), new Caveman(new Point(), false, false), new Caveman(new Point(), false, false), new Caveman(new Point(), false, false), new Caveman(new Point(), false, false), new Caveman(new Point(), false, false), new Caveman(new Point(), false, false), new Caveman(new Point(), false, false), new Caveman(new Point(), false, false), new Caveman(new Point(), false, false)};
    private static int currentSortType;
    private final int evolutionIndex, damage, range, splash, reloadSpeed, cost;
    private boolean beingPlaced;
    private long lastAttackTime;
    private ArrayList<Alien> targetAliens = new ArrayList<Alien>();

    //Constructor
    public Hooman (String name, Image sprite, Point position, boolean isActive, boolean isVisible, 
                   /*boolean beingPlaced,*/  int evolutionIndex, int damage, int range, int splash, int reloadSpeed, int cost){
        //Call to parent GameObject constructor
        super(name, sprite, position, isActive, isVisible);
        this.beingPlaced = beingPlaced;
        this.evolutionIndex = evolutionIndex;
        this.damage = damage;
        this.range = range;
        this.splash = splash;
        this.reloadSpeed = reloadSpeed;
        this.cost = cost;

        lastAttackTime = 0;
        //Adding this object to a static arrayList for all Hooman objects
        //activeHoomans.add(this);
    }


    //Accessor Methods

    public boolean getBeingPlaced(){
        return beingPlaced;
    }

    public int getEvolutionIndex(){
        return evolutionIndex;
    }

    public int getDamage(){
        return damage;
    }

    public int getRange(){
        return range;
    }

    public int getSplash(){
        return splash;
    }

    public int getReloadSpeed(){
        return reloadSpeed;
    }

    public int getCost(){
        return cost;
    }

    public long getLastAttackTime(){
        return lastAttackTime;
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
        return getEvolutionIndex() - hooman.getEvolutionIndex();
    }

    public void findTargetAliens(){
        
        // Clear the targets to be ready for next set of targets
        targetAliens.clear();

        Alien furthestAlien = null;
        double furthestAlienWaypointIndex = -1, furthestProgress = -1, deltaX, deltaY, distance;

        // Iterate through all active aliens
        for (Alien alien : Alien.getAliens()){
            
            // Find the distance between the Aslien and the Hooman
            deltaX = alien.getPosition().getX()-getPosition().getX();
            deltaY = alien.getPosition().getY()-getPosition().getY();
            distance = Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));

            // If the alien is in range and is further than the previous furthest waypoint on the path
            if ((distance<=getRange() && (alien.getCurrentWaypointIndex()>furthestAlienWaypointIndex))||(distance<=getRange() && alien.getCurrentWaypointIndex()==furthestAlienWaypointIndex && alien.getProgress()>furthestProgress)){

                // Set the new furthest alien to be the alien
                furthestAlien = alien;

                // Set the new furthest waypoint to be the alien's current waypoint
                furthestAlienWaypointIndex = alien.getCurrentWaypointIndex();

                // Set the furthest progress along that waypoint to the alien's current progress
                furthestProgress = alien.getProgress();
            }

            // If the alien is in range, is equal to the previous furthest waypoint on the path, and has made the most progress along said waypoint

        }

        // As long as there is a furthest alien
        if (furthestAlien!=null){

            //Iterate through all the aliens
            for (Alien alien : Alien.getAliens()){

                // If the distance between the furthest alien and the current alien is within the splash square
                if (Math.abs(alien.getPosition().getX()-furthestAlien.getPosition().getX())<=getSplash()
                && Math.abs(alien.getPosition().getY()-furthestAlien.getPosition().getY())<=getSplash()){

                    // Add the current alien to the targetAliens array
                    targetAliens.add(alien);
                
                } 
            }
        }
        

    }

    public void attack(){
        findTargetAliens();
        if (getTargetAliens().isEmpty()||getTargetAliens()==null) {
            return;
        }

        // Visual animation
        animateAttack();

        // Apply damage to target aliens
        for (Alien alien : getTargetAliens()) {
            alien.takeDamage(getDamage());
        }
        lastAttackTime = System.currentTimeMillis();
    }

    public abstract void animateAttack();

    public abstract void draw(Graphics g);


    //Placeable method
    //Placing the game object onto the game field
    @Override
    public void place(Point position) {
        setActivity(true);
        setVisibility(true);
        setPosition(position);
        // beingPlaced = true;
    }


    public static Hooman[] getSortedHoomans(){
        return sortedHoomans;
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


    public static ArrayList<Hooman> getHoomans(){
        return activeHoomans;
    }

    public static void addHooman(Hooman hooman){
        activeHoomans.add(hooman);
    }

    public static void addHoomans(ArrayList<Hooman> hoomans){
        activeHoomans.addAll(hoomans);
    }

    public static void setHoomans(ArrayList<Hooman> hoomans){
        activeHoomans = hoomans;
    }


}
