package com.glodblock.github.dmeblood.plugin.jei;

import com.glodblock.github.dmeblood.ModConstants;
import com.glodblock.github.dmeblood.common.Registry;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mustapelto.deepmoblearning.common.metadata.MetadataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class DigitalWillInjectorRecipeCategory implements IRecipeCategory<DigitalWillInjectorRecipeWrapper> {

    private final ItemStack catalyst;
    private final IDrawable background;
    private final IDrawableAnimated progress;

    public DigitalWillInjectorRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation base = new ResourceLocation(ModConstants.MODID, "textures/gui/jei/digital_agonizer.png");
        this.catalyst = new ItemStack(Registry.blockDigitalWillInjectorItem);

        int space = MetadataManager.getMaxDataModelTier() - MetadataManager.getMinDataModelTier();
        this.background = guiHelper.drawableBuilder(base, 0, 0, 116, 26 + space * 10).build();
        IDrawableStatic progress = guiHelper.createDrawable(base, 133, 0, 35, 6);
        this.progress = guiHelper.createAnimatedDrawable(progress, 120, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void setRecipe(IRecipeLayout layout, @Nonnull DigitalWillInjectorRecipeWrapper wrapper, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = layout.getItemStacks();

        guiItemStacks.init(0, true, 12, 3);
        guiItemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        guiItemStacks.init(1, false, 95, 3);
        guiItemStacks.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    }

    public void addCatalysts(IModRegistry registry) {
        registry.addRecipeCatalyst(catalyst, getUid());
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {
        progress.draw(minecraft, 52, 9);
    }

    @Nonnull
    @Override
    public String getUid() {
        return ModConstants.MODID + ".digital_will_injector";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return catalyst.getDisplayName();
    }

    @Nonnull
    @Override
    public String getModName() {
        return ModConstants.MODID;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

}
