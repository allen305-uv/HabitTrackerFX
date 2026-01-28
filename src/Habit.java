// src/Habit.java
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Habit {
    private String name;
    private LocalDate startDate;
    private Map<LocalDate, Boolean> history; // The Database

    public Habit(String name) {
        this.name = name;
        this.startDate = LocalDate.now();
        this.history = new HashMap<>();
    }

    public Habit(String name, LocalDate startDate) {
        this.name = name;
        this.startDate = startDate;
        this.history = new HashMap<>();
    }

    public void setStatus(LocalDate date, boolean status) {
        if (status) history.put(date, true);
        else history.remove(date);
    }

    public boolean isCompletedOn(LocalDate date) {
        return history.getOrDefault(date, false);
    }

    public String getName() { return name; }

    // 1. CURRENT STREAK (Backwards from Today)
    public int getStreak() {
        int streak = 0;
        LocalDate checkDate = LocalDate.now();
        
        // If not done today, check yesterday
        if (!isCompletedOn(checkDate)) {
            checkDate = checkDate.minusDays(1);
        }
        
        while (isCompletedOn(checkDate)) {
            streak++;
            checkDate = checkDate.minusDays(1);
        }
        return streak;
    }

    // 2. THE "CHAIN OF CONSISTENCY" (Your C++ Logic Ported)
    // Scans the entire history to find the Personal Best
    public int getBestStreak() {
        if (history.isEmpty()) return 0;

        // Sort dates to create the "Linked List" flow
        List<LocalDate> dates = new ArrayList<>(history.keySet());
        Collections.sort(dates);

        int maxStreak = 0;
        int currentRun = 0;
        LocalDate previousDate = null;

        for (LocalDate date : dates) {
            if (previousDate == null) {
                // First node in the chain
                currentRun = 1;
            } else {
                // Check if this date is exactly 1 day after the previous
                long gap = ChronoUnit.DAYS.between(previousDate, date);
                
                if (gap == 1) {
                    currentRun++; // Chain continues
                } else {
                    currentRun = 1; // Chain broken, reset
                }
            }
            
            if (currentRun > maxStreak) {
                maxStreak = currentRun;
            }
            previousDate = date;
        }
        return maxStreak;
    }

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

    // NEW: Get the last 5 days for the visual chain
    // Index 0 = 4 days ago (Oldest)
    // Index 4 = Today (Newest)
    public boolean[] getLast5Days() {
        boolean[] results = new boolean[5];
        LocalDate today = LocalDate.now();
        
        for (int i = 0; i < 5; i++) {
            // We count backwards: 0 days ago, 1 day ago...
            // But we fill the array backwards so the Visual is Left-to-Right
            LocalDate checkDate = today.minusDays(i);
            results[4 - i] = isCompletedOn(checkDate);
        }
        return results;
    }
}
