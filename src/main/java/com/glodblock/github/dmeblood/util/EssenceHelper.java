package com.glodblock.github.dmeblood.util;

import com.glodblock.github.dmeblood.ModConfig;
import mustapelto.deepmoblearning.common.metadata.MetadataDataModelTier;
import mustapelto.deepmoblearning.common.util.DataModelHelper;
import net.minecraft.item.ItemStack;

public class EssenceHelper {

    public static int getFillAmount(ItemStack dataModel, double multiplier) {
        return (int) (getFluidBaseAmount(dataModel) * multiplier);
    }

    public static int getFluidBaseAmount(ItemStack dataModel) {
        int tier = getModelTier(dataModel);
        return ModConfig.essenceAmountSubCat.getTierEssenceAmount(tier);
    }

    public static int getModelTier(ItemStack dataModel) {
        return DataModelHelper
                .getTierData(dataModel)
                .map(MetadataDataModelTier::getTier)
                .orElse(0);
    }
}
