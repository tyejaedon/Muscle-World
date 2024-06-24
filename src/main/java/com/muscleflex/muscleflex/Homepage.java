package com.muscleflex.muscleflex;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.shape.Line;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Homepage extends Application {

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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gym Application Homepage");

        // Top panel configuration
        HBox topPanel = new HBox();
        topPanel.setPadding(new Insets(10));
        topPanel.setSpacing(10);
        topPanel.setStyle("-fx-background-color: Black; -fx-text-fill: gold; -fx-font-weight: bold;"); // Deep red background, gold text

        for (Button button : new Button[]{workoutButton, userProfileButton, dashboardButton}) {
            button.setStyle("-fx-background-color: gold; -fx-text-fill: black; -fx-font-weight: bold;");
            button.setPrefSize(150, 30);
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


        changingPanel.setStyle("-fx-background-radius: 25px;-fx-border-style:solid;-fx-background-color:grey;");
        changingPanel.setPrefSize(480,500);
        // Allow changing panel to grow vertically
        VBox.setVgrow(changingPanel,Priority.ALWAYS);

        changingPanel.getChildren().add(new WorkoutPlanCreator());
        createPlan.setOnAction(e -> {
            WorkoutPlanCreator exCreator = new WorkoutPlanCreator();
            changingPanel.getChildren().clear();
            changingPanel.setBackground(Background.fill(Color.GREY));
            changingPanel.setStyle("-fx-background-radius: 25px;-fx-border-style:solid;-fx-background-color:grey;");

            changingPanel.getChildren().add(exCreator);
        });
        muscle.setOnAction(e -> {
            MuscleView muscleView = new MuscleView();



            changingPanel.getChildren().clear();
            changingPanel.setBackground(Background.fill(Color.BLACK));
            changingPanel.getChildren().add(muscleView);
        });


        // Main panel configuration
        mainPanel.setPadding(new Insets(20));
        mainPanel.setHgap(20);




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

        FontIcon pump = new FontIcon(FontAwesomeSolid.DUMBBELL);
        pump.setIconSize(40);
        pump.setIconColor(Color.WHITE);
        muscle.setGraphic(pump);
        muscle.setAlignment(Pos.CENTER);
        muscle.setBackground(Background.fill(Color.BLACK));
        muscle.setPrefSize(150,80);
        homebox.getChildren().addAll(muscle);


        FontIcon letsgo = new FontIcon(FontAwesomeSolid.RUNNING);
        letsgo.setIconSize(40);
        letsgo.setIconColor(Color.WHITE);
        startWorkout.setGraphic(letsgo);
        startWorkout.setAlignment(Pos.CENTER);
        startWorkout.setBackground(Background.fill(Color.BLACK));
        startWorkout.setPrefSize(150,80);
        homebox.getChildren().addAll(startWorkout);
        homebox.setAlignment(Pos.CENTER);
        homebox.setSpacing(10);
        homebox.setPadding(new Insets(70,0,0,0));
        Line line = new Line();
        line.setStartX(0);
        line.setEndX(448);

        line.setStroke(Color.GOLD);
        line.setStrokeWidth(5);





        mainPanel.add(changingPanel, 0, 0);
        mainPanel.add(homebox, 0, 2);
        mainPanel.add(line,0,3);
        // Combine panels into a VBox
        VBox root = new VBox();
        root.getChildren().addAll(topPanel, mainPanel);
        root.setBackground(Background.fill(Color.BLACK));


        Scene scene = new Scene(root,498, 1080);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}
