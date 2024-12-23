package fantasyadmin.api;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fantasyadmin.dto.TeamDTO;

public class RequestSender {
    public static List<TeamDTO> getPlayers() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:8080/teams");
            
            request.addHeader("Accept", "application/json");
            request.addHeader("x-csrf-token", "_csrf");
            request.addHeader("_csrf", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwZmQ1NDJhNWVjNjg0YTI4YTkyNWQ4Mzg3NmVmMjc3NCIsInN1YiI6ImFkbWluIiwiaWF0IjoxNzM0OTU4MzA0LCJuYmYiOjE3MzQ5NTgzMDQsImV4cCI6MTczNDk2MDEwNH0._dEbImHShjQG8TbD2OHw7TF6Pho3d68UDR0CoycshXM");
            
            CloseableHttpResponse response = httpClient.execute(request);
            
            try {
                HttpEntity entity = response.getEntity();
                
                if (entity != null) {
                    String result = EntityUtils.toString(entity);
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
}
