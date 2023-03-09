package com.glodblock.github.dmeblood.plugin.jei;

import mustapelto.deepmoblearning.common.metadata.MetadataDataModel;
import mustapelto.deepmoblearning.common.util.DataModelHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import java.util.ArrayList;

public class DigitalAgonizerRecipe {
    public static ArrayList<DigitalAgonizerRecipe> recipes = new ArrayList<>();
    public final ItemStack dataModels;
    public final FluidStack essence;

    private DigitalAgonizerRecipe(ItemStack dataModels) {
        this.dataModels = dataModels;
        this.essence = new FluidStack(FluidRegistry.getFluid("lifeessence"), 1000);
    }

    public static void addRecipe(NonNullList<ItemStack> dataModels) {
        for (ItemStack model : dataModels) {
            if (DataModelHelper.getDataModelMetadata(model).map(MetadataDataModel::isEnabled).orElse(false)) {
                recipes.add(new DigitalAgonizerRecipe(model));
            }
        }
    }
}
