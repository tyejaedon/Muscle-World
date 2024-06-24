package com.muscleflex.muscleflex;

import java.sql.*;
import java.util.Date;


public class DatabaseConnector {
    Connection conn;
    Statement stmt;
    ResultSet rs;

    private static final String URL = "jdbc:mysql://localhost:3306/gymApp";
    private static final String USER = "root";
    private static final String PASSWORD = "12345678";
    private String [] user_logged = new String[1];
    private static DatabaseConnector instance;
    // STEP 1: Load the JDBC driver

    // Private constructor to prevent instantiation
    private  DatabaseConnector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }    public void setLoggedUser(String username) {
        this.user_logged[0] = username;
    }

    // Public method to provide access to the single instance
    public static synchronized DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }
    public String getLoggedUser() {
        return user_logged[0];
    }
    private void connect() throws SQLException {
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
    }


    public  boolean registerUser(String Username,String email,String fullname, String password, int age, float weight,float height, String gender)  {

        String sql = "INSERT INTO users (username,email ,password,fullName ,age,gender, height,weight) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, Username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, fullname);
            preparedStatement.setInt(5, age);
            preparedStatement.setString(6, gender);
            preparedStatement.setFloat(7, height);
            preparedStatement.setFloat(8, weight);
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());

        }
        return false;
    }
    public boolean loginUser(String username, String password ){

        String sql = "SELECT password FROM users WHERE username = ? ";
        try {

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String stored_p = rs.getString("password");
                if (password.equals(stored_p)) {
                    setLoggedUser(username);
                    System.out.println(user_logged[0]);
                    return true;
                }
            }

        } catch (Exception e) {

            System.err.println(e);
        }
        return false;
    }
    public boolean addPlan(String[][] Workouts, String planName, String goal, Date date) {

        boolean success = false;
        PreparedStatement pStmt1 = null;
        PreparedStatement pStmt2 = null;
        try {
            // First insert into workoutplans (main details)
            String sql1 = "INSERT INTO workoutplans(Username, planName, EndDate, goal) VALUES (?, ?, ?, ?)";
            pStmt1 = conn.prepareStatement(sql1);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            pStmt1.setString(1, getLoggedUser());
            pStmt1.setString(2, planName);
            pStmt1.setDate(3, sqlDate);
            pStmt1.setString(4, goal);
            pStmt1.executeUpdate();

            // Second insert into workoutplans (workout details)
            String sql2 = "INSERT INTO exercises(planName, exerciseName, dayofExercises) VALUES (?, ?, ?)";
            pStmt2 = conn.prepareStatement(sql2);
            for (int i = 0; i < Workouts.length; i++) {
                String day = Workouts[i][0];
                pStmt2.setString(3, day);
                for (int j = 1; j < Workouts[i].length; j++) {
                    String exercise = Workouts[i][j];
                    if (exercise == null || exercise.trim().isEmpty()) {
                        throw new IllegalArgumentException("Exercise name cannot be null or empty.");
                    }
                    pStmt2.setString(1, planName);
                    pStmt2.setString(2, exercise);
                    System.out.println("Inserting: planName=" + planName + ", exercise=" + exercise + ", day=" + day);
                    pStmt2.executeUpdate();
                }
            }
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pStmt1 != null) pStmt1.close();
                if (pStmt2 != null) pStmt2.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }


}
