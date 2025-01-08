import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;


public class Game extends JFrame implements Runnable{
    private GameScreen gameScreen;
    private Shop shop;
    private BufferedImage tileSet;
    private Icon shopIcon;
    private Thread gameThread;
    private JButton shopToggleButton;
    private BufferedImage shopBackground;
    private int windowWidth;
    private int windowHeight;

    private long previousTime;
    private long currentTime;
    private double accumulativeLag;
    private double elapsed;
    private final double UPS = 60.0;
    private final double TIME_PER_UPDATE = 1000.0/UPS;
    

    private static int tileSize = 32;

    //Main method
    public static void main(String[] args) {
        new Game();

    }

    //Constructor
    public Game() {
        // Gets the bufferedImage of the image resource
        importImages();

        // Closes the program when the user closes the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Creates a 1280x640 px window
        setSize(tileSize*40, tileSize*20);
        // Makes window resizable
        setResizable(true);
        // Window is placed at the centre of the screen
        setLocationRelativeTo(null);
        // Removes any layouts and forces every component to have bounds
        setLayout(null);


        // Creates the game screen GUI object
        gameScreen = new GameScreen(tileSet);
        // Covers entire JFrame with gameScreen
        gameScreen.setBounds(0,0,getWidth(), getHeight());
        // Loads the GUI onto the window
        add(gameScreen);

        // Initializing the shop
        shop = new Shop(shopBackground);
        // Setting the shop bounds 
        shop.setBounds(0, (int)(3/4.0 * getHeight()), getWidth(),(int)(1/4.0*getHeight()));
        // Adds the shop to the JFrame
        shop.setVisible(false);
        // Making the shop invisible at the start
        add(shop);
    
        // Initializing new JButton to toggle the shop
        shopToggleButton = new JButton(shopIcon);
        // Setting the bounds of the button
        shopToggleButton.setBounds(0, 15*32, 96, 32);

        // Linking the button to perform a task on click
        shopToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                // Hides or shows the shop visibility
                shop.toggleVisibility();
            }
        });

        //Adds the button to the JFrame
        add(shopToggleButton);

        //Sets the order of each component on the content pane
        getContentPane().setComponentZOrder(gameScreen, 2);//Chatgpt
        getContentPane().setComponentZOrder(shop, 1);
        getContentPane().setComponentZOrder(shopToggleButton, 0);

        // Makes the window visible
        setVisible(true);

        // Making sure that everything is displayed properly
        revalidate();
        repaint();

        //Starts gameThread
        gameThread = new Thread(this);
        gameThread.start();

        //https://chatgpt.com/share/67786f4d-d214-800f-89bd-c28037dc9ea9
    }

    // Update method to be used for checking inputs and updating the changes in the actual game
    // Such as movement of aliens or projectile motion
    private void update(){}



    // Imports the required resources
    private void importImages() {
        // Retrieves the filepath of the image and reads it with a stream of bytes
        InputStream tilesetInputStream = getClass().getResourceAsStream("/Resources/tileset.png");
        try {
            // Converts the stream into a bufferedImage
            tileSet = ImageIO.read(tilesetInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Retrieves the filepath of the image and reads it with a stream of bytes
        InputStream shopInputStream = getClass().getResourceAsStream("/Resources/shopBackground.png");

        try {
            // Converts the stream into a bufferedImage
            shopBackground = ImageIO.read(shopInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Scales the icon to match the size of the button
        Image scaledImage = new ImageIcon("Summative/app/src/main/java/Resources/shopButton.png").getImage().getScaledInstance(96,32, Image.SCALE_SMOOTH);
        shopIcon = new ImageIcon(scaledImage); //https://www.tutorialspoint.com/how-to-add-icon-to-jbutton-in-java

    }

    //Runnable method
    @Override
    public void run() {
        
        accumulativeLag = 0;
        previousTime = System.currentTimeMillis();

        //Game loop that updates and renders the game whilst keeping lag to a minimum
        while (true) {

            // Finds the time elapsed and adds it to the accumulativeLag
            manageTime();

            // Since the game should run at UPS, if the total time elapsed is greater
            // than the TIME_PER_UPDATE, then update and adjust the acumulated time and lag

            while (accumulativeLag>= TIME_PER_UPDATE) {
                // Updates the game
                update();
                //Adjusts acumulativeLag
                accumulativeLag-=TIME_PER_UPDATE;
            }

            // Repaints the entire frame
            repaint();

            //Trys to sleep as much as possible to reduce CPU utilisation
            sleep(1);
            
        }
    }

    // Helped method that finds the time elapsed and adds it to the accumulativeLag
    private void manageTime(){
        currentTime = System.currentTimeMillis();
        elapsed = currentTime-previousTime;
        accumulativeLag+= elapsed;
        previousTime = System.currentTimeMillis();
    }

    // Helper method to sleep as much as possible to reduce CPU utilisation
    private void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
