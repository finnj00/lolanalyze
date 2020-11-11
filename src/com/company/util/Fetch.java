package com.company.util;

import javax.json.*;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

// Class for getting info about a player given a key and username.
public class Fetch {
    private String key;
    private String user;
    private ChampDict dict;


    public Fetch(String key, String user) {
        this.key = key;
        this.user = user;
        this.dict = new ChampDict();
    }
    public void match() {
        try {
            String response =
                    get("https://na1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + user + "?api_key=" + key);
            JsonReader jsonReader = Json.createReader(new StringReader(response));
            JsonObject json = jsonReader.readObject();
            String matches = get("https://na1.api.riotgames.com/lol/match/v4/matchlists/by-account/"
                    + json.getString("accountId") + "?api_key=" + key);
            JsonReader matchReader = Json.createReader(new StringReader(matches));
            JsonObject matchJson = matchReader.readObject();
            for(int i = 0; i < 10; i++) {
                int key = matchJson.getJsonArray("matches").getJsonObject(i).getInt("champion");
                System.out.println(dict.champ(key));
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String get(String uri) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
    private class ChampDict {

        private HashMap<String, String> dict;

        private ChampDict() {
            try{
                this.dict = new HashMap<String, String>();
                String response = get("http://ddragon.leagueoflegends.com/cdn/10.16.1/data/en_US/champion.json");
                JsonReader jsonReader = Json.createReader(new StringReader(response));
                JsonObject json = jsonReader.readObject();
                JsonObject champs = json.getJsonObject("data");
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
