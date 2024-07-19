package com.muscleflex.muscleflex;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;

public class WorkoutStarter extends VBox {

    private ObservableList<String> workoutList;
    private List<Integer> workoutTimes;
    private Label timerLabel;
    private int currentExerciseIndex = 0;
    private boolean isPaused = false;
    private boolean isRunning = false;
    private Task<Void> currentTask;
    private int remainingTime;

  

   
    public void start() {
      

        // Initialize workout list
        workoutList = FXCollections.observableArrayList();
        workoutTimes = new ArrayList<>();

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

        // Sets
        Label setsLabel = new Label("Sets:");
        setsLabel.setTextFill(Color.WHITE);
        GridPane.setConstraints(setsLabel, 0, 1);
        TextField setsInput = new TextField();
        GridPane.setConstraints(setsInput, 1, 1);

        // Reps
        Label repsLabel = new Label("Reps:");
        repsLabel.setTextFill(Color.WHITE);
        GridPane.setConstraints(repsLabel, 0, 2);
        TextField repsInput = new TextField();
        GridPane.setConstraints(repsInput, 1, 2);

        // Time
        Label timeLabel = new Label("Time (minutes:seconds):");
        timeLabel.setTextFill(Color.WHITE);
        GridPane.setConstraints(timeLabel, 0, 3);
        TextField timeInput = new TextField();
        GridPane.setConstraints(timeInput, 1, 3);

        // Add Button
        Button addButton = new Button("Add Exercise");
        addButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        GridPane.setConstraints(addButton, 1, 4);

        // List View to display workout plan
        ListView<String> workoutListView = new ListView<>(workoutList);
        GridPane.setConstraints(workoutListView, 0, 5, 2, 1);

        // Timer Label
        timerLabel = new Label("Timer: 00:00");
        timerLabel.setTextFill(Color.WHITE);
        GridPane.setConstraints(timerLabel, 0, 6);

        // Start Workout Button
        Button startButton = new Button("Start Workout");
        startButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        GridPane.setConstraints(startButton, 1, 6);

        // Pause Workout Button
        Button pauseButton = new Button("Pause Workout");
        pauseButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        GridPane.setConstraints(pauseButton, 0, 7);

        // Resume Workout Button
        Button resumeButton = new Button("Resume Workout");
        resumeButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        GridPane.setConstraints(resumeButton, 1, 7);

        grid.getChildren().addAll(exerciseLabel, exerciseInput, setsLabel, setsInput, repsLabel, repsInput, timeLabel, timeInput, addButton, workoutListView, timerLabel, startButton, pauseButton, resumeButton);

        addButton.setOnAction(e -> {
            String exercise = exerciseInput.getText();
            String sets = setsInput.getText();
            String reps = repsInput.getText();
            String time = timeInput.getText();

            String[] timeParts = time.split(":");
            int minutes = Integer.parseInt(timeParts[0]);
            int seconds = Integer.parseInt(timeParts[1]);
            int totalSeconds = minutes * 60 + seconds;

            String workout = "Exercise: " + exercise + ", Sets: " + sets + ", Reps: " + reps + ", Time: " + time + " minutes:seconds";
            workoutList.add(workout);
            workoutTimes.add(totalSeconds);

            exerciseInput.clear();
            setsInput.clear();
            repsInput.clear();
            timeInput.clear();
        });

        startButton.setOnAction(e -> {
            if (!isRunning) {
                startWorkout();
                isRunning = true;
            } else if (isPaused) {
                resumeWorkout();
                isPaused = false;
            }
        });

        pauseButton.setOnAction(e -> {
            if (isRunning && !isPaused) {
                pauseWorkout();
                isPaused = true;
            }
        });

        resumeButton.setOnAction(e -> {
            if (isPaused) {
                resumeWorkout();
                isPaused = false;
            }
        });

      getChildren().add(grid);
    }

    private void startWorkout() {
        if (workoutTimes.isEmpty()) {
            timerLabel.setText("No exercises to start.");
            return;
        }

        currentExerciseIndex = 0;
        runTimer(workoutTimes.get(currentExerciseIndex));
    }

    private void runTimer(int timeInSeconds) {
        remainingTime = timeInSeconds;
        currentTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (int i = remainingTime; i >= 0; i--) {
                    if (isCancelled()) break;
                    int minutes = i / 60;
                    int seconds = i % 60;
                    updateMessage(String.format("Timer: %02d:%02d", minutes, seconds));
                    Thread.sleep(1000);
                    remainingTime = i;
                }
                return null;
            }
        };

        currentTask.messageProperty().addListener((obs, oldMessage, newMessage) -> timerLabel.setText(newMessage));

        currentTask.setOnSucceeded(e -> {
            currentExerciseIndex++;
            if (currentExerciseIndex < workoutTimes.size()) {
                runTimer(workoutTimes.get(currentExerciseIndex));
            } else {
                timerLabel.setText("Workout complete!");
                isRunning = false;
            }
        });

        Thread currentThread = new Thread(currentTask);
        currentThread.setDaemon(true);
        currentThread.start();
    }

    private void pauseWorkout() {
        if (currentTask != null) {
            currentTask.cancel();
        }
    }

    private void resumeWorkout() {
        runTimer(remainingTime);
    }
}


