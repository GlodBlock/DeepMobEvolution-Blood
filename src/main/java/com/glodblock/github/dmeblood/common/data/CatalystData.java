package com.glodblock.github.dmeblood.common.data;

import com.glodblock.github.dmeblood.util.Catalyst;
import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mustapelto.deepmoblearning.common.DMLRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;

public class CatalystData implements IJSONSerializable {

    public static final String id = "CatalystModifier.json";

    @Override
    public void loadJSON(String json) {
        JsonArray data = JSONLoader.getJsonArray(json);
        if (data == null) {
            return;
        }
        for (JsonElement e : data) {
            JsonObject o = (JsonObject) e;
            JsonObject item = o.get("catalyst").getAsJsonObject();
            double multi = o.get("multiplier").getAsDouble();
            int op = o.get("operations").getAsInt();
            int meta = 0;
            if (item.has("meta")) {
                meta = item.get("meta").getAsInt();
            }
            String nbt = null;
            if (item.has("NBT")) {
                nbt = item.get("NBT").getAsString();
            }
            Item stack = Item.getByNameOrId(item.get("id").getAsString());
            if (stack != null) {
                ItemStack c = new ItemStack(stack, 1, meta);
                if (!Strings.isNullOrEmpty(nbt)) {
                    try {
                        c.setTagCompound(JsonToNBT.getTagFromJson(nbt));
                    } catch (NBTException ignore) {
                        c.setTagCompound(null);
                    }
                }
                Catalyst.addCatalyst(c, multi, op);
            }
        }
    }

    @Override
    public void loadDefault() {
        Catalyst.addCatalyst(DMLRegistry.getLivingMatter("overworldian"), 2.2, 10);
        Catalyst.addCatalyst(DMLRegistry.getLivingMatter("hellish"), 2.4, 10);
        Catalyst.addCatalyst(DMLRegistry.getLivingMatter("extraterrestrial"), 2.7, 10);
        Catalyst.addCatalyst(DMLRegistry.getLivingMatter("twilight"), 2.5, 10);
        Catalyst.addCatalyst(DMLRegistry.ITEM_GLITCH_HEART, 5.0, 100);
    }
}
