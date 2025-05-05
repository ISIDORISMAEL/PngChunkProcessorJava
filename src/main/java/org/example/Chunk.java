package org.example;

import org.json.JSONObject;

import java.util.Base64;

public class Chunk {
    public String type;
    public byte[] data;
    public int crc;

    public Chunk(String type, byte[] data, int crc) {
        this.type = type;
        this.data = data;
        this.crc = crc;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("data", Base64.getEncoder().encodeToString(data));
        json.put("crc", crc);
        return json;
    }

    public static Chunk fromJson(JSONObject json) {
        String type = json.getString("type");
        byte[] data = Base64.getDecoder().decode(json.getString("data"));
        int crc = json.getInt("crc");
        return new Chunk(type, data, crc);
    }
}
