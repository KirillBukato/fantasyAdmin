import dto.TeamIncomeDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChGKProcessor {

    private List<TeamIncomeDTO> processBasic(Map<Integer, List<String>> data, Integer tours, Integer questions) {
        Map<String, Integer> sum = new HashMap<>();
        Map<Integer, List<String>> tour_win = new HashMap<>();
        Map<Integer, String> coffin_save = new HashMap<>();

        for (int i = 4; i <= data.size(); i++) {
            sum.put(data.get(i).get(1), (int)Double.parseDouble(data.get(i).get(3)));
        }

        System.out.println(sum);

        for (int i = 1; i <= tours; i++) {
            int column = 4 + i * (questions + 2);
            ArrayList<Integer> tourResults = new ArrayList<>();
            for (int j = 4; j <= data.size(); j++) {
                tourResults.add((int)Double.parseDouble(data.get(j).get(column)));
            }
            int bestResult = tourResults
                    .stream()
                    .max(Integer::compareTo)
                    .orElseGet(() -> 0);
            ArrayList<String> winners = new ArrayList<>();
            for (int j = 4; j <= data.size(); j++) {
                if ((int)Double.parseDouble(data.get(j).get(column)) == bestResult) {
                    winners.add(data.get(j).get(1));
                }
            }
            tour_win.put(i, winners);
        }

        System.out.println(tour_win);

        for (int i = 0; i < tours; i++) {
            for (int j = 1; j <= questions; j++) {
                int column = 5 + i * (questions + 2) + j;
                if ((int)Double.parseDouble(data.get(3).get(column)) == 1) {
                    for (int k = 4; k <= data.size(); k++) {
                        if (data.get(k).get(column).equals("1.0")) {
                            coffin_save.put(i * questions + j, data.get(k).get(1));
                            break;
                        }
                    }
                }
            }
        }

        System.out.println(coffin_save);

        ArrayList<TeamIncomeDTO> result = new ArrayList<>();

        return result;
    }

    public List<TeamIncomeDTO> process(String path, ChGKTableType tableType) {
        try {
            Map<Integer, List<String>> data = FastexcelHelper.readExcel(path);
            switch (tableType) {
                case BASIC:
                    return processBasic(data, 7, 12);
                default:
                    throw new IllegalArgumentException("Unknown table type");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
