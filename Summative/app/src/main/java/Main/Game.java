package Main;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import GameObjects.Caveman;
import GameObjects.GameObject;
import GameObjects.Hooman;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.util.ArrayList;
import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Game extends JFrame implements Runnable{
    private GameScreen gameScreen;
    private ShopMenu shop;
    private JButton shopToggleButton;

    private BufferedImage tileSet;
    private Icon toggleShopButtonIcon;
    private Thread gameThread;
    private BufferedImage shopBackground;

    private long previousTime;
    private long currentTime;
    private double accumulativeLag;
    private double elapsed;
    private final double UPS = 60.0;
    private final double TIME_PER_UPDATE = 1000.0/UPS;

    
    private int tileSize = 32;

    // Fields for hooman placement
    private boolean isPlacingHooman = false;
    private Class<? extends Hooman> selectedHoomanType = null; // Track selected hooman type
    private ArrayList<Hooman> hoomans = new ArrayList<Hooman>(); // Store placed towers
 
    Caveman test;
     

    //Main method
    public static void main(String[] args) {
        new Game();

    }

    //Constructor
    public Game() {
        // Creates a 1280x640 px window
        setSize(tileSize*40, tileSize*20);

        // Gets the bufferedImage of the image resource
        importImages();

        // Closes the program when the user closes the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Makes window resizable
        setResizable(true);
        // Window is placed at the centre of the screen
        setLocationRelativeTo(null);
        // Removes any layouts and forces every component to have bounds
        setLayout(null);


        // Creates the game screen GUI object
        gameScreen = new GameScreen(this, tileSet);

        // Initializing the shop
        shop = new ShopMenu(this, shopBackground);

        // Initializing new JButton to toggle the shop
        shopToggleButton = new JButton(toggleShopButtonIcon);
        // Setting the bounds of the button
        shopToggleButton.setBounds(0,(int)(0.75*getHeight()),(int)(0.075*getWidth()),(int)(0.05*getHeight()));

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

  
        // Add mouse listener for hooman placement
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isPlacingHooman && selectedHoomanType != null) {
                    placeHooman(e.getPoint());
                }
            }
        });

        
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

        test = new Caveman(new Point(100,100), true, true, gameScreen);
       
    }

    // Update method to be used for checking inputs and updating the changes in the actual game
    // Such as movement of aliens or projectile motion
    private void update(){
        test.attack();
    }



    // Imports the required resources
    private void importImages() {
        // Retrieves the filepath of the image and reads it with a stream of bytes
        // and then converts the stream into a bufferedImage
        try {
            InputStream inputStream = getClass().getResourceAsStream("/Resources/tileset.png");
            tileSet = ImageIO.read(inputStream);

            inputStream = getClass().getResourceAsStream("/Resources/shopBackground.png");
            shopBackground = ImageIO.read(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        } 


        // Scales the icon to match the size of the button
        Image scaledShopImage = new ImageIcon("Summative/app/src/main/java/Resources/shopButton.png").getImage().getScaledInstance((int)(0.075*getWidth()),(int)(0.05*getHeight()), Image.SCALE_SMOOTH);
        toggleShopButtonIcon = new ImageIcon(scaledShopImage); 


    }

    // Enables placement mode for the selected hooman
    public void startPlacementMode(Class<? extends Hooman> hoomanType) {
        isPlacingHooman = true;
        selectedHoomanType = hoomanType;
    }

    // Places a hooman at the specified position
    private void placeHooman(Point position) {
        try {

            Constructor <? extends Hooman> constructor = selectedHoomanType.getConstructor(Point.class, boolean.class, boolean.class, JPanel.class);
            Hooman newHooman = constructor.newInstance(new Point((int)position.getX()-20, (int)position.getY()-50), true, true, gameScreen);
            hoomans.add(newHooman);

            // Exit placement mode
            isPlacingHooman = false;
            selectedHoomanType = null;

            // Repaint the game screen to show the new hooman
            repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
