package fantasyadmin;

import fantasyadmin.PreprocessInfo.ChGKPreprocessData;
import fantasyadmin.dto.TeamIncomeDTO;

import javax.swing.*;

import fantasyadmin.api.RequestSender;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MainWindow extends JFrame {
    private final JComboBox<String> typeComboBox;
    private final JTextField fileTextField;

    
    private final RequestSender requestSender;

    private ChGKPreprocessData data;

    public MainWindow() {
        super("ЧГК Фентези. Клиент администратора");
        requestSender = new RequestSender();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(600, 400));

        // Create main container with padding
        JPanel mainContainer = new JPanel(new BorderLayout(10, 10));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create top panel with grid layout for even spacing
        JPanel topPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        topPanel.setPreferredSize(new Dimension(800, 80));

        // File selection panel
        JPanel filePanel = new JPanel(new BorderLayout(5, 5));
        filePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Excel File Selection"
                ),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel fileLabel = new JLabel("File Path:");
        fileLabel.setFont(new Font("Arial", Font.BOLD, 12));

        fileTextField = new JTextField();
        fileTextField.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton fileButton = new JButton("Browse...");
        fileButton.setFont(new Font("Arial", Font.PLAIN, 12));
        fileButton.setFocusPainted(false);

        JPanel fileInputPanel = new JPanel(new BorderLayout(5, 0));
        fileInputPanel.add(fileTextField, BorderLayout.CENTER);
        fileInputPanel.add(fileButton, BorderLayout.EAST);

        filePanel.add(fileLabel, BorderLayout.NORTH);
        filePanel.add(fileInputPanel, BorderLayout.CENTER);

        fileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                fileTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        // Type selection panel
        JPanel typePanel = new JPanel(new BorderLayout(5, 5));
        typePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Type Selection"
                ),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel typeLabel = new JLabel("Select Type:");
        typeLabel.setFont(new Font("Arial", Font.BOLD, 12));

        typeComboBox = new JComboBox<>(
                Arrays.stream(ChGKTableType.values())
                        .map(ChGKTableType::getName)
                        .toArray(String[]::new)
        );
        typeComboBox.setFont(new Font("Arial", Font.PLAIN, 12));

        typePanel.add(typeLabel, BorderLayout.NORTH);
        typePanel.add(typeComboBox, BorderLayout.CENTER);

        // Preprocess button panel
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JButton preprocessButton = new JButton("Preprocess Data");
        preprocessButton.setFont(new Font("Arial", Font.BOLD, 12));
        preprocessButton.setFocusPainted(false);
        preprocessButton.setPreferredSize(new Dimension(150, 40));

        // Create a wrapper panel to center the button vertically
        JPanel buttonWrapper = new JPanel(new GridBagLayout());
        buttonWrapper.add(preprocessButton);

        buttonPanel.add(buttonWrapper, BorderLayout.CENTER);

        preprocessButton.addActionListener(e -> {
            if (fileTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please select an Excel file first.",
                        "Input Required",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            ChGKTableType type = ChGKTableType.getByName(
                    (String) typeComboBox.getSelectedItem()
            );
            ChGKProcessor processor = new ChGKProcessor();

            String path = fileTextField.getText();

            // Remove existing center component if it exists
            Component[] components = getContentPane().getComponents();
            for (Component component : components) {
                if (getContentPane().getLayout() instanceof BorderLayout &&
                        ((BorderLayout) getContentPane().getLayout()).getConstraints(component) == BorderLayout.CENTER) {
                    getContentPane().remove(component);
                    break;
                }
            }

            data = processor.preprocess(path, type);

            // Add new panel
            getContentPane().add(data.createEditingPanel(), BorderLayout.CENTER);
            revalidate();
            repaint();
        });

        // Add all panels to top panel
        topPanel.add(filePanel);
        topPanel.add(typePanel);
        topPanel.add(buttonPanel);

        // Add top panel to main container
        mainContainer.add(topPanel, BorderLayout.NORTH);

        // Buttons panel at the bottom
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.setMaximumSize(new Dimension(600, 40));

        JButton saveButton = new JButton("Apply");
        saveButton.setFont(new Font("Arial", Font.PLAIN, 12));
        saveButton.addActionListener(e -> {
            ArrayList<TeamIncomeDTO> incomes = data.getIncomes();
            //send on server
        });
        saveButton.setFocusPainted(false);

        buttonsPanel.add(saveButton);

        mainContainer.add(buttonsPanel, BorderLayout.SOUTH);

        // Set content pane
        setContentPane(mainContainer);
        setLocationRelativeTo(null);  // Center on screen
        setVisible(true);
    }
}