// src/GuiHeatmap.java
import javax.swing.*;
import java.awt.*;

public class GuiHeatmap {
    // Professional Colors (GitHub Style)
    private static final Color COLOR_EMPTY = new Color(235, 237, 240); // Light Gray
    private static final Color COLOR_FILLED = new Color(64, 196, 99);  // Nice Green
    private static final Color COLOR_TEXT = new Color(40, 44, 52);

    public static void show(Habit h) {
        // 1. Get the Data
        boolean[][] data = h.get4WeekMomentum();

        // 2. Create the Container Panel
        // Layout: 5 Rows (1 Header + 4 Weeks), 7 Columns (Days)
        JPanel mainPanel = new JPanel(new GridLayout(5, 7, 5, 5)); // 5px gaps
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 3. Add Header Labels (Mon, Tue...)
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String day : days) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(COLOR_TEXT);
            mainPanel.add(label);
        }

        // 4. Draw the Grid Boxes
        for (int week = 0; week < 4; week++) {
            for (int day = 0; day < 7; day++) {
                JPanel box = new JPanel();
                box.setPreferredSize(new Dimension(30, 30)); // 30x30 pixels
                
                // Color logic
                if (data[week][day]) {
                    box.setBackground(COLOR_FILLED);
                    box.setBorder(BorderFactory.createLineBorder(new Color(40, 167, 69)));
                } else {
                    box.setBackground(COLOR_EMPTY);
                    box.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
                }
                mainPanel.add(box);
            }
        }

        // 5. Show the Pop-up
        JOptionPane.showMessageDialog(null, mainPanel, 
            h.getName() + " - 4 Week Momentum", 
            JOptionPane.PLAIN_MESSAGE);
    }
}