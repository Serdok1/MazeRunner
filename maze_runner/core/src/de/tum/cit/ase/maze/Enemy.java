package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import java.util.Random;

/**
 * The Enemy class represents an enemy character in the game.
 */
public class Enemy {

    private Vector2 position;
    private float speed = 50.0f; // Enemy speed in units per second
    private Animation<TextureRegion> currentEnemyAnimation;
    private Animation<TextureRegion> enemyDownAnimation;
    private Animation<TextureRegion> enemyUpAnimation;
    private Animation<TextureRegion> enemyLeftAnimation;
    private Animation<TextureRegion> enemyRightAnimation;
    private Rectangle boundingBox;
    private int health;
    private float minCooldown = 1f; // Minimum waiting time (seconds)
    private float maxCooldown = 2f; // Maximum waiting time (seconds)
    private float changeDirectionCooldown = getRandomCooldown();
    private float timeSinceLastDirectionChange = 0f;
    private float currentDirectionDuration = 0f;
    private Vector2 previousPosition;
    int randomDirection = 3;
    private boolean activated;

    /**
     * Constructs an Enemy object with the given starting position.
     *
     * @param startPosition The starting position of the enemy.
     */
    public Enemy(Vector2 startPosition) {
        this.loadEnemyAnimation();
        this.position = startPosition;
        this.boundingBox = new Rectangle(startPosition.x, startPosition.y, getWidth(), getHeight());
        this.health = 1; // Set the initial health of the enemy to 1
        this.previousPosition = new Vector2(startPosition);
        this.activated = false;
        Random random = new Random();
        randomDirection = random.nextInt(4);
    }

    /**
     * Updates the enemy's state based on the elapsed time.
     *
     * @param delta The elapsed time since the last update.
     */
    public void update(float delta) {
        previousPosition.set(position);
        currentDirectionDuration += delta;
        timeSinceLastDirectionChange += delta;
        if (timeSinceLastDirectionChange >= changeDirectionCooldown) {
            Random random = new Random();
            randomDirection = random.nextInt(4); // 0: Up, 1: Down, 2: Left, 3: Right
            timeSinceLastDirectionChange = 0f; // Direction changed, reset the time
        }
        switch (randomDirection) {
            case 0:
                position.y += speed * delta;
                currentEnemyAnimation = enemyUpAnimation;
                break;
            case 1:
                position.y -= speed * delta;
                currentEnemyAnimation = enemyDownAnimation;
                break;
            case 2:
                position.x -= speed * delta;
                currentEnemyAnimation = enemyLeftAnimation;
                break;
            case 3:
                position.x += speed * delta;
                currentEnemyAnimation = enemyRightAnimation;
                break;
            default:
                break;
        }

        // Implement any additional logic for enemy behavior

        // Check if the enemy is out of bounds and adjust its position if necessary
        if (position.x < 0) position.x = 0;
        if (position.x > Gdx.graphics.getWidth() - getWidth()) position.x = Gdx.graphics.getWidth() - getWidth();
        if (position.y < 0) position.y = 0;
        if (position.y > Gdx.graphics.getHeight() - getHeight()) position.y = Gdx.graphics.getHeight() - getHeight();
    }

    /**
     * Generates a random cooldown time between the minimum and maximum values.
     *
     * @return A random cooldown time.
     */
    private float getRandomCooldown() {
        Random random = new Random();
        return minCooldown + random.nextFloat() * (maxCooldown - minCooldown);
    }

    /**
     * Loads enemy animations from the texture sheet.
     */
    private void loadEnemyAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("mobs.png"));
        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 3;

        // Animation frames for walking down
        com.badlogic.gdx.utils.Array<TextureRegion> standDownFrames = new com.badlogic.gdx.utils.Array<>(TextureRegion.class);
        int yDown = 4 * frameHeight;
        for (int col = 0; col < animationFrames; col++) {
            standDownFrames.add(new TextureRegion(walkSheet, col * frameWidth, yDown, frameWidth, frameHeight));
        }
        enemyDownAnimation = new Animation<>(0.1f, standDownFrames);

        // Animation frames for walking right
        com.badlogic.gdx.utils.Array<TextureRegion> walkRightFrames = new com.badlogic.gdx.utils.Array<>(TextureRegion.class);
        int yRight = 5 * frameHeight;
        for (int col = 0; col < animationFrames; col++) {
            walkRightFrames.add(new TextureRegion(walkSheet, col * frameWidth, yRight, frameWidth, frameHeight));
        }
        enemyLeftAnimation = new Animation<>(0.1f, walkRightFrames);

        // Animation frames for walking up
        com.badlogic.gdx.utils.Array<TextureRegion> walkUpFrames = new com.badlogic.gdx.utils.Array<>(TextureRegion.class);
        int yUp = 6 * frameHeight;
        for (int col = 0; col < animationFrames; col++) {
            walkUpFrames.add(new TextureRegion(walkSheet, col * frameWidth, yUp, frameWidth, frameHeight));
        }
        enemyRightAnimation = new Animation<>(0.1f, walkUpFrames);

        // Animation frames for walking left
        com.badlogic.gdx.utils.Array<TextureRegion> walkLeftFrames = new Array<>(TextureRegion.class);
        int yLeft = 7 * frameHeight;
        for (int col = 0; col < animationFrames; col++) {
            walkLeftFrames.add(new TextureRegion(walkSheet, col * frameWidth, yLeft, frameWidth, frameHeight));
        }
        enemyUpAnimation = new Animation<>(0.1f, walkLeftFrames);

        // Set the initial animation to down animation
        currentEnemyAnimation = enemyDownAnimation;
    }

    /**
     * Gets the width of the enemy.
     *
     * @return The width of the enemy.
     */
    public float getWidth() {
        return currentEnemyAnimation.getKeyFrame(0).getRegionWidth();
    }

    /**
     * Gets the height of the enemy.
     *
     * @return The height of the enemy.
     */
    public float getHeight() {
        return currentEnemyAnimation.getKeyFrame(0).getRegionHeight();
    }

    /**
     * Updates the bounding box of the enemy based on its position.
     */
    private void updateBoundingBox() {
        boundingBox.set(position.x, position.y, getWidth(), getHeight());
    }

    /**
     * Gets the bounding box of the enemy.
     *
     * @return The bounding box of the enemy.
     */
    public Rectangle getBoundingBox() {
        updateBoundingBox();
        return boundingBox;
    }

    /**
     * Checks if the enemy is activated.
     *
     * @return True if the enemy is activated, false otherwise.
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * Sets the activation status of the enemy.
     *
     * @param activated The new activation status.
     */
    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    /**
     * Gets the current animation of the enemy.
     *
     * @return The current animation of the enemy.
     */
    public Animation<TextureRegion> getCurrentEnemyAnimation() {
        return currentEnemyAnimation;
    }

    /**
     * Gets the position of the enemy.
     *
     * @return The position of the enemy.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Checks if the enemy collides with a character.
     *
     * @param character The character to check for collision.
     * @return True if the enemy collides with the character, false otherwise.
     */
    public boolean collidesWith(Character character) {
        return boundingBox.overlaps(character.getBoundingBox());
    }

    /**
     * Sets the position of the enemy.
     *
     * @param newPosition The new position of the enemy.
     */
    public void setPosition(Vector2 newPosition) {
        this.position.set(newPosition);
    }

    /**
     * Gets the previous position of the enemy.
     *
     * @return The previous position of the enemy.
     */
    public Vector2 getPreviousPosition() {
        return previousPosition;
    }
}
