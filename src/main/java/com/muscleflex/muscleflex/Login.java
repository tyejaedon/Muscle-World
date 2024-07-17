package com.muscleflex.muscleflex;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Login extends VBox {

    private static Login instance;  // Step 1: Add a private static instance variable

    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    protected Label welcomeLabel = new Label("Welcome to Muscle Flex");

    HBox loginBox;
    HBox buttonBox;
    VBox Box;
    int width;
    int height;

    // Step 2: Make the constructor private to prevent instantiation
    private Login(int width, int height) {
        this.width = width;
        this.height = height;
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
        registerButton.setOnAction(e -> handleRegister());

        // Layouts
        VBox labelBox = new VBox(20, loginLabel, passwordLabel);
        labelBox.setAlignment(Pos.CENTER_RIGHT);

        VBox fieldBox = new VBox(20, usernameField, passwordField);
        fieldBox.setAlignment(Pos.CENTER_LEFT);

        loginBox = new HBox(20, labelBox, fieldBox);
        loginBox.setAlignment(Pos.CENTER);

        buttonBox = new HBox(20, signInButton, registerButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20));
        getChildren().addAll(welcomeLabel, loginBox, buttonBox);

        setAlignment(Pos.CENTER);
        setPadding(new Insets(40));
        setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    // Step 3: Provide a public static method to return the single instance
    public static Login getInstance(int width, int height) {
        if (instance == null) {
            instance = new Login(width, height);
        }
        return instance;
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
            getChildren().clear();
            getChildren().add(new Homepage(width, height));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login");
            alert.setHeaderText(null);
            alert.setContentText("Invalid email or password.");
            alert.showAndWait();
        }
    }

    private void handleRegister() {
        Registration registration = new Registration(width, height);
        getChildren().clear();
       getChildren().add(registration);
    }
}
