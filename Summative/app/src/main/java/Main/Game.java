package Main;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Constructor;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import GameObjects.Alien;
import GameObjects.Archer;
import GameObjects.Caveman;
import GameObjects.Farmer;
import GameObjects.GameObject;
import GameObjects.Hooman;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;


public class Game extends JFrame implements Runnable{
    
    private GameScreen gameScreen;
    private ShopMenu shop;
    private JButton shopToggleButton;
    private JLabel livesLabel;
    private JLabel roundLabel;
    private JLabel coinsLabel;

    private BufferedImage tileSet;
    private Icon toggleShopButtonIcon;
    private Icon livesLabelIcon;
    private Icon roundLabelIcon;
    private Icon coinsLabelIcon;
    private Thread gameThread;
    private BufferedImage shopBackground;

    private final static double UPS = 120.0;
    private final static double TIME_PER_UPDATE = 1000.0/UPS;

    private final static double FPS = 60.0;
    private final static double TIME_PER_FRAME = 1000.0/FPS;


    private int lives;
    private boolean gameOver;

    private int round;
    private Hooman[] savedHoomans;
    private int coins;
    private int tileSize = 32;


    private long lastAlienTime, currentAlienTime;
    private int alienIndex;
     
    private Alien[] roundAliens;

    // Fields for hooman placement
    private boolean isPlacingHooman = false;
    private Class<? extends Hooman> selectedHoomanType = null; // Track selected hooman type
    private ArrayList<Hooman> pendingHoomans = new ArrayList<Hooman>(); // Store towers that need to be placed
 
    Caveman test;
    Farmer test1;
    Archer test2;
    Alien test3;
    Alien test4;
   
     

    //Main method
    public static void main(String[] args) {
        new Game();

    }

    //Constructor
    public Game(){
        this(0,null, 100, 50);
    }

    public Game(int round, Hooman[] savedHoomans, int lives, int coins) {
        this.round = round;
        this.savedHoomans = savedHoomans;
        this.lives = lives;
        this.coins = coins;

        gameOver = false;

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

        roundLabel = new JLabel(""+round, roundLabelIcon, SwingConstants.LEFT );
        roundLabel.setBounds(0, 0, 128, 64);
        roundLabel.setOpaque(false);
        roundLabel.setBackground(new Color(0,0,0,127));
        add(roundLabel);

        livesLabel = new JLabel(""+lives, livesLabelIcon, SwingConstants.LEFT );
        livesLabel.setBounds(0, 64, 128, 64);
        add(livesLabel);

        coinsLabel = new JLabel(""+coins, coinsLabelIcon, SwingConstants.LEFT );
        coinsLabel.setBounds(0, 128, 128, 64);
        add(coinsLabel);
    


        //Sets the order of each component on the content pane

        getContentPane().setComponentZOrder(gameScreen, 5);//Chatgpt
        getContentPane().setComponentZOrder(shop, 4);
        getContentPane().setComponentZOrder(roundLabel, 3);
        getContentPane().setComponentZOrder(livesLabel, 2);
        getContentPane().setComponentZOrder(coinsLabel, 1);
        getContentPane().setComponentZOrder(shopToggleButton, 0);
        

        // Makes the window visible
        setVisible(true);

        // Making sure that everything is displayed properly
        revalidate();
        repaint();

        loadRound();

        //Starts gameThread
        gameThread = new Thread(this);
        gameThread.start();

        //https://chatgpt.com/share/67786f4d-d214-800f-89bd-c28037dc9ea9

        

       
       
    }

    public void loseLives(int livesToLose){
        lives-=livesToLose;
        
        if (lives<=0){
            gameOver = true;
        }
    }

    private void loadRound() {
        alienIndex = 0;
        lastAlienTime = System.currentTimeMillis();
        roundAliens = Alien.getRoundAliens(round);
    }

