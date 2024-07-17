package com.muscleflex.muscleflex;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutPlanner extends Application {

    private ObservableList<String> workoutList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Workout Planner");

        // Initialize workout list
        workoutList = FXCollections.observableArrayList();

        // Create a GridPane layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Set background color
        grid.setStyle("-fx-background-color: black;");

        // Exercise Name
        Label exerciseLabel = new Label("Exercise:");
        exerciseLabel.setTextFill(Color.WHITE);
        GridPane.setConstraints(exerciseLabel, 0, 0);
        TextField exerciseInput = new TextField();
        GridPane.setConstraints(exerciseInput, 1, 0);

        // Day
        Label dayLabel = new Label("Days:");
        dayLabel.setTextFill(Color.WHITE);
        GridPane.setConstraints(dayLabel, 0, 1);
        VBox daysBox = new VBox(5);
        CheckBox monday = new CheckBox("Monday");
        monday.setTextFill(Color.WHITE);
        CheckBox tuesday = new CheckBox("Tuesday");
        tuesday.setTextFill(Color.WHITE);
        CheckBox wednesday = new CheckBox("Wednesday");
        wednesday.setTextFill(Color.WHITE);
        CheckBox thursday = new CheckBox("Thursday");
        thursday.setTextFill(Color.WHITE);
        CheckBox friday = new CheckBox("Friday");
        friday.setTextFill(Color.WHITE);
        CheckBox saturday = new CheckBox("Saturday");
        saturday.setTextFill(Color.WHITE);
        CheckBox sunday = new CheckBox("Sunday");
        sunday.setTextFill(Color.WHITE);
        daysBox.getChildren().addAll(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
        GridPane.setConstraints(daysBox, 1, 1);

        // Duration
        Label durationLabel = new Label("Duration:");
        durationLabel.setTextFill(Color.WHITE);
        GridPane.setConstraints(durationLabel, 0, 2);
        HBox durationBox = new HBox(5);
        TextField monthsInput = new TextField();
        monthsInput.setPromptText("Months");
        TextField weeksInput = new TextField();
        weeksInput.setPromptText("Weeks");
        durationBox.getChildren().addAll(monthsInput, weeksInput);
        GridPane.setConstraints(durationBox, 1, 2);

        // Target
        Label targetLabel = new Label("Target:");
        targetLabel.setTextFill(Color.WHITE);
        GridPane.setConstraints(targetLabel, 0, 3);
        VBox targetBox = new VBox(5);
        ToggleGroup targetGroup = new ToggleGroup();
        RadioButton slimBody = new RadioButton("Slim Body");
        slimBody.setToggleGroup(targetGroup);
        slimBody.setTextFill(Color.WHITE);
        RadioButton fitBody = new RadioButton("Fit Body");
        fitBody.setToggleGroup(targetGroup);
        fitBody.setTextFill(Color.WHITE);
        targetBox.getChildren().addAll(slimBody, fitBody);
        GridPane.setConstraints(targetBox, 1, 3);

        // Add Button
        Button addButton = new Button("Add Exercise");
        addButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        GridPane.setConstraints(addButton, 1, 4);

        // List View to display workout plan
        ListView<String> workoutListView = new ListView<>(workoutList);
        GridPane.setConstraints(workoutListView, 0, 5, 2, 1);

        // Save Button
        Button saveButton = new Button("Save Plan");
        saveButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        GridPane.setConstraints(saveButton, 0, 6);

        // Load Button
        Button loadButton = new Button("Load Plan");
        loadButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        GridPane.setConstraints(loadButton, 1, 6);

        grid.getChildren().addAll(exerciseLabel, exerciseInput, dayLabel, daysBox, durationLabel, durationBox, targetLabel, targetBox, addButton, workoutListView, saveButton, loadButton);

        addButton.setOnAction(e -> {
            String exercise = exerciseInput.getText();
            List<String> days = new ArrayList<>();
            if (monday.isSelected()) days.add("Monday");
            if (tuesday.isSelected()) days.add("Tuesday");
            if (wednesday.isSelected()) days.add("Wednesday");
            if (thursday.isSelected()) days.add("Thursday");
            if (friday.isSelected()) days.add("Friday");
            if (saturday.isSelected()) days.add("Saturday");
            if (sunday.isSelected()) days.add("Sunday");

            String duration = monthsInput.getText() + " Months, " + weeksInput.getText() + " Weeks";
            String target = slimBody.isSelected() ? "Slim Body" : fitBody.isSelected() ? "Fit Body" : "";

            String workout = "Exercise: " + exercise + ", Days: " + String.join(", ", days) + ", Duration: " + duration + ", Target: " + target;
            workoutList.add(workout);

            exerciseInput.clear();
            monday.setSelected(false);
            tuesday.setSelected(false);
            wednesday.setSelected(false);
            thursday.setSelected(false);
            friday.setSelected(false);
            saturday.setSelected(false);
            sunday.setSelected(false);
            monthsInput.clear();
            weeksInput.clear();
            targetGroup.selectToggle(null);
        });

        saveButton.setOnAction(e -> saveWorkoutPlan());
        loadButton.setOnAction(e -> loadWorkoutPlan());

        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveWorkoutPlan() {
        try (PrintWriter writer = new PrintWriter(new File("workout_plan.txt"))) {
            for (String workout : workoutList) {
                writer.println(workout);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadWorkoutPlan() {
        workoutList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("workout_plan.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                workoutList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

