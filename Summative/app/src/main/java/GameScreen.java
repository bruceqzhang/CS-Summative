import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class GameScreen extends JPanel{
    private BufferedImage tileSet;
    private BufferedImage[][] tileOrder;
    private BufferedImage[][] allTiles;
    
    public GameScreen(BufferedImage tileSet){
        this.tileSet = tileSet;
        allTiles = new BufferedImage[25][14];
        for (int x = 0; x<25; x++){
            for (int y = 0; y<14; y++){
                allTiles[x][y] = tileSet.getSubimage(x*16, y*16, 16, 16);
            }
        }
        setGrassTiles();
    }
    
    public void paintComponent(Graphics g){
        //super.paintComponent(g);
        for (int x = 0; x<40; x++){
            for (int y = 0; y<20; y++){
                g.drawImage(tileOrder[x][y], x*32, y*32, 32, 32,null);
            }

        }

    }

    private void setGrassTiles(){
        tileOrder = new BufferedImage[40][20];
        for (int i = 0; i<tileOrder.length; i++){
            for (int j=0; j<tileOrder[i].length;j++){
                tileOrder[i][j] = randomGrass();
            }
        }
        
    }

    private BufferedImage randomGrass(){
        int random = (int) (Math.random()*4);
        return switch(random){
            case 0 -> allTiles[0][0];
            case 1 -> allTiles[8][2];
            case 2 -> allTiles[2][4];
            case 3 -> allTiles[2][6];
            default -> allTiles[0][0];
        };

    }

}


