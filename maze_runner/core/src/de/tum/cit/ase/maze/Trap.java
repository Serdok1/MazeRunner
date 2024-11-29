package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * The Trap class represents a trap in the game. Traps are objects that can be activated
 * and cause damage to the character when triggered.
 */
public class Trap {
    private Vector2 position;
    private Rectangle bounds;
    private boolean activated;
    private Animation<TextureRegion> trapAnimation;
    private float stateTime;

    /**
     * Constructs a new Trap with the specified position, width, and height.
     *
     * @param position The position of the trap in the game world.
     * @param width    The width of the trap.
     * @param height   The height of the trap.
     */
    public Trap(Vector2 position, float width, float height) {
        this.position = position;
        this.bounds = new Rectangle(position.x, position.y, width, height);
        this.activated = false;
        this.loadTrapAnimation();
    }

    /**
     * Loads the animation frames for the trap from the specified texture sheet.
     */
    private void loadTrapAnimation() {
        Texture trapSheet = new Texture(Gdx.files.internal("objects.png"));
        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 11;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        int cPos = 3 * frameHeight;

        for (int i = 4; i < animationFrames; i++) {
            frames.add(new TextureRegion(trapSheet, i * frameWidth, cPos, frameWidth, frameHeight));
        }

        trapAnimation = new Animation<TextureRegion>(0.1f, frames);
    }

    /**
     * Gets the current frame of the trap animation.
     *
     * @return The animation frame of the trap.
     */
    public Animation<TextureRegion> getCurrentTrapFrame() {
        return trapAnimation;
    }

    /**
     * Checks if the trap collides with the specified character.
     *
     * @param character The character to check for collision.
     * @return True if the trap collides with the character, false otherwise.
     */
    public boolean collidesWith(Character character) {
        return bounds.overlaps(character.getBoundingBox());
    }

    /**
     * Activates the trap and decreases the health of the specified character if not already activated
     * and if there is a collision with the character.
     *
     * @param character The character to activate the trap on.
     */
    public void activate(Character character) {
        if (!activated && collidesWith(character)) {
            character.decreaseHealth();
            activated = true;
        }
    }

    /**
     * Checks if the trap is activated.
     *
     * @return True if the trap is activated, false otherwise.
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * Sets the activation state of the trap.
     *
     * @param activated The new activation state of the trap.
     */
    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    /**
     * Gets the bounds of the trap.
     *
     * @return The bounds of the trap as a Rectangle.
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Gets the position of the trap.
     *
     * @return The position of the trap as a Vector2.
     */
    public Vector2 getPosition() {
        return position;
    }
}
