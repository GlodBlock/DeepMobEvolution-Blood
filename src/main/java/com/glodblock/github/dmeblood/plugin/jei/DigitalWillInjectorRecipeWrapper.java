package com.glodblock.github.dmeblood.plugin.jei;

import WayofTime.bloodmagic.compat.jei.forge.TartaricForgeRecipeJEI;
import com.glodblock.github.dmeblood.common.data.DataSet;
import com.glodblock.github.dmeblood.util.ItemStackUtil;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mustapelto.deepmoblearning.common.metadata.MetadataManager;
import mustapelto.deepmoblearning.common.util.DataModelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class DigitalWillInjectorRecipeWrapper implements IRecipeWrapper {

    private final ItemStack dataModel;

    private final NonNullList<ItemStack> inputs = NonNullList.create();


    public DigitalWillInjectorRecipeWrapper(DigitalWillInjectorRecipe recipe) {
        this.dataModel = recipe.dataModels;
        this.inputs.addAll(ItemStackUtil.getAllModel(recipe.dataModels));
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        List<List<ItemStack>> inputList = new LinkedList<>();
        inputList.add(this.inputs);
        ingredients.setInputLists(VanillaTypes.ITEM, inputList);
        List<List<ItemStack>> outputList = new LinkedList<>();
        outputList.add(this.getWills());
        ingredients.setOutputLists(VanillaTypes.ITEM, outputList);
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
        String amount = "~" + f.format(
                DataSet.willData.getTypeModifier(this.dataModel) *
                        DataSet.willTierData.getWillOutput(tier)
        ) + " Will";
        render.drawStringWithShadow(amount, 114 - render.getStringWidth(amount), y, 0xFFFFFF);
    }

    private List<ItemStack> getWills() {
        LinkedList<ItemStack> list = new LinkedList<>();
        for (TartaricForgeRecipeJEI.DefaultWill will : TartaricForgeRecipeJEI.DefaultWill.values()) {
            list.add(will.willStack);
        }
        return list;
    }

}
