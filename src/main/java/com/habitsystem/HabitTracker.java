package com.habitsystem;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class HabitTracker {
    private List<Habit> habits;
    private final String FILE_NAME = "habit_data.csv";

    public HabitTracker() {
        habits = new ArrayList<>();
        loadData();
    }

    public void addHabit(String name) {
        habits.add(new Habit(name));
        saveData();
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
            e.printStackTrace();
        }
    }

    private void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 2) continue;
                Habit h = new Habit(parts[0], LocalDate.parse(parts[1]));
                if (parts.length > 2 && !parts[2].isEmpty()) {
                    String[] dates = parts[2].split(",");
                    for (String d : dates) {
                        h.setStatus(LocalDate.parse(d), true);
                    }
                }
                habits.add(h);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
