package GameObjects;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.Graphics;
import Interfaces.Placeable;



public abstract class Hooman extends GameObject implements Placeable{
    private static ArrayList<Hooman> activeHoomans = new ArrayList<Hooman>(); 
    private static Hooman[] sortedHoomans;
    private static int currentSortType;
    private final int evolutionIndex, damage, range, splash, reloadSpeed, cost;
    private ArrayList<Alien> targetAliens = new ArrayList<Alien>();

    //Constructor
    public Hooman (String name, BufferedImage sprite, Point position, boolean isActive, boolean isVisible,
                   int evolutionIndex, int damage, int range, int splash, int reloadSpeed, int cost){
        //Call to parent GameObject constructor
        super(name, sprite, position, isActive, isVisible);

        this.evolutionIndex = evolutionIndex;
        this.damage = damage;
        this.range = range;
        this.splash = splash;
        this.reloadSpeed = reloadSpeed;
        this.cost = cost;

        //Adding this object to a static arrayList for all Hooman objects
        activeHoomans.add(this);
    }


    //Accessor Methods

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

    public void attack(){
        // Perform game logic
        findTargetAliens();
        if (getTargetAliens().isEmpty()) {
            return;
        }

        // Visual animation
        animateAttack();

        //Apply damage to target aliens
        for (Alien alien : getTargetAliens()) {
            alien.takeDamage(getDamage());
        }
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

    public static void setHooman(ArrayList<Hooman> hoomans){
        activeHoomans = hoomans;
    }


}
