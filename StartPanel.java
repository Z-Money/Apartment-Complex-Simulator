import javax.swing.*;
import java.awt.*;

public class StartPanel extends JPanel  {
    private GUIController controller;
    private JButton manualButton;
    private JButton defaultButton;
    private JButton importButton;

    public StartPanel(GUIController controller) {
        this.controller = controller;
        manualButton = new JButton("Create Configuration");
        defaultButton = new JButton("Use Default Settings");
        importButton = new JButton("Choose Configuration File");
        buildLayout();
        addButtonFunc();
    }

    private void addButtonFunc() {
        manualButton.addActionListener(e -> controller.handleSetupModeSelected("manual"));
        defaultButton.addActionListener(e -> controller.handleSetupModeSelected("default"));
        importButton.addActionListener(e -> controller.handleSetupModeSelected("import"));
    }

    private void buildLayout() {
        removeAll();
        setLayout(new GridBagLayout()); // Sets layout manager to GridBagLayout
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(32, 22, 24, 22)); // Adds padding around edges
        Font font = new Font("Segoe UI", Font.PLAIN, 14); // Creates new font to be used
        GridBagConstraints constraints = new GridBagConstraints(); // Initializes constraints manager
        constraints.gridx = 0; // First column
        constraints.weightx = 1; // Distribute extra horizontal space
        constraints.fill = GridBagConstraints.HORIZONTAL; // Component stretches to fit horizontally
        constraints.gridy = 0; // First row
        add(createStartHeading(font), constraints); // Add heading to first row
        constraints.gridy = 1; // Second row
        constraints.insets = new Insets(40, 0, 0, 0); // Add padding above next component
        add(createStartOptions(font), constraints); // Add start options to second row
        constraints.gridy = 2; // Third row
        constraints.weighty = 1; // Distribute extra space vertically
        constraints.insets = new Insets(0, 0, 0, 0); // Resets padding for next component
        add(Box.createVerticalGlue(), constraints); // Adds invisible component to fill extra vertical empty space below start options
        revalidate(); // Recalculates layout with layout manager
        repaint(); // Redraws container
    }

    private JPanel createStartHeading(Font font) {
        JPanel heading = new JPanel(new BorderLayout(0, 8));
        heading.setOpaque(false);
        JLabel welcome = new JLabel("Welcome to the Apartment Complex Simulator", SwingConstants.CENTER);
        welcome.setFont(font.deriveFont(Font.BOLD, 30f));
        welcome.setForeground(new Color(0x20242A));
        JLabel description = new JLabel("Choose how you'd like to configure your settings to begin", SwingConstants.CENTER);
        description.setFont(font.deriveFont(15f));
        description.setForeground(new Color(0x6E747D));
        heading.add(welcome, BorderLayout.NORTH);
        heading.add(description, BorderLayout.CENTER);
        return heading;
    }

    private JPanel createStartOptions(Font font) {
        JPanel options = new JPanel(new GridLayout(1, 3, 16, 0));
        options.setOpaque(false);
        styleStartButton(defaultButton, "Use Default Settings", false, font);
        styleStartButton(manualButton, "Create Configuration", true, font);
        styleStartButton(importButton,
                "Choose Configuration File", false, font);
        options.add(createStartOption(
                "Default Setup",
                "Get started with default program settings.",
                defaultButton, font));
        options.add(createStartOption(
                "Manual Setup",
                "Enter all settings manually.",
                manualButton, font));
        options.add(createStartOption(
                "Import Configuration",
                "Load a previously exported settings CSV file.",
                importButton, font));
        return options;
    }

    private JPanel createStartOption(String title, String text, JButton button, Font font) {
        JPanel option = new JPanel(new BorderLayout(0, 12));
        option.setOpaque(false);
        option.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(font.deriveFont(Font.BOLD, 18f));
        titleLabel.setForeground(new Color(0x20242A));
        JLabel description = new JLabel("<html><center>" + text + "</center></html>", SwingConstants.CENTER);
        description.setFont(font);
        description.setForeground(new Color(0x6E747D));
        option.add(titleLabel, BorderLayout.NORTH);
        option.add(description, BorderLayout.CENTER);
        option.add(button, BorderLayout.SOUTH);
        return option;
    }

    private void styleStartButton(JButton button, String text, boolean primary, Font font) {
        String fill = primary ? "#315ff4" : "#ffffff";
        String foreground = primary ? "#ffffff" : "#20242a";
        String border = primary ? "#315ff4" : "#787B80";
        String hoverFill = primary ? "#2A52D4" : "#F2FAFF";
        button.setText(text);
        button.setFont(font.deriveFont(primary ? Font.BOLD : Font.PLAIN, 14f));
        button.setPreferredSize(new Dimension(0, 56));
        button.setMinimumSize(new Dimension(0, 56));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.putClientProperty("FlatLaf.style",
                "arc: 22; borderWidth: 1; focusWidth: 0; innerFocusWidth: 0; " +
                        "background: " + fill + "; foreground: " + foreground + "; " +
                        "borderColor: " + border + "; focusedBorderColor: #000000; " +
                        "focusedBackground: " + fill + "; focusedForeground: " + foreground + "; " +
                        "hoverBackground: " + hoverFill + "; pressedBackground: " + fill);
    }
}
