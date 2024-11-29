package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * The Character class represents the player's character in the game.
 */
public class Character {
    private Vector2 position;
    private float speed = 100.0f; // Speed of the character in units per second
    // Animations for different directions
    private Animation<TextureRegion> currentCharacterAnimation;
    private Animation<TextureRegion> characterDownAnimation;
    private Animation<TextureRegion> characterUpAnimation;
    private Animation<TextureRegion> characterLeftAnimation;
    private Animation<TextureRegion> characterRightAnimation;
    private Animation<TextureRegion> characterStandDownAnimation;
    private int health;
    private Rectangle boundingBox;
    private Vector2 previousPosition;
    private int collectedKeys;

    /**
     * Constructs a Character object with the given starting position.
     *
     * @param startPosition The starting position of the character.
     */
    public Character(Vector2 startPosition) {
        this.loadCharacterAnimation();
        this.health = 3;
        this.boundingBox = new Rectangle(startPosition.x, startPosition.y, getWidth() / 2, getHeight() / 2);
        this.position = startPosition;
        this.previousPosition = new Vector2(startPosition);
    }

    /**
     * Updates the character's state based on the elapsed time and user input.
     *
     * @param delta The elapsed time since the last update.
     */
    public void update(float delta) {
        // Update position based on input
        previousPosition.set(position);
        boolean isMoving = false;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.y += speed * delta;
            currentCharacterAnimation = characterUpAnimation;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.y -= speed * delta;
            currentCharacterAnimation = characterDownAnimation;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            position.x -= speed * delta;
            currentCharacterAnimation = characterLeftAnimation;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            position.x += speed * delta;
            currentCharacterAnimation = characterRightAnimation;
            isMoving = true;
        }
        if (!isMoving) {
            currentCharacterAnimation = characterStandDownAnimation; // Character is standing still
        }

        isMoving = Gdx.input.isKeyPressed(Input.Keys.W) ||
                Gdx.input.isKeyPressed(Input.Keys.S) ||
                Gdx.input.isKeyPressed(Input.Keys.A) ||
                Gdx.input.isKeyPressed(Input.Keys.D);

