package com.muscleflex.muscleflex;

import eu.hansolo.tilesfx.tools.DataPoint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Dashboard extends HBox {




    private LineChart<Number, Number> exerciseChart;
    XYChart.Series<Number, Number> weight = new XYChart.Series<>();;
    LineChart<Number,Number> weightChart;

    List<Map<String, Object>> userMuscleData;
    List<Map<String, Object>> userExerciseData;
    List<Map<String, Object>> tooltipdata;
    List<Map<String, Object>> monthData;
    private ComboBox<String> bodyPartComboBox = new ComboBox<>();
    private ComboBox<String> monthComboBox = new ComboBox<>();
    LineChart<Number,Number> exerciseData;
    private ComboBox<String>  WorkoutPlans = new ComboBox<>();
    private ComboBox<String> exerciseComboBox = new ComboBox<>();
    DatabaseConnector db= DatabaseConnector.getInstance();
    public Dashboard() throws Exception {
        updateCombox(null);
        monthComboBox.setOnAction(e->{
            updateChart();
        });
        initializeUI();

    }



    private void initializeUI() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Days");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Weight");
        exerciseChart = createLineChart("hello", "world");
   weightChart = createLineChart("Weight & BMI","Weight");
        WorkoutPlans.setOnAction(e->{
            try {
                updateCombox(e);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });


        bodyPartComboBox.setOnAction(e->{
            try {
                updateCombox(e);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        exerciseComboBox.setOnAction(e->{
            try {
                updateCombox(e);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });














        // Tab for strength exercises
        Tab strengthTab = new Tab("Strength");
        ScrollPane strengthScrollPane = new ScrollPane(updateChart());
        strengthScrollPane.setFitToHeight(false);
        strengthScrollPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muscleflex/muscleflex/scrollpane.css")).toExternalForm());
        strengthTab.setContent(strengthScrollPane);
        strengthTab.setClosable(false);

        //weight tab and BMI
        Tab weightTab = new Tab("Weight & BMI");
        ScrollPane weightScroll = new ScrollPane(updateWeightChart());
        weightScroll.setFitToHeight(false);
        weightScroll.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muscleflex/muscleflex/scrollpane.css")).toExternalForm());
        weightTab.setContent(weightScroll);
        weightTab.setClosable(false);

        // Create and configure the TabPane
        TabPane tabPane = new TabPane();
        tabPane.setTabMinWidth(100);
        tabPane.getTabs().addAll(strengthTab,weightTab,workoutTab());



        // Link to the external stylesheet
        String stylesheet = Objects.requireNonNull(getClass().getResource("/com/muscleflex/muscleflex/dashboard.css")).toExternalForm();
        tabPane.getStylesheets().add(stylesheet);

        // Add components to the HBox
        getChildren().addAll(tabPane);

    }


    public GridPane exerciseSelection() {
        String stylesheet = Objects.requireNonNull(getClass().getResource("/com/muscleflex/muscleflex/dashboard.css")).toExternalForm();

    bodyPartComboBox.getStylesheets().add(stylesheet);
    exerciseComboBox.getStylesheets().add(stylesheet);
    WorkoutPlans.getStylesheets().add(stylesheet);
    monthComboBox.getStylesheets().add(stylesheet);

    Label muscleGroup = new Label("Target Muscle Group: ");
        Label exercise = new Label("Exercise: ");
        muscleGroup.setStyle("-fx-text-fill: white;" +
                "-fx-font-family: cursive;" +
                "-fx-font-style:italic;" +
                "-fx-margin-top:5px;" +
                "-fx-font-size:8px;"
        );

        Label plan_label = new Label("Exercise: ");
        plan_label.setStyle("-fx-text-fill: white;" +
                "-fx-font-family: cursive;" +
                "-fx-font-style:italic;" +
                "-fx-margin-top:5px;" +
                "-fx-font-size:8px;"
        );
        exercise.setStyle("-fx-text-fill: white;" +
                "-fx-font-family: cursive;" +
                "-fx-font-style:italic;" +
                "-fx-font-size:8px");


        Label month_name = new Label("Month: ");
        month_name.setStyle("-fx-text-fill: white;" +
                "-fx-font-family: cursive;" +
                "-fx-font-style:italic;" +
                "-fx-margin-top:5px;" +
                "-fx-font-size:8px;"
        );
    GridPane box = new GridPane();
    box.add(plan_label,0,0);
    box.add(WorkoutPlans,1,0);
    box.add(muscleGroup,2,0);
    box.add(bodyPartComboBox,3,0);
    box.add(exercise,4,0);
    box.add(exerciseComboBox,5,0);
    box.add(month_name,2,1);
        box.add(monthComboBox,3,1);
    box.setHgap(10);




        return box;
    }


    private void updateCombox(ActionEvent e) throws Exception {
        try {


            // Fetch user data
            db.getUserData(db.getLoggedUser());
            List<Map<String, Object>> workoutInfo = db.getWorkoutPlans();

            // Update WorkoutPlans ComboBox
            if (WorkoutPlans.getItems().isEmpty()) {
                workoutInfo.forEach(k -> {
                    String planName = k.get("plan_name").toString();
                    WorkoutPlans.getItems().add(planName);
                });
            }

            // Update selections based on ComboBox triggering the event
            if (WorkoutPlans.getSelectionModel().getSelectedItem() == null) {
                WorkoutPlans.setValue(WorkoutPlans.getItems().get(0));
                userMuscleData = db.getWorkoutData(db.getLoggedUser(), WorkoutPlans.getItems().get(0));
            } else if (e.getSource().equals(WorkoutPlans)) {
                userMuscleData = db.getWorkoutData(db.getLoggedUser(), WorkoutPlans.getSelectionModel().getSelectedItem());
                bodyPartComboBox.getItems().clear();
            } else if (e.getSource().equals(bodyPartComboBox)) {
                exerciseComboBox.getItems().clear();
            } else if (e.getSource().equals(exerciseComboBox)) {
                monthComboBox.getItems().clear();
            }

            // Fetch month data

            // Update bodyPartComboBox
            if (bodyPartComboBox.getItems().isEmpty()) {
                userMuscleData.forEach(k -> {
                    String targetMuscle = k.get("target_muscle").toString();
                    if (!bodyPartComboBox.getItems().contains(targetMuscle)) {
                        bodyPartComboBox.getItems().add(targetMuscle);
                    }
                });
                exerciseComboBox.getItems().clear();
            }

            if (bodyPartComboBox.getSelectionModel().getSelectedItem() == null && !bodyPartComboBox.getItems().isEmpty()) {
                bodyPartComboBox.setValue(bodyPartComboBox.getItems().get(0));
            }

            // Update user exercise data
            userExerciseData = db.getWorkoutData(db.getLoggedUser(), WorkoutPlans.getSelectionModel().getSelectedItem(), bodyPartComboBox.getSelectionModel().getSelectedItem());

            userExerciseData.forEach(k -> {
                String exerciseName = k.get("exercise_name").toString();
                if (!exerciseComboBox.getItems().contains(exerciseName)) {
                    exerciseComboBox.getItems().add(exerciseName);
                }
            });

            if (exerciseComboBox.getSelectionModel().getSelectedItem() == null && !exerciseComboBox.getItems().isEmpty()) {
                exerciseComboBox.setValue(exerciseComboBox.getItems().getFirst());
            }
            monthData = db.getWorkoutData(db.getLoggedUser(), WorkoutPlans.getSelectionModel().getSelectedItem(), bodyPartComboBox.getSelectionModel().getSelectedItem(), exerciseComboBox.getSelectionModel().getSelectedItem());

            // Update monthComboBox
            if (monthComboBox.getItems().isEmpty()) {
                monthData.forEach(k -> {
                    Object date = k.get("exercise_date");
                    System.out.println(date.toString());
                    LocalDate localDate = LocalDate.parse(date.toString(), DateTimeFormatter.ISO_DATE);
                    String monthString = localDate.getMonth().toString();

                    if (!monthComboBox.getItems().contains(monthString)) {
                        monthComboBox.getItems().add(monthString);
                    }
                });
            }

            if (monthComboBox.getSelectionModel().getSelectedItem() == null && !monthComboBox.getItems().isEmpty()) {
                monthComboBox.setValue(monthComboBox.getItems().getFirst());

            }
        } catch (Exception i) {
            throw new Exception(i);
        }
    }




private  LineChart<Number,Number> updateWeightChart(){

        double maxX = weight.getData().stream()
                .mapToDouble(data -> data.getXValue().doubleValue())
                .max()
                .orElse(0);
    double minY = weight.getData().stream()
            .mapToDouble(data -> data.getYValue().doubleValue())
            .min()
            .orElse(0);
    double maxy = weight.getData().stream()
            .mapToDouble(data -> data.getYValue().doubleValue())
            .max()
            .orElse(0);
    NumberAxis xAxis = (NumberAxis) weightChart.getXAxis();
    NumberAxis yAxis = (NumberAxis) weightChart.getYAxis();
    xAxis.setUpperBound(Math.min(maxX+1,31));
    xAxis.setLowerBound(1);
    yAxis.setLowerBound(minY);
    yAxis.setUpperBound(maxy+5);



    // Add the series to the chart
   weightChart.getData().add(weight);
    String cssPath = getClass().getResource("/com/muscleflex/muscleflex/gymgraph.css").toExternalForm();
    weightChart.getStylesheets().add(cssPath);
    return weightChart;
    }
    String dateMonth;


    private LineChart<Number,Number> updateChart() {
        exerciseChart.getData().clear();
        String selectedPlan = WorkoutPlans.getSelectionModel().getSelectedItem();
        String selectedBodyPart = bodyPartComboBox.getSelectionModel().getSelectedItem();
        String selectedExercise = exerciseComboBox.getSelectionModel().getSelectedItem();
        String selectedMonth = monthComboBox.getSelectionModel().getSelectedItem();
        List<Map<String, Object>> graphData = db.getWorkoutData(db.getLoggedUser(), selectedPlan, selectedBodyPart, selectedExercise);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();


        // Clear existing chart data and potentially reset axes


        series.setName(selectedExercise);
        graphData.forEach(k -> {
            int y = (int) k.get("weight");

            Object date = k.get("exercise_date");
            //part for getting the date
            //

            // Parse the date string into a LocalDate instance
            LocalDate localDate = LocalDate.parse(date.toString(), DateTimeFormatter.ISO_DATE);

            // Get the DayOfWeek enum value (MONDAY, TUESDAY, ..., SUNDAY)
            dateMonth = localDate.getMonth().toString();
            if (Objects.equals(dateMonth, selectedMonth)) {
                int dayOfWeek = localDate.getDayOfMonth();
                series.getData().addAll(new XYChart.Data<>(dayOfWeek, y));
            }

            System.out.println(y);


        });


        // Clear existing data from the chart


        if (series != null && !series.getData().isEmpty()) {


            // Find the maximum X and minimum Y values in the series
            // Find the maximum X and minimum Y values in the series
            double maxX = series.getData().stream()
                    .mapToDouble(data -> data.getXValue().doubleValue())
                    .max()
                    .orElse(0);
            double minX = series.getData().stream()
                    .mapToDouble(data -> data.getXValue().doubleValue())
                    .min()
                    .orElse(0);
            double minY = series.getData().stream()
                    .mapToDouble(data -> data.getYValue().doubleValue())
                    .min()
                    .orElse(0);
            double maxy = series.getData().stream()
                    .mapToDouble(data -> data.getYValue().doubleValue())
                    .max()
                    .orElse(0);

            // Update the bounds of the existing chart's axes

            System.out.println(maxX);


            NumberAxis xAxis = (NumberAxis) exerciseChart.getXAxis();
            NumberAxis yAxis = (NumberAxis) exerciseChart.getYAxis();
            xAxis.setUpperBound(Math.min(maxX + 1, 31));
            xAxis.setLowerBound(minX);
            yAxis.setLowerBound(minY);
            yAxis.setUpperBound(maxy + 5);
            yAxis.setTickUnit(2);


            // Add the series to the chart
            exerciseChart.getData().add(series);
            exerciseChart.setTitle(selectedExercise);
            exerciseChart.setPrefWidth(550);
            exerciseChart.setCreateSymbols(true);
            String cssPath = getClass().getResource("/com/muscleflex/muscleflex/gymgraph.css").toExternalForm();
            if (cssPath != null) {
                exerciseChart.getStylesheets().add(cssPath);
            }

            // Add tooltips to data points
            for (XYChart.Data<Number, Number> dataPoint : series.getData()) {
                if (dataPoint.getNode() != null) {
                    // Update user exercise data
                    tooltipdata = db.getWorkoutData(db.getLoggedUser(), WorkoutPlans.getSelectionModel().getSelectedItem(), bodyPartComboBox.getSelectionModel().getSelectedItem(), exerciseComboBox.getSelectionModel().getSelectedItem());

                    tooltipdata.forEach(k -> {
                        String exerciseName = k.get("reps").toString();

                    });
                    Tooltip tooltip = new Tooltip("Day: " + dataPoint.getXValue() + "\nWeight: " + dataPoint.getYValue() + " kg");
                    Tooltip.install(dataPoint.getNode(), tooltip);
                }

            }
            ArrayList<String> data = new ArrayList<>();
            // Add tooltips to data points
            for (int i = 0; i <series.getData().size(); i++) {


                if (series.getData().get(i) != null) {
                    // Update user exercise data
                    tooltipdata = db.getWorkoutData(db.getLoggedUser(), WorkoutPlans.getSelectionModel().getSelectedItem(), bodyPartComboBox.getSelectionModel().getSelectedItem(), exerciseComboBox.getSelectionModel().getSelectedItem());

                    tooltipdata.forEach(k -> {
                  data.add(k.get("reps").toString());

                    });
                    Tooltip tooltip = new Tooltip("Day: " + series.getData().get(i).getXValue() + "\nWeight: " + series.getData().get(i).getYValue() + " kg"+ "\nReps: "+data.get(i));
                    Tooltip.install(series.getData().get(i).getNode(), tooltip);
                }

            }



        }
        return exerciseChart;
    }
    private LineChart<Number, Number> createLineChart(String title, String yAxisLabel) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Days");
        xAxis.setAutoRanging(false);
        xAxis.setTickUnit(1);


        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yAxisLabel);
        yAxis.setAutoRanging(false);
     ;

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);


        return chart;
    }

public  Tab workoutTab(){



    // Create the workout tab
    Tab workoutTab = new Tab("Workout");
    // Create TableView for displaying previous sets
    TableView<WorkoutSet> tableView = new TableView<>();

    // Create columns
    TableColumn<WorkoutSet, String> day = new TableColumn<>("Day");
    day.setCellValueFactory(cellData -> cellData.getValue().workoutNameProperty());

    TableColumn<WorkoutSet, Integer> durationColumn = new TableColumn<>("Duration (seconds)");
    durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty().asObject());

    TableColumn<WorkoutSet, Integer> setsColumn = new TableColumn<>("Total Sets");
    setsColumn.setCellValueFactory(cellData -> cellData.getValue().setsProperty().asObject());

    TableColumn<WorkoutSet, Integer> restPeriodColumn = new TableColumn<>("Rest Period (seconds)");
    restPeriodColumn.setCellValueFactory(cellData -> cellData.getValue().restPeriodProperty().asObject());

    // Add columns to the table
    tableView.getColumns().addAll(day, durationColumn, setsColumn, restPeriodColumn);

    // Create sample data
    ObservableList<WorkoutSet> data = FXCollections.observableArrayList(
            new WorkoutSet("June 21", 60, 3, 30),
            new WorkoutSet("June 22", 90, 4, 45),
            new WorkoutSet("June 23", 45, 5, 15)
    );

    // Set data to the table
    tableView.setItems(data);
    workoutTab.setContent(tableView);
    workoutTab.setClosable(false);



    return workoutTab;
}

}
