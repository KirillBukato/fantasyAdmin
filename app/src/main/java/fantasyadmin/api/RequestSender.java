package fantasyadmin.api;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fantasyadmin.dto.IncomeType;
import fantasyadmin.dto.TeamDTO;
import fantasyadmin.dto.TeamIncomeDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

public class RequestSender {
    private static String requestToken;

    public RequestSender() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost request = new HttpPost("http://localhost:8080/login");
            
            request.addHeader("Accept", "application/json");
            request.addHeader("Content-Type", "application/json");
            
            LoginRequest loginData = new LoginRequest("admin", "Svetlana Vidisheva is the best!");
            
            ObjectMapper mapper = new ObjectMapper();
            String jsonBody = mapper.writeValueAsString(loginData);
            
            StringEntity entity = new StringEntity(jsonBody);
            request.setEntity(entity);
            
            CloseableHttpResponse response = httpClient.execute(request);
            
            try {
                System.out.println(response.getFirstHeader("_csrf"));
                requestToken = response.getFirstHeader("_csrf").getValue();
                
            } finally {
                response.close();
                httpClient.close();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<TeamDTO> getTeams() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:8080/teams");
            
            request.addHeader("Accept", "application/json");
            request.addHeader("x-csrf-token", "_csrf");
            request.addHeader("_csrf", requestToken);
            
            CloseableHttpResponse response = httpClient.execute(request);
            
            try {
                HttpEntity entity = response.getEntity();
                
                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    System.out.println("Response: " + result);
                    ObjectMapper mapper = new ObjectMapper();
                    
                    List<TeamDTO> teams = mapper.readValue(result, new TypeReference<List<TeamDTO>>(){});
                    
                    teams.forEach(team -> System.out.println("Parsed team: " + team));
                    
                    for(TeamDTO team : teams) {
                        System.out.println("Team ID: " + team.getId());
                        System.out.println("Team Name: " + team.getName());
                        System.out.println("Players count: " + team.getPlayers().size());
                        System.out.println("---------------");
                    }
                    return teams;
                } else {
                    return null;
                }       
            } finally {
                response.close();
                httpClient.close();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

        public static void sendIncomes(List<TeamIncomeDTO> data) {
        try {
            List<TeamIncome> teamIncomes = data.stream().map(dto -> new TeamIncome(
                    dto.getId(),
                    dto.getType(),
                    dto.getDescription(),
                    dto.getAmount(),
                    new Team(dto.getTeam_id())
            )).collect(Collectors.toList());
            
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost request = new HttpPost("http://localhost:8080/teamIncomes");
            
            request.addHeader("Accept", "application/json");
            request.addHeader("Content-Type", "application/json");
            request.addHeader("x-csrf-token", "_csrf");
            request.addHeader("_csrf", requestToken);
            
            ObjectMapper mapper = new ObjectMapper();
            String jsonBody = mapper.writeValueAsString(teamIncomes);
            
            StringEntity entity = new StringEntity(jsonBody);
            request.setEntity(entity);
            
            CloseableHttpResponse response = httpClient.execute(request);
            
            try {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    String result = EntityUtils.toString(responseEntity);
                    System.out.println("Response: " + result);
                }
            } finally {
                response.close();
                httpClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class LoginRequest {
    private String login;
    private String password;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Team {
    private Long id;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TeamIncome {
    private Long id;
    private IncomeType type;
    private String description;
    private Integer amount;

    private Team team;
}