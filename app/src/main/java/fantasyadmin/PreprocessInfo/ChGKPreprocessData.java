package fantasyadmin.PreprocessInfo;

import fantasyadmin.dto.IncomeType;
import fantasyadmin.dto.TeamDTO;
import fantasyadmin.dto.TeamIncomeDTO;
import lombok.Data;

import javax.swing.*;

import fantasyadmin.api.RequestSender;
import fantasyadmin.dto.Team2;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ChGKPreprocessData {
    private Map<String, Integer> sum;
    private Map<Integer, List<String>> tour_win;
    private Map<Integer, String> coffin_save;

    private ArrayList<JTextField> tournamentNames = new ArrayList<>();
    private ArrayList<JComboBox<String>> basicNames = new ArrayList<>();
    private HashMap<String, Long> teamIdMap = new HashMap<>();

    public ChGKPreprocessData(Map<String, Integer> sum, Map<Integer, List<String>> tour_win, Map<Integer, String> coffin_save) {
        this.sum = sum;
        this.tour_win = tour_win;
        this.coffin_save = coffin_save;
    }

    public JPanel createEditingPanel() {
        String[] teams = sum
                .keySet()
                .toArray(new String[0]);

        // from api
        List<TeamDTO> allTeams = RequestSender.getTeams();

        for (TeamDTO team : allTeams) {
            teamIdMap.put(team.getName(), team.getId());
        }

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
                            .map(TeamDTO::getName)
                            .toArray(String[]::new));
            comboBox.setFont(new Font("Arial", Font.PLAIN, 12));

            row.add(name);
            tournamentNames.add(name);
            row.add(comboBox);
            basicNames.add(comboBox);

            contentPanel.add(row);
            contentPanel.add(Box.createVerticalStrut(5));
        }

        // Create scroll pane and add content panel to it
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    public ArrayList<TeamIncomeDTO> getIncomes() {
        Map<String, String> nameMap = new HashMap<>();
        for (int i = 0; i < basicNames.size(); i++) {
            nameMap.put(tournamentNames.get(i).getText(), basicNames.get(i).getSelectedItem().toString());
        }

        ArrayList<TeamIncomeDTO> result = new ArrayList<>();

        sum.entrySet()
                .forEach(entry -> {
                    TeamIncomeDTO teamIncomeDTO = new TeamIncomeDTO();
                    teamIncomeDTO.setAmount(10 * entry.getValue());
                    teamIncomeDTO.setTeam_id(teamIdMap.get(nameMap.get(entry.getKey())));
                    teamIncomeDTO.setType(IncomeType.ChGK_Sum);
                    teamIncomeDTO.setDescription("Sum on the tournament is " + entry.getValue());
                    result.add(teamIncomeDTO);
                    System.out.println(teamIncomeDTO);
                });

        tour_win.entrySet()
                .forEach(entry -> {
                    entry.getValue().forEach(
                            value -> {
                                TeamIncomeDTO teamIncomeDTO = new TeamIncomeDTO();
                                teamIncomeDTO.setAmount(5);
                                teamIncomeDTO.setTeam_id(teamIdMap.get(nameMap.get(value)));
                                teamIncomeDTO.setType(IncomeType.ChGK_BestInTour);
                                teamIncomeDTO.setDescription("Was best on " + entry.getKey() + " tour");
                                result.add(teamIncomeDTO);
                                System.out.println(teamIncomeDTO);
                            }
                    );
                });

        coffin_save.entrySet()
                .forEach(entry -> {
                    TeamIncomeDTO teamIncomeDTO = new TeamIncomeDTO();
                    teamIncomeDTO.setAmount(10);
                    teamIncomeDTO.setTeam_id(teamIdMap.get(nameMap.get(entry.getValue())));
                    teamIncomeDTO.setType(IncomeType.ChGK_CoffinSave);
                    teamIncomeDTO.setDescription("Saved " + entry.getKey() + " question");
                    result.add(teamIncomeDTO);
                    System.out.println(teamIncomeDTO);
                });

        return result;
    }
}
