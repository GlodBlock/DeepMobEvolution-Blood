package com.glodblock.github.dmeblood.common.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class WillTierData implements IJSONSerializable {

    private static final Map<Integer, Double> tierToWillOutput = new HashMap<>();
    public static final String id = "TierWillOutput.json";

    @Override
    public void loadJSON(String json) {
        JsonArray data = JSONLoader.getJsonArray(json);
        if (data == null) {
            return;
        }
        for (JsonElement e : data) {
            JsonObject o = (JsonObject) e;
            tierToWillOutput.put(o.get("tier").getAsInt(), o.get("will_output").getAsDouble());
        }
    }

    @Override
    public void loadDefault() {
        tierToWillOutput.put(1, 1.0);
        tierToWillOutput.put(2, 1.5);
        tierToWillOutput.put(3, 2.25);
        tierToWillOutput.put(4, 3.37);
    }

    public double getWillOutput(int tier) {
        return tierToWillOutput.getOrDefault(tier, 0.0);
    }

}
