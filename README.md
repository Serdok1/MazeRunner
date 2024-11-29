# Maze Runner Game

Welcome to the Maze Runner Game! This README provides an overview of our code structure, instructions on how to run and use the game, and additional information about the game mechanics.

## Code Structure

Our codebase is organized as follows:

- **core**: Contains the core game logic and classes.
    - **de.tum.cit.ase.maze**: Main package for Maze Runner game.
        - **Character.java**: Represents the player character.
        - **Coin.java**: Represents collectible coins in the game.
        - **HUD.java**: Manages the Heads-Up Display (HUD) for the game.
        - **Key.java**: Represents keys that the player can collect.
        - **MapSelectionScreen.java**: Handles map selection functionality.
        - **MenuScreen.java**: Manages the main menu of the game.
        - **MazeRunnerGame.java**: Main class for the game, extends the LibGDX Game class.
        - **GameScreen.java**: Represents the main game screen.
        - **Trap.java**: Represents traps in the game.
        - **Wall.java**: Represents walls in the game.
- **desktop**: Contains the desktop launcher class.
    - **DesktopLauncher.java**: Entry point for the desktop version of the game.

## How to Run

Follow these steps to run the Maze Runner Game:

1. Clone the repository to your local machine.
2. Open the project in your preferred Java development environment (e.g., IntelliJ, Eclipse).
3. Locate and run the `DesktopLauncher` class.
4. Enjoy playing the Maze Runner Game!

## How to Play

- **Objective**: Navigate through the maze, collect keys, and reach the exit point to complete the level.
- **Controls**: Use arrow keys to move the player character.
- **Collectibles**: Collect keys to unlock the exit and avoid traps to maintain health.
- **Heads-Up Display (HUD)**: Keep an eye on the HUD for information on remaining lives, collected keys, and other important details.

## Game Mechanics

Our game goes beyond the minimal requirements with the following mechanics:

- **Collectibles**: Keys are collectible items that the player needs to collect to unlock the exit.
- **Traps**: Traps pose a threat to the player. Colliding with traps will decrease the player's health.
- **Enemies**: Enemies roam the maze and challenge the player. If they touch the player, they cause the player to lose a life.
- **Map Selection**: Players can choose from different maze maps to enhance gameplay variety.

## Detailed Document

You can access the document via javadoc/index.html

## License

This project is licensed under the [MIT License](LICENSE).
