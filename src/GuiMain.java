// src/GuiMain.java
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class GuiMain {
    static HabitTracker tracker;
    static JPanel listPanel;
    static JProgressBar progressBar;

    public static void main(String[] args) {
        tracker = new HabitTracker();
        
        if (tracker.getHabits().isEmpty()) {
            tracker.addHabit("Code in Java");
            tracker.addHabit("Drink Water");
            tracker.addHabit("Exercise");
        }

        JFrame frame = new JFrame("HabitTrackerFX - Ultimate System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 600);
        frame.setLayout(new BorderLayout());

        // HEADER
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        headerPanel.setBackground(new Color(40, 44, 52));

        JLabel titleLabel = new JLabel("HABIT SYSTEM");
        titleLabel.setForeground(new Color(97, 218, 251));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(152, 195, 121));
        progressBar.setBackground(new Color(60, 60, 60));
        progressBar.setBorderPainted(false);
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        headerPanel.add(progressBar);
        frame.add(headerPanel, BorderLayout.NORTH);

        // LIST
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        refreshUI();

        frame.add(new JScrollPane(listPanel), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public static void refreshUI() {
        listPanel.removeAll();
        List<Habit> habits = tracker.getHabits();
        LocalDate today = LocalDate.now();
        int completedCount = 0;

        for (Habit h : habits) {
            boolean isDoneToday = h.isCompletedOn(today);
            if (isDoneToday) completedCount++;

            JPanel row = new JPanel(new BorderLayout());
            row.setMaximumSize(new Dimension(400, 40));

            JCheckBox checkBox = new JCheckBox(h.getName());
            checkBox.setSelected(isDoneToday);
            checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            checkBox.setFocusable(false);

            // Dynamic Streak Display
            int streak = h.getStreak();
            JLabel streakLabel = new JLabel("STREAK: " + streak + " ");
            streakLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            
            if (streak >= 10) {
                streakLabel.setForeground(Color.RED);
                streakLabel.setText("MASTER: " + streak + " ");
            } else if (streak >= 3) {
                streakLabel.setForeground(Color.ORANGE);
            } else {
                streakLabel.setForeground(Color.GRAY);
            }

            // ACTION: Uses the new setStatus with Today's Date
            checkBox.addActionListener(e -> {
                h.setStatus(today, checkBox.isSelected());
                if (checkBox.isSelected()) Toolkit.getDefaultToolkit().beep();
                
                tracker.saveData();
                refreshUI();
            });

            row.add(checkBox, BorderLayout.CENTER);
            row.add(streakLabel, BorderLayout.EAST);
            listPanel.add(row);
            listPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        int percent = (habits.isEmpty()) ? 0 : (int)((double)completedCount / habits.size() * 100);
        progressBar.setValue(percent);
        listPanel.revalidate();
        listPanel.repaint();
    }
}