package com.glodblock.github.dmeblood.common.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mustapelto.deepmoblearning.common.metadata.MetadataDataModel;
import mustapelto.deepmoblearning.common.util.DataModelHelper;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class WillData implements IJSONSerializable {

    private static final Map<String, Double> willMultiplier = new HashMap<>();
    public static final String id = "ModelWillModifier.json";

    @Override
    public void loadJSON(String json) {
        JsonArray data = JSONLoader.getJsonArray(json);
        if (data == null) {
            return;
        }
        for (JsonElement e : data) {
            JsonObject o = (JsonObject) e;
            willMultiplier.put(o.get("id").getAsString().toLowerCase(), o.get("will").getAsDouble());
        }
    }

    @Override
    public void loadDefault() {
        willMultiplier.put("blaze", 1.0);
        willMultiplier.put("creeper", 1.0);
        willMultiplier.put("dragon", 10.0);
        willMultiplier.put("enderman", 2.0);
        willMultiplier.put("ghast", 1.0);
        willMultiplier.put("guardian", 1.5);
        willMultiplier.put("shulker", 1.5);
        willMultiplier.put("skeleton", 1.0);
        willMultiplier.put("slime", 0.536);
        willMultiplier.put("spider", 0.8);
        willMultiplier.put("witch", 1.3);
        willMultiplier.put("wither_skeleton", 1.0);
        willMultiplier.put("zombie", 1.0);
        willMultiplier.put("thermal_elemental", 1.0);
        willMultiplier.put("tinker_slime", 0.67);
        willMultiplier.put("mo_android", 1.0);
    }

    public double getTypeModifier(ItemStack model) {
        String id = DataModelHelper.getDataModelMetadata(model).map(MetadataDataModel::getID).orElse(null);
        if (id != null) {
            return willMultiplier.getOrDefault(id, 0.0);
        }
        return 0.0;
    }

}
