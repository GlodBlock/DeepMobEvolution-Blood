package com.glodblock.github.dmeblood.util;

import mustapelto.deepmoblearning.common.metadata.MetadataManager;
import mustapelto.deepmoblearning.common.util.DataModelHelper;
import net.minecraft.item.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class ItemStackUtil {

    public static boolean isStackEqual(ItemStack stack1, ItemStack stack2, boolean checkNBT) {
        boolean stackEqual = ItemStack.areItemsEqual(stack1, stack2);
        if (checkNBT) {
            return stackEqual & ItemStack.areItemStackTagsEqual(stack1, stack2);
        }
        return stackEqual;
    }

    public static List<ItemStack> getAllModel(ItemStack model) {
        LinkedList<ItemStack> list = new LinkedList<>();
        for (int tier = MetadataManager.getMinDataModelTier(); tier < MetadataManager.getMaxDataModelTier(); tier ++) {
            ItemStack copy = model.copy();
            DataModelHelper.setTierLevel(copy, tier);
            list.addFirst(copy);
        }
        return list;
    }

}
