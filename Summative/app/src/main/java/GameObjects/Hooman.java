package GameObjects;

import java.awt.Point;
import java.util.ArrayList;
import java.awt.Graphics;
import Interfaces.Placeable;
import java.awt.Image;

/**
 * Represents a generic Hooman in the game.
 */
public abstract class Hooman extends GameObject implements Placeable {
    private static ArrayList<Hooman> activeHoomans = new ArrayList<>();
    private static Hooman[] sortedHoomans = {
        new Caveman(new Point(), false, false), 
        new Archer(new Point(), false, false), 
        new Farmer(new Point(), false, false), 
        new Musketeer(new Point(), false, false), 
        new Knight(new Point(), false, false), 
        new Wizard(new Point(), false, false), 
        new Tank(new Point(), false, false), 
        new Soldier(new Point(), false, false), 
        new Ballista(new Point(), false, false), 
        new Brute(new Point(), false, false), 
        new Laser(new Point(), false, false), 
        new Bowler(new Point(), false, false)
    };
    private static int currentSortType;
    private final int evolutionIndex, damage, range, splash, reloadSpeed, cost;
    private long lastAttackTime;
    private ArrayList<Alien> targetAliens = new ArrayList<>();

    /**
     * Constructor for the Hooman class.
     * 
     * @param name The name of the Hooman.
     * @param sprite The sprite image of the Hooman.
     * @param position The position of the Hooman.
     * @param isActive Whether the Hooman is active.
     * @param isVisible Whether the Hooman is visible.
     * @param evolutionIndex The evolution index of the Hooman.
     * @param damage The damage dealt by the Hooman.
     * @param range The range of the Hooman.
     * @param splash The splash damage radius of the Hooman.
     * @param reloadSpeed The reload speed of the Hooman.
     * @param cost The cost of the Hooman.
     */
    public Hooman(String name, Image sprite, Point position, boolean isActive, boolean isVisible, 
                  int evolutionIndex, int damage, int range, int splash, int reloadSpeed, int cost) {
        super(name, sprite, position, isActive, isVisible);
        this.evolutionIndex = evolutionIndex;
        this.damage = damage;
        this.range = range;
        this.splash = splash;
        this.reloadSpeed = reloadSpeed;
        this.cost = cost;
        this.lastAttackTime = 0;
        currentSortType = 0;
    }

    /**
     * Gets the evolution index of the Hooman.
     * 
     * @return The evolution index of the Hooman.
     */
    public int getEvolutionIndex() {
        return evolutionIndex;
    }

    /**
     * Gets the damage dealt by the Hooman.
     * 
     * @return The damage dealt by the Hooman.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Gets the range of the Hooman.
     * 
     * @return The range of the Hooman.
     */
    public int getRange() {
        return range;
    }

    /**
     * Gets the splash damage radius of the Hooman.
     * 
     * @return The splash damage radius of the Hooman.
     */
    public int getSplash() {
        return splash;
    }

    /**
     * Gets the reload speed of the Hooman.
     * 
     * @return The reload speed of the Hooman.
     */
    public int getReloadSpeed() {
        return reloadSpeed;
    }

    /**
     * Gets the cost of the Hooman.
     * 
     * @return The cost of the Hooman.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Gets the last attack time of the Hooman.
     * 
     * @return The last attack time of the Hooman.
     */
    public long getLastAttackTime() {
        return lastAttackTime;
    }

    /**
     * Gets the target aliens of the Hooman.
     * 
     * @return The target aliens of the Hooman.
     */
    public ArrayList<Alien> getTargetAliens() {
        return targetAliens;
    }

    /**
     * Finds the target aliens within range.
     */
    public void findTargetAliens() {
        targetAliens.clear();
        Alien furthestAlien = null;
        double furthestAlienWaypointIndex = -1, furthestProgress = -1, deltaX, deltaY, distance;

        for (Alien alien : Alien.getAliens()) {
            deltaX = alien.getPosition().getX() - getPosition().getX();
            deltaY = alien.getPosition().getY() - getPosition().getY();
            distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

            if ((distance <= getRange() && (alien.getCurrentWaypointIndex() > furthestAlienWaypointIndex)) ||
                (distance <= getRange() && alien.getCurrentWaypointIndex() == furthestAlienWaypointIndex && alien.getProgress() > furthestProgress)) {
                furthestAlien = alien;
                furthestAlienWaypointIndex = alien.getCurrentWaypointIndex();
                furthestProgress = alien.getProgress();
            }
        }

        if (furthestAlien != null) {
            for (Alien alien : Alien.getAliens()) {
                if (Math.abs(alien.getPosition().getX() - furthestAlien.getPosition().getX()) <= getSplash() &&
                    Math.abs(alien.getPosition().getY() - furthestAlien.getPosition().getY()) <= getSplash()) {
                    targetAliens.add(alien);
                }
            }
        }
    }

