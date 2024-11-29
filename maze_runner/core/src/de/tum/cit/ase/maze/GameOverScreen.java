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

/**
 * The GameOverScreen class represents the screen displayed when the game is over (victory or defeat).
 */
public class GameOverScreen implements Screen {
    private final Stage stage;
    private static FileHandle selectedMapFile;
    private final MazeRunnerGame game;
    private NativeFileChooser fileChooser;
    private boolean isVictory;

    /**
     * Constructs a GameOverScreen with the given MazeRunnerGame instance and victory status.
     *
     * @param game      The MazeRunnerGame instance.
     * @param isVictory True if the game ended in victory, false if it ended in defeat.
     */
    public GameOverScreen(MazeRunnerGame game, boolean isVictory) {
        this.isVictory = isVictory;
        this.game = game;
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        // Add a label as a title
        table.add(new Label(isVictory ? "VICTORY (you won!)" : "Game over (you lost...)", game.getSkin(), "title"))
                .padBottom(80).row();

        // Create and add a button to go to the game screen
        TextButton goToMenu = new TextButton("Go To Menu", game.getSkin());
        table.add(goToMenu).width(600).row();
        goToMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToMenu(); // Change to the game screen when the button is pressed
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