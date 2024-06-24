package com.muscleflex.muscleflex;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Registration extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Muscle Flex Membership Registration");

        // Creating the GridPane layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);



        // Welcome Label
        Label welcome = new Label("Become a Muscle Flex Member!");
        welcome.setFont(Font.font("cursive", FontWeight.BOLD, 28));
        welcome.setTextFill(Color.web("#FFD700")); // Gold color
        welcome.setEffect(new DropShadow(5, Color.BLACK));
        GridPane.setConstraints(welcome, 0, 0, 2, 1);

        // Labels and Fields
        String[] labelNames = {"Username: ", "Email Address: ", "Full Name: ", "Password: ", "Re-type Password: ", "Age: ", "Weight (kg): ", "Height (cm): ", "Gender: "};
        TextField[] textFields = new TextField[8];
        PasswordField pwordField = new PasswordField();
        PasswordField repwordField = new PasswordField();
        ComboBox<String> gender = new ComboBox<>();
        gender.getItems().addAll("Male", "Female");

        for (int i = 0; i < labelNames.length; i++) {
            Label label = new Label(labelNames[i]);
            label.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
            label.setTextFill(Color.web("#FFFFFF")); // White color
            GridPane.setConstraints(label, 0, i + 1);

            if ((i <3||i>4) && i!=8) {

                textFields[i] = new TextField();
                textFields[i].setFont(Font.font("Arial", 18));
                textFields[i].setStyle("-fx-control-inner-background: #D3D3D3; -fx-text-fill: #000000;");
                textFields[i].setEffect(new DropShadow(3, Color.BLACK));
                GridPane.setConstraints(textFields[i], 1, i + 1);
                grid.getChildren().addAll(label, textFields[i]);
            } else if (i == 3) {
                pwordField.setFont(Font.font("Arial", 18));
                pwordField.setStyle("-fx-control-inner-background: #D3D3D3; -fx-text-fill: #000000;");
                pwordField.setEffect(new DropShadow(3, Color.BLACK));
                GridPane.setConstraints(pwordField, 1, i + 1);
                grid.getChildren().addAll(label, pwordField);
            } else if (i == 4) {
                repwordField.setFont(Font.font("Arial", 18));
                repwordField.setStyle("-fx-control-inner-background: #D3D3D3; -fx-text-fill: #000000;");
                repwordField.setEffect(new DropShadow(3, Color.BLACK));
                GridPane.setConstraints(repwordField, 1, i + 1);
                grid.getChildren().addAll(label, repwordField);
            } else if (i == 8) {
                gender.setStyle("-fx-control-inner-background: #D3D3D3; -fx-text-fill: #000000;");
                gender.setEffect(new DropShadow(3, Color.BLACK));
                GridPane.setConstraints(gender, 1, i + 1);
                grid.getChildren().addAll(label, gender);
            }
        }

        // Submit Button
        Button submitButton = new Button("Submit");
        submitButton.setFont(Font.font("SansSerif", FontWeight.BOLD, 18));
        submitButton.setStyle("-fx-background-color: #FF4500; -fx-text-fill: #FFFFFF;"); // Orange red background
        submitButton.setEffect(new DropShadow(5, Color.BLACK));

        submitButton.setOnAction(e -> {
            String tempUser = textFields[0].getText();
            String email = textFields[1].getText();
            String tempName = textFields[2].getText();
            String password = pwordField.getText();
            String rePassword = repwordField.getText();
            int age = Integer.parseInt(textFields[3].getText());
            float weight = Float.parseFloat(textFields[4].getText());
            float tempHeight = Float.parseFloat(textFields[5].getText());

            if (password.equals(rePassword)) {
                DatabaseConnector dbConnector = DatabaseConnector.getInstance();
                boolean registered = dbConnector.registerUser(tempUser, email, tempName, password, age, weight, tempHeight, gender.getValue());

                if (registered) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Registration Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("Registration successful!");
                    alert.showAndWait();
                    primaryStage.close();
                    LoginApp exLogin = new LoginApp();
                    exLogin.start(new Stage());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Registration Error");
                    alert.setHeaderText(null);
                    alert.setContentText("An error occurred!");
                    alert.showAndWait();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Password Mismatch");
                alert.setHeaderText(null);
                alert.setContentText("Passwords do not match.");
                alert.showAndWait();
            }
        });

        GridPane.setConstraints(submitButton, 0, labelNames.length + 1, 2, 1);

        // Adding all elements to the grid
        grid.getChildren().addAll(welcome, submitButton);
        // HBox container centered in the scene
        VBox container = new VBox();
        HBox root = new HBox();


        root.setStyle("-fx-background-color: linear-gradient(#FF5733, #FFC300);");
        container.setAlignment(Pos.CENTER);
        root.setAlignment(Pos.CENTER);
        container.getChildren().add(grid);
        root.getChildren().addAll(container);





        // Setting the scene and showing the stage
        Scene scene = new Scene(root, 800, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
