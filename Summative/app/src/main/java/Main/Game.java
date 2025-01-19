package Main;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import javax.swing.JFrame;
import GameObjects.Alien;
import GameObjects.Archer;
import GameObjects.Caveman;
import GameObjects.Farmer;
import GameObjects.GameObject;
import GameObjects.Hooman;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Game extends JFrame implements Runnable{
    
    private GameScreen gameScreen;
    private ShopMenu shop;
    private SidePanel sidePanel;

    private Thread gameThread;
    private Insets insets;


    private final static double UPS = 120.0;
    private final static double TIME_PER_UPDATE = 1000.0/UPS;

    private final static double FPS = 60.0;
    private final static double TIME_PER_FRAME = 1000.0/FPS;


    private int lives;
    private boolean roundIsFinished;
    private boolean gameOver;

    private int round;
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

    public Game() {
        this.round = 0;
        this.lives = 100;
        this.coins = 50;
        roundIsFinished = true;

        // Creates a 1280x640 px window
        setPreferredSize(new Dimension(tileSize * 40, tileSize * 20));
        
        setVisible(true);

        
        insets = getInsets();
        int width = tileSize * 40 + insets.left + insets.right;
        int height = tileSize * 20 + insets.top + insets.bottom;
        setSize(width, height); 
        System.out.println(width + " " + height);

        // Closes the program when the user closes the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        
        // Window is placed at the centre of the screen
        setLocationRelativeTo(null);
        // Removes any layouts and forces every component to have bounds
        setLayout(null);


        // Creates the game screen GUI object
        gameScreen = new GameScreen(this);
        gameScreen.setBounds(insets.left, 0, tileSize * 40, tileSize * 20);
        add(gameScreen);
        // Initializing the shop
        shop = new ShopMenu(this);
        shop.setBounds(insets.left, tileSize * 20 - 160, tileSize * 40, 160);
        add(shop);

        sidePanel = new SidePanel(this);
        sidePanel.setBounds(insets.left, tileSize * 20 - 400, 180, tileSize * 20);
        add(sidePanel);

        // Linking the button to perform a task on click
        sidePanel.getShopToggleButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                // Hides or shows the shop visibility
                shop.toggleVisibility();
            }
        });

        sidePanel.getMessageButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if(gameOver){
                    System.exit(0);
                }
                else if (roundIsFinished){
                    roundIsFinished = false;
                    round++;
                    loadRound();
                }
            }
        });

  
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
        getContentPane().setComponentZOrder(sidePanel,0);
        

        // Makes the window visible
        setVisible(true);

        // Makes window not resizable
        setResizable(false);

        // Making sure that everything is displayed properly
        revalidate();
        repaint();

        //Starts gameThread
        gameThread = new Thread(this);
        gameThread.start();

        //https://chatgpt.com/share/67786f4d-d214-800f-89bd-c28037dc9ea9

        

       
       
    }

    public int getRound(){
        return round;
    }

    public int getLives(){
        return lives;
    }

    public int getCoins(){
        return coins;
    }

    public GameScreen getGameScreen(){
        return gameScreen;
    }

    public ShopMenu getShopMenu(){
        return shop;
    }

    public SidePanel getSidePanel(){
        return sidePanel;
    }

    public boolean getRoundFinished(){
        return roundIsFinished;
    }

    public boolean getGameOver(){
        return gameOver;
    }

    public void setRoundFinished(boolean roundIsFinished) {
        this.roundIsFinished = roundIsFinished;
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

        if (gameOver||roundIsFinished||roundAliens==null){
            return;
        }
        
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

        
        if (activeAliens.isEmpty() && alienIndex>=roundAliens.length && Alien.getAliens().isEmpty()){
            roundIsFinished = true;
        }
        else{
            roundIsFinished = false;
        }

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
