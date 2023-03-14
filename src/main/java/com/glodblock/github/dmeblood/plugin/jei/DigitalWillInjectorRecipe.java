package com.glodblock.github.dmeblood.plugin.jei;

import com.glodblock.github.dmeblood.common.data.DataSet;
import mustapelto.deepmoblearning.common.metadata.MetadataDataModel;
import mustapelto.deepmoblearning.common.util.DataModelHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;

public class DigitalWillInjectorRecipe {

    public static ArrayList<DigitalWillInjectorRecipe> recipes = new ArrayList<>();
    public final ItemStack dataModels;

    private DigitalWillInjectorRecipe(ItemStack dataModels) {
        this.dataModels = dataModels;
    }

    public static void addRecipe(NonNullList<ItemStack> dataModels) {
        for (ItemStack model : dataModels) {
            if (DataModelHelper.getDataModelMetadata(model).map(MetadataDataModel::isEnabled).orElse(false) &&
                DataSet.willData.getTypeModifier(model) > 0) {
                recipes.add(new DigitalWillInjectorRecipe(model));
            }
        }
    }

}
