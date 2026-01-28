// src/GuiMain.java
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GuiMain {
    static HabitTracker tracker;
    static JPanel listPanel;
    static JProgressBar progressBar;

    public static void main(String[] args) {
        tracker = new HabitTracker();
        // Default habits if empty
        if (tracker.getHabits().isEmpty()) {
            tracker.addHabit("Code in Java");
            tracker.addHabit("Drink Water");
            tracker.addHabit("Exercise");
        }

        JFrame frame = new JFrame("HabitTrackerFX - Ultimate Edition");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 600);
        frame.setLayout(new BorderLayout());

        // HEADER
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        headerPanel.setBackground(new Color(40, 44, 52));

        JLabel titleLabel = new JLabel("ULTIMATE TRACKER");
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
        int completed = 0;

        for (Habit h : habits) {
            if (h.isCompleted()) completed++;

            JPanel row = new JPanel(new BorderLayout());
            row.setMaximumSize(new Dimension(400, 40));

            JCheckBox checkBox = new JCheckBox(h.getName());
            checkBox.setSelected(h.isCompleted());
            checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            checkBox.setFocusable(false);

            // --- THE GAMIFICATION LOGIC ---
            // Instead of an Emoji, we use standard text
            JLabel streakLabel = new JLabel("STREAK: " + h.getStreak() + " ");
            streakLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            
            // Dynamic Coloring (The "Heat Map")
            if (h.getStreak() >= 10) {
                streakLabel.setForeground(Color.RED);     // ON FIRE
                streakLabel.setText("MASTER: " + h.getStreak() + " ");
            } else if (h.getStreak() >= 3) {
                streakLabel.setForeground(Color.ORANGE);  // Warming Up
            } else {
                streakLabel.setForeground(Color.GRAY);    // Cold
            }

            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) {
                    h.markComplete();
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    h.reset();
                }
                tracker.saveData();
                refreshUI();
            });

            row.add(checkBox, BorderLayout.CENTER);
            row.add(streakLabel, BorderLayout.EAST);
            listPanel.add(row);
            listPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        int percent = (habits.isEmpty()) ? 0 : (int)((double)completed / habits.size() * 100);
        progressBar.setValue(percent);
        listPanel.revalidate();
        listPanel.repaint();
    }
}