    /**
     * Attacks the target aliens.
     */
    public void attack() {
        findTargetAliens();
        if (getTargetAliens().isEmpty()) {
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

    /**
     * Animates the attack of the Hooman.
     */
    public abstract void animateAttack();

    /**
     * Draws the Hooman.
     * 
     * @param g The Graphics object used for drawing.
     */
    public abstract void draw(Graphics g);

    /**
     * Places the Hooman onto the game field.
     * 
     * @param position The position to place the Hooman.
     */
    @Override
    public void place(Point position) {
        setActive(true);
        setVisible(true);
        setPosition(position);
    }

    /**
     * Gets the sorted array of Hoomans.
     * 
     * @return The sorted array of Hoomans.
     */
    public static Hooman[] getSortedHoomans() {
        return sortedHoomans;
    }

    /**
     * Gets the current sort type.
     * 
     * @return The current sort type.
     */
    public static int getCurrentSortType() {
        return currentSortType;
    }

    /**
     * Sorts the Hoomans based on the current sort type.
     */
    public static void sort() {
        switch (currentSortType) {
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

    /**
     * Sorts the Hoomans alphabetically.
     */
    private static void sortAZ() {
        for (int i = 0; i < sortedHoomans.length; i++) {
            for (int j = i + 1; j < sortedHoomans.length; j++) {
                if (sortedHoomans[i].compareAlphabeticTo(sortedHoomans[j]) > 0) {
                    Hooman temp = sortedHoomans[i];
                    sortedHoomans[i] = sortedHoomans[j];
                    sortedHoomans[j] = temp;
                }
            }
        }
    }

    /**
     * Sorts the Hoomans by cost.
     */
    private static void sortCost() {
        for (int i = 0; i < sortedHoomans.length; i++) {
            int smallest = i;
            for (int j = i + 1; j < sortedHoomans.length; j++) {
                if (sortedHoomans[smallest].compareCostTo(sortedHoomans[j]) > 0) {
                    smallest = j;
                }
            }
            Hooman temp = sortedHoomans[i];
            sortedHoomans[i] = sortedHoomans[smallest];
            sortedHoomans[smallest] = temp;
        }
    }

    /**
     * Sorts the Hoomans by evolution index.
     */
    private static void sortEvolution() {
        for (int i = 1; i < sortedHoomans.length; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (sortedHoomans[i].compareEvolutionTo(sortedHoomans[j]) < 0) {
                    Hooman temp = sortedHoomans[j];
                    sortedHoomans[j] = sortedHoomans[j + 1];
                    sortedHoomans[j + 1] = temp;
                }
            }
        }
    }

    /**
     * Gets the list of active Hoomans.
     * 
     * @return The list of active Hoomans.
     */
    public static ArrayList<Hooman> getHoomans() {
        return activeHoomans;
    }

    /**
     * Adds a Hooman to the list of active Hoomans.
     * 
     * @param hooman The Hooman to add.
     */
    public static void addHooman(Hooman hooman) {
        activeHoomans.add(hooman);
    }

    /**
     * Adds multiple Hoomans to the list of active Hoomans.
     * 
     * @param hoomans The list of Hoomans to add.
     */
    public static void addHoomans(ArrayList<Hooman> hoomans) {
        activeHoomans.addAll(hoomans);
    }

    /**
     * Sets the list of active Hoomans.
     * 
     * @param hoomans The new list of active Hoomans.
     */
    public static void setHoomans(ArrayList<Hooman> hoomans) {
        activeHoomans = hoomans;
    }

    /**
     * Compares the Hooman alphabetically to another Hooman.
     * 
     * @param hooman The Hooman to compare to.
     * @return The comparison result.
     */
    private int compareAlphabeticTo(Hooman hooman) {
        return getName().compareTo(hooman.getName());
    }

    /**
     * Compares the cost of the Hooman to another Hooman.
     * 
     * @param hooman The Hooman to compare to.
     * @return The comparison result.
     */
    private int compareCostTo(Hooman hooman) {
        return getCost() - hooman.getCost();
    }

    /**
     * Compares the evolution index of the Hooman to another Hooman.
     * 
     * @param hooman The Hooman to compare to.
     * @return The comparison result.
     */
    private int compareEvolutionTo(Hooman hooman) {
        return getEvolutionIndex() - hooman.getEvolutionIndex();
    }
}