package fantasyadmin;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class MainWindow extends JFrame {

    private final JComboBox<String> typeComboBox;
    private final JTextField fileTextField;

    public MainWindow() {
        super("ЧГК Фентези. Клиент администратора");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setSize(600, 200);
        {
            JPanel filePanel = new JPanel(new BorderLayout());

            JLabel fileLabel = new JLabel("Choose excel file");
            filePanel.add(fileLabel, BorderLayout.NORTH);

            fileTextField = new JTextField();
            filePanel.add(fileTextField, BorderLayout.SOUTH);

            JButton fileButton = new JButton("Choose");
            fileButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showOpenDialog(null);
                fileChooser.getSelectedFile();
                fileTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            });
            filePanel.add(fileButton, BorderLayout.CENTER);

            topPanel.add(filePanel, BorderLayout.WEST);
        }

        {
            JPanel typePanel = new JPanel(new BorderLayout());

            JLabel typeLabel = new JLabel("Choose type");
            typePanel.add(typeLabel, BorderLayout.NORTH);

            typeComboBox = new JComboBox<>(
                    Arrays.stream(ChGKTableType.values())
                            .map(ChGKTableType::getName)
                            .toArray(String[]::new)
            );
            typePanel.add(typeComboBox, BorderLayout.CENTER);
            topPanel.add(typePanel, BorderLayout.CENTER);
        }

        JButton preprocessButton = new JButton("Preprocess");
        preprocessButton.addActionListener(e -> {
            ChGKTableType type = ChGKTableType.getByName(
                    (String) typeComboBox.getSelectedItem()
            );
            ChGKProcessor processor = new ChGKProcessor();

            String path = fileTextField.getText();

            getContentPane().add(processor.preprocess(path, type).createEditingPanel(), BorderLayout.CENTER);

        });

        topPanel.add(preprocessButton, BorderLayout.EAST);
        getContentPane().add(topPanel, BorderLayout.NORTH);
        setVisible(true);
    }


}
