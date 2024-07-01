package com.muscleflex.muscleflex;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MuscleView extends VBox {
    private ImageView bodyImageView;
    private Map<String, String> muscleGifsMap;
    private ImageView gifImageView;
    private Text infoText;
   private StackPane pane;
    FadeTransition infoTextFadeIn;
    FadeTransition infoTextFadeOut;
    FadeTransition gifFadeIn;
    FadeTransition gifFadeOut;
    public MuscleView(double width, double height) {
        // Load the human body image
        Image bodyImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/muscleflex/muscleflex/anatomy.jpeg")));
        // Ensure body image fits within 500x500
        double maxWidth = 440;
        double maxHeight = 250;

        // Calculate scaled width and height maintaining aspect ratio
        double fitWidth = Math.min(maxWidth, bodyImage.getWidth());
        double fitHeight = Math.min(maxHeight, bodyImage.getHeight());
     pane= new StackPane();
        bodyImageView = new ImageView(bodyImage);
        bodyImageView.setFitWidth(width);
        bodyImageView.setFitHeight(height/2);

        // Create rounded rectangle as a clipping mask
        Rectangle clip = new Rectangle(bodyImageView.getFitWidth(), bodyImageView.getFitHeight());
        clip.setArcWidth(20); // Set arc width (adjust as needed)
        clip.setArcHeight(20); // Set arc height (adjust as needed)

        // Apply clipping to ImageView
        bodyImageView.setClip(clip);
        bodyImageView.setPreserveRatio(false);


        // Initialize the info text
        infoText = new Text();
        infoText.setStyle("-fx-font-size: 14px; -fx-color: white; -fx-padding: 5px;");
        infoText.setVisible(true);

        // Initialize the gif image view
        gifImageView = new ImageView();
        gifImageView.setPreserveRatio(false);
        gifImageView.setVisible(true);

        VBox.setVgrow(this,Priority.ALWAYS);
        // Initialize the muscle gifs map
        muscleGifsMap = new HashMap<>();
        muscleGifsMap.put("Chest", "/com/muscleflex/muscleflex/chest.gif");
        muscleGifsMap.put("Back", "/com/muscleflex/muscleflex/row.gif");
        muscleGifsMap.put("Legs", "/com/muscleflex/muscleflex/legpress.gif");
        muscleGifsMap.put("Arms", "/com/muscleflex/muscleflex/bicepcurl.gif");
        muscleGifsMap.put("Shoulders", "/com/muscleflex/muscleflex/shoulder.gif");
        muscleGifsMap.put("Abs", "/com/muscleflex/muscleflex/row.gif");

        // Add the body image to the pane
        Pane imagePane = new Pane();
        imagePane.getChildren().add(bodyImageView);

        // Add interactive areas
        Circle chestArea = createMuscleArea(120, 70, 20, "Chest");
        Circle backArea = createMuscleArea(320, 100, 25, "Back");
        Circle legsArea = createMuscleArea(150, 190, 25, "Legs");
        Circle armsArea = createMuscleArea(75, 95, 15, "Arms");
        Circle shouldersArea = createMuscleArea(150, 55, 10, "Shoulders");
        Circle absArea = createMuscleArea(130, 110, 20, "Abs");

        // Add components to the pane
        imagePane.getChildren().addAll(chestArea, backArea, legsArea, armsArea, shouldersArea, absArea);

        // Main layout
        getChildren().addAll(imagePane);
        setSpacing(5);
        pane.setPrefSize(width, height/2);
        pane.setStyle("-fx-background-color: #333; -fx-background-radius: 25px;");
        pane.getChildren().add(gifImageView);
        pane.setAlignment(Pos.CENTER);

        getChildren().add(pane);
       setSpacing(20);

        // Create fade transitions
        infoTextFadeIn = new FadeTransition(Duration.millis(2000), infoText);
        infoTextFadeIn.setFromValue(0.0);
        infoTextFadeIn.setToValue(1.0);

        infoTextFadeOut = new FadeTransition(Duration.millis(2000), infoText);
        infoTextFadeOut.setFromValue(1.0);
        infoTextFadeOut.setToValue(0.0);

         gifFadeIn = new FadeTransition(Duration.millis(3000), gifImageView);
        gifFadeIn.setFromValue(0.0);
        gifFadeIn.setToValue(1.0);

        gifFadeOut = new FadeTransition(Duration.millis(3000), gifImageView);
        gifFadeOut.setFromValue(1.0);
        gifFadeOut.setToValue(0.0);
        
    }

    private Circle createMuscleArea(double xRatio, double yRatio, double radiusRatio, String muscleGroup) {
        // Calculate the actual position and radius based on image size
        double centerX = xRatio * bodyImageView.getFitWidth() / 440; // 440 is the original width of the image
        double centerY = yRatio * bodyImageView.getFitHeight() / 250; // 250 is the original height of the image
        double radius = radiusRatio * Math.min(bodyImageView.getFitWidth(), bodyImageView.getFitHeight()) / 440; // Adjust radius proportionally

        Circle circle = new Circle(centerX, centerY, radius);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.RED);

        circle.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> showMuscleGif(muscleGroup, event.getSceneX(), event.getSceneY()));
        circle.addEventHandler(MouseEvent.MOUSE_EXITED, event -> hideMuscleGif());

        return circle;
    }


    private void showMuscleGif(String muscleGroup, double x, double y) {
        String gifPath = muscleGifsMap.get(muscleGroup);
        if (gifPath != null) {
            Image gifImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(gifPath)));

            gifImageView.setImage(gifImage);
gifImageView.setFitWidth(400);
gifImageView.setFitHeight(200);

          gifFadeIn.play();
          infoTextFadeIn.play();
        }
    }

    private void hideMuscleGif() {
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
       pause.setOnFinished(e->{

           gifFadeOut.play();
           infoTextFadeOut.play();

       });
    }
}
