import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.ArrayList;

public class ImportPanel extends JPanel {
    private GUIController controller;

    private JButton chooseFileButton;
    private JButton useConfigurationButton;
    private JButton backButton;

    private JLabel selectedFileLabel;
    private JLabel fileDetailsLabel;
    private JLabel fileStateLabel;
    private JLabel statusLabel;
    private JLabel initialSettingLabel;
    private JLabel capacityLabel;

    private JTextField complexNameField;
    private JTextField floorsField;
    private JTextField roomsPerFloorField;
    private JTextField initialValueField;

    private JComboBox<String> apartmentTypeBox;
    private ArrayList<JComboBox<String>> amenityStatusBoxes;
    private ArrayList<JTextField> distributionFields;

    private JPanel selectedFilePanel;
    private JPanel validationPanel;
    private JTextArea validationMessagesArea;
    private JFileChooser fileChooser;

    public ImportPanel(GUIController controller) {
        this.controller = controller;
        buildLayout();
        applyButtonFunc();
    }

    private void applyButtonFunc() {
        backButton.addActionListener(e -> controller.returnToStart());
        useConfigurationButton.addActionListener(e -> controller.handleUseImportedConfiguration());
    }

    private void buildLayout() {
        if (complexNameField == null) createImportComponents();
        removeAll();
        setLayout(new BorderLayout(0, 18));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        add(createImportHeader(), BorderLayout.NORTH);
        JPanel columns = new JPanel(new GridLayout(1, 2, 16, 0));
        columns.setOpaque(false);
        columns.setPreferredSize(new Dimension(1080, 480));
        columns.add(createFileColumn());
        columns.add(createPreviewColumn());
        JScrollPane scroll = new JScrollPane(columns);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
        add(createImportActions(), BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void createImportComponents() {
        chooseFileButton = new JButton("Browse Files");
        useConfigurationButton = new JButton("Use This Configuration");
        backButton = new JButton("Back");
        selectedFileLabel = importLabel("No file selected", 14, true);
        fileDetailsLabel = importLabel("Choose a settings CSV to begin.", 12, false);
        fileStateLabel = importLabel("Waiting", 12, false);
        statusLabel = importLabel("Awaiting validation", 15, true);
        initialSettingLabel = importLabel("INITIAL RESIDENTS", 12, true);
        capacityLabel = importLabel("Capacity: -", 12, false);
        complexNameField = importTextField("Complex name");
        floorsField = importTextField("2-8");
        roomsPerFloorField = importTextField("4-12");
        initialValueField = importTextField("Initial value");
        apartmentTypeBox = createApartmentTypeBox();
        createAmenityStatusBoxes();
        distributionFields = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            distributionFields.add(importTextField("0-100"));
        }
        createFileChooser();
    }

    private JComboBox<String> createApartmentTypeBox() {
        JComboBox<String> box = new JComboBox<>(new String[]{
                "1 - Singles only", "2 - Randomized Singles & Doubles"
        });
        box.setEditable(false);
        box.setSelectedIndex(-1);
        styleImportInput(box);
        return box;
    }

    private void createAmenityStatusBoxes() {
        String[][] options = {
                {"Not Offered", "Full Operation", "Half Operation", "Closed"},
                {"Not Offered", "Full Operation", "Closed"},
                {"Not Offered", "Full Operation", "Reduced Machines", "Closed"},
                {"Not Offered", "Full Operation", "Reduced Force", "Not Functioning"}
        };
        amenityStatusBoxes = new ArrayList<>();
        for (String[] statuses : options) {
            JComboBox<String> box = new JComboBox<>(statuses);
            box.setEditable(false);
            box.setSelectedIndex(-1);
            styleImportInput(box);
            amenityStatusBoxes.add(box);
        }
    }

    private void createFileChooser() {
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Configuration File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Settings CSV (*.csv)", "csv"));
        chooseFileButton.addActionListener(e -> chooseImportFile());
    }

    private void chooseImportFile() {
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File selected = fileChooser.getSelectedFile();
        showSelectedFile(selected.getName());
        controller.handleImportFileChosen(selected.getAbsolutePath());
    }

    private JPanel createImportHeader() {
        JPanel header = new JPanel(new BorderLayout(0, 7));
        header.setOpaque(false);
        header.add(importLabel("Import Configuration File", 26, true), BorderLayout.NORTH);
        header.add(importLabel(
                "Select a settings CSV previously exported by the simulator.",
                15, false), BorderLayout.CENTER);
        return header;
    }

    private JPanel createFileColumn() {
        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setOpaque(false);
        content.add(createFilePicker(), BorderLayout.NORTH);
        content.add(createSelectedFilePanel(), BorderLayout.CENTER);
        JPanel aligned = new JPanel(new BorderLayout());
        aligned.setOpaque(false);
        aligned.add(content, BorderLayout.NORTH);
        return importCard("Choose File", aligned);
    }

    private JPanel createFilePicker() {
        styleImportButton(chooseFileButton, true);
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonRow.setOpaque(false);
        buttonRow.add(chooseFileButton);
        return buttonRow;
    }

    private JPanel createSelectedFilePanel() {
        selectedFilePanel = new JPanel(new BorderLayout(12, 0));
        selectedFilePanel.putClientProperty("FlatLaf.style",
                "background: #f0f3f7; arc: 20; border: 12,14,12,14,#dce0e5,1,20");
        JLabel badge = importLabel("CSV", 16, true);
        badge.setForeground(new Color(0x198754));
        JPanel badgePanel = new JPanel(new BorderLayout());
        badgePanel.putClientProperty("FlatLaf.style", "background: #ffffff; arc: 18");
        badgePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        badgePanel.add(badge, BorderLayout.CENTER);
        JPanel text = new JPanel(new BorderLayout(0, 4));
        text.setOpaque(false);
        text.add(selectedFileLabel, BorderLayout.NORTH);
        text.add(fileDetailsLabel, BorderLayout.CENTER);
        selectedFilePanel.add(badgePanel, BorderLayout.WEST);
        selectedFilePanel.add(text, BorderLayout.CENTER);
        selectedFilePanel.add(fileStateLabel, BorderLayout.EAST);
        return selectedFilePanel;
    }

    private JPanel createPreviewColumn() {
        JScrollPane scroll = new JScrollPane(createImportTable());
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setOpaque(false);
        content.add(createRoundedPreview(scroll), BorderLayout.CENTER);
        content.add(createValidationPanel(), BorderLayout.SOUTH);
        return importCard("Configuration Preview", content);
    }

    private JPanel createRoundedPreview(JComponent content) {
        JPanel rounded = new JPanel(new BorderLayout()) {
            @Override
            public void paint(Graphics graphics) {
                Graphics2D copy = (Graphics2D) graphics.create();
                copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                copy.clip(new RoundRectangle2D.Double(
                        0, 0, getWidth(), getHeight(), 24, 24));
                super.paint(copy);
                copy.dispose();
            }
        };
        rounded.setOpaque(false);
        rounded.add(content, BorderLayout.CENTER);
        return rounded;
    }

    private JPanel createImportTable() {
        JPanel table = new JPanel(new GridBagLayout());
        table.setBackground(Color.WHITE);
        JLabel[] titles = {importLabel("COMPLEX", 12, true), importLabel("FLOORS", 12, true),
                importLabel("ROOMS / FLOOR", 12, true), importLabel("APARTMENT TYPE", 12, true),
                initialSettingLabel, importLabel("AMENITIES", 12, true),
                importLabel("DISTRIBUTION", 12, true)};
        JComponent[] editors = {complexNameField, floorsField, roomsPerFloorField,
                apartmentTypeBox, createInitialImportEditor(),
                createAmenityEditors(), createDistributionEditors()};
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        for (int i = 0; i < titles.length; i++) {
            c.gridy = i;
            c.gridx = 0;
            c.weightx = 0.32;
            table.add(importCell(titles[i], true), c);
            c.gridx = 1;
            c.weightx = 0.68;
            table.add(importCell(editors[i], false), c);
        }
        c.gridy = titles.length;
        c.gridx = 0;
        c.gridwidth = 2;
        c.weighty = 1;
        table.add(Box.createVerticalGlue(), c);
        return table;
    }

    private JPanel importCell(JComponent component, boolean heading) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setBackground(new Color(heading ? 0xFAFBFC : 0xFFFFFF));
        cell.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xEDF0F2)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        cell.add(component, BorderLayout.NORTH);
        return cell;
    }

    private JPanel createInitialImportEditor() {
        JPanel input = new JPanel(new BorderLayout(8, 0));
        input.setOpaque(false);
        input.add(initialValueField, BorderLayout.CENTER);
        input.add(capacityLabel, BorderLayout.EAST);
        return input;
    }

    private JPanel createAmenityEditors() {
        JPanel rows = new JPanel(new GridLayout(4, 1, 0, 8));
        rows.setOpaque(false);
        String[] names = {"Laundry", "Pool", "Gym", "Security"};
        for (int i = 0; i < names.length; i++) {
            JPanel row = new JPanel(new BorderLayout(8, 0));
            row.setOpaque(false);
            JLabel name = importLabel(names[i], 12, false);
            name.setPreferredSize(new Dimension(86, 34));
            row.add(name, BorderLayout.WEST);
            row.add(amenityStatusBoxes.get(i), BorderLayout.CENTER);
            rows.add(row);
        }
        return rows;
    }

    private JPanel createDistributionEditors() {
        JPanel rows = new JPanel(new GridLayout(4, 1, 0, 8));
        rows.setOpaque(false);
        String[] names = {"No-Maintenance", "Low-Maintenance",
                "Standard-Maintenance", "High-Maintenance"};
        for (int i = 0; i < names.length; i++) {
            JPanel row = new JPanel(new BorderLayout(8, 0));
            row.setOpaque(false);
            JLabel name = importLabel(names[i], 12, false);
            name.setPreferredSize(new Dimension(132, 34));
            JPanel input = new JPanel(new BorderLayout(5, 0));
            input.setOpaque(false);
            input.add(distributionFields.get(i), BorderLayout.CENTER);
            input.add(importLabel("%", 13, false), BorderLayout.EAST);
            row.add(name, BorderLayout.WEST);
            row.add(input, BorderLayout.CENTER);
            rows.add(row);
        }
        return rows;
    }

    private JPanel createValidationPanel() {
        validationPanel = new JPanel(new BorderLayout(0, 6));
        validationPanel.putClientProperty("FlatLaf.style",
                "background: #f0f3f7; arc: 20; border: 12,14,12,14,#dce0e5,1,20");
        validationMessagesArea = new JTextArea("Import a file to review or edit its values.", 3, 0);
        validationMessagesArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        validationMessagesArea.setForeground(new Color(0x6E747D));
        validationMessagesArea.setEditable(false);
        validationMessagesArea.setLineWrap(true);
        validationMessagesArea.setWrapStyleWord(true);
        validationMessagesArea.setOpaque(false);
        JScrollPane messages = new JScrollPane(validationMessagesArea);
        messages.setBorder(BorderFactory.createEmptyBorder());
        messages.setOpaque(false);
        messages.getViewport().setOpaque(false);
        validationPanel.add(statusLabel, BorderLayout.NORTH);
        validationPanel.add(messages, BorderLayout.CENTER);
        return validationPanel;
    }

    private JPanel importCard(String title, JPanel content) {
        JPanel card = new JPanel(new BorderLayout(0, 16));
        card.putClientProperty("FlatLaf.style", "background: #f5f6f8; arc: 28");
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.add(importLabel(title, 20, true), BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JPanel createImportActions() {
        styleImportButton(backButton, false);
        styleImportButton(useConfigurationButton, true);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        actions.add(backButton);
        actions.add(useConfigurationButton);
        return actions;
    }

    private JLabel importLabel(String text, int size, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size));
        label.setForeground(new Color(bold ? 0x20242A : 0x6E747D));
        return label;
    }

    private JTextField importTextField(String placeholder) {
        JTextField field = new JTextField();
        styleImportInput(field);
        field.putClientProperty("JTextField.placeholderText", placeholder);
        return field;
    }

    private void styleImportInput(JComponent component) {
        component.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        component.setPreferredSize(new Dimension(0, 34));
        component.setMinimumSize(new Dimension(0, 34));
        component.putClientProperty("FlatLaf.style",
                "arc: 10; background: #ffffff; borderColor: #dce0e5; " +
                        "focusedBorderColor: #000000; focusWidth: 0");
    }

    private void styleImportButton(JButton button, boolean primary) {
        String fill = primary ? "#315ff4" : "#ffffff";
        button.setFont(new Font("Segoe UI", primary ? Font.BOLD : Font.PLAIN, 15));
        button.setMargin(new Insets(12, 18, 12, 18));
        button.putClientProperty("JComponent.minimumHeight", 46);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.putClientProperty("FlatLaf.style",
                "arc: 20; focusWidth: 0; innerFocusWidth: 0; background: " + fill + "; " +
                        "foreground: " + (primary ? "#ffffff" : "#20242a") + "; " +
                        "borderColor: " + (primary ? "#315ff4" : "#dce0e5") + "; " +
                        "focusedBorderColor: #000000; focusedBackground: " + fill + "; " +
                        "hoverBackground: " + fill + "; pressedBackground: " + fill);
    }

    public void showSelectedFile(String fileName) {
        selectedFileLabel.setText(fileName);
        fileDetailsLabel.setText("Selected configuration file");
        fileStateLabel.setText("Loaded");
        selectedFilePanel.putClientProperty("FlatLaf.style",
                "background: #e9f7ef; arc: 20; border: 12,14,12,14,#bfe2cc,1,20");
    }

    public void showPreview(String[] values) {
        if (values == null || values.length < 5) return;
        complexNameField.setText(values[0]);
        floorsField.setText(values[1]);
        roomsPerFloorField.setText(values[2]);
        apartmentTypeBox.setSelectedIndex(values[3].equals("1") ? 0 : 1);
        initialValueField.setText(values[5]);
        for (int i = 0; i < 4; i++) {
            distributionFields.get(i).setText(values[6+i]);
        }
        capacityLabel.setText("Capacity: " + values[11]);
        for (JComboBox<String> box : amenityStatusBoxes) box.setSelectedIndex(0);
        for (int i = 0; i < (values.length - 12) / 2; i++) {
            String[] amenityStatusArr = values[13+2*i].toLowerCase().split("_");
            for (int j = 0; j < amenityStatusArr.length; j++) amenityStatusArr[j] = amenityStatusArr[j].substring(0, 1).toUpperCase() + amenityStatusArr[j].substring(1);
            String amenityStatus = String.join(" ", amenityStatusArr);
            amenityStatusBoxes.get(i).setSelectedItem(amenityStatus);
        }
    }

    public void showValidationStatus(ArrayList<String> invalidSettings) {
        boolean valid = invalidSettings != null && invalidSettings.isEmpty();
        String messages = valid ? "No corrections are required."
                : invalidSettings == null ? "Validate the imported settings."
                : String.join("\n", invalidSettings);
        setValidationDisplay(valid, messages);
    }

    public void setValidationDisplay(boolean valid, String messages) {
        String background = valid ? "#e9f7ef" : "#fff0f1";
        String border = valid ? "#bfe2cc" : "#f0b7bc";
        Color foreground = new Color(valid ? 0x155F39 : 0x8D2028);
        validationPanel.putClientProperty("FlatLaf.style",
                "arc: 20; background: " + background + "; border: 12,14,12,14," + border + ",1,20");
        statusLabel.setText(valid ? "All imported settings are valid."
                : "Correct the following values.");
        statusLabel.setForeground(foreground);
        validationMessagesArea.setForeground(foreground);
        validationMessagesArea.setText(messages);
        validationMessagesArea.setCaretPosition(0);
        validationPanel.revalidate();
        validationPanel.repaint();
    }

    public String[] getImportFormValues() {
        ArrayList<String> formValues = new ArrayList<>();
        formValues.add(String.format("Complex Name,%s", complexNameField.getText()));
        formValues.add(String.format("Number of Floors,%s", floorsField.getText()));
        formValues.add(String.format("Rooms per Floor,%s", roomsPerFloorField.getText()));
        String apartmentType = apartmentTypeBox.getSelectedIndex() == 0 ? "1" : "2";
        formValues.add(String.format("Apartment Type,%s", apartmentType));
        formValues.add(String.format("Initial Number of Residents,%s", initialValueField.getText()));
        // Maybe change to only show offered amenities
        String[] amenityNames = new String[]{"Laundry Machines", "Pool", "Gym", "Security"};
        for (int i = 0; i < 4; i++) {
            if (amenityStatusBoxes.get(i).getSelectedIndex() != 0) {
                String amenityName = amenityNames[i];
                String amenityStatus = String.valueOf(amenityStatusBoxes.get(i).getSelectedItem());
                amenityStatus = String.join("_", amenityStatus.toUpperCase().split(" "));
                formValues.add(String.format("Amenity,%s,%s", amenityName, amenityStatus));
            }
        }
        StringBuilder distributionStr = new StringBuilder("Resident Distribution");
        for (JTextField distributionPercentage : distributionFields)
            distributionStr.append(String.format(",%s", distributionPercentage.getText()));
        formValues.add(distributionStr.toString());
        return formValues.toArray(new String[0]);
    }
}