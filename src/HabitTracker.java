// src/HabitTracker.java
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HabitTracker {
    private List<Habit> habits;
    private static final String FILE_NAME = "habit_data.csv";

    public HabitTracker() {
        this.habits = new ArrayList<>();
        loadData(); // Auto-load on startup
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
                writer.println(h.toDataString());
            }
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    private void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        habits.clear(); 

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                
                if (parts.length >= 2) {
                    String name = parts[0];
                    LocalDate startDate = LocalDate.parse(parts[1]);

                    Habit h = new Habit(name, startDate);

                    // Load History if exists
                    if (parts.length > 2 && !parts[2].isEmpty()) {
                        String[] dates = parts[2].split(",");
                        for (String dateStr : dates) {
                            h.setStatus(LocalDate.parse(dateStr), true);
                        }
                    }
                    habits.add(h);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading data.");
        }
    }
}