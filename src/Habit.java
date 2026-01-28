// src/Habit.java
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Habit {
    private String name;
    private LocalDate startDate;
    // The Core Data Structure: Linking Dates to Status
    private Map<LocalDate, Boolean> history;

    // Constructor 1: New Habit
    public Habit(String name) {
        this.name = name;
        this.startDate = LocalDate.now();
        this.history = new HashMap<>();
    }

    // Constructor 2: Loading from File
    public Habit(String name, LocalDate startDate) {
        this.name = name;
        this.startDate = startDate;
        this.history = new HashMap<>();
    }

    public void setStatus(LocalDate date, boolean status) {
        if (status) {
            history.put(date, true);
        } else {
            history.remove(date); // Remove entry if unchecked
        }
    }

    public boolean isCompletedOn(LocalDate date) {
        return history.getOrDefault(date, false);
    }

    public String getName() { return name; }

    // DYNAMIC STREAK CALCULATION (The Smart Logic)
    // It counts backwards from today. If you miss a day, it resets automatically.
    public int getStreak() {
        int streak = 0;
        LocalDate checkDate = LocalDate.now();
        
        // If not done today, check if done yesterday to keep streak alive
        if (!isCompletedOn(checkDate)) {
            checkDate = checkDate.minusDays(1);
        }
        
        while (isCompletedOn(checkDate)) {
            streak++;
            checkDate = checkDate.minusDays(1);
        }
        return streak;
    }

    // SERIALIZATION: Convert Object -> String for saving
    public String toDataString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(";").append(startDate).append(";");
        
        List<LocalDate> sortedDates = new ArrayList<>(history.keySet());
        Collections.sort(sortedDates);

        for (int i = 0; i < sortedDates.size(); i++) {
            sb.append(sortedDates.get(i));
            if (i < sortedDates.size() - 1) sb.append(",");
        }
        return sb.toString();
    }
}