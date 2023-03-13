package com.glodblock.github.dmeblood.util;

import net.minecraft.item.ItemStack;

public class ItemStackUtil {

    public static boolean isStackEqual(ItemStack stack1, ItemStack stack2, boolean checkNBT) {
        boolean stackEqual = ItemStack.areItemsEqual(stack1, stack2);
        if (checkNBT) {
            return stackEqual & ItemStack.areItemStackTagsEqual(stack1, stack2);
        }
        return stackEqual;
    }

}
