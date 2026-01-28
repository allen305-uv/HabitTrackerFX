// src/HabitTracker.java
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Scanner;

public class HabitTracker {
    private List<Habit> habits;
    private static final String FILE_NAME = "habit_data.csv";

    public HabitTracker() {
        this.habits = new ArrayList<>();
        loadData();
    }

    public void addHabit(String name) {
        habits.add(new Habit(name));
    }

    public List<Habit> getHabits() {
        return habits;
    }

    public void saveData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Habit h : habits) {
                // Format: Name,IsCompleted,Streak
                writer.println(h.getName() + "," + h.isCompleted() + "," + h.getStreak());
            }
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    private void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(","); 
                
                if (parts.length >= 2) {
                    String name = parts[0];
                    boolean isDone = Boolean.parseBoolean(parts[1]);
                    
                    Habit h = new Habit(name);
                    if (isDone) h.markComplete(); // Sets isCompleted=true
                    
                    // NEW: Load streak if available (Backward compatibility)
                    if (parts.length >= 3) {
                        h.setStreak(Integer.parseInt(parts[2]));
                    }
                    
                    habits.add(h);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading data.");
        }
    }
}