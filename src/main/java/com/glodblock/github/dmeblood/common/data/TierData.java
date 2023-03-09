package com.glodblock.github.dmeblood.common.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class TierData implements IJSONSerializable {

    private static final Map<Integer, Integer> tierToOutput = new HashMap<>();
    public static final String id = "TierBaseOutput.json";

    @Override
    public void loadJSON(String json) {
        JsonArray data = JSONLoader.getJsonArray(json);
        if (data == null) {
            return;
        }
        for (JsonElement e : data) {
            JsonObject o = (JsonObject) e;
            tierToOutput.put(o.get("tier").getAsInt(), o.get("base_output").getAsInt());
        }
    }

    @Override
    public void loadDefault() {
        tierToOutput.put(1, 50);
        tierToOutput.put(2, 75);
        tierToOutput.put(3, 150);
        tierToOutput.put(4, 300);
    }

    public int getOutput(int tier) {
        return tierToOutput.getOrDefault(tier, 0);
    }

}
