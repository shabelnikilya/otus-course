package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.json.*;
import ru.otus.model.Measurement;

import java.util.List;
import java.util.stream.Collectors;

public class ResourcesFileLoader implements Loader {
    private final String fileName;
    private final Gson gson = new GsonBuilder().create();

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        JsonArray jsonValues = getJsonArray();
        return jsonValues.stream()
                .map(json -> gson.fromJson(json.toString(), Measurement.class))
                .collect(Collectors.toList());
    }

    private JsonArray getJsonArray() {
        JsonArray jsonValues;
        try (JsonReader jsonReader = Json.createReader(
                ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName))
        ) {
            jsonValues = jsonReader.readArray();
        }
        return jsonValues;
    }
}
