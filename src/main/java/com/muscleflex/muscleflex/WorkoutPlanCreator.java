package com.muscleflex.muscleflex;

import javafx.application.Application;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.util.*;


public class WorkoutPlanCreator extends VBox {

    // Components for the form
    ComboBox<String> exerciseField = new ComboBox<>();
    TextArea exerciseList = new TextArea();
    ComboBox<String> dayComboBox = new ComboBox<>();
    ComboBox<String> targetMuscleGrp = new ComboBox<>();
    DatePicker endDatePicker = new DatePicker();
    TextField targetField = new TextField();
    TextField planNameField = new TextField();
    Button addButton = new Button("Add Exercise");
    Button saveButton = new Button("Save Plan");

    // Exercise map to store exercises by day
    Map<String, ArrayList<String>> exerciseMap = new HashMap<>();

    public WorkoutPlanCreator() {
        setupUI();
        setupListeners();
    }

    private void setupUI() {
        FontIcon icon = new FontIcon(FontAwesomeSolid.SAVE);
        setSpacing(20);


        // Initialize combo boxes and date picker
        exerciseField.getItems().addAll("Squats", "Lunges", "Leg Press", "Deadlifts", "Leg Curls", "Calf Raises");
        dayComboBox.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        targetMuscleGrp.getItems().addAll("Legs", "Arms", "Chest", "Back", "Full Body", "Push", "Pull", "Abs");

        // Main form setup
        VBox formPanel = new VBox(15);
        formPanel.setPadding(new Insets(20));
        formPanel.setStyle(" -fx-border-color: #d3d3d3; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;;");

        // Title label
        Label titleLabel = new Label("Create Your Workout Plan");
        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333;");

        // Form elements
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        gridPane.add(new Label("Exercise:"), 0, 0);
        gridPane.add(exerciseField, 1, 0);

        gridPane.add(new Label("Day:"), 0, 1);
        gridPane.add(dayComboBox, 1, 1);

        gridPane.add(new Label("Muscle Group:"), 0, 2);
        gridPane.add(targetMuscleGrp, 1, 2);

        gridPane.add(new Label("End Date:"), 0, 3);
        gridPane.add(endDatePicker, 1, 3);

        gridPane.add(new Label("Target:"), 0, 4);
        gridPane.add(targetField, 1, 4);

        gridPane.add(new Label("Plan Name:"), 0, 5);
        gridPane.add(planNameField, 1, 5);

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        addButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-weight: bold;");
        saveButton.setGraphic(icon);
        saveButton.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-font-weight: bold;");
        buttonBox.getChildren().addAll(addButton, saveButton);

        // Exercise list setup
        VBox exerciseListPanel = new VBox(10);
        exerciseListPanel.setPadding(new Insets(10));
        exerciseList.setEditable(false);
        exerciseList.setPrefHeight(150);
        exerciseList.setStyle("-fx-control-inner-background: #ffffff; -fx-border-color: #d3d3d3;");
        Label exerciseListLabel = new Label("Exercises List:");
        exerciseListLabel.setFont(new Font("Arial", 16));
        exerciseListLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333;");
        exerciseListPanel.getChildren().addAll(exerciseListLabel, exerciseList);

        // Adding all components to formPanel
        formPanel.getChildren().addAll(titleLabel, gridPane, buttonBox, exerciseListPanel);
        formPanel.setBackground(Background.fill(Color.DARKGRAY));

        // Adding formPanel to this VBox (WorkoutPlanCreator)
        getChildren().add(formPanel);


    }

    private void setupListeners() {
        // Populate exercise field based on selected muscle group
        targetMuscleGrp.setOnAction(e -> populateExercises());

        // Add exercise button action
        addButton.setOnAction(e -> addExercise());

        // Save plan button action
        saveButton.setOnAction(e -> savePlan());
    }

