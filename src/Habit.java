// src/Habit.java

public class Habit {
    // 1. Properties: The data this object holds
    private String name;
    private boolean isCompleted;

    // 2. Constructor: How to build a new habit
    public Habit(String name) {
        this.name = name;
        this.isCompleted = false; // Default: Not done yet
    }

    // 3. Methods: What this object can DO
    public void markComplete() {
        this.isCompleted = true;
    }

    public void reset() {
        this.isCompleted = false;
    }

    // 4. Getters: Allowing other parts of the system to read the data
    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}