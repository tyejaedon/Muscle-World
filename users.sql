

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    weight FLOAT NOT NULL,
    height FLOAT NOT NULL,
    gender ENUM('Male', 'Female') NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO users (username, email, full_name, password, age, weight, height, gender)
VALUES 
('john_doe', 'john.doe@example.com', 'John Doe', 'password123', 25, 70.5, 175, 'Male'),
('jane_smith', 'jane.smith@example.com', 'Jane Smith', 'securepassword', 30, 60.0, 165, 'Female'),
('alex_jones', 'alex.jones@example.com', 'Alex Jones', 'mysecretpass', 28, 80.0, 180, 'Male');



CREATE TABLE workout_plans (
    plan_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    plan_name VARCHAR(100) NOT NULL,
    target VARCHAR(100) NOT NULL,
    end_date DATE NOT NULL,
    CONSTRAINT fk_username
        FOREIGN KEY (username) 
        REFERENCES users (username)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
CREATE TABLE exercises (
    exercise_id INT AUTO_INCREMENT PRIMARY KEY,
    exercise_name VARCHAR(100) NOT NULL,
    muscle_group VARCHAR(100) NOT NULL,
    plan_id INT NOT NULL,
     FOREIGN KEY (plan_id) 
        REFERENCES workout_plans (plan_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
    
);
CREATE TABLE exercise_data (

    exercise_date DATE PRIMARY KEY DEFAULT (CURDATE()),
    exercise_id INT,
    exercise_name VARCHAR(255) UNIQUE,
    target_muscle VARCHAR(255),
    duration INT,
    sets INT,
    rest_period TIME,
  
    FOREIGN KEY (exercise_id)
        REFERENCES exercises (exercise_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);



-- John Doe's plans
INSERT INTO workout_plans (username, plan_name, target, end_date)
VALUES ('john_doe', 'Beginner Strength Training', 'Strength', '2024-12-31');

-- Jane Smith's plans
INSERT INTO workout_plans (username, plan_name, target, end_date)
VALUES ('jane_smith', 'Cardio and Flexibility', 'Cardio', '2024-12-31');
-- Exercises for John Doe's plan
INSERT INTO exercises (exercise_name, muscle_group, plan_id)
VALUES ('Squats', 'Legs', 1),
       ('Bench Press', 'Chest', 1),
       ('Deadlifts', 'Legs', 1);

-- Exercises for Jane Smith's plan
INSERT INTO exercises (exercise_name, muscle_group, plan_id)
VALUES ('Running', 'Cardio', 2),
       ('Yoga', 'Flexibility', 2),
       ('Plank', 'Core', 2);
ALTER TABLE exercises AUTO_INCREMENT = 1;

-- Exercise data for John Doe's plan
INSERT INTO exercise_data (exercise_id, day_of_week, weight, repetitions, sets, rest_period, duration)
VALUES (1, 'Monday', 50.0, 10, 3, '00:30:00', '00:45:00'),
       (2, 'Tuesday', 60.0, 12, 4, '00:45:00', '01:00:00'),
       (3, 'Wednesday', 70.0, 8, 3, '00:40:00', '00:50:00');

-- Exercise data for Jane Smith's plan
INSERT INTO exercise_data (exercise_id, day_of_week, weight, repetitions, sets, rest_period, duration)
VALUES (4, 'Monday', 0, 0, 0, NULL, '01:00:00'),
       (5, 'Tuesday', 0, 0, 0, NULL, '00:45:00'),
       (6, 'Wednesday', 0, 0, 0, NULL, '00:55:00');
CREATE TABLE listed_exercises (
    id INT AUTO_INCREMENT PRIMARY KEY,
    exerciseName VARCHAR(255) NOT NULL,
    targetMuscle VARCHAR(255) NOT NULL
);

INSERT INTO listed_exercises (exerciseName, targetMuscle) VALUES 
('Bench Press', 'Chest'),
('Squat', 'Legs'),
('Deadlift', 'Back'),
('Shoulder Press', 'Shoulders'),
('Bicep Curl', 'Arms'),
('Tricep Extension', 'Arms'),
('Pull Up', 'Back'),
('Lat Pulldown', 'Back'),
('Leg Press', 'Legs'),
('Leg Curl', 'Legs'),
('Calf Raise', 'Legs'),
('Chest Fly', 'Chest'),
('Dumbbell Row', 'Back'),
('Lateral Raise', 'Shoulders'),
('Front Raise', 'Shoulders'),
('Hammer Curl', 'Arms'),
('Overhead Tricep Extension', 'Arms'),
('Crunch', 'Abs'),
('Russian Twist', 'Abs'),
('Plank', 'Abs');

ALTER TABLE listed_exercises
ADD CONSTRAINT uc_exerciseName UNIQUE (exerciseName);

