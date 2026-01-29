package com.habitsystem;

import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.Modality;

public class HeatmapWindow {

    public static void show(Habit habit) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); // Block interaction with main window
        window.setTitle("Momentum: " + habit.getName());

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #202225;");
        root.setAlignment(Pos.CENTER);

        // 1. Header
        Label header = new Label(habit.getName() + " - Last 4 Weeks");
        header.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        // 2. The Grid (4 Weeks x 7 Days)
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setAlignment(Pos.CENTER);

        boolean[][] data = habit.get4WeekMomentum();

        // Add Day Labels (Mon, Tue...)
        String[] days = {"M", "T", "W", "T", "F", "S", "S"};
        for (int i = 0; i < 7; i++) {
            Label d = new Label(days[i]);
            d.setStyle("-fx-text-fill: #72767d; -fx-font-size: 10px;");
            d.setAlignment(Pos.CENTER);
            grid.add(d, i, 0); // Column i, Row 0
        }

        // Draw the Squares
        for (int w = 0; w < 4; w++) {
            for (int d = 0; d < 7; d++) {
                Rectangle box = new Rectangle(30, 30);
                box.setArcWidth(5);
                box.setArcHeight(5);

                if (data[w][d]) {
                    // SUCCESS COLOR (Green)
                    box.setFill(Color.web("#2ecc71"));
                    box.setEffect(new javafx.scene.effect.Glow(0.5)); // Glowing effect
                } else {
                    // EMPTY COLOR (Dark Gray)
                    box.setFill(Color.web("#2f3136"));
                }
                
                // Add to grid (Row is w+1 because Row 0 is labels)
                grid.add(box, d, w + 1);
            }
        }

        root.getChildren().addAll(header, grid);

        Scene scene = new Scene(root, 300, 250);
        window.setScene(scene);
        window.showAndWait();
    }
}