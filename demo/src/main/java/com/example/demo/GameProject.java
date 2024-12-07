package com.example.demo;

import com.sun.tools.javac.Main;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import javax.swing.text.html.StyleSheet;
import java.io.IOException;

public class GameProject extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML so that we can access all groups.
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        AnchorPane root = fxmlLoader.load();



        // Set the scene
        Scene scene = new Scene(root, 800, 600);

        // Show the stage
        stage.setTitle("JavaFX Application");
        stage.setScene(scene);
        scene.setOnMouseClicked(e -> root.requestFocus());
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}