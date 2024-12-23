package fantasyadmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Team2 {
    Long id;
    String name;
    List<Player2> players;
}