package com.habitsystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class HabitGuiPrototype extends Application {
    
    private HabitTracker tracker = new HabitTracker();
    private ListView<Habit> habitList;

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #202225;"); 

        Label title = new Label("HABIT MASTERY");
        title.setStyle("-fx-text-fill: #5865F2; -fx-font-size: 28px; -fx-font-weight: 900;");

        habitList = new ListView<>();
        habitList.getItems().addAll(tracker.getHabits());
        habitList.setCellFactory(param -> new HabitCell(tracker));
        habitList.setStyle("-fx-background-color: transparent; -fx-control-inner-background: #202225;");
        VBox.setVgrow(habitList, Priority.ALWAYS);

        // INPUT AREA
        HBox inputBox = new HBox(10);
        inputBox.setAlignment(Pos.CENTER);
        
        TextField nameInput = new TextField();
        nameInput.setPromptText("New Habit Name...");
        nameInput.setStyle("-fx-background-color: #40444b; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10;");
        HBox.setHgrow(nameInput, Priority.ALWAYS);

        Button addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: #5865F2; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        
        addButton.setOnAction(e -> {
            String text = nameInput.getText();
            if (!text.isEmpty()) {
                tracker.addHabit(text);
                habitList.getItems().setAll(tracker.getHabits());
                nameInput.clear();
            }
        });

        inputBox.getChildren().addAll(nameInput, addButton);
        root.getChildren().addAll(title, habitList, inputBox);

        Scene scene = new Scene(root, 450, 650);
        stage.setTitle("HabitTrackerFX 2.0");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}