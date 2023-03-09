package com.glodblock.github.dmeblood.util;

import com.glodblock.github.dmeblood.ModConfig;
import mustapelto.deepmoblearning.common.DMLRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class Catalyst {
    public static NonNullList<Catalyst> catalysts = NonNullList.create();
    private final ItemStack stack;
    private final double multiplier;
    private final int operations;

    private Catalyst(ItemStack item, double multiplier, int operations) {
        this.stack = item.copy();
        this.multiplier = multiplier;
        this.operations = operations;
    }

    public static void init() {
        addCatalyst(DMLRegistry.getLivingMatter("overworldian"), ModConfig.essenceMultiplierSubCat.getOverworldianCatalystMultiplier(), 10);
        addCatalyst(DMLRegistry.getLivingMatter("hellish"), ModConfig.essenceMultiplierSubCat.getHellishCatalystMultiplier(), 10);
        addCatalyst(DMLRegistry.getLivingMatter("extraterrestrial"), ModConfig.essenceMultiplierSubCat.getExtraterrestrialCatalystMultiplier(), 10);
        addCatalyst(DMLRegistry.getLivingMatter("twilight"), ModConfig.essenceMultiplierSubCat.getTwilightCatalystMultiplier(), 10);
        addCatalyst(DMLRegistry.ITEM_GLITCH_HEART, ModConfig.essenceMultiplierSubCat.getHeartCatalystMultiplier(), 100);
    }

    public static void addCatalyst(ItemStack item, double multiplier, int operations) {
        if (!item.isEmpty()) {
            catalysts.add(new Catalyst(item, multiplier, operations));
        }
    }

    public static void addCatalyst(Item item, double multiplier, int operations) {
        if (item != null) {
            catalysts.add(new Catalyst(new ItemStack(item), multiplier, operations));
        }
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getOperations() {
        return operations;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public static Catalyst getCatalyst(ItemStack stack) {
        for (Catalyst catalyst : catalysts) {
            if(ItemStack.areItemsEqual(catalyst.stack, stack)) {
                return catalyst;
            }
        } return null;
    }

    public static boolean isValidCatalyst(ItemStack stack) {
        for (Catalyst catalyst : catalysts) {
            if(ItemStack.areItemsEqual(catalyst.stack, stack)) {
                return true;
            }
        } return false;
    }
}
