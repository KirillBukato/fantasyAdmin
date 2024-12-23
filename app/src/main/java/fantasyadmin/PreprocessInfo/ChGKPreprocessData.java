package fantasyadmin.PreprocessInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.swing.*;

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

        //from api
        ArrayList<Team2> allTeams = new ArrayList<>();
        allTeams.add(new Team2(1L, "B", new ArrayList<>()));

        JPanel result = new JPanel(new GridLayout(teams.length, 1));

        for (String team : teams) {
            JPanel row = new JPanel(new BorderLayout());
            JTextField name = new JTextField(team);
            JComboBox<String> comboBox = new JComboBox<>(
                    allTeams
                    .stream()
                    .map(Team2::getName)
                    .toArray(String[]::new));
            row.add(name, BorderLayout.WEST);
            row.add(comboBox, BorderLayout.EAST);
            result.add(row);
        }

        return result;
    }
}
