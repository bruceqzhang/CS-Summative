package Main;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.util.ArrayList;

import GameObjects.Alien;
import GameObjects.GameObject;
import GameObjects.Hooman;

import java.awt.image.BufferedImage;

public class GameScreen extends JPanel{
    private BufferedImage tileSet;
    private BufferedImage[][] tileOrder;
    private BufferedImage[][] propOrder;
    private BufferedImage[][] allTiles;

    private Game game;
    
    //Constructor
    public GameScreen(Game game, BufferedImage tileSet){
        this.tileSet = tileSet;
        this.game = game;
        // Covers entire JFrame with gameScreen
        this.setBounds(0,0, game.getWidth(), game.getHeight());
        // Loads the GUI onto the window
        game.add(this);
        //Creates a 25x14 matrix array with every possibly individual tile from the imported tileset
        allTiles = new BufferedImage[25][14];
        for (int x = 0; x<25; x++){
            for (int y = 0; y<14; y++){
                allTiles[x][y] = tileSet.getSubimage(x*16, y*16, 16, 16);
            }
        }
        //Sets the base tiles to be grass
        setGrassTiles();
        setRandomProps();
    }
    //Method to actually paint and draw out the GUI
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        //Drawing each tile as a grass tile, and sometimes drawing a prop or object on top
        for (int x = 0; x<40; x++){
            for (int y = 0; y<20; y++){
                g.drawImage(tileOrder[x][y], x*32, y*32, 32, 32,null);
                g.drawImage(propOrder[x][y], x*32, y*32, 32, 32, null);
            }

        }
        //Draws the set path
        drawPath(g);
        for (Alien alien: Alien.getAliens()){
            if (alien.getVisibility()){
                alien.draw(g);
            }
        }
        for (Hooman hooman: Hooman.getHoomans()){
            if(hooman.getVisibility()){
                hooman.draw(g);
            }
        }
        
        Alien.drawWaypoints(g);

    }

    //Sets the base tiles to be grass
    private void setGrassTiles(){
        tileOrder = new BufferedImage[40][20];
        for (int i = 0; i<tileOrder.length; i++){
            for (int j=0; j<tileOrder[i].length;j++){
                tileOrder[i][j] = randomGrass();
            }
        }
        
    }

    //Finds random grasstiles
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

    //Occasionally finds random props 
    private void setRandomProps(){
        int randomProp;
        propOrder = new BufferedImage[40][20];
        //Removing ugly and annoying tiles
        allTiles[14][12] = null;
        allTiles[19][12] = null;
        allTiles[20][12] = null;
        allTiles[18][12] = null;
        allTiles[21][12] = null;
        allTiles[21][13] = null;
        allTiles[22][13] = null;
    
        //Randomly and occasioanlly finds props
        for (int i = 0; i<propOrder.length; i++){
            for (int j = 0; j<propOrder[i].length; j++){
                randomProp = (int)(Math.random()*40);
                propOrder[i][j] = switch(randomProp){
                    case 0 -> allTiles[10+(int)(Math.random()*15)][11];
                    case 1 -> allTiles[(int)(Math.random()*25)][12];
                    case 2 -> allTiles[(int)(Math.random()*25)][13];
                    //From cases 3-39, does not return a prop so that only some tiles have props
                    default -> null;
                };
            }
        }
    }

    //Draws the path
    private void drawPath(Graphics g){
        Alien.addWaypoint(0, 128);
        drawHorizontal(g, 17, 0, 3);
        drawTopRight(g, 17, 3);
        drawVertical(g,2,17,5);
        drawBottomRight(g,17,7);
        drawHorizontal(g, 8, 9, 7);
        drawTopLeft(g,7,7);
        drawVertical(g, 2, 7, 9);
        drawBottomLeft(g, 7, 11);
        drawHorizontal(g, 12, 9, 11);
        drawBottomRight(g, 21, 11);
        drawVertical(g,6,21,5);
        drawTopLeft(g,21,3);
        drawHorizontal(g, 6, 23, 3);
        drawTopRight(g,29,3);
        drawVertical(g, 15, 29, 5);
        Alien.addWaypoint(960, 640);
    }

    //Many helper methods designed to help draw the path; names are somewhat self-explanatory

    private void drawHorizontal(Graphics g, int length, int xStart, int yStart){
        for (int x = xStart; x<xStart+length; x++){
            g.drawImage(allTiles[14][4], x*32, yStart*32, 32,32, null);
        }
        for (int x = xStart; x<xStart+length; x++){
            //Alien.addWaypoint(xStart*32, (yStart+1)*32);
            g.drawImage(allTiles[14][6], x*32, (yStart+1)*32, 32,32, null);
        }
    }

    private void drawVertical(Graphics g, int length, int xStart, int yStart){
        for (int y = yStart; y<yStart+length; y++){
            g.drawImage(allTiles[13][5], xStart*32, y*32, 32,32, null);
        }
        for (int y = yStart; y<yStart+length; y++){
            //Alien.addWaypoint((xStart+1)*32, yStart*32);
            g.drawImage(allTiles[15][5], (xStart+1)*32, y*32, 32,32, null);
        }        
    }

    private void drawTopRight(Graphics g, int xStart, int yStart){
        Alien.addWaypoint((xStart+1)*32, (yStart+1)*32);
        g.drawImage(tileSet.getSubimage(14*16, 4*16,32,32), xStart*32, yStart*32, 64,64,null);
    }

    private void drawTopLeft(Graphics g, int xStart, int yStart){
        Alien.addWaypoint((xStart+1)*32, (yStart+1)*32);
        g.drawImage(tileSet.getSubimage(13*16, 4*16,32,32), xStart*32, yStart*32, 64,64,null);
    }

    private void drawBottomRight(Graphics g, int xStart, int yStart){
        Alien.addWaypoint((xStart+1)*32, (yStart+1)*32);
        g.drawImage(tileSet.getSubimage(14*16, 5*16,32,32), xStart*32, yStart*32, 64,64,null);
    }

    private void drawBottomLeft(Graphics g, int xStart, int yStart){
        Alien.addWaypoint((xStart+1)*32, (yStart+1)*32);
        g.drawImage(tileSet.getSubimage(13*16, 5*16,32,32), xStart*32, yStart*32, 64,64,null);
    }

}


