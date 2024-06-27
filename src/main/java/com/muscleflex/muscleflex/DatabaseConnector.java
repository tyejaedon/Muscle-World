package com.muscleflex.muscleflex;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseConnector {
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;

    private static final String URL = "jdbc:mysql://localhost:3306/muscle-world";
    private static final String USER = "root";
    private static final String PASSWORD = "12345678";
    private String[] user_logged = new String[1];
    private static DatabaseConnector instance;

    private DatabaseConnector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    private void connect() throws SQLException {
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void setLoggedUser(String username) {
        this.user_logged[0] = username;
    }

    public String getLoggedUser() {
        return user_logged[0];
    }

    public boolean registerUser(String username, String email, String fullname, String password, int age, float weight, float height, String gender) {
        String sql = "INSERT INTO users (username, email, full_name, password, age, weight, height, gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, fullname);
            preparedStatement.setString(4, password);
            preparedStatement.setInt(5, age);
            preparedStatement.setFloat(6, weight);
            preparedStatement.setFloat(7, height);
            preparedStatement.setString(8, gender);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean loginUser(String username, String password) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (password.equals(storedPassword)) {
                    setLoggedUser(username);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addPlan(String[][] Workouts, String planName, String goal, LocalDate date) {
        boolean success = false;
        try {
            // Insert into workout_plans
            String sql1 = "INSERT INTO workout_plans (username, plan_name, target, end_date) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pStmt1 = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS)) {
                java.sql.Date sqlDate =  java.sql.Date.valueOf((date));
                pStmt1.setString(1, getLoggedUser());
                pStmt1.setString(2, planName);
                pStmt1.setString(3, goal);
                pStmt1.setDate(4, sqlDate);
                pStmt1.executeUpdate();

                ResultSet generatedKeys = pStmt1.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int planId = generatedKeys.getInt(1);

                    // Insert into exercises
                    String sql2 = "INSERT INTO exercises (exercise_name, muscle_group, plan_id) VALUES (?, ?, ?)";
                    try (PreparedStatement pStmt2 = conn.prepareStatement(sql2)) {
                        for (String[] workout : Workouts) {
                            for (int j = 1; j < workout.length; j++) {
                                String exercise = workout[j];
                                if (exercise != null && !exercise.trim().isEmpty()) {
                                    pStmt2.setString(1, exercise);
                                    pStmt2.setString(2, workout[0]);
                                    pStmt2.setInt(3, planId);
                                    pStmt2.executeUpdate();
                                }
                            }
                        }
                    }
                    success = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public List<String> getUserWorkoutPlans() {
        List<String> plans = new ArrayList<>();
        String sql = "SELECT plan_name FROM workout_plans WHERE username = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, user_logged[0]);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                plans.add(rs.getString("plan_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plans;
    }

    public List<String> getExercisesForPlan(String plan_name) {
        List<String> exercises = new ArrayList<>();
        String sql = "SELECT exercise_name FROM exercises WHERE plan_name = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, plan_name);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                exercises.add(rs.getString("exercise_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exercises;
    }

    public List<String[]> getExerciseData(String exerciseName) {
        List<String[]> data = new ArrayList<>();
        String sql = "SELECT day_of_week, weight, repetitions, sets, rest_period, duration FROM exercise_data WHERE exerciseName = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, exerciseName);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String[] record = new String[6];
                record[0] = rs.getString("day_of_week");
                record[1] = rs.getString("weight");
                record[2] = rs.getString("repetitions");
                record[3] = rs.getString("sets");
                record[4] = rs.getString("rest_period");
                record[5] = rs.getString("duration");
                data.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public boolean addExerciseData(int exerciseId, String dayOfWeek, double weight, int repetitions, int sets, Time restPeriod, Time duration) {
        String sql = "INSERT INTO exercise_data (exercise_id, day_of_week, weight, repetitions, sets, rest_period, duration) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, exerciseId);
            preparedStatement.setString(2, dayOfWeek);
            preparedStatement.setDouble(3, weight);
            preparedStatement.setInt(4, repetitions);
            preparedStatement.setInt(5, sets);
            preparedStatement.setTime(6, restPeriod);
            preparedStatement.setTime(7, duration);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    Map<String, Object> userData;
    List<Map<String, Object>> workoutPlans;
    public Map<String, Object> getUserData() {
        return userData;
    }

    public List<Map<String, Object>> getWorkoutPlans() {
        return workoutPlans;
    }

    // Method to retrieve user data and related information
    public void  getUserData(String username) {
        userData = new HashMap<>();

        try {
            // Query to retrieve user details and workout plans
            String query = "SELECT u.*, wp.plan_id, wp.plan_name, wp.target, wp.end_date " +
                    "FROM users u " +
                    "LEFT JOIN workout_plans wp ON u.username = wp.username " +
                    "WHERE u.username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            // Check if user exists
            if (rs.next()) {
                // Extract user information
                userData.put("user_id", rs.getInt("user_id"));
                userData.put("username", rs.getString("username"));
                userData.put("email", rs.getString("email"));
                userData.put("full_name", rs.getString("full_name"));
                userData.put("age", rs.getInt("age"));
                userData.put("weight", rs.getFloat("weight"));
                userData.put("height", rs.getFloat("height"));
                userData.put("gender", rs.getString("gender"));
                userData.put("registration_date", rs.getTimestamp("registration_date").toLocalDateTime());

                // Initialize list to hold workout plans
             workoutPlans = new ArrayList<>();


                // Loop through result set to fetch workout plans
                do {
                    Map<String, Object> planData = new HashMap<>();
                    planData.put("plan_id", rs.getInt("plan_id"));
                    planData.put("plan_name", rs.getString("plan_name"));
                    planData.put("target", rs.getString("target"));
                    planData.put("end_date", rs.getDate("end_date").toLocalDate());
                    workoutPlans.add(planData);
                } while (rs.next());

                // Add workout plans to userData map

            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    // Method to retrieve exercise data for a user
    public List<Map<String, Object>> getWorkoutData(String username, String plan_name) {
        List<Map<String, Object>> exerciseDataList = new ArrayList<>();

        try {
            // Query to retrieve exercise data based on username
            String query = "SELECT ed.*, e.muscle_group " +
                    "FROM exercise_data ed " +
                    "JOIN exercises e ON ed.exercise_id = e.exercise_id " +
                    "JOIN workout_plans wp ON e.plan_id = wp.plan_id " +
                    "JOIN users u ON wp.username = u.username " +
                    "WHERE u.username = ? AND wp.plan_name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2,plan_name);
            ResultSet rs = stmt.executeQuery();

            // Loop through result set to fetch exercise data
            while (rs.next()) {
                Map<String, Object> exerciseData = new HashMap<>();
                exerciseData.put("exercise_date", rs.getDate("exercise_date").toString());
                exerciseData.put("exercise_name", rs.getString("exercise_name"));
                exerciseData.put("target_muscle", rs.getString("target_muscle"));
                exerciseData.put("duration", rs.getInt("duration"));
                exerciseData.put("sets", rs.getInt("sets"));
                if ( rs.getTime("rest_period") == null){
                    exerciseData.put("rest_period","N/A");
                }else {
                    exerciseData.put("rest_period", rs.getTime("rest_period").toString());
                }


                exerciseDataList.add(exerciseData);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exerciseDataList;
    }
    public List<Map<String, Object>> getWorkoutData(String username, String plan_name,String muscle) {
        List<Map<String, Object>> exerciseDataList = new ArrayList<>();

        try {
            // Query to retrieve exercise data based on username
            String query = "SELECT ed.*, e.exercise_name, e.muscle_group " +
                    "FROM exercise_data ed " +
                    "JOIN exercises e ON ed.exercise_id = e.exercise_id " +
                    "JOIN workout_plans wp ON e.plan_id = wp.plan_id " +
                    "JOIN users u ON wp.username = u.username " +
                    "WHERE u.username = ? AND wp.plan_name = ? AND ed.target_muscle = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2,plan_name);
            stmt.setString(3,muscle);
            ResultSet rs = stmt.executeQuery();

            // Loop through result set to fetch exercise data
            while (rs.next()) {
                Map<String, Object> exerciseData = new HashMap<>();
                exerciseData.put("exercise_date", rs.getDate("exercise_date").toString());
                exerciseData.put("exercise_name", rs.getString("exercise_name"));
                exerciseData.put("target_muscle", rs.getString("target_muscle"));
                exerciseData.put("duration", rs.getInt("duration"));
                exerciseData.put("sets", rs.getInt("sets"));
                if ( rs.getTime("rest_period") == null){
                    exerciseData.put("rest_period","N/A");
                }else {
                    exerciseData.put("rest_period", rs.getTime("rest_period").toString());
                }


                exerciseDataList.add(exerciseData);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exerciseDataList;
    }




    public HashMap<String, List<String>> getAllExercises() {
        HashMap<String, List<String>> exercises = new HashMap<>();
        List<String> muscleGroups = getAllTargetMuscles();

        for (String muscleGroup : muscleGroups) {
            exercises.put(muscleGroup, new ArrayList<>()); // Initialize with an empty list for each muscle group
        }

        String sql = "SELECT exerciseName, targetMuscle FROM listed_exercises";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String targetMuscle = rs.getString("targetMuscle");
                String exerciseName = rs.getString("exerciseName");
                List<String> exercisesList = exercises.get(targetMuscle);
                if (exercisesList != null) {
                    exercisesList.add(exerciseName); // Add exercise name to the corresponding muscle group's list
                } else {
                    // Handle case where muscle group from exercises does not exist in muscleGroups
                    // This could occur if data is inconsistent or a new muscle group is added in the database
                    // You may log or handle this case as appropriate for your application
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging or throwing a custom exception
        }

        return exercises;
    }

    public List<String> getAllTargetMuscles() {
        List<String> targetMuscles = new ArrayList<>();
        String sql = "SELECT DISTINCT targetMuscle FROM listed_exercises";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                targetMuscles.add(rs.getString("targetMuscle"));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging or throwing a custom exception
        }
        return targetMuscles;
    }
    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
