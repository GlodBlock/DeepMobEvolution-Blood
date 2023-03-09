package com.glodblock.github.dmeblood.plugin.jei;

import com.glodblock.github.dmeblood.util.Catalyst;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class DigitalAgonizerCatalystWrapper implements IRecipeWrapper {
    private final Catalyst catalyst;
    private final NonNullList<ItemStack> inputs = NonNullList.create();

    public DigitalAgonizerCatalystWrapper(Catalyst catalyst) {
        this.catalyst = catalyst;
        this.inputs.add(catalyst.getStack());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        FontRenderer render = minecraft.fontRenderer;
        render.drawStringWithShadow(I18n.format("jei.catalyst.info.0", this.catalyst.getMultiplier()), 32, 3, 0xFFFFFF);
        render.drawStringWithShadow(I18n.format("jei.catalyst.info.1", this.catalyst.getOperations()), 32, 15, 0xFFFFFF);
    }

    @Override
    public void getIngredients(IIngredients iIngredients) {
        iIngredients.setInput(VanillaTypes.ITEM, inputs.get(0));
    }
}
