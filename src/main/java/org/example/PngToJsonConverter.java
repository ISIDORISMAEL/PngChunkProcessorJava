package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PngToJsonConverter {

    private static final byte[] PNG_SIGNATURE = new byte[]{
            (byte) 137, 80, 78, 71, 13, 10, 26, 10
    };

    public static List<Chunk> parseChunks(File pngFile) throws IOException {
        List<Chunk> chunks = new ArrayList<>();
        try (DataInputStream dis = new DataInputStream(new FileInputStream(pngFile))) {
            byte[] signature = new byte[8];
            dis.readFully(signature);
            if (!java.util.Arrays.equals(signature, PNG_SIGNATURE)) {
                throw new IllegalArgumentException("Invalid PNG signature");
            }

            while (dis.available() > 0) {
                int length = dis.readInt();
                byte[] typeBytes = new byte[4];
                dis.readFully(typeBytes);
                String type = new String(typeBytes, "UTF-8");

                byte[] data = new byte[length];
                dis.readFully(data);
                int crc = dis.readInt();

                chunks.add(new Chunk(type, data, crc));
            }
        }
        return chunks;
    }

    public static void saveChunksToJson(List<Chunk> chunks, File jsonFile) throws IOException {
        JSONArray array = new JSONArray();
        for (Chunk chunk : chunks) {
            array.put(chunk.toJson());
        }
        JSONObject root = new JSONObject();
        root.put("chunks", array);

        try (Writer writer = new FileWriter(jsonFile)) {
            writer.write(root.toString(2));
        }
    }

    public static void main(String[] args) throws IOException {
        File input = new File("src/main/java/org/example/input.png");
        File outputJson = new File("chunks.json");

        List<Chunk> chunks = parseChunks(input);
        saveChunksToJson(chunks, outputJson);

        System.out.println("PNG parsed and JSON written successfully.");
    }
}
