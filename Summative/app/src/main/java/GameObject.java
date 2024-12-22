
public class GameObject {
    private String name;
    private int level = 0, xPos = 0, yPos = 0, size = 0;
    private boolean isActive, isVisible;

    public GameObject(int level, String name, int xPos, int yPos, int size){
        setLevel(level);
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.size = size;
    }

    public int getLevel(){
        return level;
    }

    public String name(){
        return name;
    }

    public int getXPos(){
        return xPos;
    }

    public int getYPos(){
        return yPos;
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

    public void setXPos(int xPos){
        if (xPos>=0){
            this.xPos = xPos;
        }
    }

    public void setYPos(int yPos){
        if (yPos>=0){
            this.yPos = yPos;
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
