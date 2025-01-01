import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends JFrame implements Runnable{
    private GameScreen gameScreen;
    private BufferedImage tileSet;
    private Thread gameThread;

    private long previousTime;
    private long currentTime;
    //private int updateCount;
    //private long upsStartTime;
    private final double UPS = 60.0;
    private final double TIME_PER_UPDATE = 1000.0/UPS;


    public static void main(String[] args) {
        new Game();

    }

    public Game() {
        // Gets the bufferedImage of the image resource
        importImage();

        // Closes the program when the user closes the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Creates a 1280x640 px window
        setSize(1280, 640);
        // Window is placed at the centre of the screen
        setLocationRelativeTo(null);

        // Creates the game screen GUI object
        gameScreen = new GameScreen(tileSet);
        // Loads the GUI onto the window
        this.add(gameScreen);

        // Makes the window visible
        setVisible(true);

        gameThread = new Thread(this);
        gameThread.start();

    }


    private void update() {
        //updateCount++;

    }

    /**private void getUPS(){
        long currentUPSTime = System.currentTimeMillis();
        if (currentTime - upsStartTime >= 1000) { // Check if one second has passed
            System.out.println("UPS: " + updateCount); // Print the number of updates in the past second
            updateCount = 0; // Reset the count
            upsStartTime = currentUPSTime; // Reset the start time
        }
    }*/

    // Imports the tileset
    private void importImage() {
        // Retrieves the filepath of the image and reads it with a stream of bytes
        InputStream inputStream = getClass().getResourceAsStream("/Resources/GRASS.png");
        try {
            // Converts the stream into a bufferedImage
            tileSet = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        double accumulativeLag = 0;
        double elapsed;
        //upsStartTime = System.currentTimeMillis();
        //updateCount = 0;
        previousTime = System.currentTimeMillis();

        while (true) {
            currentTime = System.currentTimeMillis();
            elapsed = currentTime-previousTime;
            accumulativeLag+= elapsed;
            previousTime = System.currentTimeMillis();
            // Since the game should run at 60FPS and UPS, if the time elapsed is greater
            // than the time it takes for 1 frame/update to load, then update and repaint
            // the game

            while (accumulativeLag>= TIME_PER_UPDATE) {
                // Updates the game
                update();

                accumulativeLag-=TIME_PER_UPDATE;
            }
            //getUPS();


            // Calls the paintComponent method
            repaint();
            
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
