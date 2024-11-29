package de.tum.cit.ase.maze;

import com.badlogic.gdx.math.Rectangle;

/**
 * The ExitPoint class represents an exit point in the maze.
 * It inherits from the Wall class.
 */
public class ExitPoint extends Wall {
    private boolean isOpen;

    /**
     * Constructs an ExitPoint object with the given position, width, and height.
     *
     * @param x      The x-coordinate of the exit point.
     * @param y      The y-coordinate of the exit point.
     * @param width  The width of the exit point.
     * @param height The height of the exit point.
     */
    public ExitPoint(float x, float y, float width, float height) {
        super(x, y, width, height);
        isOpen = false;
    }

    /**
     * Checks if the exit point is open.
     *
     * @return True if the exit point is open, false otherwise.
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Sets the open status of the exit point.
     *
     * @param open The new open status of the exit point.
     */
    public void setOpen(boolean open) {
        isOpen = open;
    }
}
