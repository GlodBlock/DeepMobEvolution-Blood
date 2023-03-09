package com.glodblock.github.dmeblood.util;

import com.glodblock.github.dmeblood.common.data.DataSet;
import mustapelto.deepmoblearning.common.metadata.MetadataDataModelTier;
import mustapelto.deepmoblearning.common.util.DataModelHelper;
import net.minecraft.item.ItemStack;

public class EssenceHelper {

    public static int getFillAmount(ItemStack dataModel, double multiplier) {
        return (int) (getFluidBaseAmount(dataModel) * multiplier * getModelModifier(dataModel));
    }

    public static double getModelModifier(ItemStack dataModel) {
        return DataSet.modelData.getTypeModifier(dataModel);
    }

    public static int getFluidBaseAmount(ItemStack dataModel) {
        int tier = getModelTier(dataModel);
        return DataSet.tierData.getOutput(tier);
    }

    public static int getModelTier(ItemStack dataModel) {
        return DataModelHelper
                .getTierData(dataModel)
                .map(MetadataDataModelTier::getTier)
                .orElse(0);
    }
}
