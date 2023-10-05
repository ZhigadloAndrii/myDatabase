package com.example.My_Database.utils.toGson;


import com.example.My_Database.Domain.Entity.Columns;
import com.example.My_Database.Domain.Entity.Row;
import com.example.My_Database.Domain.Entity.Table;
import com.example.My_Database.Domain.Entity.types.Attribute;
import com.example.My_Database.Domain.Entity.types.Value;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TableDeserializer implements JsonDeserializer {
    @Override
    public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        Table result = new Table(name);

        ArrayList<Attribute> attributes = new ArrayList<>();
        jsonObject.get("attributes")
              .getAsJsonArray()
              .forEach(attributesJsonElement -> attributes.add(
                    jsonDeserializationContext.deserialize(attributesJsonElement, Attribute.class)
              ));
        result.setColumns(new Columns(attributes));

        ArrayList<Row> rows = new ArrayList<>();
        jsonObject.get("rows").getAsJsonArray().forEach(rowJsonElement -> {
            HashMap<String, Value> values = new HashMap<>();
            JsonObject jsonValues = rowJsonElement.getAsJsonObject().get("values").getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : jsonValues.entrySet()) {
                values.put(entry.getKey(), findByName(entry.getKey(), attributes).getValue(jsonValues.get(entry.getKey()).getAsString()));
            }
            rows.add(new Row(values));
        });

        result.setRows(rows);
        return result;
    }

    private Attribute findByName(String name, ArrayList<Attribute> attributes) {
        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).getName().equals(name)) {
                return attributes.get(i);
            }
        }
        return null;
    }
}
