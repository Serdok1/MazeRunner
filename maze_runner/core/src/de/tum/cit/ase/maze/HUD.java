package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * The HUD (Heads-Up Display) class handles the display of essential game information
 * such as remaining lives and keys collected.
 */
public class HUD {

    /** The stage for UI elements. */
    public Stage stage;

    /** The viewport for the HUD. */
    private FitViewport viewport;

    /** Label displaying the remaining lives. */
    private Label livesLabel;

    /** Label displaying the keys collected. */
    private Label keysLabel;

    /** The size of keys to be collected. */
    private int keySize;

    /**
     * Constructs a new HUD with the specified SpriteBatch, game instance, and key size.
     *
     * @param sb      The SpriteBatch used for rendering.
     * @param game    The main game class, used to access global resources and methods.
     * @param keySize The size of keys to be collected.
     */
    public HUD(SpriteBatch sb, MazeRunnerGame game, int keySize) {
        this.keySize = keySize;
        viewport = new FitViewport(MazeRunnerGame.V_WIDTH, MazeRunnerGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        livesLabel = new Label("Remaining lives: 3", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        keysLabel = new Label("Keys collected 0/" + keySize, new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.row();
        table.add(livesLabel).expandX().padTop(10);
        table.add(keysLabel).expandX().padTop(10);

        stage.addActor(table);
    }

    /**
     * Updates the displayed remaining lives.
     *
     * @param health The remaining lives of the player.
     */
    public void updateHealth(int health) {
        livesLabel.setText("Remaining lives: " + health);
    }

    /**
     * Updates the displayed keys collected information.
     *
     * @param keys The number of keys collected.
     */
    public void updateKeys(int keys) {
        keysLabel.setText("Keys collected " + keys + "/" + keySize);
    }

    /**
     * Draws the HUD on the screen.
     */
    public void draw() {
        stage.draw();
    }

    /**
     * Disposes of the HUD, freeing up resources.
     */
    public void dispose() {
        stage.dispose();
    }
}