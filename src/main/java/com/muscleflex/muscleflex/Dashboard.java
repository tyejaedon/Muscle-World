package com.muscleflex.muscleflex;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Dashboard extends HBox {




    private LineChart<Number, Number> exerciseChart;
    XYChart.Series<Number, Number> weight;
    LineChart<Number,Number> weightChart;

    List<Map<String, Object>> userMuscleData;
    List<Map<String, Object>> userExerciseData;
    private ComboBox<String> bodyPartComboBox = new ComboBox<>();
    private Map<String, Map<String, XYChart.Series<Number, Number>>>  exerciseData = new HashMap<>();
    private ComboBox<String>  WorkoutPlans = new ComboBox<>();
    private ComboBox<String> exerciseComboBox = new ComboBox<>();
    DatabaseConnector db= DatabaseConnector.getInstance();
    public Dashboard() {
        updateCombox(null);
        initializeData();

        initializeUI();

    }

    private void initializeData() {
        exerciseChart  =createLineChart("Exercise Chart", "Weight", 31, 0);
        // Initialize ComboBoxes

        // Initalizing chart


        //weight chart demo:
        weight = new XYChart.Series<>();
        weight.setName("Weight & BMI");
        weight.getData().add(new XYChart.Data<>(1, 80));
        weight.getData().add(new XYChart.Data<>(2, 85));
        weight.getData().add(new XYChart.Data<>(3, 87));







    }

    private void initializeUI() {
   weightChart = createLineChart("Weight & BMI","Weight",31,0);
        WorkoutPlans.setOnAction(e->{
            updateCombox(e);
        });


        bodyPartComboBox.setOnAction(e->{
            updateCombox(e);
        });

        exerciseComboBox.setOnAction(e->{
            updateChart();
        });













        // Tab for strength exercises
        Tab strengthTab = new Tab("Strength");
        ScrollPane strengthScrollPane = new ScrollPane(exerciseChart);
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
    GridPane box = new GridPane();
    box.add(plan_label,0,0);
    box.add(WorkoutPlans,1,0);
    box.add(muscleGroup,2,0);
    box.add(bodyPartComboBox,3,0);
    box.add(exercise,4,0);
    box.add(exerciseComboBox,5,0);
    box.setHgap(10);




        return box;
    }


    private void updateCombox(ActionEvent e) {


            exerciseComboBox.getItems().clear();

        //the real stuff
        db.getUserData(db.getLoggedUser());
        List<Map<String, Object>> workout_info = db.getWorkoutPlans();
        if (WorkoutPlans.getItems().isEmpty()){
            workout_info.forEach((k)->{
                Object x =  k.get("plan_name");

                WorkoutPlans.getItems().addAll(x.toString());
            });
        }

        if (WorkoutPlans.getSelectionModel().getSelectedItem() == null){
            WorkoutPlans.setValue(WorkoutPlans.getItems().getFirst());
            userMuscleData = db.getWorkoutData(db.getLoggedUser(),WorkoutPlans.getItems().getFirst());

        } else if(e.equals(WorkoutPlans)) {
            userMuscleData = db.getWorkoutData(db.getLoggedUser(),WorkoutPlans.getSelectionModel().getSelectedItem());
            bodyPartComboBox.getItems().clear();
        }



        if (bodyPartComboBox.getItems().isEmpty()){
            userMuscleData.forEach((k)->{
                Object y = k.get("target_muscle");
                if (!bodyPartComboBox.getItems().contains(y.toString())){
                    bodyPartComboBox.getItems().addAll(y.toString());

                }
            });
            exerciseComboBox.getItems().clear();
        }


        if (bodyPartComboBox.getSelectionModel().getSelectedItem() == null){
            bodyPartComboBox.setValue(bodyPartComboBox.getItems().getFirst());

        }
            userExerciseData = db.getWorkoutData(db.getLoggedUser(),WorkoutPlans.getSelectionModel().getSelectedItem(),bodyPartComboBox.getSelectionModel().getSelectedItem());




        userExerciseData.forEach((k)->{
            Object y = k.get("exercise_name");
            if (!exerciseComboBox.getItems().contains(y.toString())){
                exerciseComboBox.getItems().addAll(y.toString());
                System.out.println(k.get("exercise_name"));
            }



        });

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

    private void updateChart() {
        String selectedPlan = WorkoutPlans.getSelectionModel().getSelectedItem();
        String selectedBodyPart = bodyPartComboBox.getSelectionModel().getSelectedItem();
        String selectedExercise = exerciseComboBox.getSelectionModel().getSelectedItem();
        List<Map<String, Object>> graphData = db.getWorkoutData(db.getLoggedUser(),selectedPlan);
        graphData.forEach(k->{

        });
        if (selectedBodyPart != null && selectedExercise != null) {
            // Clear existing data from the chart
            if (!exerciseChart.getData().isEmpty()){
                exerciseChart.getData().clear();
            }



            XYChart.Series<Number, Number> series = exerciseData.get(selectedBodyPart).get(selectedExercise);

            if (series != null && !series.getData().isEmpty()) {
                exerciseChart.getData().clear();
                // Find the maximum X and minimum Y values in the series
                // Find the maximum X and minimum Y values in the series
                double maxX = series.getData().stream()
                        .mapToDouble(data -> data.getXValue().doubleValue())
                        .max()
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
                NumberAxis xAxis = (NumberAxis) exerciseChart.getXAxis();
                NumberAxis yAxis = (NumberAxis) exerciseChart.getYAxis();
                xAxis.setUpperBound(Math.min(maxX+1,31));
                xAxis.setLowerBound(1);
                yAxis.setLowerBound(minY);
                yAxis.setUpperBound(maxy+5);



                // Add the series to the chart
                exerciseChart.getData().add(series);
                String cssPath = getClass().getResource("/com/muscleflex/muscleflex/gymgraph.css").toExternalForm();
                if (cssPath != null) {
                    exerciseChart.getStylesheets().add(cssPath);
                }

                // Add tooltips to data points
                for (XYChart.Data<Number, Number> dataPoint : series.getData()) {
                    if (dataPoint.getNode() != null) {
                        Tooltip tooltip = new Tooltip("Day: " + dataPoint.getXValue() + "\nWeight: " + dataPoint.getYValue() + " kg");
                        Tooltip.install(dataPoint.getNode(), tooltip);
                    }
                }
            }

        }
    }

    private LineChart<Number, Number> createLineChart(String title, String yAxisLabel, double upperBound, double lowerBound) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Days");
        xAxis.setAutoRanging(false);
        xAxis.setTickUnit(1);
        xAxis.setUpperBound(upperBound);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yAxisLabel);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(lowerBound);

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setPrefWidth(550);
        chart.setCreateSymbols(true);

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