    private void populateExercises() {
        String selectedGroup = targetMuscleGrp.getValue();
        switch (selectedGroup) {
            case "Legs":
                exerciseField.getItems().setAll("Squats", "Lunges", "Leg Press", "Deadlifts", "Leg Curls", "Calf Raises");
                break;
            case "Chest":
                exerciseField.getItems().setAll("Bench Press", "Incline Dumbbell Press", "Push-Ups", "Chest Flys", "Cable Crossovers", "Dips");
                break;
            case "Back":
                exerciseField.getItems().setAll("Pull-Ups", "Bent Over Rows", "Lat Pulldowns", "Deadlifts", "T-Bar Rows", "Seated Cable Rows");
                break;
            case "Arms":
                exerciseField.getItems().setAll("Bicep Curls", "Tricep Dips", "Hammer Curls", "Tricep Extensions", "Concentration Curls", "Skull Crushers");
                break;
            case "Push":
                exerciseField.getItems().setAll("Bench Press", "Overhead Press", "Push-Ups", "Dips", "Incline Press", "Tricep Pushdowns");
                break;
            case "Pull":
                exerciseField.getItems().setAll("Pull-Ups", "Bent Over Rows", "Lat Pulldowns", "Face Pulls", "Deadlifts", "Dumbbell Rows");
                break;
            case "Abs":
                exerciseField.getItems().setAll("Crunches", "Leg Raises", "Russian Twists", "Planks", "Bicycle Crunches", "Mountain Climbers");
                break;
            default:
                exerciseField.getItems().clear();
                break;
        }
    }

    private void addExercise() {
        String exercise = exerciseField.getValue();
        String day = dayComboBox.getValue();

        if (exercise != null && !exercise.isEmpty() && day != null && !day.isEmpty()) {
            if (!exerciseMap.containsKey(day)) {
                exerciseMap.put(day, new ArrayList<>());
            }
            exerciseMap.get(day).add(exercise);
            updateExerciseList();
        }
    }

    private void updateExerciseList() {
        StringBuilder sb = new StringBuilder();
        for (String day : exerciseMap.keySet()) {
            sb.append(day).append(": ").append(exerciseMap.get(day)).append("\n");
        }
        exerciseList.setText(sb.toString());
    }

    private void savePlan() {
        String[][] savedExercises = new String[exerciseMap.size()][];
        int index = 0;
        for (String day : exerciseMap.keySet()) {
            ArrayList<String> exercises = exerciseMap.get(day);
            savedExercises[index] = new String[exercises.size() + 1];
            savedExercises[index][0] = day;
            for (int i = 0; i < exercises.size(); i++) {
                savedExercises[index][i + 1] = exercises.get(i);
            }
            index++;
        }

        String planName = planNameField.getText();
        String target = targetField.getText();
        Date endDate = java.sql.Date.valueOf(endDatePicker.getValue());

        if (planName.isEmpty() || target.isEmpty() || endDate == null || savedExercises.length == 0) {
            // Validation failed
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields and add at least one exercise.");
            alert.showAndWait();
        } else {
            // Save plan logic (database connection would typically be here)
            // For demonstration, just printing details
            System.out.println("Plan Name: " + planName);
            System.out.println("Target: " + target);
            System.out.println("End Date: " + new SimpleDateFormat("dd-MM-yyyy").format(endDate));
            System.out.println("Exercises:");
            for (String[] dayExercises : savedExercises) {
                System.out.println(dayExercises[0] + ": " + Arrays.toString(Arrays.copyOfRange(dayExercises, 1, dayExercises.length)));
            }

            // Clear form after saving
            clearForm();
        }
    }

    private void clearForm() {
        exerciseField.getSelectionModel().clearSelection();
        dayComboBox.getSelectionModel().clearSelection();
        targetMuscleGrp.getSelectionModel().clearSelection();
        endDatePicker.getEditor().clear();
        targetField.clear();
        planNameField.clear();
        exerciseMap.clear();
        updateExerciseList();
    }


}
