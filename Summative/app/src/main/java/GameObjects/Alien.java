package GameObjects;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import Interfaces.Downgradable;
import Interfaces.Removable;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Represents an Alien in the game.
 */
public class Alien extends GameObject implements Downgradable, Removable {
    private static ArrayList<Alien> activeAliens = new ArrayList<>();
    private static final ArrayList<Point> WAYPOINTS = new ArrayList<>();
    private static final Point STARTING_POINT = new Point(0, 112);
    private static final String[] NAME_PER_LEVEL = {"Crawler", "Stinger", "Shell", "Gloop", "Bloble", "Glob", "Tentacule", "Garugant", "Droid"};
    private static final Image[] SPRITE_PER_LEVEL = importSprites();
    private static final int[] SPEED_PER_LEVEL = {3, 4, 3, 5, 5, 6, 6, 7, 7};
    private static final int[] MAX_HEALTH_PER_LEVEL = {40, 50, 80, 70, 100, 120, 140, 150, 200};

    private boolean reachedGoal, isKilled, beingRemoved;
    private int levelIndex, speed, maxHealth, currentWaypointIndex, currentHealth, originalLevelIndex;
    private final int delay;
    private double progress;

    /**
     * Constructor for the Alien class.
     * 
     * @param isActive Whether the Alien is active.
     * @param isVisible Whether the Alien is visible.
     * @param levelIndex The level index of the Alien.
     * @param delay The delay before the Alien starts moving.
     */
    public Alien(boolean isActive, boolean isVisible, int levelIndex, int delay) {
        super(NAME_PER_LEVEL[levelIndex], SPRITE_PER_LEVEL[levelIndex], STARTING_POINT, isActive, isVisible);
        this.levelIndex = levelIndex;
        this.speed = SPEED_PER_LEVEL[levelIndex];
        this.maxHealth = MAX_HEALTH_PER_LEVEL[levelIndex];
        this.currentWaypointIndex = 0;
        this.delay = delay;
        this.progress = 0;
        this.currentHealth = maxHealth;
        this.originalLevelIndex = levelIndex;
        this.reachedGoal = false;
        this.isKilled = false;
        this.beingRemoved = false;

        if (isActive) {
            addAlien(this);
        }
    }

    /**
     * Constructor for the Alien class with default activity and visibility.
     * 
     * @param levelIndex The level index of the Alien.
     * @param delay The delay before the Alien starts moving.
     */
    public Alien(int levelIndex, int delay) {
        this(false, false, levelIndex, delay);
    }

    /**
     * Gets the level index of the Alien.
     * 
     * @return The level index of the Alien.
     */
    public int getLevelIndex() {
        return levelIndex;
    }

    /**
     * Gets the speed of the Alien.
     * 
     * @return The speed of the Alien.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Gets the maximum health of the Alien.
     * 
     * @return The maximum health of the Alien.
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Gets the current waypoint index of the Alien.
     * 
     * @return The current waypoint index of the Alien.
     */
    public int getCurrentWaypointIndex() {
        return currentWaypointIndex;
    }

    /**
     * Gets the delay before the Alien starts moving.
     * 
     * @return The delay before the Alien starts moving.
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Gets the progress of the Alien along the current path segment.
     * 
     * @return The progress of the Alien along the current path segment.
     */
    public double getProgress() {
        return progress;
    }

    /**
     * Gets the current health of the Alien.
     * 
     * @return The current health of the Alien.
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Gets the original level index of the Alien.
     * 
     * @return The original level index of the Alien.
     */
    public int getOriginalLevelIndex() {
        return originalLevelIndex;
    }

    /**
     * Checks if the Alien has reached the goal.
     * 
     * @return True if the Alien has reached the goal, false otherwise.
     */
    public boolean getReachedGoal() {
        return reachedGoal;
    }

    /**
     * Checks if the Alien is killed.
     * 
     * @return True if the Alien is killed, false otherwise.
     */
    public boolean getIsKilled() {
        return isKilled;
    }

