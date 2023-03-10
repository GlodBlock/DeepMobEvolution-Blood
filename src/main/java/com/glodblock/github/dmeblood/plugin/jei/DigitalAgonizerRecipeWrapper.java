package com.glodblock.github.dmeblood.plugin.jei;

import com.glodblock.github.dmeblood.util.EssenceHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mustapelto.deepmoblearning.common.metadata.MetadataManager;
import mustapelto.deepmoblearning.common.util.DataModelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.text.NumberFormat;
import java.util.Locale;

public class DigitalAgonizerRecipeWrapper implements IRecipeWrapper {
    private final ItemStack dataModel;

    private final NonNullList<ItemStack> inputs = NonNullList.create();
    private final FluidStack output;


    public DigitalAgonizerRecipeWrapper(DigitalAgonizerRecipe recipe) {
        this.dataModel = recipe.dataModels;
        this.inputs.add(recipe.dataModels);
        this.output = recipe.essence;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, this.inputs);
        ingredients.setOutput(VanillaTypes.FLUID, this.output);
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        FontRenderer render = minecraft.fontRenderer;
        for (int tier = MetadataManager.getMinDataModelTier(); tier < MetadataManager.getMaxDataModelTier(); tier ++) {
            int offset = tier - MetadataManager.getMinDataModelTier();
            drawTierInfo(27 + offset * 10, tier, render);
        }
    }

    private void drawTierInfo(int y, int tier, FontRenderer render) {
        DataModelHelper.setTierLevel(this.dataModel, tier);
        String tierName = DataModelHelper.getTierDisplayNameFormatted(this.dataModel);
        render.drawStringWithShadow(tierName, 2, y, 0xFFFFFF);
        NumberFormat f = NumberFormat.getNumberInstance(Locale.ENGLISH);
        String amount = f.format(EssenceHelper.getFillAmount(this.dataModel, 1.0)) + " mB";
        render.drawStringWithShadow(amount, 114 - render.getStringWidth(amount), y, 0xFFFFFF);
    }

}
