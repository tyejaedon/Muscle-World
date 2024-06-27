package com.muscleflex.muscleflex;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Objects;

public class Registration extends VBox {


    public  Registration(int width, int Height) {


        // Creating the GridPane layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        // Welcome Label
        Label welcome = new Label("Become a Muscle Flex Member!");
        welcome.setFont(Font.font("cursive", 28));
        welcome.setTextFill(Color.GOLD);
        welcome.setEffect(new DropShadow(5, Color.BLACK));
        GridPane.setConstraints(welcome, 0, 0, 2, 1);

        // Labels and Fields
        String[] labelNames = {"Username: ", "Email Address: ", "Full Name: ", "Password: ", "Re-type Password: ", "Age: ", "Weight (kg): ", "Height (cm): ", "Gender: "};
        TextField[] textFields = new TextField[8]; // Adjusted based on your fields
        PasswordField pwordField = new PasswordField();
        PasswordField repwordField = new PasswordField();
        ComboBox<String> gender = new ComboBox<>();
        gender.getItems().addAll("Male", "Female");

        for (int i = 0; i < labelNames.length; i++) {
            Label label = new Label(labelNames[i]);
            label.setFont(Font.font("Verdana", 18));
            label.setTextFill(Color.WHITE);
            GridPane.setConstraints(label, 0, i + 1);

            if ((i != 4) && i!=8 && i != 3) {
                textFields[i] = new TextField();
                textFields[i].setFont(Font.font(18));
                textFields[i].setStyle("-fx-control-inner-background: #D3D3D3; -fx-text-fill: #000000;");
                textFields[i].setEffect(new DropShadow(3, Color.BLACK));
                GridPane.setConstraints(textFields[i], 1, i + 1);
                grid.getChildren().addAll(label, textFields[i]);
            } else if (i == 3) {
                pwordField.setFont(Font.font(18));
                pwordField.setStyle("-fx-control-inner-background: #D3D3D3; -fx-text-fill: #000000;");
                pwordField.setEffect(new DropShadow(3, Color.BLACK));
                GridPane.setConstraints(pwordField, 1, i + 1);
                grid.getChildren().addAll(label, pwordField);
            } else if (i == 4) {
                repwordField.setFont(Font.font(18));
                repwordField.setStyle("-fx-control-inner-background: #D3D3D3; -fx-text-fill: #000000;");
                repwordField.setEffect(new DropShadow(3, Color.BLACK));
                GridPane.setConstraints(repwordField, 1, i + 1);
                grid.getChildren().addAll(label, repwordField);
            } else {
                gender.setStyle("-fx-control-inner-background: #D3D3D3; -fx-text-fill: #000000;");
                gender.setEffect(new DropShadow(3, Color.BLACK));
                GridPane.setConstraints(gender, 1, i + 1);
                grid.getChildren().addAll(label, gender);
            }
        }

        // Submit Button
        Button submitButton = new Button("Sign Up");
        submitButton.setFont(Font.font("SansSerif", FontWeight.BOLD, 18));
        submitButton.setStyle("-fx-background-color: gold; -fx-text-fill: #FFFFFF;");
        submitButton.setEffect(new DropShadow(5, Color.BLACK));

        submitButton.setOnAction(e -> {
            String username = textFields[0].getText();
            String email = textFields[1].getText();
            String fullName = textFields[2].getText();
            String password = pwordField.getText();
            String rePassword = repwordField.getText();
            int age = Integer.parseInt(textFields[5].getText());
            float weight = Float.parseFloat(textFields[6].getText());
            float height = Float.parseFloat(textFields[7].getText());
            String selectedGender = gender.getValue();

            if (password.equals(rePassword)) {

                DatabaseConnector dbConnector = DatabaseConnector.getInstance();
              boolean registered = dbConnector.registerUser(username, email, fullName, password, age, weight, height, selectedGender);
if (registered) {


    // Example alert for successful registration
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Registration Successful");
    alert.setHeaderText(null);
    alert.setContentText("Registration successful!");
    alert.showAndWait();

    // Close registration window and open login window
LoginApp loginApp =  LoginApp.getInstance();
loginApp.mainLayout.getChildren().clear();
loginApp.mainLayout.getChildren().addAll(loginApp.welcomeLabel,loginApp.loginBox,loginApp.buttonBox);

} }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Password Mismatch");
                alert.setHeaderText(null);
                alert.setContentText("Passwords do not match.");
                alert.showAndWait();
            }
        });

        GridPane.setConstraints(submitButton, 0, labelNames.length + 1, 2, 1);
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/muscleflex/muscleflex/refer.png")));

        ImageView home = new ImageView(image);
     home.setFitWidth(120);
     home.setFitHeight(120);
     HBox box = new HBox();
     box.getChildren().add(home);
     box.setAlignment(Pos.CENTER);

        // Adding elements to the grid
        grid.getChildren().addAll(welcome, submitButton);

        // Root layout

        getChildren().add(grid);
        setPadding(new Insets(10,0,0,0));
        getChildren().add(box);

        setPrefSize(width,Height);


    }


}
