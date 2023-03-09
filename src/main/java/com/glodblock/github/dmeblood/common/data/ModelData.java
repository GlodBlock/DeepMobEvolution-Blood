package com.glodblock.github.dmeblood.common.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mustapelto.deepmoblearning.common.metadata.MetadataDataModel;
import mustapelto.deepmoblearning.common.metadata.MetadataDataModelTier;
import mustapelto.deepmoblearning.common.util.DataModelHelper;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ModelData implements IJSONSerializable {

    private final static Map<String, Double> modelTypeModifier = new HashMap<>();
    public static final String id = "ModelModifier.json";

    @Override
    public void loadJSON(String json) {
        JsonArray data = JSONLoader.getJsonArray(json);
        if (data == null) {
            return;
        }
        for (JsonElement e : data) {
            JsonObject o = (JsonObject) e;
            modelTypeModifier.put(o.get("id").getAsString().toLowerCase(), o.get("modifier").getAsDouble());
        }
    }

    @Override
    public void loadDefault() {
        modelTypeModifier.put("blaze", 0.5);
        modelTypeModifier.put("dragon", 1.6);
        modelTypeModifier.put("ghast", 0.1);
        modelTypeModifier.put("skeleton", 0.1);
        modelTypeModifier.put("wither", 1.5);
        modelTypeModifier.put("wither_skeleton", 0.2);
    }

    public double getTypeModifier(ItemStack model) {
        String id = DataModelHelper.getDataModelMetadata(model).map(MetadataDataModel::getID).orElse(null);
        if (id != null) {
            return modelTypeModifier.getOrDefault(id, 1.0);
        }
        return 0.0;
    }
}
