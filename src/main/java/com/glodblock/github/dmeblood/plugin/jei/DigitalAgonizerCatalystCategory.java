package com.glodblock.github.dmeblood.plugin.jei;

import com.glodblock.github.dmeblood.ModConstants;
import com.glodblock.github.dmeblood.common.Registry;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class DigitalAgonizerCatalystCategory implements IRecipeCategory<DigitalAgonizerCatalystWrapper> {
    private final ItemStack catalyst;
    private final IDrawable background;
    private final IDrawable slotBackground;

    public DigitalAgonizerCatalystCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(114, 30);
        this.catalyst = new ItemStack(Registry.blockDigitalAgonizerItem);
        this.slotBackground = guiHelper.getSlotDrawable();
    }

    @Override
    public void setRecipe(IRecipeLayout layout, @Nonnull DigitalAgonizerCatalystWrapper wrapper, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = layout.getItemStacks();
        guiItemStacks.init(0, true, 6, 6);
        guiItemStacks.setBackground(0, this.slotBackground);
        guiItemStacks.set(ingredients);
    }

    public void addCatalysts(IModRegistry registry) {
        registry.addRecipeCatalyst(this.catalyst, this.getUid());
    }

    @Nonnull
    @Override
    public String getUid() {
        return ModConstants.MODID + ".digital_agonizer_catalyst";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return "Agonizer Catalysts";
    }

    @Nonnull
    @Override
    public String getModName() {
        return ModConstants.MODID;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return this.background;
    }
}
