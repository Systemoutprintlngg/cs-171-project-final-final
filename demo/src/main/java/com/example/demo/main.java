package com.example.demo;
import java.io.*;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class main {
    // Omar's work:
    private GameData gameData;
    private long bestTimeInSeconds = Long.MAX_VALUE; // Store best time in seconds (start with a high value)
    private static final String FILE_NAME = "leaderboard.txt"; // The name of the high score file
    private ArrayList<Long> storage = new ArrayList<>();

    // Leaderboard done by Omar, ChatGPT consulted.
    public long loadHighScore() {
        File file = new File(FILE_NAME);  // Use a writable file location

        if (!file.exists()) {
            return 0L; // Return 0 if the file doesn't exist
        }

        // Initialize a reader iff there is a file found
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Get the last known line and read it
            String line = reader.readLine();

            // If there is a line, then just parse and return it as a long, which is how most times are being kept.
            if (line != null) {
                return Long.parseLong(line);
            } else {
                // File is empty
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return 0L; // Default value if there is an error
    }

    // Save high score to the file. Omar's work
    public void saveHighScore(long score) {
        File file = new File(FILE_NAME);  // Initialize the file

        // Initialize a writer so that we can override the last known line
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Need to convert to a string from a long, as that's how we returned the number.
            writer.write(String.valueOf(score));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Set the GameData object (this method can be called from Leaderboard method). Omar's work
    public void setGameData(GameData gameData) {
        this.gameData = gameData;
        storage.add(bestTimeInSeconds);
    }

    // Update the leaderboard label with the game data. Omar's work
    public void updateLeaderboardLabel() {
        long elapsedTime = gameData.getElapsedTime();
        currentTime.setText("Time: " + formatTime(elapsedTime));

        System.out.println(loadHighScore());
        // Update best time if the current time is better
        if (elapsedTime < loadHighScore()) {
            saveHighScore(elapsedTime); // Set new best time
            bestTime.setText("Best Time: " + formatTime(loadHighScore())); // Update the label
        } else {
            bestTime.setText("Best Time remains: " + formatTime(loadHighScore()));
        }
    }


    // Format the time in minutes:seconds format. Omar's work
    private String formatTime(long seconds) {
        long minutes = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    // Omar's work
    public class GameData {
        private long startTime;
        private long elapsedTime; // This will store the elapsed time in milliseconds
        private AnimationTimer timer;

        public GameData() {
            // Start the timer when the GameData is initialized
            startTime = System.nanoTime(); // High precision timestamp (in nanoseconds)
            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    elapsedTime = (now - startTime) / 1_000_000; // Convert nanoseconds to milliseconds
                }
            };
        }

        // Start the game timer
        public void startTimer() {
            startTime = System.nanoTime(); // Reinitialize start time
            timer.start(); // Start the animation timer
        }

        // Stop the game timer
        public void stopTimer() {
            timer.stop(); // Stop the animation timer
        }

        // Get the elapsed time in seconds
        public long getElapsedTime() {
            return elapsedTime / 1000; // Convert milliseconds to seconds
        }
    }


    // ... (rest of your existing code)

    @FXML
    public Label timetext;
    @FXML
    public Label bestTime;
    @FXML
    public Label currentTime;
    // Omar's work
public void startGameTimer() {
    Timeline timelineone = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
        seconds++;
        UpdateTime("Time: " + formatTime(seconds)); // Update UI with the current time
        /*if (seconds < bestTimeInSeconds) {
            bestTimeInSeconds = seconds; // Update best time
        }*/
    }));
    timelineone.setCycleCount(Timeline.INDEFINITE);
    timelineone.play();
}

