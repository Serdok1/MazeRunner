package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * The Key class represents a key that can be collected in the game.
 * Keys are typically used to unlock doors or other game elements.
 */
public class Key {

    /** The position of the key in the game world. */
    private Vector2 position;

    /** The width of the key. */
    private float width;

    /** The height of the key. */
    private float height;

    /** The bounding box of the key for collision detection. */
    private Rectangle boundingBox;

    /** A flag indicating whether the key has been collected or not. */
    private boolean collected;

    /**
     * Constructs a new Key with the specified position, width, and height.
     *
     * @param position The initial position of the key.
     * @param width    The width of the key.
     * @param height   The height of the key.
     */
    public Key(Vector2 position, float width, float height) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.boundingBox = new Rectangle(position.x, position.y, getWidth(), getHeight());
        this.collected = false;
    }

    /**
     * Gets the bounding box of the key for collision detection.
     *
     * @return The bounding box of the key.
     */
    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    /**
     * Gets the position of the key in the game world.
     *
     * @return The position vector of the key.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Checks if the key has been collected.
     *
     * @return True if the key has been collected, false otherwise.
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * Sets the collected status of the key.
     */
    public void setCollected() {
        this.collected = true;
    }

    /**
     * Gets the width of the key.
     *
     * @return The width of the key.
     */
    public float getWidth() {
        return this.width;
    }

    /**
     * Gets the height of the key.
     *
     * @return The height of the key.
     */
    public float getHeight() {
        return this.height;
    }
}