package fantasyadmin.PreprocessInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.swing.*;

import fantasyadmin.api.RequestSender;
import fantasyadmin.dto.Team2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ChGKPreprocessData {
    private Map<String, Integer> sum;
    private Map<Integer, List<String>> tour_win;
    private Map<Integer, String> coffin_save;

    public JPanel createEditingPanel() {
        String[] teams = sum
                .keySet()
                .toArray(new String[0]);

        // from api
        ArrayList<Team2> allTeams = new ArrayList<>();
        RequestSender.getTeams();
        allTeams.add(new Team2(1L, "B", new ArrayList<>()));
        allTeams.add(new Team2(1L, "C", new ArrayList<>()));

        // Create main container panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title panel at the top
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Team Mapping");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Create the content panel that will go inside the scroll pane
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Header row
        JPanel headerRow = new JPanel(new GridLayout(1, 2, 10, 0));
        headerRow.setMaximumSize(new Dimension(600, 30));
        JLabel originalTeamHeader = new JLabel("Original Team Name");
        JLabel mappedTeamHeader = new JLabel("Map to Team");
        originalTeamHeader.setFont(new Font("Arial", Font.BOLD, 12));
        mappedTeamHeader.setFont(new Font("Arial", Font.BOLD, 12));
        headerRow.add(originalTeamHeader);
        headerRow.add(mappedTeamHeader);
        contentPanel.add(headerRow);
        contentPanel.add(Box.createVerticalStrut(10));

        // Team rows
        for (String team : teams) {
            JPanel row = new JPanel(new GridLayout(1, 2, 10, 0));
            row.setMaximumSize(new Dimension(600, 30));

            JTextField name = new JTextField(team);
            name.setFont(new Font("Arial", Font.PLAIN, 12));

            JComboBox<String> comboBox = new JComboBox<>(
                    allTeams
                            .stream()
                            .map(Team2::getName)
                            .toArray(String[]::new));
            comboBox.setFont(new Font("Arial", Font.PLAIN, 12));

            row.add(name);
            row.add(comboBox);

            contentPanel.add(row);
            contentPanel.add(Box.createVerticalStrut(5));
        }

        // Create scroll pane and add content panel to it
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons panel at the bottom
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.setMaximumSize(new Dimension(600, 40));

        JButton saveButton = new JButton("Save Changes");
        saveButton.setFont(new Font("Arial", Font.PLAIN, 12));
        saveButton.setFocusPainted(false);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 12));
        cancelButton.setFocusPainted(false);

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return mainPanel;
    }
}
