package com.jojo.player;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private MediaPlayer mediaPlayer;
    private MediaView mediaView;

    @Override
    public void start(Stage stage) {

        BorderPane root = new BorderPane();

        // Video Area
        StackPane videoPane = new StackPane();
        root.setCenter(videoPane);

        mediaView = new MediaView();
        mediaView.setPreserveRatio(true);

        mediaView.fitWidthProperty().bind(videoPane.widthProperty());
        mediaView.fitHeightProperty().bind(videoPane.heightProperty());

        videoPane.getChildren().add(mediaView);

        // Timeline Slider
        Slider timeline = new Slider(0, 100, 0);
        Label timeLabel = new Label("00:00 / 00:00");

        // Buttons
        Button openButton = new Button("Open");
        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button stopButton = new Button("Stop");

        // Volume
        Label volumeLabel = new Label("Volume");
        Slider volumeSlider = new Slider(0, 100, 50);

        // Controls
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER);

        controls.getChildren().addAll(
                openButton,
                playButton,
                pauseButton,
                stopButton,
                volumeLabel,
                volumeSlider
        );

        VBox bottomBox = new VBox(5);
        bottomBox.getChildren().addAll(timeline, timeLabel, controls);

        root.setBottom(bottomBox);

        // Open File
        openButton.setOnAction(e -> {

            FileChooser chooser = new FileChooser();

            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Media Files",
                            "*.mp4",
                            "*.mkv",
                            "*.avi",
                            "*.mov"
                    )
            );

            File file = chooser.showOpenDialog(stage);

            if (file == null) {
                return;
            }

            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }

            Media media = new Media(file.toURI().toString());

            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);

            mediaView.setMediaPlayer(mediaPlayer);

            // Update Timeline
            mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {

                if (!timeline.isValueChanging()) {

                    Duration total = mediaPlayer.getTotalDuration();

                    if (total != null && total.toMillis() > 0) {

                        timeline.setValue((newTime.toMillis() / total.toMillis()) * 100);
                         timeLabel.setText( formatTime(newTime) + " / " + formatTime(total));
                    }
                }
            });

            // Seek
            timeline.valueChangingProperty().addListener((obs, wasChanging, changing) -> {

                if (!changing && mediaPlayer != null) {

                    Duration total = mediaPlayer.getTotalDuration();

                    mediaPlayer.seek(
                            total.multiply(timeline.getValue() / 100.0)
                    );
                }
            });

            mediaPlayer.play();
        });

        // Play
        playButton.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.play();
            }
        });

        // Pause
        pauseButton.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        });

        // Stop
        stopButton.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
        });

        // Volume
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {

            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newVal.doubleValue() / 100.0);
            }
        });

        Scene scene = new Scene(root, 1280, 720);

        stage.setTitle("Jojo Player");
        stage.setScene(scene);
        stage.show();
    }

    private String formatTime(Duration duration) {

    int totalSeconds = (int) duration.toSeconds();

    int hours = totalSeconds / 3600;
    int minutes = (totalSeconds % 3600) / 60;
    int seconds = totalSeconds % 60;

    if (hours > 0) {
        return String.format("%d:%02d:%02d",
                hours,
                minutes,
                seconds);
    }

    return String.format("%02d:%02d",
            minutes,
            seconds);
}

    public static void main(String[] args) {
        launch(args);
    }
}