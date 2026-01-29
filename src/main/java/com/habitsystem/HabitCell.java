package com.habitsystem;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import java.time.LocalDate;

public class HabitCell extends ListCell<Habit> {
    private final HBox root;
    private final CheckBox checkBox;
    private final Label nameLabel;
    private final Label streakLabel;
    private final Button mapButton;
    private final HabitTracker tracker; 

    public HabitCell(HabitTracker tracker) {
        this.tracker = tracker;

        checkBox = new CheckBox();
        checkBox.setStyle("-fx-font-size: 16px; -fx-cursor: hand;");

        nameLabel = new Label();
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        
        streakLabel = new Label();
        streakLabel.setStyle("-fx-text-fill: #ffa726; -fx-font-size: 14px;"); 

        mapButton = new Button("📊");
        mapButton.setStyle("-fx-background-color: #40444b; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 5;");
        
        // HOVER EFFECTS
        mapButton.setOnMouseEntered(e -> mapButton.setStyle("-fx-background-color: #5865F2; -fx-text-fill: white; -fx-background-radius: 5;"));
        mapButton.setOnMouseExited(e -> mapButton.setStyle("-fx-background-color: #40444b; -fx-text-fill: white; -fx-background-radius: 5;"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS); 

        root = new HBox(15); 
        root.setAlignment(Pos.CENTER_LEFT);
        root.getChildren().addAll(checkBox, nameLabel, spacer, streakLabel, mapButton);
        root.setStyle("-fx-padding: 10; -fx-background-color: #2f3136; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

        checkBox.setOnAction(e -> {
            Habit h = getItem();
            if (h != null) {
                h.setStatus(LocalDate.now(), checkBox.isSelected());
                tracker.saveData(); 
                updateItem(h, false); 
            }
        });
        
        // --- THE UPDATE IS HERE ---
        mapButton.setOnAction(e -> {
            Habit h = getItem();
            if (h != null) {
                // Call the new Window
                HeatmapWindow.show(h);
            }
        });
    }

    @Override
    protected void updateItem(Habit habit, boolean empty) {
        super.updateItem(habit, empty);

        if (empty || habit == null) {
            setGraphic(null);
            setText(null);
            setStyle("-fx-background-color: transparent;"); 
        } else {
            nameLabel.setText(habit.getName());
            checkBox.setSelected(habit.isCompletedOn(LocalDate.now()));
            streakLabel.setText(habit.getStreak() + " 🔥");
            setGraphic(root);
            setStyle("-fx-background-color: transparent; -fx-padding: 5;");
        }
    }
}