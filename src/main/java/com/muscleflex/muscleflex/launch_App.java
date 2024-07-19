package com.muscleflex.muscleflex;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class launch_App extends Application {
    private StackPane root = new StackPane();
    Scene scene;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");
    int width = 900;
    int height = 700;
        Login login = new Login(width,height);

       scene = new Scene(switchtoview(login), width, height);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public StackPane switchtoview(VBox view) {
        root.getChildren().clear();
        root.getChildren().add(view);
        return  root;
    }
    public static   void main(String[] args) {
        launch(args);
    }
}
