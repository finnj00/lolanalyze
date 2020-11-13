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
    private String accountId;
    private ChampDict dict;


    public Fetch(String key, String user) {
        try{
            JsonObject summoner =
                    get("https://na1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + user + "?api_key=" + key);
            this.accountId = summoner.getString("accountId");
            this.key = key;
            this.dict = new ChampDict();
        } catch(Exception ex) {
            throw new IllegalArgumentException();
        }
    }

    // Returns the heroes played for the last num number of matches.
    public void prevMatches(int num) {
        try {
            JsonArray matches = get("https://na1.api.riotgames.com/lol/match/v4/matchlists/by-account/"
                    + accountId + "?api_key=" + key).getJsonArray("matches");
            for(int i = 0; i < num; i++) {
                JsonObject match = matches.getJsonObject(i);
                int key = match.getInt("champion");
                System.out.print(dict.champ(key) + ": ");
                JsonNumber matchId = match.getJsonNumber("gameId");
                matchSummary(matchId, key); // Prints small summary for each champ
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Used for printing a small summary of a given match including win/loss and kda.
    public void matchSummary(JsonNumber matchId, int champId) {
        try{
            JsonObject match = get("https://na1.api.riotgames.com/lol/match/v4/matches/" + matchId + "?api_key=" + key);
            JsonObject stats = getParticipant(match.getJsonArray("participants"), champId).getJsonObject("stats");
            if(stats.getBoolean("win")) {
                System.out.print("Win ");
            } else {
                System.out.print("Loss ");
            }
            System.out.println(stats.getInt("kills") + "/" +
                               stats.getInt("deaths") + "/" +
                               stats.getInt("assists"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Helper method for getting participant in a match.
    private JsonObject getParticipant(JsonArray participants, int champId) {
        int i = 0;
        while(i < 10 && !(participants.getJsonObject(i).getInt("championId") == (champId))) {
            i++;
        }
        return participants.getJsonObject(i);
    }

    // Helper method for performing HTTP requests.
    private JsonObject get(String uri) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200) {
            throw new Exception("No data found");
        }
        JsonReader jsonReader = Json.createReader(new StringReader(response.body()));
        JsonObject json = jsonReader.readObject();

        return json;
    }

    // Private class for converting a champ key into the champion name.
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
