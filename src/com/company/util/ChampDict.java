package com.company.util;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class ChampDict {
    public static HashMap<String, String> dict;

    public ChampDict() {
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

    public String champ(int key) {
        String temp = Integer.toString(key);
        return dict.get(temp);
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
}
