import java.awt.Image;
import java.awt.Point;
import java.io.*;

public class Tower extends GameObject{
    private final Weapon attack;
    private final Image[] sprites;
    private final int[] damages, ranges, splashes, attackSpeeds, costs;

    public Tower (String name, int level, Point position, int size, boolean isActive, boolean isVisible, Weapon attack, Image[] sprites, int[] damages, int[] ranges, int[] splashes, int[] attackSpeeds, int[] costs){
        super(name,level, position, size, isActive, isVisible);
        this.attack = attack;
        this.sprites = sprites;
        this.damages = damages;
        this.ranges = ranges;
        this.splashes = splashes;
        this.attackSpeeds = attackSpeeds;
        this.costs = costs;
    }

    public Tower(String line){
        String[] parameters = line.split(",");
        this(parameters[0],Integer.parseInt(parameters[1]), new Point(Integer.parseInt(parameters[2]),Integer.parseInt(parameters[3])), Integer.parseInt(parameters[4]), Boolean.parseBoolean(parameters[5], Boolean.parseBoolean(parameters[6])
    }

    public Tower(int lineNum){
        try{
            BufferedReader br = new BufferedReader(new FileReader("Summative/app/src/main/java/TextFiles/TowerInfo.txt"));
            String line = "";
            int count = 0;
            while (line!=null&&count<=lineNum){
                line = br.readLine();
                count++;
            }
            this(line);
        }
    }

    public Image getSprite(){
        return sprites[super.getLevel()];
    }

    public int getDamage(){
        return damages[super.getLevel()];
    }


    /**Tower extends GameObject implements Upgradable
- attack: Weapon
- sprites: Image[]
- ranges: int[]
- splashes: int[]
- attackSpeeds: int[]
- costs: int[]
+ Tower(String line)
+ Tower(String name, int level, int xPos, int yPos, int size, image[] sprites, int[] damages, int[] ranges, int[] splashes, int[] attackSpeeds, int[] costs)
+ getSprite(): Image
+ getDamage(): int
+ getRange(): int
+ getSplash(): int
+ getAttackSpeed(): int
+ getCost(): int
+ getAttack(): Weapon
+ Attack(ArrayList<Enemy> enemies): void
+ Upgrade(): void
+ Place(int xPos, int yPos): void

 */
}
