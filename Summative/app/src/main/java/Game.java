import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;


public class Game extends JFrame implements Runnable{
    private GameScreen gameScreen;
    private Shop shop;
    private BufferedImage tileSet;
    private Thread gameThread;
    private JButton shopToggleButton;

    private long previousTime;
    private long currentTime;
    private double accumulativeLag;
    private double elapsed;
    private final double UPS = 60.0;
    private final double TIME_PER_UPDATE = 1000.0/UPS;

    private final static int tileSize = 32;

    public static void main(String[] args) {
        new Game();

    }

    public Game() {
        // Gets the bufferedImage of the image resource
        importImage();

        // Closes the program when the user closes the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Creates a 1280x640 px window
        setSize(tileSize*40, tileSize*20);
        // Window is placed at the centre of the screen
        setLocationRelativeTo(null);

        setLayout(null);


        // Creates the game screen GUI object
        gameScreen = new GameScreen(tileSet);
        gameScreen.setBounds(0,0,getWidth(), getHeight());
        // Loads the GUI onto the window
        add(gameScreen);

        Rectangle shopBounds  = new Rectangle(0, (int)(3/4.0 * getHeight()), getWidth(),(int)(1/4.0*getHeight()));
        shop = new Shop(tileSet, shopBounds);
        shop.setBounds(shopBounds);
        shop.setVisible(false);
        add(shop);
    

        shopToggleButton = new JButton("Shop");
        shopToggleButton.setBounds(0, 15*32, 96, 32);
        shopToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                shop.toggleVisibility();
            }
        });
        add(shopToggleButton);

        getContentPane().setComponentZOrder(gameScreen, 2);
        getContentPane().setComponentZOrder(shop, 1);
        getContentPane().setComponentZOrder(shopToggleButton, 0);

        // Makes the window visible
        
        setVisible(true);
        revalidate();
        repaint();
        System.out.println("Shop visible: " + shop.isVisible());
        System.out.println("Shop Bounds: " + shop.getBounds());
        System.out.println("Button Bounds: " + shopToggleButton.getBounds());

        gameThread = new Thread(this);
        gameThread.start();

        //https://chatgpt.com/share/67786f4d-d214-800f-89bd-c28037dc9ea9
    }

    private void update(){}



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
        accumulativeLag = 0;
        previousTime = System.currentTimeMillis();
        while (true) {

            // Finds the time elapsed and adds it to the accumulativeLag
            manageTime();

            // Since the game should run at UPS, if the total time elapsed is greater
            // than the TIME_PER_UPDATE, then update and adjust the acumulated time and lag

            while (accumulativeLag>= TIME_PER_UPDATE) {
                // Updates the game
                update();
                accumulativeLag-=TIME_PER_UPDATE;
            }

            // Repaints the entire frame
            repaint();

            //Trys to sleep as much as possible to reduce CPU utilisation
            sleep(1);
            
        }
    }

    // Finds the time elapsed and adds it to the accumulativeLag
    private void manageTime(){
        currentTime = System.currentTimeMillis();
        elapsed = currentTime-previousTime;
        accumulativeLag+= elapsed;
        previousTime = System.currentTimeMillis();
    }

    //Trys to sleep as much as possible to reduce CPU utilisation
    private void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
