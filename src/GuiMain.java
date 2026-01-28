// src/GuiMain.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GuiMain {
    public static void main(String[] args) {
        // 1. Create the Backend Engine
        HabitTracker tracker = new HabitTracker();
        
        // If empty, add defaults
        if (tracker.getHabits().isEmpty()) {
            tracker.addHabit("Code in Java");
            tracker.addHabit("Drink Water");
            tracker.addHabit("Exercise");
        }

        // 2. Create the Main Window (JFrame)
        JFrame frame = new JFrame("HabitTrackerFX v1.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());

        // 3. Create the Header Panel (Title + Progress Bar)
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        headerPanel.setBackground(new Color(50, 50, 50)); // Dark Gray

        JLabel titleLabel = new JLabel("MY HABITS");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(0, 200, 0)); // Matrix Green
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(progressBar);
        
        frame.add(headerPanel, BorderLayout.NORTH);

        // 4. Create the Habit List Panel
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        List<Habit> habits = tracker.getHabits();
        
        // This helper function updates the bar whenever a box is clicked
        Runnable updateBar = () -> {
            int completed = 0;
            for (Habit h : habits) {
                if (h.isCompleted()) completed++;
            }
            int percent = (int)((double)completed / habits.size() * 100);
            progressBar.setValue(percent);
        };

        // Create a Checkbox for each habit
        for (Habit h : habits) {
            JCheckBox checkBox = new JCheckBox(h.getName());
            checkBox.setSelected(h.isCompleted());
            checkBox.setFont(new Font("Arial", Font.PLAIN, 18));
            checkBox.setFocusable(false);
            
            // What happens when you click it?
            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) h.markComplete();
                else h.reset();
                updateBar.run(); // Refresh bar
            });
            
            listPanel.add(checkBox);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
        }
        
        frame.add(new JScrollPane(listPanel), BorderLayout.CENTER);

        // 5. Create the Save Button
        JButton saveButton = new JButton("SAVE PROGRESS");
        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveButton.setBackground(new Color(50, 50, 50));
        saveButton.setForeground(Color.WHITE);
        
        saveButton.addActionListener(e -> {
            tracker.saveData();
            JOptionPane.showMessageDialog(frame, "Progress Saved Successfully!");
        });
        
        frame.add(saveButton, BorderLayout.SOUTH);

        // Initial Progress Calculation
        updateBar.run();

        // Show the window
        frame.setVisible(true);
    }
}