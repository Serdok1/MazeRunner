package de.tum.cit.ase.maze;

import com.badlogic.gdx.math.Rectangle;

/**
 * The Wall class represents a wall in the game. Walls are static objects that define
 * the boundaries of the game world and can be used to create the maze layout.
 */
public class Wall {
    private Rectangle bounds;

    /**
     * Constructs a new Wall with the specified position and dimensions.
     *
     * @param x      The x-coordinate of the top-left corner of the wall.
     * @param y      The y-coordinate of the top-left corner of the wall.
     * @param width  The width of the wall.
     * @param height The height of the wall.
     */
    public Wall(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
    }

    /**
     * Gets the bounds of the wall.
     *
     * @return The bounds of the wall as a Rectangle.
     */
    public Rectangle getBounds() {
        return bounds;
    }
}