// Omar's work
public void UpdateTime(String text) {
    timetext.setText(text);
}
    // Below is where you place the references of every group.
    // ------------------------------------------------------------------- \\
    @FXML
    private Group collisionGroup;
    @FXML
    private Group successGroup;
    @FXML
    private Group WinnerGroup;
    @FXML
    private Group tempdeath;
    @FXML
    private Group tempdeath2;
    @FXML
    private Group dashboost;
    @FXML
    private Group jumpboost;
    // ------------------------------------------------------------------- \\
    // Holden work with some of Holden and Omar
    private ImageView sprite;
    private AnchorPane scene;
    private double startPositionX;
    private double startPositionY;
    @FXML
    private Button stage2button;
    @FXML
    public Button startbutton1;
    @FXML
    public Button youdiedbutton;

    private Bounds spriteBounds;
    private Bounds targetBounds;



    private Rectangle targetObj;

    private boolean wallJumping = false;

    private double velocityY = 0; // Vertical velocity
    private final double GRAVITY = 0.5; // Gravity acceleration
    private double JUMP_FORCE = -10; // Jump force (negative for upward movement)
    private int jumpCounter = 0; // Tracks current jumps
    private final int MAX_JUMPS = 1;

    public int seconds;
    public int placeholder;
    public boolean arr = false;

    private Rectangle darkness;


    // Player Inputs
    private BooleanProperty spacePressed = new SimpleBooleanProperty();
    private BooleanProperty aPressed = new SimpleBooleanProperty();
    private BooleanProperty dPressed = new SimpleBooleanProperty();

    private BooleanBinding keyPressed = spacePressed.or(aPressed).or(dPressed);

    // Holden's work
    public void init(ImageView sprite, AnchorPane scene, double startPositionX, double startPositionY, GameData gameData) throws IOException {
        // Initialize all important variables, which is only done after the FXML is loaded.
        this.scene = scene;
        this.sprite = sprite;
        this.startPositionX = startPositionX;
        this.startPositionY = startPositionY;
        this.gameData = gameData;

        // Initialize the darkness
        this.darkness = new Rectangle();
        darkness.setLayoutX(startPositionX - 10);
        darkness.setLayoutY(startPositionY);
        darkness.setWidth(5);
        darkness.setHeight(scene.getPrefHeight());
        scene.getChildren().add(darkness);

        movePlayer();
        startGameLoop();
    }

    // Simply listens for player input, Holden's work
    private void movePlayer() {
        scene.setOnKeyPressed(e -> {
            System.out.println(e.getCode());
            if (e.getCode() == KeyCode.SPACE) {
                spacePressed.set(true);
            }

            if (e.getCode() == KeyCode.A) {
                aPressed.set(true);
                //     System.out.println("A pressed");
            }

            if (e.getCode() == KeyCode.D) {
                dPressed.set(true);
                //    System.out.println("D pressed");
            }
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                spacePressed.set(false);
            }

            if (e.getCode() == KeyCode.A) {
                aPressed.set(false);
            }

            if (e.getCode() == KeyCode.D) {
                dPressed.set(false);
            }
        });
    }

    // Setting up an update function, which is supposed to get called EVERY FRAME.
    private AnimationTimer updateFunction;
    // Holden's work
    public void startGameLoop() {
        updateFunction = new AnimationTimer() {
            @Override
            public void handle(long timer) {
                update();
            }
        };

        updateFunction.start();
    }

    // Set up important variables
    private boolean grounded;
    private double MOVE_SPEED = 1;

    // Holden's work
    private void update() {
        // Get the position of the player from the last frame.
        double lastPosX = sprite.getLayoutX();
        double lastPosY = sprite.getLayoutY();

        // Check all case scenarios for movement.


        if (dPressed.get() || aPressed.get()) {
            // If there is input, and we're grounded, regular movement speed
            if (grounded && !checkDarknessCollision(sprite, darkness)) {
                MOVE_SPEED = 1;
                darkness.setWidth(darkness.getWidth() + 0.3);
            } else if (!grounded && !checkDarknessCollision(sprite, darkness)) { // If there's input and we're not grounded, air multiplier
                MOVE_SPEED = 2;
                darkness.setWidth(darkness.getWidth() + 0.3);
            } else if (checkDarknessCollision(sprite, darkness)) { // If there's input and we hit the darkness, stop player and start stuff for darkness.
                MOVE_SPEED = 0;
                darkness.setWidth(darkness.getWidth() + 100);
                youdiedbutton.visibleProperty().set(true);
                youdiedbutton.toFront();

                darkness.toBack();
            }
        }

        // Set the layout on A press
        if (aPressed.get()) {
            sprite.setLayoutX(sprite.getLayoutX() - MOVE_SPEED);
        }

        // Set the layout on D press
        if (dPressed.get()) {
            sprite.setLayoutX(sprite.getLayoutX() + MOVE_SPEED);
        }

        // Check collision with the level on the x axis
        if (checkCollision(sprite, collisionGroup)) {
            // If there is a collision, set X position to last known position before the collision
            sprite.setLayoutX(lastPosX);

            // Wall jumping logic, if we do a horizontal collision and are not grounded, we must be hitting a wall, therefore initiate a wall jump
            if (!grounded) {
                wallJumping = true;

                jumpCounter = 0;

                velocityY = GRAVITY / 2;
            } else {
                wallJumping = false;
            }
        }

        // Get input for space, if we can jump then jump
        if (spacePressed.get() && jumpCounter < MAX_JUMPS) {
            velocityY = JUMP_FORCE; // Apply jump force

            jumpCounter++; // Increment jump counter

        }

        velocityY += GRAVITY; // Apply gravity every frame that we can

        // Set the sprite y velocity every frame
        sprite.setLayoutY(sprite.getLayoutY() + velocityY);

        // Checks collision between the sprite and the level on the y axis
        if (checkCollision(sprite, collisionGroup)) {
            // Set sprite's position to its last known y position
            sprite.setLayoutY(lastPosY);

            // Set variables
            grounded = true;

            velocityY = 0; // Stop downward movement
            jumpCounter = 0; // Reset all jumps
        } else {
            grounded = false;
        }

        // Temp death technically resets position, but darkness usually occludes it no matter what. All this does is reset the position to the starting position
        if (checkCollision(sprite, tempdeath)) {
            sprite.setLayoutX(startPositionX);
            sprite.setLayoutY(startPositionY);
        }

        // Checks if we come into contact with our only ability, if it does, we decrement the jump counter so that we can do another jump.
        if (checkCollision(sprite, jumpboost)) {
            jumpCounter = 0;
        }

        // Checks if we come into contact with the win condition, if so, a button appears which takes you to the next level.
        if (checkCollision(sprite, successGroup)) { // The first check for if you've reached the door, button to go next stage appears
            MOVE_SPEED = 0;
            startbutton1.visibleProperty().set(true);
            if (stage2button != null) {
                stage2button.visibleProperty().set(true);
            }
        }

    }
    // All this method does is check if the bounds between the sprite and the current node instance of an ImageView come into contact, and if they do, it returns true. Holden's work
    private boolean checkCollision(ImageView sprite, Group targetGroup) {
        if (targetGroup != null) {
            for (Node node : targetGroup.getChildren()) {
                if (node instanceof ImageView) {
                    ImageView test = (ImageView) node;

                    Bounds rectBounds = test.localToScene(test.getBoundsInLocal());
                    Bounds spriteBounds = sprite.localToScene(sprite.getBoundsInLocal());

                    if (rectBounds.intersects(spriteBounds)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // This is the same as the previous method, but instead of iterating through nodes, we simply check the rectangle bounds of the darkness. Holden's work
    private boolean checkDarknessCollision(ImageView sprite, Rectangle rectangle) {
        Bounds rectBounds = rectangle.localToScene(rectangle.getBoundsInLocal());
        Bounds spriteBounds = sprite.localToScene(sprite.getBoundsInLocal());

        if (rectBounds.intersects(spriteBounds)) {
            return true;
        }
        return false;
    }

    // We initialize a lot of stuff for the level's FXML and overall stuff here. Essentially, this is called whenever the player presses the button in the starting screen, respawn screen, or final screen.
    // Omar and Holden's work
    @FXML
    public void switchTo(MouseEvent event) throws IOException {
        // Load the FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        AnchorPane root = fxmlLoader.load();

        // Initialize the sprite and adjust its parameters
        Image image = new Image("newSprite.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);
        imageView.setPreserveRatio(true);

        // Get the controller, which just ensures that whatever we do is only done once the FXML is loaded
        main mainController = fxmlLoader.getController();

        // Initialize gameData before starting the game
        gameData = new GameData();
        gameData.startTimer();  // Start the timer when the game scene begins

        // Pass the shared GameData instance
        mainController.init(imageView, root, 0, 0, this.gameData);

        // Add the sprite to the scene
        root.getChildren().add(imageView);
        mainController.startGameTimer();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        root.requestFocus();
    }


    // This is the exact same as the method above, but just loads a different FXML
    // Omar and Holden's work
    @FXML
    public void switchTo2(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gameview2.fxml"));

        AnchorPane root = fxmlLoader.load();

        Image image = new Image("newSprite.png");
        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(30);
        imageView.setFitWidth(30);

        imageView.setPreserveRatio(true);

        main PC = fxmlLoader.getController();

        // Pass the shared GameData instance
        PC.init(imageView, root, 0, 0, this.gameData);

        root.getChildren().add(imageView);
        PC.startGameTimer();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        root.requestFocus();
    }

    // This is the leaderboard once the player beats hte second level. Done by Omar, ChatGPT was consulted for some problems.
    public void Leaderboard(MouseEvent event) throws IOException {
        // Load the FXML file (no controller in the FXML)
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Leaderboard.fxml"));

        // Load the root AnchorPane from the FXML
        AnchorPane root = fxmlLoader.load();

        // Get the main controller instance (which is the controller for Leaderboard.fxml in this case)
        main mainController = fxmlLoader.getController();

        // Pass the shared GameData object or any other data you want to the main controller
        mainController.setGameData(this.gameData); // Set the data to the main controller

        // Stop the timer before switching scenes
        gameData.stopTimer();

        // Update the label in Leaderboard.fxml using the data
        mainController.updateLeaderboardLabel();
      //  mainController.updateBestTimeDisplay();
        // Switch the scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        // Request focus for the new scene
        root.requestFocus();
    }



    // Done by Omar
    public void DeathScreen(MouseEvent event) throws IOException {
        // Load the FXML so that we can access all groups.
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("death.fxml"));

        AnchorPane root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        root.requestFocus();
    }

    // Formats the time for the UI and player info into the correct format. Omar's work
    public String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }
}



