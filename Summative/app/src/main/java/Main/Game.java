package Main;

import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import javax.swing.JFrame;
import GameObjects.Alien;
import GameObjects.GameObject;
import GameObjects.Hooman;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Represents the main game class.
 */
public class Game extends JFrame implements Runnable {
    
    private GameScreen gameScreen;
    private ShopMenu shop;
    private SidePanel sidePanel;

    private Thread gameThread;
    private Insets insets;

    private static final double UPS = 60.0;
    private static final double TIME_PER_UPDATE = 1000.0 / UPS;

    private static final double FPS = 60.0;
    private static final double TIME_PER_FRAME = 1000.0 / FPS;

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
    private ArrayList<Hooman> pendingHoomans = new ArrayList<>(); // Store towers that need to be placed

    /**
     * Main method to start the game.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        new Game();
    }

    /**
     * Constructor for the Game class.
     */
    public Game() {
        this.round = 0;
        this.lives = 100;
        this.coins = 75;
        roundIsFinished = true;

        setTitle("Hoomans vs Aliens");

        // Creates a 1280x640 px window
        setPreferredSize(new Dimension(tileSize * 40, tileSize * 20));
        
        setVisible(true);

        insets = getInsets();
        int width = tileSize * 40 + insets.left + insets.right;
        int height = tileSize * 20 + insets.top + insets.bottom;
        setSize(width, height); 

        // Closes the program when the user closes the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Window is placed at the centre of the screen
        setLocationRelativeTo(null);
        // Removes any layouts and forces every component to have bounds
        setLayout(null);

        // Creates the game screen GUI object
        gameScreen = new GameScreen();
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
            public void actionPerformed(ActionEvent e) {
                // Hides or shows the shop visibility
                shop.toggleVisibility();
            }
        });

        sidePanel.getMessageButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameOver) {
                    System.exit(0);
                } else if (roundIsFinished) {
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

        // Sets the order of each component on the content pane
        getContentPane().setComponentZOrder(gameScreen, 2);
        getContentPane().setComponentZOrder(shop, 1);
        getContentPane().setComponentZOrder(sidePanel, 0);

        // Makes the window visible
        setVisible(true);

        // Makes window not resizable
        setResizable(false);

        // Making sure that everything is displayed properly
        revalidate();
        repaint();

        // Starts gameThread
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Gets the current round.
     * 
     * @return The current round.
     */
    public int getRound() {
        return round;
    }

    /**
     * Gets the remaining lives.
     * 
     * @return The remaining lives.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Gets the current coins.
     * 
     * @return The current coins.
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Gets the game screen.
     * 
     * @return The game screen.
     */
    public GameScreen getGameScreen() {
        return gameScreen;
    }

    /**
     * Gets the shop menu.
     * 
     * @return The shop menu.
     */
    public ShopMenu getShopMenu() {
        return shop;
    }

    /**
     * Gets the side panel.
     * 
     * @return The side panel.
     */
    public SidePanel getSidePanel() {
        return sidePanel;
    }

    /**
     * Checks if the round is finished.
     * 
     * @return True if the round is finished, false otherwise.
     */
    public boolean getRoundFinished() {
        return roundIsFinished;
    }

    /**
     * Checks if the game is over.
     * 
     * @return True if the game is over, false otherwise.
     */
    public boolean getGameOver() {
        return gameOver;
    }

    /**
     * Sets the round finished status.
     * 
     * @param roundIsFinished The new round finished status.
     */
    public void setRoundFinished(boolean roundIsFinished) {
        this.roundIsFinished = roundIsFinished;
    }

    /**
     * Reduces the lives by the specified amount.
     * 
     * @param lives The amount of lives to lose.
     */
    public void loseLives(int lives) {
        this.lives -= lives;
        
        if (this.lives <= 0) {
            gameOver = true;
        }
    }

    /**
     * Adds the specified amount of coins.
     * 
     * @param coins The amount of coins to add.
     */
    public void addCoins(int coins) {
        this.coins += coins;
    }

    /**
     * Loads the next round of aliens.
     */
    private void loadRound() {
        alienIndex = 0;
        lastAlienTime = System.currentTimeMillis();
        roundAliens = Alien.getRoundAliens(round);
        if (roundAliens == null) {
            gameOver = true;
        }
    }

    /**
     * Updates the game state.
     */
    private void update() {

        if (!pendingHoomans.isEmpty()) {
            Hooman.addHoomans(pendingHoomans);
            pendingHoomans.clear();
        }

        ArrayList<Alien> activeAliens = new ArrayList<>();
        for (Alien alien : Alien.getAliens()) {
            alien.move();
            if (alien.getReachedGoal()) {
                loseLives(alien.getLevelIndex() + 1);
            }
            if (alien.getIsKilled()) {
                addCoins((int) Math.pow(alien.getOriginalLevelIndex() + 1, 1.25));
            }
            if (!alien.getBeingRemoved()) {
                activeAliens.add(alien);
            }
        }
        Alien.setAliens(activeAliens);


        
        if (gameOver || roundIsFinished || roundAliens == null) {
            return;
        }
        
        long currentTime = System.currentTimeMillis();
        for (Hooman hooman : Hooman.getHoomans()) {
            if (currentTime - hooman.getLastAttackTime() >= hooman.getReloadSpeed()) {
                hooman.findTargetAliens();
                hooman.attack();
            }
        }

        if (alienIndex < roundAliens.length) {
            currentAlienTime = System.currentTimeMillis();
            if (currentAlienTime - lastAlienTime >= roundAliens[alienIndex].getDelay()) {
                roundAliens[alienIndex].initAlien();
                alienIndex++;
                lastAlienTime = System.currentTimeMillis();
            }
        }

        if (activeAliens.isEmpty() && alienIndex >= roundAliens.length && Alien.getAliens().isEmpty()) {
            roundIsFinished = true;
        } else {
            roundIsFinished = false;
        }

    }

    /**
     * Enables placement mode for the selected hooman.
     * 
     * @param hoomanType The class of the hooman to place.
     */
    public void startPlacementMode(Class<? extends Hooman> hoomanType) {
        isPlacingHooman = true;
        selectedHoomanType = hoomanType;
    }

    /**
     * Places a hooman at the specified position.
     * 
     * @param position The position to place the hooman.
     */
    private void placeHooman(Point position) {
        try {
            Constructor<? extends Hooman> constructor = selectedHoomanType.getConstructor(Point.class, boolean.class, boolean.class);
            Hooman newHooman = constructor.newInstance(new Point((int) (position.getX() - GameObject.getSize() / 2.0), (int) position.getY() - GameObject.getSize()), true, true);

            if (coins < newHooman.getCost()) {
                newHooman = null;
                return;
            }
            addCoins(-newHooman.getCost());
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

    /**
     * Runnable method for the game loop.
     */
    @Override
    public void run() {

        long lastRepaintTime = System.currentTimeMillis(), lastUpdateTime = System.currentTimeMillis();
        long currentRepaintTime, currentUpdateTime;
        long updateTimeElapsed, repaintTimeElapsed;
        long accumulativeUpdateLag = 0;
        
        lastRepaintTime = System.currentTimeMillis();

        // Game loop that updates and renders the game whilst keeping lag to a minimum
        while (true) {

            // Finds the time elapsed and adds it to the accumulativeLag
            currentUpdateTime = System.currentTimeMillis();
            updateTimeElapsed = currentUpdateTime - lastUpdateTime;
            accumulativeUpdateLag += updateTimeElapsed;
            lastUpdateTime = System.currentTimeMillis();

            // Since the game should run at UPS, if the total time elapsed is greater
            // than the TIME_PER_UPDATE, then update and adjust the accumulated time and lag
            while (accumulativeUpdateLag >= TIME_PER_UPDATE) {
                // Updates the game
                update();
                // Adjusts accumulativeLag
                accumulativeUpdateLag -= TIME_PER_UPDATE;
            }

            currentRepaintTime = System.currentTimeMillis();
            repaintTimeElapsed = currentRepaintTime - lastRepaintTime;
            
            // Repaint at 60 FPS
            if (repaintTimeElapsed >= TIME_PER_FRAME) {
                repaint();
                lastRepaintTime = System.currentTimeMillis();
            }

            // Try to sleep as much as possible to reduce CPU utilization
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}