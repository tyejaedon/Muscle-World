package com.muscleflex.muscleflex;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class ExerciseIcon {

    private ImageView icon;
    private String exerciseName;

    public ExerciseIcon(String iconName, String exerciseName) {
        this.icon = createExerciseIcon(iconName);
        this.exerciseName = exerciseName;
    }

    private ImageView createExerciseIcon(String iconName) {
        // Load the image from resources
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/muscleflex/muscleflex/" + iconName + ".png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(24); // Adjust width as needed
        imageView.setFitHeight(24); // Adjust height as needed
        return imageView;
    }

    public ImageView getIcon() {
        return icon;
    }

    public String getExerciseName() {
        return exerciseName;
    }
}
