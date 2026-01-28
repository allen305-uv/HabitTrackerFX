// src/Main.java
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HabitTracker tracker = new HabitTracker();
        Scanner input = new Scanner(System.in);

        // Default habits if empty
        if (tracker.getHabits().isEmpty()) {
            tracker.addHabit("Code in Java");
            tracker.addHabit("Drink Water");
            tracker.addHabit("Exercise");
            tracker.addHabit("Read 10 Pages");
            tracker.addHabit("Sleep 8 Hours");
        }

        while (true) {
            // CLEAR CONSOLE (Simulated by printing lines)
            System.out.print("\033[H\033[2J"); 
            System.out.flush();
            
            System.out.println("\n=================================");
            System.out.println("   HABIT TRACKER FX - DASHBOARD  ");
            System.out.println("=================================");

            List<Habit> myHabits = tracker.getHabits();
            int completedCount = 0;
            
            // 1. Calculate Stats
            for (Habit h : myHabits) {
                if (h.isCompleted()) completedCount++;
            }
            double percentage = (double) completedCount / myHabits.size() * 100;

            // 2. Draw Progress Bar
            System.out.print("Daily Progress: [");
            int bars = (int) (percentage / 10);
            for (int i = 0; i < 10; i++) {
                if (i < bars) System.out.print("="); // Completed part
                else System.out.print(" ");          // Empty part
            }
            System.out.println("] " + (int)percentage + "%");
            System.out.println("---------------------------------");

            // 3. List Habits
            for (int i = 0; i < myHabits.size(); i++) {
                Habit h = myHabits.get(i);
                String checkbox = h.isCompleted() ? "[X]" : "[ ]";
                System.out.println(" " + i + ". " + checkbox + " " + h.getName());
            }

            System.out.println("---------------------------------");
            System.out.println("COMMANDS: [0-" + (myHabits.size()-1) + "] Toggle Habit | [99] Save & Exit");
            System.out.print("Select ID: ");

            // 4. Handle Input
            try {
                String inputStr = input.next(); // Read as String to prevent crashing
                int choice = Integer.parseInt(inputStr);

                if (choice == 99) {
                    tracker.saveData();
                    System.out.println("\n>> Progress Saved. Keep it up! <<");
                    break;
                }
                
                if (choice >= 0 && choice < myHabits.size()) {
                    Habit selected = myHabits.get(choice);
                    if (selected.isCompleted()) {
                        selected.reset(); // Toggle OFF
                    } else {
                        selected.markComplete(); // Toggle ON
                    }
                }
            } catch (NumberFormatException e) {
                // Ignore invalid input so app doesn't crash
            }
        }
    }
}