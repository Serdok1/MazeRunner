package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private OrthographicCamera camera;
    private final BitmapFont font;

    private float sinusInput = 0f;
    private HUD hud;
    private SpriteBatch spriteBatch;

    private FitViewport playerViewport;
    private Viewport gamePort;
    private Character character;
    private Properties mapProperties;
    private static FileHandle selectedMapFile;
    private GameState savedGameState;
    private int score;
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();
    private static Vector2 characterPosition;
    public Trap trap;
    private Array<Wall> walls;
    private Array<ExitPoint> exitPoints;
    private Array<Enemy> enemyArray;
    private Array<Trap> traps;
    private Array<Coin> keys;
    private static int remainingLives;
    private static int collectedKeys;
    float elapsedTime;

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game, FileHandle selectedMapFile, boolean isNew){
        this.game = game;
        game.getBackgroundMusic().stop();
        game.getGameplayMusic().play();
        game.getGameplayMusic().setLooping(true);
        this.selectedMapFile = selectedMapFile;
        enemyArray = new Array<>();
        if(isNew)
        {
            try {
                findStartPoint();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            resetGameState();
        }
        else
        {
            loadGameState();
        }
        this.character = new Character(characterPosition);
        this.character.setHealth(remainingLives);
        // Map properties
        mapProperties = new Properties();
        camera = new OrthographicCamera(character.getPosition().x, character.getPosition().y);
        // Get the font from the game's skin
        font = game.getSkin().getFont("font");
        //For HUD
        gamePort = new FitViewport(MazeRunnerGame.V_WIDTH, MazeRunnerGame.V_HEIGHT,camera);
        spriteBatch = game.getSpriteBatch();
        walls = new Array<>();
        exitPoints = new Array<>();
        traps = new Array<>();
        keys = new Array<>();
    }

    /**
     * Initializes the game screen, particularly setting up the enemy array.
     *
     * @throws IOException If an I/O error occurs while reading the map file.
     */
    @Override
    public void show() {
        try {
            setEnemyArray();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        hud = new HUD(game.getSpriteBatch(), game, keys.size);
    }

    /**
     * Renders the game screen.
     *
     * @param delta The time in seconds since the last render.
     */
    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen
        elapsedTime += Gdx.graphics.getDeltaTime();
        remainingLives = this.character.getHealth();
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            saveGameState();
            game.goToPause();
        }
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().begin(); // Begin the sprite batch
        camera.position.set(character.getPosition().x, character.getPosition().y, 0);
        updateCamera(mapProperties);
        try {
            // Render the background
            renderBackground(mapProperties);
            renderMap();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        character.update(delta);
        for (Wall wall : walls) {

            if (wall.getBounds().overlaps(character.getBoundingBox())) {
                character.setPosition(character.getPreviousPosition());
            }
            for(Enemy enemy1 : enemyArray) {
                if (wall.getBounds().overlaps(enemy1.getBoundingBox())) {
                    enemy1.setPosition(enemy1.getPreviousPosition());
                }
            }
        }
        for (ExitPoint exitPoint : exitPoints) {

            exitPoint.setOpen(character.getCollectedKeys() == keys.size);

            if (exitPoint.getBounds().overlaps(character.getBoundingBox())) {
                if(!exitPoint.isOpen())
                    character.setPosition(character.getPreviousPosition());
                else
                    game.goToVictory();
            }
            for(Enemy enemy1 : enemyArray) {
                if (exitPoint.getBounds().overlaps(enemy1.getBoundingBox())) {
                    enemy1.setPosition(enemy1.getPreviousPosition());
                }
            }
        }
        hud.updateHealth(character.getHealth());
        // Draw the character
        Animation<TextureRegion> anim = character.getcurrentCharacterAnimation();
        if (anim != null) {
            game.getSpriteBatch().draw(
                    anim.getKeyFrame(elapsedTime, true),
                    character.getPosition().x,
                    character.getPosition().y,
                    16,32
            );
            for(Enemy enemy1 : enemyArray)
            {
                Animation<TextureRegion> enemyAnim = enemy1.getCurrentEnemyAnimation();
                game.getSpriteBatch().draw(
                        enemyAnim.getKeyFrame(elapsedTime, true),
                        enemy1.getPosition().x,
                        enemy1.getPosition().y,
                        16,16
                );
                enemy1.update(delta);
            }
            for(Trap trap : traps)
            {
                Animation<TextureRegion> trapAnim = trap.getCurrentTrapFrame();
                game.getSpriteBatch().draw(
                        trapAnim.getKeyFrame(elapsedTime, true),
                        trap.getPosition().x,
                        trap.getPosition().y,
                        16,16
                );
            }
            for(Coin key: keys)
            {
                Animation<TextureRegion> coinAnim = key.getCoinAnimation();
                if(!key.isActivated()) {
                    game.getSpriteBatch().draw(
                            coinAnim.getKeyFrame(elapsedTime, true),
                            key.getPosition().x,
                            key.getPosition().y,
                            16, 16
                    );
                }
            }
        }
        game.getSpriteBatch().end(); // Important to call this after drawing everything
        game.getSpriteBatch().setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();// Important to call this before drawing anything
        for(Trap trap : traps) {
            if (trap != null && trap.collidesWith(character)) {
                // Eğer tuzak etkin değilse
                if (!trap.isActivated()) {
                    // Burada karakter tuzakla temas ettiğinde yapılacak işlemleri ekleyebilirsiniz
                    trap.setActivated(true); // Tuzak artık etkin durumda
                    character.decreaseHealth(); // Örneğin, karakterin canını azaltabilirsiniz
                }
            } else {
                // Eğer karakter tuzakla temas etmiyorsa, tuzak etkin durumunu sıfırla
                trap.setActivated(false);
            }
        }
        for(Coin key : keys) {
            if (key != null && key.collidesWith(character)) {
                if (!key.isActivated()) {
                    game.getCoinSound().play();
                    key.setActivated(true);
                    character.increaseCollectedKeys();
                    System.out.println(character.getCollectedKeys());
                    hud.updateKeys(character.getCollectedKeys());
                }
            }
        }
        for(Enemy enemy : enemyArray) {
            if (enemy != null && enemy.collidesWith(character)) {
                if (!enemy.isActivated()) {
                    enemy.setActivated(true);
                    character.decreaseHealth();
                }
            } else {
                enemy.setActivated(false);
            }
        }
        if(character.getHealth() <= 0)
        {
            game.goToGameOver();
        }
    }

    private static class GameState {
        FileHandle selectedMap;
        Vector2 characterPosition;
        int score;
        int remainingLives;
        int collected;

        GameState(Vector2 characterPosition, int score, int remainingLives,int collected, FileHandle selectedMap) {
            this.selectedMap = selectedMap;
            this.characterPosition = characterPosition;
            this.score = score;
            this.remainingLives = remainingLives;
            this.collected = collected;
        }
    }
    /**
     * Saves the current game state.
     */
    public void saveGameState() {
        savedGameState = new GameState(character.getPosition(), score, remainingLives, collectedKeys, selectedMapFile);
    }
    /**
     * Loads the saved game state.
     */
    public void loadGameState() {
        if (savedGameState != null) {
            characterPosition = savedGameState.characterPosition;
            score = savedGameState.score;
            remainingLives = savedGameState.remainingLives;
            collectedKeys = savedGameState.collected;
        }
    }
    /**
     * Resets the game state to the initial state for a new game.
     */
    private void resetGameState() {
        savedGameState = new GameState(new Vector2(0, 0), 0, 3, collectedKeys, selectedMapFile);
        remainingLives = 3;
        collectedKeys = 0;
    }
    /**
     * Resizes the game viewport.
     *
     * @param width The new width of the viewport.
     * @param height The new height of the viewport.
     */
    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    /**
     * Updates the camera position based on the player's position and map boundaries.
     *
     * @param mapProperties The properties of the map containing coordinates and tile IDs.
     */
    private void updateCamera(Properties mapProperties) {

        int maxX = 0;
        int maxY = 0;

        for (Map.Entry<Object, Object> entry : mapProperties.entrySet()) {
            String[] coordinates = entry.getKey().toString().split(",");
            int x = Integer.parseInt(coordinates[0].trim());
            int y = Integer.parseInt(coordinates[1].trim());

            // Update max coordinates
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }
        Vector2 playerPosition = character.getPosition();
        float cameraX = camera.position.x;
        float cameraY = camera.position.y;
        float cameraSpeed = 5f;

        // Haritanın sınırlarını al
        float mapLeftBoundary = 0;  // Haritanın sol sınırı
        float mapRightBoundary = (maxX * 16) + 16;;  // Haritanın sağ sınırı
        float mapBottomBoundary = 0;  // Haritanın alt sınırı
        float mapTopBoundary = (maxY * 16) + 16;  // Haritanın üst sınırı

        float marginX = camera.viewportWidth * 0.4f;
        float marginY = camera.viewportHeight * 0.4f;

        // Kamera pozisyonunu güncelle
        if (playerPosition.x > camera.position.x + marginX) {
            cameraX += (playerPosition.x - cameraX - marginX) * cameraSpeed * Gdx.graphics.getDeltaTime();
        } else if (playerPosition.x < camera.position.x - marginX) {
            cameraX -= (cameraX - marginX - playerPosition.x) * cameraSpeed * Gdx.graphics.getDeltaTime();
        }

        // Kamerayı haritanın sınırları içinde tut
        cameraX = MathUtils.clamp(cameraX, mapLeftBoundary + camera.viewportWidth / 2, mapRightBoundary - camera.viewportWidth / 2);
        cameraY = MathUtils.clamp(cameraY, mapBottomBoundary + camera.viewportHeight / 2, mapTopBoundary - camera.viewportHeight / 2);

        // Kamerayı güncelle
//        camera.position.set(cameraX, cameraY, 0);
        camera.update();
    }

    /**
     * Renders the background of the game screen based on the provided map properties.
     *
     * @param mapProperties The properties of the map containing coordinates and tile IDs.
     */
    private void renderBackground(Properties mapProperties) {
        // Determine the dimensions of the maze
        int maxX = 0;
        int maxY = 0;

        for (Map.Entry<Object, Object> entry : mapProperties.entrySet()) {
            String[] coordinates = entry.getKey().toString().split(",");
            int x = Integer.parseInt(coordinates[0].trim());
            int y = Integer.parseInt(coordinates[1].trim());

            // Update max coordinates
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }

        // Adjust based on your image dimensions
        float frameWidth = 16;
        float frameHeight = 16;

        // Assuming getBackground() returns your background image
        TextureRegion backgroundTexture = game.getBackground();

        // Calculate the number of tiles needed to cover the entire maze
        int numTilesX = (int) Math.ceil(maxX * frameWidth);
        int numTilesY = (int) Math.ceil(maxY * frameHeight);

        // Draw the background
        for (float x = 0; x < numTilesX; x += frameWidth) {
            for (float y = 0; y < numTilesY; y += frameHeight) {
                    game.getSpriteBatch().draw(backgroundTexture, x, y);
            }
        }
    }

    /**
     * Renders the map elements based on the tile IDs from the loaded map file.
     *
     * @throws IOException If an I/O error occurs while reading the map file.
     */
    private void renderMap() throws IOException {
        // Load the map
        mapProperties = new Properties();
        mapProperties.load(selectedMapFile.reader());

        TextureRegion wallTexture = game.getWall(); // Assuming getWall returns your wall image
        TextureRegion entryPointTexture = game.getEntryPoint(); // Assuming getWall returns your wall image
        TextureRegion exitPointTexture = game.getExit();
        TextureRegion trapTexture = game.getTrap();

        for (Map.Entry<Object, Object> entry : mapProperties.entrySet()) {
            String[] coordinates = entry.getKey().toString().split(",");
            String tileId = entry.getValue().toString();
            int x = Integer.parseInt(coordinates[0].trim()) * 16; // Multiply by 16 for correct position
            int y = Integer.parseInt(coordinates[1].trim()) * 16; // Multiply by 16 for correct position
            if (tileId.equals("0")) {
                game.getSpriteBatch().draw(wallTexture, x, y);
            } else if (tileId.equals("1")) {
                game.getSpriteBatch().draw(entryPointTexture, x, y);
            } else if(tileId.equals("2")) {
                game.getSpriteBatch().draw(exitPointTexture, x, y);
            }
        }
        // Additional methods and logic can be added as needed for the game screen
    }

    /**
     * Finds the starting point of the character in the map and sets the initial position.
     *
     * @throws IOException If an I/O error occurs while reading the map file.
     */
    private void findStartPoint() throws IOException {
        // Load the map
        mapProperties = new Properties();
        mapProperties.load(selectedMapFile.reader());

        for (Map.Entry<Object, Object> entry : mapProperties.entrySet()) {
            String[] coordinates = entry.getKey().toString().split(",");
            String tileId = entry.getValue().toString();
            int x = Integer.parseInt(coordinates[0].trim()) * 16; // Multiply by 16 for correct position
            int y = Integer.parseInt(coordinates[1].trim()) * 16; // Multiply by 16 for correct position
            if (tileId.equals("1")) {
                this.characterPosition = new Vector2(x, y);
            }
        }

        // Additional methods and logic can be added as needed for the game screen
    }

    /**
     * Initializes the enemy array based on the tile IDs from the loaded map file.
     *
     * @throws IOException If an I/O error occurs while reading the map file.
     */
    private void setEnemyArray() throws IOException {
        // Load the map
        mapProperties = new Properties();
        mapProperties.load(selectedMapFile.reader());

        for (Map.Entry<Object, Object> entry : mapProperties.entrySet()) {
            String[] coordinates = entry.getKey().toString().split(",");
            String tileId = entry.getValue().toString();
            int x = Integer.parseInt(coordinates[0].trim()) * 16; // Multiply by 16 for correct position
            int y = Integer.parseInt(coordinates[1].trim()) * 16; // Multiply by 16 for correct position
             if (tileId.equals("4")) {
                enemyArray.add(new Enemy(new Vector2(x,y)));
            } else if (tileId.equals("0")) {
                 walls.add(new Wall(x, y,16 ,16));
             } else if (tileId.equals("3")){
                 traps.add(new Trap(new Vector2(x, y), 8, 8));
             } else if(tileId.equals("5")) {
                 keys.add(new Coin(new Vector2(x,y), 8,8));
             } else if(tileId.equals("2")) {
                 exitPoints.add(new ExitPoint(x, y, 16, 16));
             }
        }

        // Additional methods and logic can be added as needed for the game screen
    }
}