import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
public class Game extends JFrame{
    private GameScreen gameScreen;
    private BufferedImage tileSet;

    public static void main(String[] args){
        new Game();

    }
    public Game(){
        importImg();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280,640);
        setLocationRelativeTo(null);
        gameScreen = new GameScreen(tileSet);
        this.add(gameScreen);       
        setVisible(true);
    }
    private void importImg() {
        InputStream inputStream = getClass().getResourceAsStream("/Resources/GRASS.png");
        try {
            tileSet = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
