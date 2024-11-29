package de.tum.cit.ase.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {

    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;

    //Background image
    private TextureRegion background;

    // Map attributes
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    //Objects
    private TextureRegion wall;
    private TextureRegion exit;
    private TextureRegion entryPoint;
    private TextureRegion key;
    private TextureRegion trap;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;

    // UI Skin
    private Skin skin;

    // Player characteristics
    private float speed;

    private Sound coinSound;
    private Music backgroundMusic;
    private Music gameplayMusic;

    /**
     * The HUD (Head-Up Display) class represents the user interface overlay in the Maze Runner game.
     * It includes elements such as remaining lives and keys collected.
     * The HUD is displayed on the screen using the LibGDX framework.
     * For more details on HUD implementation, refer to the tutorial at: https://www.youtube.com/watch?v=7idwNW5a8Qs
     */
    public static final int V_WIDTH = 500;
    public static final int V_HEIGHT = 260;
    private NativeFileChooser fileChooser;
    private BitmapFont font;

    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
        this.fileChooser = fileChooser;
    }

    public NativeFileChooser getFileChooser() {
        return fileChooser;
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     * This method sets up the necessary components for the game by performing the following steps:
     * 1. Load the background - Calls loadBackground() to set up the background of the game scene.
     * 2. Load map textures - Invokes loadWallTexture(), loadEntryPointTexture(), and other methods
     *    to load textures for various elements in the game map.
     * 3. Initialize character position - Sets the initial position of the character in the game world.
     */
    @Override
    public void create() {
        font = new BitmapFont();
        spriteBatch = new SpriteBatch(); // Create SpriteBatch
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin

        // Load the background
        this.loadBackground();

        // Load the map textures
        this.loadWallTexture();
        this.loadEntryPointTexture();
        this.loadExitPointTexture();
        this.loadKeyTexture();
        this.loadTrapTexture();

        // Background sound
        gameplayMusic = Gdx.audio.newMusic(Gdx.files.internal("Caketown 1.mp3"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("awesomeness.wav"));
        coinSound = Gdx.audio.newSound(Gdx.files.internal("coin10.wav"));

        goToMenu(); // Navigate to the menu screen
    }

    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        this.setScreen(new MenuScreen(this, false)); // Set the current screen to MenuScreen;
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    public Sound getCoinSound()
    {
        return this.coinSound;
    }
    public Music getBackgroundMusic()
    {
        return this.backgroundMusic;
    }
    public Music getGameplayMusic()
    {
        return this.gameplayMusic;
    }

    /**
     * Switches to the victory screen.
     */
    public void goToVictory() {
        this.setScreen(new GameOverScreen(this, true)); // Set the current screen to MenuScreen;
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the game over screen.
     */
    public void goToGameOver() {
        this.setScreen(new GameOverScreen(this, false)); // Set the current screen to MenuScreen;
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the pause screen.
     */
    public void goToPause() {
        this.setScreen(new MenuScreen(this, true)); // Set the current screen to MenuScreen;
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame(FileHandle selectedMapFile, boolean isNew) {
        this.setScreen(new GameScreen(this, selectedMapFile, isNew)); // Set the current screen to GameScreen
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
        }
        gameScreen = new GameScreen(this, selectedMapFile, isNew);
        this.setScreen(gameScreen); // Set the current screen to GameScreen
    }

    /**
     * Switches to the map selection screen.
     */
    public void loadMap() {
        this.setScreen(new MapSelectionScreen(this)); // Switch to MapSelectionScreen
        // Dispose other screens if necessary
    }

    /**
     * Loads the background image.
     */
    private void loadBackground(){
        // Load the background texture
        Texture backgroundTexture = new Texture(Gdx.files.internal("basictiles.png"));

        // Define the size of each tile
        int tileWidth = 16;
        int tileHeight = 16;

        // Extract the specific tile (column 2, row 1)
        int x = tileWidth * 1; // Column 2
        int y = tileHeight * 8; // Row 2

        background = new TextureRegion(backgroundTexture, x, y, tileWidth, tileHeight);
    }

    /**
     * Loads the wall texture.
     */
    public void loadWallTexture() {
        // Load the wall texture
        Texture wallTexture = new Texture(Gdx.files.internal("basictiles.png"));

        // Define the size of each tile
        int tileWidth = 16;
        int tileHeight = 16;

        // Extract the specific tile (column 2, row 1)
        int x = tileWidth * 0; // Column 2
        int y = tileHeight * 0; // Row 2

        wall = new TextureRegion(wallTexture, x, y, tileWidth, tileHeight);
    }

    /**
     * Loads the entry point texture.
     */
    public void loadEntryPointTexture() {
        // Load the entry point texture
        Texture entryPointTexture = new Texture(Gdx.files.internal("things.png"));

        // Define the size of each tile
        int tileWidth = 16;
        int tileHeight = 16;

        // Extract the specific tile (column 2, row 1)
        int x = tileWidth * 3; // Column 2
        int y = tileHeight * 3; // Row 2

        entryPoint = new TextureRegion(entryPointTexture, x, y, tileWidth, tileHeight);
    }

    /**
     * Loads the exit point texture.
     */
    public void loadExitPointTexture() {
        Texture exitTexture = new Texture(Gdx.files.internal("basictiles.png"));

        // Define the size of each tile
        int tileWidth = 16;
        int tileHeight = 16;

        // Extract the specific tile (column 2, row 1)
        int x = tileWidth * 0; // Column 2
        int y = tileHeight * 6; // Row 2

        exit = new TextureRegion(exitTexture, x, y, tileWidth, tileHeight);
    }

    /**
     * Loads the key texture.
     */
    public void loadKeyTexture() {
        Texture keyTexture = new Texture(Gdx.files.internal("objects.png"));

        // Define the size of each tile
        int tileWidth = 16;
        int tileHeight = 16;

        // Extract the specific tile (column 2, row 1)
        int x = tileWidth * 1; // Column 2
        int y = tileHeight * 4; // Row 2

        key = new TextureRegion(keyTexture, x, y, tileWidth, tileHeight);
    }

    /**
     * Loads the trap texture.
     */
    public void loadTrapTexture() {
        Texture trapTexture = new Texture(Gdx.files.internal("objects.png"));

        // Define the size of each tile
        int tileWidth = 16;
        int tileHeight = 16;

        // Extract the specific tile (column 2, row 1)
        int x = tileWidth * 5; // Column 2
        int y = tileHeight * 3; // Row 2

        trap = new TextureRegion(trapTexture, x, y, tileWidth, tileHeight);
    }

    /**
     * Gets the speed of the player.
     * If the ENTER key is pressed, the speed is set to 2; otherwise, it is set to 1.
     *
     * @return The speed of the player.
     */
    public float getSpeed() {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            speed = 2;
        } else {
            speed = 1;
        }

        return speed;
    }

    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }

    // Getter methods
    public Skin getSkin() {
        return skin;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public TextureRegion getBackground() {
        return background;
    }

    public TextureRegion getWall() {
        return wall;
    }

    public TextureRegion getExit() {
        return exit;
    }

    public TextureRegion getEntryPoint() {
        return entryPoint;
    }

    public TextureRegion getKey() {
        return key;
    }

    public TextureRegion getTrap() {
        return trap;
    }

    public void setBackground(TextureRegion background) {
        this.background = background;
    }

    public void setWall(TextureRegion wall) {
        this.wall = wall;
    }

    public void setExit(TextureRegion exit) {
        this.exit = exit;
    }

    public void setEntryPoint(TextureRegion entryPoint) {
        this.entryPoint = entryPoint;
    }

    public void setKey(TextureRegion key) {
        this.key = key;
    }

    public void setTrap(TextureRegion trap) {
        this.trap = trap;
    }

    public BitmapFont getFont() {
        return this.font;
    }
}
