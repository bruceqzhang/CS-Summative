package GameObjects;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.util.ArrayList;

public abstract class GameObject {
    //Instance variables
    private final int size = 48;
    private String name;
    private BufferedImage sprite;
    private Point position; 
    private boolean isActive = false, isVisible = false;
    private static ArrayList<GameObject> activeGameObjects = new ArrayList<GameObject>();

    //Main constructor
    public GameObject(String name, BufferedImage sprite, Point position, boolean isActive, boolean isVisible){
        this.name = name;
        this.sprite = sprite;
        this.position = position;
        this.isActive = isActive;
        this.isVisible = isVisible;
    }

    //Accessor methods

    public String getName(){
        return name;
    }
   
    public BufferedImage getSprite(){
        return sprite;
    }

    public int getSize(){
        return size;
    }

    public Point getPosition(){
        return position;
    }

    public boolean getActivity(){
        return isActive;
    }

    public boolean getVisibility(){
        return isVisible;
    }

    //Mutator Methods

    public void setName(String name){
        this.name = name;
    }

    public void setSprite(BufferedImage sprite){
        this.sprite = sprite;
    }

    public void setPosition(Point position){
        if (position.getX()>=0 && position.getY()>=0){
            this.position = position;
        }
    }

    public void setActivity(boolean isActive){
        this.isActive = isActive;
    }

    public void setVisibility(boolean isVisible){
        this.isVisible = isVisible;
    }


    public abstract void draw(Graphics g);

    /*public static ArrayList<GameObject> getGameObjects(){
        return activeGameObjects;
    }

    public static void setGameObjects(ArrayList<GameObject> gameObjects){
        activeGameObjects = gameObjects;
    }

    public static void addGameObject(GameObject gameObject){
        activeGameObjects.add(gameObject);
    }

    public static void removeGameObject(GameObject gameObject){
        activeGameObjects.remove(gameObject);
    }*/
}
