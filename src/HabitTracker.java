// src/HabitTracker.java
import java.util.ArrayList;
import java.util.List;
import java.io.*; // Import the tools to read/write files
import java.util.Scanner;

public class HabitTracker {
    private List<Habit> habits;
    private static final String FILE_NAME = "habit_data.csv";

    public HabitTracker() {
        this.habits = new ArrayList<>();
        loadData(); // Load data as soon as the tracker starts!
    }

    public void addHabit(String name) {
        habits.add(new Habit(name));
    }

    public List<Habit> getHabits() {
        return habits;
    }

    // 1. SAVE: Write the list to a text file
    public void saveData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Habit h : habits) {
                // Format: Name,IsCompleted (e.g., "Code Java,true")
                writer.println(h.getName() + "," + h.isCompleted());
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // 2. LOAD: Read the list from the text file
    private void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return; // If no file exists yet, do nothing

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(","); // Split "Code Java,true"
                
                String name = parts[0];
                boolean isDone = Boolean.parseBoolean(parts[1]);

                Habit h = new Habit(name);
                if (isDone) h.markComplete();
                habits.add(h);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error loading data.");
        }
    }
}