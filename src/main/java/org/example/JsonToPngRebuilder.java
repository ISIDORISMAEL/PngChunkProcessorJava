package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class JsonToPngRebuilder {

    private static final byte[] PNG_SIGNATURE = new byte[]{
            (byte) 137, 80, 78, 71, 13, 10, 26, 10
    };

    public static List<Chunk> readChunksFromJson(File jsonFile) throws IOException {
        String jsonText = new String(Files.readAllBytes(jsonFile.toPath()), "UTF-8");
        JSONObject root = new JSONObject(jsonText);
        JSONArray array = root.getJSONArray("chunks");

        List<Chunk> chunks = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            chunks.add(Chunk.fromJson(array.getJSONObject(i)));
        }
        return chunks;
    }

    public static void writePngFromChunks(List<Chunk> chunks, File outputFile) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile))) {
            dos.write(PNG_SIGNATURE);
            for (Chunk chunk : chunks) {
                dos.writeInt(chunk.data.length);
                dos.write(chunk.type.getBytes("UTF-8"));
                dos.write(chunk.data);
                dos.writeInt(chunk.crc);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        File inputJson = new File("chunks.json");
        File outputPng = new File("output.png");

        List<Chunk> chunks = readChunksFromJson(inputJson);
        writePngFromChunks(chunks, outputPng);

        System.out.println("PNG reconstructed from JSON successfully.");
    }
}
