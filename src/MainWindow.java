import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        super("ЧГК Фентези. Клиент администратора");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        JPanel contentPanel = new JPanel();

        JPanel topPanel = new JPanel(new BorderLayout());
        {
            JPanel filePanel = new JPanel(new BorderLayout());

            JLabel fileLabel = new JLabel("Choose excel file");
            filePanel.add(fileLabel, BorderLayout.NORTH);

            JButton fileButton = new JButton("Choose");
            fileButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showOpenDialog(null);
                fileChooser.getSelectedFile();
            });
            filePanel.add(fileButton, BorderLayout.CENTER);
            topPanel.add(filePanel, BorderLayout.WEST);
        }

        {
            JPanel typePanel = new JPanel(new BorderLayout());

            JLabel typeLabel = new JLabel("Choose type");
            typePanel.add(typeLabel, BorderLayout.NORTH);


        }

        contentPanel.add(topPanel, BorderLayout.NORTH);

        getContentPane().add(contentPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}
