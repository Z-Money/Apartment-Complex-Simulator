import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;

public class ApartmentSimulator {
    public static void main(String[] args) {
        // Initialize FlatLaf styling
        FlatLightLaf.setup();

        // Create test Swing app
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("FlatLaf Styling");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);

            JButton button = new JButton("Click Me!");
            button.setSize(150, 50);
            frame.add(button);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
