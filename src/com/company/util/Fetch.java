package com.company.util;

import javax.json.*;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

// Fetches match information when given a username and api key.
public class Fetch {

    private String key;
    private String user;
    private ChampDict dict;


    public Fetch(String key, String user) {
        this.key = key;
        this.user = user;
        this.dict = new ChampDict();
    }

    // Returns the heroes played for the last num number of matches.
    public void prevMatches(int num) {
        try {
            // Searches summoner API to retrieve the accountId associated with the given username
            JsonObject summoner =
                    get("https://na1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + user + "?api_key=" + key);
            // Uses the accoundId to get information on the match history of that account
            JsonObject matches = get("https://na1.api.riotgames.com/lol/match/v4/matchlists/by-account/"
                    + summoner.getString("accountId") + "?api_key=" + key);
            for(int i = 0; i < num; i++) {

                int key = matches.getJsonArray("matches").getJsonObject(i).getInt("champion");
                System.out.print(dict.champ(key) + " ");
                JsonNumber matchId = matches.getJsonArray("matches").getJsonObject(i).getJsonNumber("gameId");
                matchSummary(matchId);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void matchSummary(JsonNumber matchId) {
        try{
            JsonObject match = get("https://na1.api.riotgames.com/lol/match/v4/matches/" + matchId + "?api_key=" + key);
            JsonObject teamStats = match.getJsonArray("teams").getJsonObject(0);
            System.out.println(teamStats.getString("win"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Helper method for performing HTTP requests.
    private JsonObject get(String uri) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonReader jsonReader = Json.createReader(new StringReader(response.body()));
        JsonObject json = jsonReader.readObject();

        return json;
    }
    private class ChampDict {

        private HashMap<String, String> dict;

        private ChampDict() {
            try{
                this.dict = new HashMap<String, String>();
                JsonObject champs = get("http://ddragon.leagueoflegends.com/cdn/10.16.1/data/en_US/champion.json").getJsonObject("data");
                for(String k : champs.keySet()) {
                    JsonObject champ = champs.getJsonObject(k);
                    dict.put(champ.getString("key"), champ.getString("name"));
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }

        }

        private String champ(int key) {
            String temp = Integer.toString(key);
            return dict.get(temp);
        }

    }

}
