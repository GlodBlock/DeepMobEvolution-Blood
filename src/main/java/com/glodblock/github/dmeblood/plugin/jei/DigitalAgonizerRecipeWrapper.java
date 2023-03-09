package com.glodblock.github.dmeblood.plugin.jei;

import com.glodblock.github.dmeblood.util.EssenceHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mustapelto.deepmoblearning.common.metadata.MetadataDataModelTier;
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
    private final NonNullList<ItemStack> dataModels;

    private final NonNullList<ItemStack> inputs = NonNullList.create();
    private final FluidStack output;


    public DigitalAgonizerRecipeWrapper(DigitalAgonizerRecipe recipe) {
        this.dataModels = recipe.dataModels;
        this.inputs.addAll(this.dataModels);
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
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        render(minecraft);
        if (this.lastWorldTime == minecraft.world.getTotalWorldTime()) {
            return;
        } else {
            this.ticks ++;
            this.lastWorldTime = minecraft.world.getTotalWorldTime();
        }
        if(this.ticks % (20 * 2) == 0)  {
            cycleTierAndModel();
        }
    }

    public void render(Minecraft minecraft) {
        FontRenderer render = minecraft.fontRenderer;

        String tierName = MetadataManager.getDataModelTierData(this.tier).map(MetadataDataModelTier::getDisplayName).orElse("Error");
        render.drawStringWithShadow(tierName, 2, 27, 0xFFFFFF);
        NumberFormat f = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DataModelHelper.setTierLevel(this.dataModels.get(0), this.tier);

        String amount = f.format(EssenceHelper.getFillAmount(this.dataModels.get(0), 1.0)) + " mB";
        render.drawStringWithShadow(amount, 114 - render.getStringWidth(amount), 27, 0xFFFFFF);
    }
}
