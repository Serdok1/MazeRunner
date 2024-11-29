package de.tum.cit.ase.maze; // Use your package name

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * The MapSelectionScreen class represents the screen where players can select a game map.
 * It displays a list of available maps and allows the player to choose a map to play.
 */
public class MapSelectionScreen implements Screen {

    /** The main game instance. */
    private MazeRunnerGame game;

    /** The stage for UI elements. */
    private Stage stage;

    /** The list of available map files. */
    private List<FileHandle> mapList;

    /** The skin for UI elements. */
    private Skin skin;

    /** The selected map file. */
    private FileHandle selectedMapFile;

    /** The table for layout. */
    private Table table;

    /**
     * Constructs a new MapSelectionScreen with the specified game instance.
     *
     * @param game The main game instance.
     */
    public MapSelectionScreen(MazeRunnerGame game) {
        this.game = game;
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("path/to/your/skin.json")); // Update with your skin file

        // Populate your map list here
        mapList = new List<>(skin);
        // Add maps to mapList

        ScrollPane scrollPane = new ScrollPane(mapList, skin);
        stage.addActor(scrollPane);

        TextButton goToGameButton = new TextButton("Level 1", game.getSkin());
        table.add(goToGameButton).width(600).row();
        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToGame(selectedMapFile, true); // Change to the game screen when button is pressed
            }
        });
    }

    @Override
    public void show() {
        // Implement show logic
    }

    @Override
    public void render(float delta) {
        // Implement render logic
    }
    @Override
    public void resize(int width, int height) {
        // Adjust your layout here based on the new size
    }

    @Override
    public void pause() {
        // Implement pause logic if necessary
    }

    @Override
    public void resume() {
        // Implement resume logic if necessary
    }

    @Override
    public void hide() {
        // Implement hide logic if necessary
    }

    @Override
    public void dispose() {
        // Dispose of resources (like Stage, Skin, etc.) here
        if (stage != null) {
            stage.dispose();
        }
        if (skin != null) {
            skin.dispose();
        }
    }


    // Implement other required methods from Screen interface
}
