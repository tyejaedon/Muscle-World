package com.muscleflex.muscleflex;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Homepage extends VBox {

    GridPane mainPanel = new GridPane();
    VBox tasksPanel = new VBox(); // Changed to VBox for a list-like behavior
    VBox changingPanel = new VBox();
    Button workoutButton = new Button("Workout");
    Button userProfileButton = new Button("User Profile");
    Button dashboardButton = new Button("Dashboard");
    Button createPlan = new Button();
    Button muscle = new Button();
    Button startWorkout = new Button();
    TextField taskInput = new TextField();
    Button addTaskButton = new Button("Add Task");
    ListView<String> taskListView = new ListView<>(); // ListView for dynamic tasks
    ObservableList<String> tasksList = FXCollections.observableArrayList(); // Observable list for tasks



    public Homepage(int width,int height) {

// config for create plan muscle view and start workout
        HBox homebox = new HBox();
        FontIcon plan = new FontIcon(FontAwesomeSolid.BOOK);
        plan.setIconSize(40);
        plan.setIconColor(Color.WHITE);
        createPlan.setGraphic(plan);
        createPlan.setBackground(Background.fill(Color.BLACK));
        createPlan.setAlignment(Pos.CENTER);

        createPlan.setPrefSize(150,80);
        homebox.getChildren().add(createPlan);
        homebox.setSpacing(2);


        muscle.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/muscleflex/muscleflex/muscular.png")))));
        muscle.setAlignment(Pos.CENTER);
        muscle.setBackground(Background.fill(Color.BLACK));
        muscle.setPrefSize(150,80);
        homebox.getChildren().addAll(muscle);

        FontIcon letsgo = new FontIcon(FontAwesomeSolid.DUMBBELL);
        letsgo.setIconSize(40);
        letsgo.setIconColor(Color.WHITE);
        startWorkout.setGraphic(letsgo);
        startWorkout.setAlignment(Pos.CENTER);
        startWorkout.setBackground(Background.fill(Color.BLACK));
        startWorkout.setPrefSize(150,80);
        homebox.getChildren().addAll(startWorkout);
        homebox.setAlignment(Pos.CENTER);
        homebox.setSpacing(10);
        homebox.setPadding(new Insets(70,0,10,0));
        Line line = new Line();
        line.setStartX(0);
        line.setEndX(width-100);

        line.setStroke(Color.GOLD);
        line.setStrokeWidth(5);
        // Top panel configuration
        HBox topPanel = new HBox();
        topPanel.setPadding(new Insets(10));
        topPanel.setSpacing(10);
        topPanel.setStyle("-fx-background-color: Black; -fx-text-fill: gold; -fx-font-weight: bold;"); // Deep red background, gold text

        for (Button button : new Button[]{workoutButton, userProfileButton, dashboardButton}) {
            button.setStyle("-fx-background-color: gold; -fx-text-fill: black; -fx-font-weight: bold;");
            button.setPrefSize((double) width /2, 30);
            topPanel.getChildren().add(button);
            topPanel.setAlignment(Pos.CENTER);
        }

      /*  // Tasks panel configuration with MuscleView
        tasksPanel.setPadding(new Insets(15));
        tasksPanel.setSpacing(10);

*/
        // Changing panel configuration
        changingPanel.setPadding(new Insets(2));
        changingPanel.setSpacing(1);


        changingPanel.setStyle("-fx-background-radius: 25px;-fx-border-style:solid;-fx-background-color:white;");
        changingPanel.setPrefSize((double) width /2, (double) height /2);
        changingPanel.setPadding(new Insets(10));

        // Allow changing panel to grow vertically


        changingPanel.getChildren().add(new WorkoutPlanCreator());
        changingPanel.setAlignment(Pos.CENTER_LEFT);

        createPlan.setOnAction(e -> {
            WorkoutPlanCreator exCreator = new WorkoutPlanCreator();
            changingPanel.getChildren().clear();
            changingPanel.setBackground(Background.fill(Color.GREY));
            changingPanel.setStyle("-fx-background-radius: 25px;-fx-border-style:solid;-fx-background-color:white;");

            changingPanel.getChildren().add(exCreator);
        });
        workoutButton.setOnAction(e->{
           homebox.getChildren().clear();
           homebox.getChildren().addAll(createPlan,muscle,startWorkout);
            changingPanel.getChildren().clear();
            changingPanel.setStyle("-fx-background-color:white; -fx-background-radius:25px;-fx-border-style:solid;");

            changingPanel.getChildren().add(new WorkoutPlanCreator());
        });
        dashboardButton.setOnAction(e -> {
            changingPanel.getChildren().clear();
            homebox.getChildren().clear();
            Dashboard dashboard = null;
            try {
                dashboard = new Dashboard();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }


            homebox.getChildren().add(dashboard.exerciseSelection());
            changingPanel.setStyle("-fx-background-color:white; -fx-background-radius:25px;-fx-border-style:solid;");
            changingPanel.getChildren().add(dashboard);
        });
        muscle.setOnAction(e -> {
            MuscleView muscleView = new MuscleView((double) width /2, (double) height /2);



            changingPanel.getChildren().clear();
            changingPanel.setStyle("-fx-background-radius: 25px;-fx-border-style:solid;-fx-background-color:white;");
            changingPanel.getChildren().add(muscleView);
        });


        // Main panel configuration












        mainPanel.add(changingPanel, 0, 0);
        mainPanel.add(homebox, 0, 2);
        mainPanel.add(line,0,3);
        mainPanel.setAlignment(Pos.CENTER);
        // Combine panels into a VBox
        VBox root = new VBox();
        root.getChildren().addAll(topPanel, mainPanel);
        root.setBackground(Background.fill(Color.BLACK));
getChildren().add(root);
setPrefSize(width,height);
setAlignment(Pos.CENTER);

    }
}
