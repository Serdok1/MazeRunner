package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * The Coin class represents a coin in the game.
 */
public class Coin {
    private Vector2 position;
    private Rectangle bounds;
    private boolean activated;
    private Animation<TextureRegion> coinAnimation;

    /**
     * Constructs a Coin object with the given position, width, and height.
     *
     * @param position The position of the coin.
     * @param width    The width of the coin.
     * @param height   The height of the coin.
     */
    public Coin(Vector2 position, float width, float height) {
        this.position = position;
        this.bounds = new Rectangle(position.x, position.y, width, height);
        this.activated = false;
        this.loadCoinAnimation();
    }

    /**
     * Loads the animation frames for the coin.
     */
    private void loadCoinAnimation() {
        Texture trapSheet = new Texture(Gdx.files.internal("objects.png"));
        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 4;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        int cPos = 4 * frameHeight;

        for (int i = 0; i < animationFrames; i++) {
            frames.add(new TextureRegion(trapSheet, i * frameWidth, cPos, frameWidth, frameHeight));
        }

        coinAnimation = new Animation<TextureRegion>(0.1f, frames);
    }

    /**
     * Gets the animation of the coin.
     *
     * @return The animation of the coin.
     */
    public Animation<TextureRegion> getCoinAnimation() {
        return coinAnimation;
    }

    /**
     * Checks if the coin collides with the character.
     *
     * @param character The character to check for collision.
     * @return True if the coin collides with the character, false otherwise.
     */
    public boolean collidesWith(Character character) {
        return bounds.overlaps(character.getBoundingBox());
    }

    /**
     * Checks if the coin is activated.
     *
     * @return True if the coin is activated, false otherwise.
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * Sets the activation status of the coin.
     *
     * @param activated The new activation status of the coin.
     */
    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    /**
     * Gets the bounding box of the coin.
     *
     * @return The bounding box of the coin.
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Gets the position of the coin.
     *
     * @return The position of the coin.
     */
    public Vector2 getPosition() {
        return position;
    }
}