    /**
     * Checks if the Alien is being removed.
     * 
     * @return True if the Alien is being removed, false otherwise.
     */
    public boolean getBeingRemoved() {
        return beingRemoved;
    }

    /**
     * Sets the activity status of the Alien.
     * 
     * @param isActive The new activity status of the Alien.
     */
    @Override
    public void setActive(boolean isActive) {
        super.setActive(isActive);
        if (isActive && !getAliens().contains(this)) {
            addAlien(this);
        }
    }

    /**
     * Updates the level index of the Alien.
     */
    public void updateLevelIndex() {
        levelIndex--;
    }

    /**
     * Updates the speed of the Alien based on its level index.
     */
    public void updateSpeed() {
        speed = SPEED_PER_LEVEL[getLevelIndex()];
    }

    /**
     * Updates the health of the Alien based on its level index.
     */
    public void updateHealth() {
        maxHealth = MAX_HEALTH_PER_LEVEL[getLevelIndex()];
        currentHealth = maxHealth;
    }

    /**
     * Updates the current waypoint index of the Alien.
     */
    public void updateCurrentWaypointIndex() {
        currentWaypointIndex++;
    }

    /**
     * Initializes the Alien by setting it active and visible.
     */
    public void initAlien() {
        setActive(true);
        setVisible(true);
    }

