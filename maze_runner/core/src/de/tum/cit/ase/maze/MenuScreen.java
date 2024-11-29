package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;
import games.spooky.gdx.nativefilechooser.NativeFileChooserIntent;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {

    private final Stage stage;
    private static FileHandle selectedMapFile;
    private final MazeRunnerGame game;
    private NativeFileChooser fileChooser;
    private boolean isPaused;


    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game      The main game class, used to access global resources and methods.
     * @param isPaused  A flag indicating whether the game is currently paused.
     */
    public MenuScreen(MazeRunnerGame game, boolean isPaused) {
        this.isPaused = isPaused;
        this.game = game;
        this.fileChooser = game.getFileChooser();
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage
        game.getGameplayMusic().stop();
        game.getBackgroundMusic().play();
        game.getBackgroundMusic().setLooping(true);

        // Add a label as a title
        table.add(new Label("Hello World from the Menu!", game.getSkin(), "title")).padBottom(80).row();

        // Create and add a button to go to the game screen
        TextButton goToGameButton = new TextButton(isPaused ? "Resume" : "Go To Game", game.getSkin());
        table.add(goToGameButton).width(600).row();
        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(selectedMapFile != null)
                    game.goToGame(selectedMapFile, isPaused ? false : true); // Change to the game screen when button is pressed
                else if(selectedMapFile == null)
                {
                    openFileChooser();
                    game.goToGame(selectedMapFile, true); // Change to the game screen when button is pressed
                }
            }
        });

        // Button to exit the game
        TextButton exitButton = new TextButton("Exit Game", game.getSkin());
        table.add(exitButton).width(600).padTop(10).row();
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit(); // Closes the application
            }
        });

        // Add a button to open the file chooser
        TextButton openFileChooserButton = new TextButton("Open File Chooser", game.getSkin());
        table.add(openFileChooserButton).width(600).padTop(10).row();
        openFileChooserButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                openFileChooser(); // Correctly open the file chooser
                game.goToMenu();
            }
        });
    }

    /**
     * Opens the file chooser dialog for selecting maze files.
     * If a file is chosen, the game is navigated to the menu screen.
     */
    private void openFileChooser() {
        if (fileChooser == null) {
            Gdx.app.error("MenuScreen", "File chooser is null");
            return;
        }

        Gdx.app.log("MenuScreen", "Attempting to open file chooser");
        NativeFileChooserConfiguration fileChooserConfig = new NativeFileChooserConfiguration();
        fileChooserConfig.title = "Pick a maze file";
        fileChooserConfig.intent = NativeFileChooserIntent.OPEN;
        fileChooserConfig.nameFilter = (file, name) -> name.endsWith("properties");
        fileChooserConfig.directory = Gdx.files.local("itp2324itp2324projectwork-fri2mu1saritasplantor/maps/");

        fileChooser.chooseFile(fileChooserConfig, new NativeFileChooserCallback() {
            @Override
            public void onFileChosen(FileHandle fileHandle) {
                Gdx.app.log("MenuScreen", "File chosen: " + fileHandle.path());
                isPaused = false;
                selectedMapFile = fileHandle;
                // Handle file selection
            }

            @Override
            public void onCancellation() {
                game.goToMenu();
                Gdx.app.log("MenuScreen", "File chooser cancelled");
            }

            @Override
            public void onError(Exception exception) {
                Gdx.app.error("MenuScreen", "Error picking maze file: " + exception.getMessage(), exception);
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    @Override
    public void dispose() {
        // Dispose of the stage when the screen is disposed
        stage.dispose();
    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

    // The following methods are part of the Screen interface but are not used in this screen.
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
