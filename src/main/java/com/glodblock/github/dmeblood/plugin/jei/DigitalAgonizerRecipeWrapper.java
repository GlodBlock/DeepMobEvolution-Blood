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
    private int tier = 1;
    private long ticks = 0;
    private long lastWorldTime;
    private final ItemStack dataModel;

    private final NonNullList<ItemStack> inputs = NonNullList.create();
    private final FluidStack output;


    public DigitalAgonizerRecipeWrapper(DigitalAgonizerRecipe recipe) {
        this.dataModel = recipe.dataModels;
        this.inputs.add(this.dataModel);
        this.output = recipe.essence;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, this.inputs);
        ingredients.setOutput(VanillaTypes.FLUID, this.output);
    }

    private void cycleTierAndModel() {
        this.tier = MetadataManager.isMaxDataModelTier(this.tier) ?
                MetadataManager.getMinDataModelTier() : this.tier + 1;
        DataModelHelper.setTierLevel(this.dataModel, this.tier);
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        render(minecraft);
        if (this.lastWorldTime != minecraft.world.getTotalWorldTime()) {
            this.ticks ++;
            this.lastWorldTime = minecraft.world.getTotalWorldTime();
            if (this.ticks % 30 == 0)  {
                cycleTierAndModel();
            }
        }
    }

    public void render(Minecraft minecraft) {
        FontRenderer render = minecraft.fontRenderer;

        String tierName = DataModelHelper.getTierDisplayNameFormatted(this.dataModel);
        render.drawStringWithShadow(tierName, 2, 27, 0xFFFFFF);
        NumberFormat f = NumberFormat.getNumberInstance(Locale.ENGLISH);

        String amount = f.format(EssenceHelper.getFillAmount(this.dataModel, 1.0)) + " mB";
        render.drawStringWithShadow(amount, 114 - render.getStringWidth(amount), 27, 0xFFFFFF);
    }
}