    /**
     * Takes damage and downgrades the Alien if health drops below zero.
     * 
     * @param damage The amount of damage taken.
     */
    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth <= 0) {
            downgrade();
        }
    }

    /**
     * Reaches the goal and removes the Alien.
     */
    private void reachGoal() {
        remove();
        reachedGoal = true;
    }

    /**
     * Moves the Alien along the waypoints.
     */
    public void move() {
        if (currentWaypointIndex < WAYPOINTS.size() - 1) {
            animateMovement();
        } else {
            reachGoal();
        }
    }

    /**
     * Animates the movement of the Alien.
     */
    public void animateMovement() {
        Point currentPosition = getPosition();
        Point nextWaypoint = WAYPOINTS.get(currentWaypointIndex + 1);
        int deltaX = (int) (nextWaypoint.getX() - currentPosition.getX());
        int deltaY = (int) (nextWaypoint.getY() - currentPosition.getY());

        if (deltaX != 0) {
            progress = WAYPOINTS.get(currentWaypointIndex).getX() - nextWaypoint.getX() - Math.abs(deltaX);
            setPosition(new Point(
                (int) (currentPosition.getX() + Math.min(getSpeed(), Math.abs(deltaX)) * Integer.signum(deltaX)),
                (int) currentPosition.getY()
            ));
        } else if (deltaY != 0) {
            progress = WAYPOINTS.get(currentWaypointIndex).getY() - nextWaypoint.getY() - Math.abs(deltaY);
            setPosition(new Point(
                (int) currentPosition.getX(),
                (int) (currentPosition.getY() + Math.min(getSpeed(), Math.abs(deltaY)) * Integer.signum(deltaY))
            ));
        }

        if (getPosition().equals(nextWaypoint)) {
            updateCurrentWaypointIndex();
        }
    }

    /**
     * Draws the Alien.
     * 
     * @param g The Graphics object used for drawing.
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(getSprite(), (int) getPosition().getX(), (int) getPosition().getY(), null);
    }

    /**
     * Removes the Alien.
     */
    @Override
    public void remove() {
        setActive(false);
        setVisible(false);
        beingRemoved = true;
    }

    /**
     * Downgrades the Alien to a lower level.
     */
    @Override
    public void downgrade() {
        if (getLevelIndex() > 0) {
            updateLevelIndex();
            updateSpeed();
            updateHealth();
            setName(NAME_PER_LEVEL[levelIndex]);
            setSprite(SPRITE_PER_LEVEL[levelIndex]);
        } else {
            isKilled = true;
            remove();
        }
    }

    /**
     * Adds a waypoint to the list of waypoints.
     * 
     * @param x The x-coordinate of the waypoint.
     * @param y The y-coordinate of the waypoint.
     */
    public static void addWaypoint(int x, int y) {
        WAYPOINTS.add(new Point(x, y));
    }

    /**
     * Gets the list of active Aliens.
     * 
     * @return The list of active Aliens.
     */
    public static ArrayList<Alien> getAliens() {
        return activeAliens;
    }

    /**
     * Removes an Alien from the list of active Aliens.
     * 
     * @param alien The Alien to remove.
     */
    public static void removeAlien(Alien alien) {
        activeAliens.remove(alien);
    }

    /**
     * Adds an Alien to the list of active Aliens.
     * 
     * @param alien The Alien to add.
     */
    public static void addAlien(Alien alien) {
        activeAliens.add(alien);
    }

    /**
     * Sets the list of active Aliens.
     * 
     * @param aliens The new list of active Aliens.
     */
    public static void setAliens(ArrayList<Alien> aliens) {
        activeAliens = aliens;
    }

    /**
     * Gets the aliens for a specific round from the Rounds.txt file.
     * 
     * @param round The round number.
     * @return An array of Aliens for the specified round.
     */
    public static Alien[] getRoundAliens(int round) {
        // Uses FileIO to read the Aliens for the specified round from the Rounds.txt file
        try {
            BufferedReader br = new BufferedReader(new FileReader("/Users/brucezhang/Coding/CS-Summative/Summative/app/src/main/java/TextFiles/Rounds.txt"));
            String line = "";
            boolean lineFound = false;
            while (line != null) {
                if (line.startsWith("Round " + round)) {
                    lineFound = true;
                    break;
                }
                line = br.readLine();
            }
            if (lineFound) {
                line = br.readLine();
                String[] alienText = line.split(",");
                Alien[] roundAliens = new Alien[alienText.length];
                for (int i = 0; i < alienText.length; i++) {
                    roundAliens[i] = new Alien(Integer.parseInt(alienText[i].split(":")[0]), Integer.parseInt(alienText[i].split(":")[1]));
                }
                br.close();
                return roundAliens;
            } else {
                br.close();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Imports the sprites for the Aliens.
     * 
     * @return An array of Images representing the Aliens' sprites.
     */
    public static Image[] importSprites() {
        Image[] sprites = new Image[9];
        BufferedImage spriteSheet;
        try {
            InputStream inputStream = Alien.class.getResourceAsStream("/Resources/alienSpriteSheet.png");
            spriteSheet = ImageIO.read(inputStream);
            sprites[0] = spriteSheet.getSubimage(24, 24, 128, 128).getScaledInstance(getSize() / 2, getSize() / 2, Image.SCALE_AREA_AVERAGING);
            sprites[1] = spriteSheet.getSubimage(176, 24, 128, 128).getScaledInstance(getSize() / 2, getSize() / 2, Image.SCALE_AREA_AVERAGING);
            sprites[2] = spriteSheet.getSubimage(24, 176, 128, 128).getScaledInstance(getSize() / 2, getSize() / 2, Image.SCALE_AREA_AVERAGING);
            sprites[3] = spriteSheet.getSubimage(24, 328, 128, 128).getScaledInstance(getSize() / 2, getSize() / 2, Image.SCALE_AREA_AVERAGING);
            sprites[4] = spriteSheet.getSubimage(328, 328, 128, 128).getScaledInstance(getSize() / 2, getSize() / 2, Image.SCALE_AREA_AVERAGING);
            sprites[5] = spriteSheet.getSubimage(176, 176, 128, 128).getScaledInstance(getSize() / 2, getSize() / 2, Image.SCALE_AREA_AVERAGING);
            sprites[6] = spriteSheet.getSubimage(328, 176, 128, 128).getScaledInstance(getSize() / 2, getSize() / 2, Image.SCALE_AREA_AVERAGING);
            sprites[7] = spriteSheet.getSubimage(176, 328, 128, 128).getScaledInstance(getSize() / 2, getSize() / 2, Image.SCALE_AREA_AVERAGING);
            sprites[8] = spriteSheet.getSubimage(328, 24, 128, 128).getScaledInstance(getSize() / 2, getSize() / 2, Image.SCALE_AREA_AVERAGING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sprites;
    }
}