import java.awt.Point;

public class GameObject {
    private String name = "N/A";
    private int level = 0, size = 0;
    private Point position = new Point(); //https://docs.oracle.com/javase/8/docs/api/java/awt/Point.html
    private boolean isActive = false, isVisible = false;

    public GameObject(int level, String name, Point position, int size){
        setLevel(level);
        setName(name);
        setPosition(position);
        setSize(size);
    }

    public int getLevel(){
        return level;
    }

    public String name(){
        return name;
    }

    public Point position(){
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

    public void setLevel(int level){
        if (level>=0){
            this.level = level;
        }
    }

    public void setName(String name){
        this.name = name;
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

}
