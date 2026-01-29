package com.habitsystem;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Habit {
    private String name;
    private LocalDate startDate;
    private Map<LocalDate, Boolean> history;

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

    public int getStreak() {
        int streak = 0;
        LocalDate checkDate = LocalDate.now();
        if (!isCompletedOn(checkDate)) checkDate = checkDate.minusDays(1);
        while (isCompletedOn(checkDate)) {
            streak++;
            checkDate = checkDate.minusDays(1);
        }
        return streak;
    }

    public boolean[][] get4WeekMomentum() {
        boolean[][] matrix = new boolean[4][7];
        LocalDate today = LocalDate.now();
        LocalDate startPoint = today.minusWeeks(3);
        while (startPoint.getDayOfWeek() != DayOfWeek.MONDAY) {
            startPoint = startPoint.minusDays(1);
        }
        LocalDate cursor = startPoint;
        for (int w = 0; w < 4; w++) {
            for (int d = 0; d < 7; d++) {
                if (isCompletedOn(cursor)) matrix[w][d] = true;
                cursor = cursor.plusDays(1);
            }
        }
        return matrix;
    }

    public String toDataString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(";").append(startDate).append(";");
        List<LocalDate> sorted = new ArrayList<>(history.keySet());
        Collections.sort(sorted);
        for (int i = 0; i < sorted.size(); i++) {
            sb.append(sorted.get(i));
            if (i < sorted.size() - 1) sb.append(",");
        }
        return sb.toString();
    }
}
