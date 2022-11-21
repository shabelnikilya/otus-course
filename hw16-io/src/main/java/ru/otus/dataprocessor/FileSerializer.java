package ru.otus.dataprocessor;

import jakarta.json.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try (JsonWriter jsonWriter = Json.createWriter(new FileWriter(fileName))) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            addValues(objectBuilder, data);
            jsonWriter.write(objectBuilder.build());
        } catch (IOException e) {
            throw new FileProcessException("Исключение при создании файла!");
        }
    }

    private void addValues(JsonObjectBuilder objectBuilder, Map<String, Double> data) {
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            objectBuilder.add(entry.getKey(), entry.getValue());
        }
    }
}
