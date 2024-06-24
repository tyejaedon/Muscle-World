package com.muscleflex.muscleflex;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginApp extends Application {

    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Label welcomeLabel = new Label("Welcome to Muscle Flex");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        // Welcome label styling
        welcomeLabel.setFont(new Font("Arial", 30));
        welcomeLabel.setTextFill(Color.RED);
        welcomeLabel.setAlignment(Pos.CENTER);

        // Username and password labels
        Label loginLabel = new Label("Username:");
        loginLabel.setFont(new Font("Verdana", 20));
        loginLabel.setTextFill(Color.WHITE);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(new Font("Verdana", 20));
        passwordLabel.setTextFill(Color.WHITE);

        // Username and password fields
        usernameField.setFont(new Font("Arial", 18));
        usernameField.setPrefSize(200, 30);
        usernameField.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-alignment: center;");

        passwordField.setFont(new Font("Arial", 18));
        passwordField.setPrefSize(200, 30);
        passwordField.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-alignment: center;");

        // Buttons
        Button signInButton = new Button("Login");
        signInButton.setFont(new Font("SansSerif", 18));
        signInButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        signInButton.setOnAction(e -> handleLogin());

        Button registerButton = new Button("Register");
        registerButton.setFont(new Font("SansSerif", 18));
        registerButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        registerButton.setOnAction(e -> {
            primaryStage.close();
            handleRegister();
        });

        // Layouts
        VBox labelBox = new VBox(20, loginLabel, passwordLabel);
        labelBox.setAlignment(Pos.CENTER_RIGHT);

        VBox fieldBox = new VBox(20, usernameField, passwordField);
        fieldBox.setAlignment(Pos.CENTER_LEFT);

        HBox loginBox = new HBox(20, labelBox, fieldBox);
        loginBox.setAlignment(Pos.CENTER);

        HBox buttonBox = new HBox(20, signInButton, registerButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20));

        VBox mainLayout = new VBox(30, welcomeLabel, loginBox, buttonBox);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(40));
        mainLayout.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(mainLayout, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        DatabaseConnector dbConnector = DatabaseConnector.getInstance();
        boolean isValidUser = dbConnector.loginUser(username, password);

        if (isValidUser) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login");
            alert.setHeaderText(null);
            alert.setContentText("Login successful!");
            alert.showAndWait();

            // Navigate to Homepage
            // Note: You need to implement Homepage class and its config method
            // Homepage exHomepage = new Homepage();
            // exHomepage.config();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login");
            alert.setHeaderText(null);
            alert.setContentText("Invalid email or password.");
            alert.showAndWait();
        }
    }

    private void handleRegister() {
       Registration registration = new Registration();
       registration.start(new Stage());


    }

    public static void main(String[] args) {
        launch(args);
    }
}