        if (!isMoving) {
            // Set a default animation when no movement keys are pressed
            currentCharacterAnimation = characterStandDownAnimation;
        }
    }

    /**
     * Loads character animations from the texture sheet.
     */
    private void loadCharacterAnimation() {

        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));

        int frameWidth = 16;
        int frameHeight = 32;
        int animationFrames = 4;

        // Animation frames for walking down
        com.badlogic.gdx.utils.Array<TextureRegion> walkDownFrames = new com.badlogic.gdx.utils.Array<TextureRegion>();
        int yDown = 0; //y coordinate for the down animation
        // Add all frames to the animation
        for (int i = 0; i < animationFrames; i++)
            walkDownFrames.add(new TextureRegion(walkSheet, i * frameWidth, yDown, frameWidth, frameHeight));
        characterDownAnimation = new Animation(0.1f, walkDownFrames);

        // Animation frames for standing down
        com.badlogic.gdx.utils.Array<TextureRegion> standDownFrames = new com.badlogic.gdx.utils.Array<>(TextureRegion.class);
        // Add all frames to the animation
        for (int col = 0; col < 1; col++) {
            standDownFrames.add(new TextureRegion(walkSheet, col * frameWidth, yDown, frameWidth, frameHeight));
        }
        characterStandDownAnimation = new Animation<>(0.1f, standDownFrames);

        // Animation frames for walking right
        com.badlogic.gdx.utils.Array<TextureRegion> walkRightFrames = new com.badlogic.gdx.utils.Array<>(TextureRegion.class);
        int yRight = 1 * frameHeight; //y coordinate for the up animation
        // Add all frames to the animation
        for (int col = 0; col < animationFrames; col++) {
            walkRightFrames.add(new TextureRegion(walkSheet, col * frameWidth, yRight, frameWidth, frameHeight));
        }
        characterRightAnimation = new Animation<>(0.1f, walkRightFrames);

        // Animation frames for walking up
        com.badlogic.gdx.utils.Array<TextureRegion> walkUpFrames = new com.badlogic.gdx.utils.Array<>(TextureRegion.class);
        int yUp = 2 * frameHeight; //y coordinate for the up animation
        // Add all frames to the animation
        for (int col = 0; col < animationFrames; col++) {
            walkUpFrames.add(new TextureRegion(walkSheet, col * frameWidth, yUp, frameWidth, frameHeight));
        }
        characterUpAnimation = new Animation<>(0.1f, walkUpFrames);

        // Animation frames for walking left
        com.badlogic.gdx.utils.Array<TextureRegion> walkLeftFrames = new Array<>(TextureRegion.class);
        int yLeft = 3 * frameHeight; //y coordinate for the up animation
        // Add all frames to the animation
        for (int col = 0; col < animationFrames; col++) {
            walkLeftFrames.add(new TextureRegion(walkSheet, col * frameWidth, yLeft, frameWidth, frameHeight));
        }
        characterLeftAnimation = new Animation<>(0.1f, walkLeftFrames);

        currentCharacterAnimation = characterStandDownAnimation; // Set the current animation to the down animation
    }

    /**
     * Gets the previous position of the character.
     *
     * @return The previous position of the character.
     */
    public Vector2 getPreviousPosition() {
        return previousPosition;
    }

    /**
     * Gets the width of the character.
     *
     * @return The width of the character.
     */
    public float getWidth() {
        return currentCharacterAnimation.getKeyFrame(0).getRegionWidth() / 2;
    }

    /**
     * Gets the height of the character.
     *
     * @return The height of the character.
     */
    public float getHeight() {
        return currentCharacterAnimation.getKeyFrame(0).getRegionHeight() / 4;
    }

    /**
     * Updates the bounding box of the character based on its position.
     */
    private void updateBoundingBox() {
        boundingBox.set(position.x, position.y, getWidth(), getHeight());
    }

    /**
     * Gets the bounding box of the character.
     *
     * @return The bounding box of the character.
     */
    public Rectangle getBoundingBox() {
        updateBoundingBox();
        return boundingBox;
    }

    /**
     * Decreases the health of the character.
     */
    public void decreaseHealth() {
        health--;
    }

    /**
     * Increases the health of the character.
     */
    public void increaseHealth() {
        health++;
    }

    /**
     * Gets the health of the character.
     *
     * @return The health of the character.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the health of the character.
     *
     * @param health The new health value.
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Increases the count of collected keys.
     */
    public void increaseCollectedKeys() {
        this.collectedKeys++;
    }

    /**
     * Gets the count of collected keys.
     *
     * @return The count of collected keys.
     */
    public int getCollectedKeys() {
        return this.collectedKeys;
    }

    /**
     * Gets the current position of the character.
     *
     * @return The current position of the character.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Sets the starting point of the character.
     *
     * @param startPoint The new starting point.
     */
    public void setCharacterStartPoint(Vector2 startPoint) {
        this.position.set(startPoint);
    }

    /**
     * Gets the starting point of the character.
     *
     * @return The starting point of the character.
     */
    public Vector2 getCharacterStartPoint() {
        return this.position;
    }

    /**
     * Sets the position of the character.
     *
     * @param newPosition The new position of the character.
     */
    public void setPosition(Vector2 newPosition) {
        this.position.set(newPosition);
    }

    /**
     * Gets the current animation of the character.
     *
     * @return The current animation of the character.
     */
    public Animation<TextureRegion> getcurrentCharacterAnimation() {
        return currentCharacterAnimation;
    }

    // Additional methods to set animations if needed

    /**
     * Sets the animation for walking down.
     *
     * @param animation The new animation for walking down.
     */
    public void setDownAnimation(Animation<TextureRegion> animation) {
        this.characterDownAnimation = animation;
    }

    /**
     * Sets the animation for walking up.
     *
     * @param animation The new animation for walking up.
     */
    public void setUpAnimation(Animation<TextureRegion> animation) {
        this.characterUpAnimation = animation;
    }

    /**
     * Sets the animation for walking left.
     *
     * @param animation The new animation for walking left.
     */
    public void setLeftAnimation(Animation<TextureRegion> animation) {
        this.characterLeftAnimation = animation;
    }

    /**
     * Sets the animation for walking right.
     *
     * @param animation The new animation for walking right.
     */
    public void setRightAnimation(Animation<TextureRegion> animation) {
        this.characterRightAnimation = animation;
    }

    /**
     * Sets the animation for standing down.
     *
     * @param animation The new animation for standing down.
     */
    public void setStandDownAnimation(Animation<TextureRegion> animation) {
        this.characterStandDownAnimation = animation;
    }

    /**
     * Sets the speed of the character.
     *
     * @param speed The new speed of the character.
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
