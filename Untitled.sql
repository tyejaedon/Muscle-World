-- Assuming the table structure is as follows:
-- CREATE TABLE john_doe_workout_data (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     exercise_date DATE,
--     exercise_id INT,
--     exercise_name VARCHAR(255),
--     target_muscle VARCHAR(255),
--     duration TIME,
--     sets INT,
--     rest_period TIME,
--     weight_kg VARCHAR(45)
-- );

-- Insert exercise data for John Doe including weights
INSERT INTO exercise_data (exercise_date, exercise_id, exercise_name, target_muscle, duration, sets, rest_period, weight)
VALUES
    ('2024-06-20', 1, 'Squats', 'Legs', '00:30:00', 3, '00:01:00', '50'),  -- Day 1
    ('2024-06-20', 2, 'Bench Press', 'Chest', '00:45:00', 3, '00:01:00', '60'),  -- Day 1
    ('2024-06-20', 3, 'Deadlifts', 'Legs', '00:40:00', 3, '00:01:00', '70'),  -- Day 1

    ('2024-06-21', 14, 'Push Up', 'Chest', '00:20:00', 3, '00:01:00', NULL),  -- Day 2
    ('2024-06-21', 15, 'Pull Up', 'Back', '00:15:00', 3, '00:01:00', NULL),  -- Day 2
    ('2024-06-21', 16, 'Squat', 'Legs', '00:30:00', 3, '00:01:00', '55'),  -- Day 2

    ('2024-06-22', 17, 'Bicep Curl', 'Arms', '00:20:00', 3, '00:01:00', '25'),  -- Day 3
    ('2024-06-22', 18, 'Tricep Dip', 'Arms', '00:15:00', 3, '00:01:00', '30'),  -- Day 3
    ('2024-06-22', 19, 'Lunge', 'Legs', '00:25:00', 3, '00:01:00', '40'),  -- Day 3

    ('2024-06-23', 1, 'Squats', 'Legs', '00:30:00', 3, '00:01:00', '52'),  -- Day 4
    ('2024-06-23', 2, 'Bench Press', 'Chest', '00:45:00', 3, '00:01:00', '62'),  -- Day 4
    ('2024-06-23', 3, 'Deadlifts', 'Legs', '00:40:00', 3, '00:01:00', '72'),  -- Day 4

    ('2024-06-24', 14, 'Push Up', 'Chest', '00:20:00', 3, '00:01:00', NULL),  -- Day 5
    ('2024-06-24', 15, 'Pull Up', 'Back', '00:15:00', 3, '00:01:00', NULL),  -- Day 5
    ('2024-06-24', 16, 'Squat', 'Legs', '00:30:00', 3, '00:01:00', '57'),  -- Day 5

    ('2024-06-25', 17, 'Bicep Curl', 'Arms', '00:20:00', 3, '00:01:00', '27'),  -- Day 6
    ('2024-06-25', 18, 'Tricep Dip', 'Arms', '00:15:00', 3, '00:01:00', '32'),  -- Day 6
    ('2024-06-25', 19, 'Lunge', 'Legs', '00:25:00', 3, '00:01:00', '42'),  -- Day 6

    ('2024-06-26', 1, 'Squats', 'Legs', '00:30:00', 3, '00:01:00', '54'),  -- Day 7
    ('2024-06-26', 2, 'Bench Press', 'Chest', '00:45:00', 3, '00:01:00', '64'),  -- Day 7
    ('2024-06-26', 3, 'Deadlifts', 'Legs', '00:40:00', 3, '00:01:00', '74');  -- Day 7
