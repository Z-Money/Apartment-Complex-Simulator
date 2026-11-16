import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public static void main(String[] args) {
        System.setProperty("flatlaf.useNativeLibrary", "false");
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            ApartmentSimulator app = new ApartmentSimulator();
            new GUIController(app);
//            MainWindow window = new MainWindow("Apartment Complex Simulator");
//            StartPanel startPanel =
//                    new StartPanel(new GUIController(new ApartmentSimulator()));
//
//            window.addScreen("start", startPanel);
//            window.showScreen("start");
//
//            window.setVisible(true);
        });
    }

    public MainWindow(String title) {
        // TODO: Create window with title and set up card layout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);

        add(cardPanel);
    }

    public void addScreen(String name, JPanel panel) {
        // TODO: Add a named screen to the card container
        cardPanel.add(panel, name);
    }

    public void showScreen(String name) {
        // TODO: Display the screen with name
        cardLayout.show(cardPanel, name);
    }
}
