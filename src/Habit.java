// src/Habit.java
public class Habit {
    private String name;
    private boolean isCompleted;
    private int streak; // NEW: Tracks your momentum

    public Habit(String name) {
        this.name = name;
        this.isCompleted = false;
        this.streak = 0;
    }

    public void markComplete() {
        if (!isCompleted) { // Only increment if not already marked today
            this.isCompleted = true;
            this.streak++;
        }
    }

    public void reset() {
        if (isCompleted) { // Only decrement if it was marked today
            this.isCompleted = false;
            if (this.streak > 0) this.streak--;
        }
    }
    
    // Allow manual setting of streaks (for loading from file)
    public void setStreak(int s) {
        this.streak = s;
    }

    public String getName() { return name; }
    public boolean isCompleted() { return isCompleted; }
    public int getStreak() { return streak; }
}