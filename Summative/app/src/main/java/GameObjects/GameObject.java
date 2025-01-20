package GameObjects;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

/**
 * Represents a generic game object in the game.
 */
public abstract class GameObject {
    // Instance variables
    private static final int SIZE = 64;
    private String name;
    private Image sprite;
    private Point position; 
    private boolean isActive;
    private boolean isVisible;
    private static ArrayList<GameObject> activeGameObjects = new ArrayList<>();

    /**
     * Main constructor for the GameObject class.
     * 
     * @param name The name of the game object.
     * @param sprite The sprite image of the game object.
     * @param position The position of the game object.
     * @param isActive Whether the game object is active.
     * @param isVisible Whether the game object is visible.
     */
    public GameObject(String name, Image sprite, Point position, boolean isActive, boolean isVisible) {
        this.name = name;
        this.sprite = sprite;
        this.position = position;
        this.isActive = isActive;
        this.isVisible = isVisible;
    }

    /**
     * Gets the name of the game object.
     * 
     * @return The name of the game object.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the sprite image of the game object.
     * 
     * @return The sprite image of the game object.
     */
    public Image getSprite() {
        return sprite;
    }

    /**
     * Gets the position of the game object.
     * 
     * @return The position of the game object.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Gets the activity status of the game object.
     * 
     * @return True if the game object is active, false otherwise.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Gets the visibility status of the game object.
     * 
     * @return True if the game object is visible, false otherwise.
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Gets the size of the game object.
     * 
     * @return The size of the game object.
     */
    public static int getSize() {
        return SIZE;
    }

    /**
     * Sets the name of the game object.
     * 
     * @param name The new name of the game object.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the sprite image of the game object.
     * 
     * @param sprite The new sprite image of the game object.
     */
    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    /**
     * Sets the position of the game object.
     * 
     * @param position The new position of the game object.
     */
    public void setPosition(Point position) {
        if (position.getX() >= 0 && position.getY() >= 0) {
            this.position = position;
        }
    }

    /**
     * Sets the activity status of the game object.
     * 
     * @param isActive The new activity status of the game object.
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Sets the visibility status of the game object.
     * 
     * @param isVisible The new visibility status of the game object.
     */
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * Draws the game object.
     * 
     * @param g The Graphics object used for drawing.
     */
    public abstract void draw(Graphics g);

    /**
     * Gets the list of active game objects.
     * 
     * @return The list of active game objects.
     */
    public static ArrayList<GameObject> getActiveGameObjects() {
        return activeGameObjects;
    }

    /**
     * Sets the list of active game objects.
     * 
     * @param gameObjects The new list of active game objects.
     */
    public static void setActiveGameObjects(ArrayList<GameObject> gameObjects) {
        activeGameObjects = gameObjects;
    }

    /**
     * Adds a game object to the list of active game objects.
     * 
     * @param gameObject The game object to add.
     */
    public static void addGameObject(GameObject gameObject) {
        activeGameObjects.add(gameObject);
    }

    /**
     * Removes a game object from the list of active game objects.
     * 
     * @param gameObject The game object to remove.
     */
    public static void removeGameObject(GameObject gameObject) {
        activeGameObjects.remove(gameObject);
    }
}