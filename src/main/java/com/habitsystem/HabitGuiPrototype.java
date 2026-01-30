package com.habitsystem; 

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class HabitGuiPrototype extends Application {

    private HabitTracker tracker = new HabitTracker();
    private BorderPane mainLayout; // Reference to refresh UI if needed

    @Override
    public void start(Stage primaryStage) {
        // 1. SETUP LAYOUT
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #fdf6e3;");

        // 2. DRAW UI (Extracted to method so we can potentially refresh it)
        refreshUI();

        Scene scene = new Scene(mainLayout, 1150, 600);
        primaryStage.setTitle("HabitTrackerFX v2.3 - Live Calendar");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void refreshUI() {
        // --- DATA CALCS ---
        int habitCount = tracker.getHabits().size();
        int totalCompletions = 0;
        for (Habit h : tracker.getHabits()) totalCompletions += h.getStreak(); 
        double overallProgress = (habitCount == 0) ? 0 : (totalCompletions / (double)(habitCount * 30));

        // --- TOP: NAVY HEADER ---
        HBox statsBar = new HBox(40);
        statsBar.setPadding(new Insets(15, 30, 15, 30));
        statsBar.setAlignment(Pos.CENTER);
        statsBar.setStyle("-fx-background-color: #1a2f4b;");

        String currentMonth = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        Label monthLabel = new Label(currentMonth);
        monthLabel.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");

        statsBar.getChildren().addAll(
            monthLabel, 
            createStatItem("Number of habits", String.valueOf(habitCount)),
            createStatItem("Total Streak Count", String.valueOf(totalCompletions)),
            createProgressSection(overallProgress),
            createStatItem("Progress in %", String.format("%.1f%%", overallProgress * 100))
        );
        mainLayout.setTop(statsBar);

        // --- CENTER: LIVE GRID ---
        VBox centerLayout = new VBox(10);
        centerLayout.setPadding(new Insets(20));
        centerLayout.getChildren().add(createGridHeader());
        
        // Pass the REAL Habit object to the row creator
        for (Habit h : tracker.getHabits()) {
            centerLayout.getChildren().add(createInteractiveHabitRow(h));
        }
        
        ScrollPane scrollPane = new ScrollPane(centerLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #fdf6e3; -fx-background-color: transparent;");
        mainLayout.setCenter(scrollPane);

        // --- RIGHT: ANALYSIS ---
        VBox analysisBar = new VBox(15);
        analysisBar.setPadding(new Insets(20));
        analysisBar.setPrefWidth(260);
        analysisBar.setStyle("-fx-background-color: #f4eee1; -fx-border-color: #dcdcdc; -fx-border-width: 0 0 0 1;");
        
        Label analysisTitle = new Label("Analysis (Goal: 30)");
        analysisTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #444;");
        analysisBar.getChildren().add(analysisTitle);

        for (Habit h : tracker.getHabits()) {
            analysisBar.getChildren().add(createMicroChart(h));
        }
        mainLayout.setRight(analysisBar);
    }

    // --- NEW: INTERACTIVE ROW LOGIC ---
    private HBox createInteractiveHabitRow(Habit h) {
        HBox row = new HBox(5);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: white; -fx-border-color: #eee; -fx-padding: 2;");
        
        Label lbl = new Label(h.getName());
        lbl.setPrefWidth(125);
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        row.getChildren().add(lbl);

        // Create checkboxes mapped to the FIRST 15 DAYS of current month
        LocalDate today = LocalDate.now();
        LocalDate firstOfMonth = today.withDayOfMonth(1);

        for (int i = 0; i < 15; i++) {
            LocalDate targetDate = firstOfMonth.plusDays(i);
            CheckBox cb = new CheckBox();
            cb.setStyle("-fx-opacity: 0.8; -fx-cursor: hand;");
            
            // 1. Load State: Is this day completed in the CSV?
            if (h.isCompletedOn(targetDate)) {
                cb.setSelected(true);
                cb.setStyle("-fx-mark-highlight-color: white; -fx-mark-color: white; -fx-background-color: #2ecc71;");
            }

            // 2. Save Action: When clicked, update data
            cb.setOnAction(e -> {
                h.setStatus(targetDate, cb.isSelected());
                tracker.saveData(); // <--- CRITICAL: SAVES TO DISK
                
                // Visual toggle
                if (cb.isSelected()) {
                     cb.setStyle("-fx-mark-highlight-color: white; -fx-mark-color: white; -fx-background-color: #2ecc71;");
                } else {
                     cb.setStyle("-fx-opacity: 0.8;");
                }
                
                // Note: Stats update requires restart in this version to keep code simple
                System.out.println("Saved: " + h.getName() + " for " + targetDate);
            });

            row.getChildren().add(cb);
        }
        return row;
    }

    private VBox createProgressSection(double progress) {
        VBox section = new VBox(5);
        Label lbl = new Label("Overall Progress");
        lbl.setStyle("-fx-text-fill: #adb5bd; -fx-font-size: 11px;");
        ProgressBar pb = new ProgressBar(progress);
        pb.setPrefWidth(150);
        pb.setStyle("-fx-accent: #2ecc71;");
        section.getChildren().addAll(lbl, pb);
        return section;
    }

    private VBox createMicroChart(Habit h) {
        VBox container = new VBox(4);
        HBox textRow = new HBox();
        textRow.setAlignment(Pos.CENTER_LEFT);
        
        Label name = new Label(h.getName());
        name.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #555;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        int actual = h.getStreak();
        int goal = 30;
        Label stats = new Label(actual + " / " + goal);
        stats.setStyle("-fx-font-size: 10px; -fx-text-fill: #777;");
        textRow.getChildren().addAll(name, spacer, stats);

        double percentage = (double) actual / goal;
        if (percentage > 1.0) percentage = 1.0;
        Rectangle bg = new Rectangle(220, 8, Color.web("#e0e0e0"));
        Rectangle bar = new Rectangle(220 * percentage, 8, Color.web("#20c997")); 
        bg.setArcWidth(4); bg.setArcHeight(4);
        bar.setArcWidth(4); bar.setArcHeight(4);
        
        StackPane stack = new StackPane(bg, bar);
        stack.setAlignment(Pos.CENTER_LEFT);
        container.getChildren().addAll(textRow, stack);
        return container;
    }

    private VBox createStatItem(String title, String value) {
        VBox box = new VBox(2);
        box.setAlignment(Pos.CENTER);
        Label t = new Label(title);
        t.setStyle("-fx-text-fill: #adb5bd; -fx-font-size: 11px;");
        Label v = new Label(value);
        v.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        box.getChildren().addAll(t, v);
        return box;
    }

    private HBox createGridHeader() {
        HBox header = new HBox(5);
        header.setPadding(new Insets(0, 0, 0, 130));
        for (int i = 1; i <= 15; i++) {
            Label day = new Label(String.format("%02d", i));
            day.setPrefWidth(25);
            day.setAlignment(Pos.CENTER);
            day.setStyle("-fx-font-size: 9px; -fx-text-fill: #777;");
            header.getChildren().add(day);
        }
        return header;
    }

    public static void main(String[] args) {
        launch(args);
    }
}