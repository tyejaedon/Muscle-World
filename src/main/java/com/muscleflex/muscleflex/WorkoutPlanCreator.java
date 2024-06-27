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
import java.time.LocalDate;
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
    DatabaseConnector db = DatabaseConnector.getInstance();
    // Exercise map to store exercises by day
    Map<String, ArrayList<String>> exerciseMap = new HashMap<>();

    public WorkoutPlanCreator() {
        setupUI();
        setupListeners();

    }

    private void setupUI() {
        FontIcon icon = new FontIcon(FontAwesomeSolid.SAVE);
        setSpacing(20);
dayComboBox.getItems().addAll("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday");
        // Initialize combo boxes and date picker
        if(targetMuscleGrp.getValue() == null){
          populateExercises();

        }


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
        saveButton.setOnAction(e ->{ String[][] sample_exercises = savePlan();
        String temp_planname = planNameField.getText();
        String temp_target = targetField.getText();
        LocalDate temp_date =  endDatePicker.getValue();
        Boolean success = false;
        if (temp_planname != null && temp_target != null && temp_date != null) {
            DatabaseConnector dbConnector = DatabaseConnector.getInstance();
            success = dbConnector.addPlan(sample_exercises, temp_planname, temp_target, temp_date);
        }

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success ");
            alert.setContentText(planNameField+" Has been added!");
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failure");
            alert.setContentText("Something went Wrong!");
        } });
    }


    private void populateExercises() {
        String selectedGroup = targetMuscleGrp.getValue();
        HashMap<String, List<String> >muscleExercises  = db.getAllExercises();;
        exerciseField.getItems().clear();
        if (selectedGroup != null) {

            for (String X : muscleExercises.get(selectedGroup)) {
                exerciseField.getItems().addAll(X);


            }
        }
                // Set default value if items are populated
                if (targetMuscleGrp.getItems().isEmpty()) {
                    for (Map.Entry<String, List<String>> X : muscleExercises.entrySet()) {
                        targetMuscleGrp.getItems().addAll(X.getKey());




                }
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

    private String[][] savePlan() {
        String[][] savedexercises = new String[exerciseMap.size()][];
        for (String string : exerciseMap.keySet()) {
            ArrayList<String> value = exerciseMap.get(string);

            for (int i = 0; i < savedexercises.length; i++) {
                savedexercises[i] = new String[value.size()+1];
                savedexercises[i][0] = string;

                for (int j = 1; j < value.size(); j++) {

                    savedexercises[i][j] = value.get(j);

                }
            }
        }
    clearForm();

        return savedexercises;
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
