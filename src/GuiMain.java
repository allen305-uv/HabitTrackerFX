// src/GuiMain.java
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.DayOfWeek; // <--- The missing piece!
import java.util.List;

public class GuiMain {
    static HabitTracker tracker;
    static JPanel listPanel;
    static JProgressBar progressBar;

    public static void main(String[] args) {
        tracker = new HabitTracker();
        
        JFrame frame = new JFrame("HabitTrackerFX - Professional Edition");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700); 
        frame.setLayout(new BorderLayout());

        // --- HEADER ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        headerPanel.setBackground(new Color(40, 44, 52));

        JLabel titleLabel = new JLabel("HABIT MASTERY");
        titleLabel.setForeground(new Color(97, 218, 251));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // TEST DATA BUTTON
        JButton mockBtn = new JButton("Inject Test Data");
        mockBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        mockBtn.setFocusable(false);
        mockBtn.addActionListener(e -> {
            injectTestData();
            refreshUI();
            JOptionPane.showMessageDialog(frame, "Test data injected! Click the Graph button to see it.");
        });

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(152, 195, 121)); 
        progressBar.setBackground(new Color(60, 60, 60));    
        progressBar.setBorderPainted(false);
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(mockBtn);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        headerPanel.add(progressBar);
        frame.add(headerPanel, BorderLayout.NORTH);

        // --- LIST ---
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
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

            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setMaximumSize(new Dimension(550, 55));
            row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));

            // 1. LEFT: CHECKBOX
            JCheckBox checkBox = new JCheckBox(h.getName());
            checkBox.setSelected(isDoneToday);
            checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            checkBox.setFocusable(false);

            // 2. CENTER: VISUAL CHAIN
            boolean[] history = h.getLast5Days();
            StringBuilder htmlChain = new StringBuilder("<html>");
            for (boolean day : history) {
                if (day) htmlChain.append("<font color='#4CAF50' size='5'>■ </font>");
                else htmlChain.append("<font color='#CCCCCC' size='5'>□ </font>");
            }
            htmlChain.append("</html>");
            JLabel chainLabel = new JLabel(htmlChain.toString());
            chainLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // 3. RIGHT: STATS + HEATMAP BUTTON
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            
            // Stats Text
            int current = h.getStreak();
            int best = h.getBestStreak();
            String statsText = "<html><div style='text-align: right;'>Curr: <b>" + current + "</b><br>" + 
                          "<font color='gray'>Best: " + best + "</font></div></html>";
            JLabel statsLabel = new JLabel(statsText);
            statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

            // THE GRAPH BUTTON
            JButton mapBtn = new JButton("📊");
            mapBtn.setToolTipText("View 4-Week Visual Heatmap");
            mapBtn.setFocusable(false);
            mapBtn.setPreferredSize(new Dimension(40, 30));
            mapBtn.addActionListener(e -> {
                GuiHeatmap.show(h);
            });

            // Action: Checkbox Click
            checkBox.addActionListener(e -> {
                h.setStatus(today, checkBox.isSelected());
                if (checkBox.isSelected()) Toolkit.getDefaultToolkit().beep();
                tracker.saveData();
                refreshUI();
            });

            rightPanel.add(statsLabel);
            rightPanel.add(mapBtn);

            row.add(checkBox, BorderLayout.WEST);
            row.add(chainLabel, BorderLayout.CENTER);
            row.add(rightPanel, BorderLayout.EAST);
            
            listPanel.add(row);
            listPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        int percent = (habits.isEmpty()) ? 0 : (int)((double)completedCount / habits.size() * 100);
        progressBar.setValue(percent);
        listPanel.revalidate();
        listPanel.repaint();
    }

    // Helper to add fake data for testing
    private static void injectTestData() {
        if (tracker.getHabits().isEmpty()) {
            tracker.addHabit("Code in Java");
            tracker.addHabit("Exercise");
        }
        
        LocalDate today = LocalDate.now();
        Habit h = tracker.getHabits().get(0);
        
        // Create a pattern for the last 30 days
        for (int i = 0; i < 30; i++) {
            LocalDate d = today.minusDays(i);
            DayOfWeek day = d.getDayOfWeek();
            // Mark true if NOT Saturday/Sunday
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                h.setStatus(d, true);
            }
        }
        tracker.saveData();
    }
}