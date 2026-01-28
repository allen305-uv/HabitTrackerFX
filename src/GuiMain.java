// src/GuiMain.java
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class GuiMain {
    // SHARED VARIABLES
    static HabitTracker tracker;
    static JPanel listPanel;
    static JProgressBar progressBar;

    public static void main(String[] args) {
        tracker = new HabitTracker();
        
        // Add defaults if empty
        if (tracker.getHabits().isEmpty()) {
            tracker.addHabit("Code in Java");
            tracker.addHabit("Drink Water");
        }

        JFrame frame = new JFrame("HabitTrackerFX - Visual Chain Edition");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(550, 600); // Slightly wider for the chain
        frame.setLayout(new BorderLayout());

        // --- HEADER ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        headerPanel.setBackground(new Color(40, 44, 52));

        JLabel titleLabel = new JLabel("HABIT FLOW");
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

        // --- LIST ---
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        refreshUI();

        frame.add(new JScrollPane(listPanel), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // --- THE VISUAL CHAIN LOGIC ---
    public static void refreshUI() {
        listPanel.removeAll();
        List<Habit> habits = tracker.getHabits();
        LocalDate today = LocalDate.now();
        int completedCount = 0;

        for (Habit h : habits) {
            boolean isDoneToday = h.isCompletedOn(today);
            if (isDoneToday) completedCount++;

            JPanel row = new JPanel(new BorderLayout());
            row.setMaximumSize(new Dimension(500, 50));
            row.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.LIGHT_GRAY));

            // 1. Checkbox
            JCheckBox checkBox = new JCheckBox(h.getName());
            checkBox.setSelected(isDoneToday);
            checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            checkBox.setFocusable(false);

            // 2. The Visual Chain (HTML Rendering)
            boolean[] history = h.getLast5Days();
            StringBuilder htmlChain = new StringBuilder("<html>");
            
            for (boolean day : history) {
                if (day) {
                    // Green Box
                    htmlChain.append("<font color='#4CAF50' size='5'>■ </font>");
                } else {
                    // Gray Box
                    htmlChain.append("<font color='#CCCCCC' size='5'>□ </font>");
                }
            }
            htmlChain.append("</html>");
            
            JLabel chainLabel = new JLabel(htmlChain.toString());
            chainLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // 3. Stats (Current & Best)
            int current = h.getStreak();
            int best = h.getBestStreak();
            String statsText = "<html><div style='text-align: right;'>Curr: <b>" + current + "</b><br>" + 
                          "<font color='gray'>Best: " + best + "</font></div></html>";
            
            JLabel statsLabel = new JLabel(statsText);
            statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

            // Logic
            checkBox.addActionListener(e -> {
                h.setStatus(today, checkBox.isSelected());
                if (checkBox.isSelected()) Toolkit.getDefaultToolkit().beep();
                tracker.saveData();
                refreshUI();
            });

            row.add(checkBox, BorderLayout.WEST);
            row.add(chainLabel, BorderLayout.CENTER);
            row.add(statsLabel, BorderLayout.EAST);
            
            listPanel.add(row);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        int percent = (habits.isEmpty()) ? 0 : (int)((double)completedCount / habits.size() * 100);
        progressBar.setValue(percent);
        listPanel.revalidate();
        listPanel.repaint();
    }
}