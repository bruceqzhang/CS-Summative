package GameObjects;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.util.ArrayList;

public abstract class GameObject {
    //Instance variables
    private String name = "N/A";
    private int level = 0, size = 0;
    private final BufferedImage[] SPRITE_PER_LEVEL;
    private Point position = new Point(); //https://docs.oracle.com/javase/8/docs/api/java/awt/Point.html
    private boolean isActive = false, isVisible = false;
    private static ArrayList<GameObject> activeGameObjects = new ArrayList<GameObject>();

    //Main constructor
    public GameObject(String name, int level, BufferedImage[] SPRITE_PER_LEVEL, Point position, boolean isActive, boolean isVisible){
        setName(name);
        setLevel(level);
        this.SPRITE_PER_LEVEL = SPRITE_PER_LEVEL;
        setPosition(position);
        setSize(size);
        setActivity(isActive);
        setVisibility(isVisible);
    }

    //Accessor methods

    public String getName(){
        return name;
    }
   
    public int getLevel(){
        return level;
    }
    
    public BufferedImage getSprite(){
        return SPRITE_PER_LEVEL[level];
    }


    public Point getPosition(){
        return position;
    }

    public int getSize(){
        return size;
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

    public void setLevel(int level){
        if (level>=0){
            this.level = level;
        }
    }

    public void setPosition(Point position){
        if (position.getX()>=0 && position.getY()>=0){
            this.position = position;
        }
    }

    public void setSize(int size){
        if (size>=0){
            this.size = size;
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
