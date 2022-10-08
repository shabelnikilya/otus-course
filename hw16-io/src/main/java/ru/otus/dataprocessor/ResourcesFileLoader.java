package ru.otus.dataprocessor;

import jakarta.json.*;
import ru.otus.model.Measurement;

import java.util.List;
import java.util.stream.Collectors;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        JsonArray jsonValues = getJsonArray();
        return jsonValues.stream()
                .map(JsonValue::asJsonObject)
                .map(json ->
                        new Measurement(json.getString("name"), json.getJsonNumber("value").doubleValue())
                )
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
