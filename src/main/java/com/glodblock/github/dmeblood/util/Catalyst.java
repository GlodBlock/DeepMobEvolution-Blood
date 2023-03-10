package com.glodblock.github.dmeblood.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class Catalyst {
    public static NonNullList<Catalyst> catalysts = NonNullList.create();
    private final ItemStack stack;
    private final boolean nbtSensitive;
    private final double multiplier;
    private final int operations;

    private Catalyst(ItemStack item, double multiplier, int operations, boolean nbt) {
        this.stack = item.copy();
        this.multiplier = multiplier;
        this.operations = operations;
        this.nbtSensitive = nbt;
    }

    private Catalyst(ItemStack item, double multiplier, int operations) {
        this(item, multiplier, operations, false);
    }

    public static void addCatalyst(ItemStack item, double multiplier, int operations) {
        if (!item.isEmpty()) {
            catalysts.add(new Catalyst(item, multiplier, operations, item.hasTagCompound()));
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
            if (isStackEqual(catalyst.stack, stack, catalyst.nbtSensitive)) {
                return catalyst;
            }
        }
        return null;
    }

    public static boolean isValidCatalyst(ItemStack stack) {
        for (Catalyst catalyst : catalysts) {
            if(isStackEqual(catalyst.stack, stack, catalyst.nbtSensitive)) {
                return true;
            }
        } return false;
    }

    public static boolean isStackEqual(ItemStack stack1, ItemStack stack2, boolean checkNBT) {
        boolean stackEqual = ItemStack.areItemsEqual(stack1, stack2);
        if (checkNBT) {
            return stackEqual & ItemStack.areItemStackTagsEqual(stack1, stack2);
        }
        return stackEqual;
    }
}