    // Update method to be used for checking inputs and updating the changes in the actual game
    // Such as movement of aliens or projectile motion
    private void update(){

        
        long currentTime = System.currentTimeMillis();
        for (Hooman hooman: Hooman.getHoomans()){
            if(currentTime-hooman.getLastAttackTime()>=hooman.getReloadSpeed()){
                hooman.findTargetAliens();
                hooman.attack();
            }
            
        
        }


        if (!pendingHoomans.isEmpty()){
            Hooman.addHoomans(pendingHoomans);
            pendingHoomans.clear();
        }


        if (alienIndex<roundAliens.length){
            currentAlienTime = System.currentTimeMillis();
            if (currentAlienTime - lastAlienTime>= roundAliens[alienIndex].getDelay()){
                roundAliens[alienIndex].initAlien();
                alienIndex++;
                lastAlienTime = System.currentTimeMillis();
            }
        }


        ArrayList<Alien> activeAliens = new ArrayList<Alien>();
        for (Alien alien: Alien.getAliens()){
            alien.move();
            if (alien.getReachedGoal()){
                loseLives(alien.getLevelIndex()+1);
            }
            if (!alien.getBeingRemoved()){
                activeAliens.add(alien);
            }
        }
        Alien.setAliens(activeAliens);

        
        // if (roundIsFinished){
        //     roundIsFinished = false;
        //     round++;
        //     loadRound();
        // }

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
        toggleShopButtonIcon = new ImageIcon(new ImageIcon("Summative/app/src/main/java/Resources/shopButton.png").getImage().getScaledInstance((int)(0.075*getWidth()),(int)(0.05*getHeight()), Image.SCALE_SMOOTH)); 
        livesLabelIcon = new ImageIcon(new ImageIcon("Summative/app/src/main/java/Resources/livesLabel.png").getImage().getScaledInstance((int)(0.075*getWidth()),(int)(0.05*getHeight()), Image.SCALE_SMOOTH));
        roundLabelIcon = new ImageIcon(new ImageIcon("Summative/app/src/main/java/Resources/roundLabel.png").getImage().getScaledInstance((int)(0.075*getWidth()),(int)(0.05*getHeight()), Image.SCALE_SMOOTH));
        coinsLabelIcon = new ImageIcon(new ImageIcon("Summative/app/src/main/java/Resources/coinsLabel.png").getImage().getScaledInstance((int)(0.075*getWidth()),(int)(0.05*getHeight()), Image.SCALE_SMOOTH));


    }

    // Enables placement mode for the selected hooman
    public void startPlacementMode(Class<? extends Hooman> hoomanType) {
        isPlacingHooman = true;
        selectedHoomanType = hoomanType;
    }

    // Places a hooman at the specified position
    private void placeHooman(Point position) {
        try {

            Constructor <? extends Hooman> constructor = selectedHoomanType.getConstructor(Point.class, boolean.class, boolean.class);
            Hooman newHooman = constructor.newInstance(new Point((int)(position.getX()-GameObject.getSize()/2.0), (int)position.getY()-GameObject.getSize()), true, true);
            pendingHoomans.add(newHooman);

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

        long lastRepaintTime = System.currentTimeMillis(), lastUpdateTime = System.currentTimeMillis();
        long currentRepaintTime, currentUpdateTime;
        long updateTimeElapsed, repaintTimeElapsed;
        long accumulativeUpdateLag = 0;
        
        lastRepaintTime = System.currentTimeMillis();


        //Game loop that updates and renders the game whilst keeping lag to a minimum
        while (true) {

            // Finds the time elapsed and adds it to the accumulativeLag
            currentUpdateTime = System.currentTimeMillis();
            updateTimeElapsed = currentUpdateTime-lastUpdateTime;
            accumulativeUpdateLag+= updateTimeElapsed;
            lastUpdateTime = System.currentTimeMillis();



            // Since the game should run at UPS, if the total time elapsed is greater
            // than the TIME_PER_UPDATE, then update and adjust the acumulated time and lag

            while (accumulativeUpdateLag>= TIME_PER_UPDATE) {
                // Updates the game
                update();
                //Adjusts acumulativeLag
                accumulativeUpdateLag-=TIME_PER_UPDATE;
            }

            currentRepaintTime = System.currentTimeMillis();
            repaintTimeElapsed = currentRepaintTime - lastRepaintTime;
            
            
            // Repaint at 60 FPS
            if (repaintTimeElapsed >= TIME_PER_FRAME) {
                repaint();
                lastRepaintTime = System.currentTimeMillis();
            }

            //Trys to sleep as much as possible to reduce CPU utilisation
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }



}
