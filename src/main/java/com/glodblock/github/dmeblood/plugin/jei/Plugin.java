package com.glodblock.github.dmeblood.plugin.jei;

import com.glodblock.github.dmeblood.util.Catalyst;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mustapelto.deepmoblearning.common.DMLRegistry;
import mustapelto.deepmoblearning.common.items.ItemDataModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class Plugin implements IModPlugin {
    private static IJeiHelpers jeiHelpers;
    private static DigitalAgonizerRecipeCategory agonizerCategory;
    private static DigitalAgonizerCatalystCategory agonizerCatalystCategory;
    private static DigitalWillInjectorRecipeCategory willInjectorCategory;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        jeiHelpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        agonizerCategory = new DigitalAgonizerRecipeCategory(guiHelper);
        registry.addRecipeCategories(agonizerCategory);

        agonizerCatalystCategory = new DigitalAgonizerCatalystCategory(guiHelper);
        registry.addRecipeCategories(agonizerCatalystCategory);

        willInjectorCategory = new DigitalWillInjectorRecipeCategory(guiHelper);
        registry.addRecipeCategories(willInjectorCategory);
    }

    @Override
    public void register(IModRegistry registry) {
        jeiHelpers = registry.getJeiHelpers();

        registry.handleRecipes(DigitalAgonizerRecipe.class, DigitalAgonizerRecipeWrapper::new, agonizerCategory.getUid());
        addDigitalAgonizerRecipes(registry);

        registry.handleRecipes(Catalyst.class, DigitalAgonizerCatalystWrapper::new, agonizerCatalystCategory.getUid());
        addDigitalAgonizerCatalysts(registry);

        registry.handleRecipes(DigitalWillInjectorRecipe.class, DigitalWillInjectorRecipeWrapper::new, willInjectorCategory.getUid());
        addDigitalWillInjectorRecipes(registry);
    }

    private void addDigitalAgonizerCatalysts(IModRegistry registry) {
        List<Catalyst> e = new ArrayList<>(Catalyst.catalysts);
        registry.addRecipes(e, agonizerCatalystCategory.getUid());
        agonizerCatalystCategory.addCatalysts(registry);
    }

    private void addDigitalWillInjectorRecipes(IModRegistry registry) {
        NonNullList<ItemStack> dataModels = NonNullList.create();
        for (ItemDataModel dataModel : DMLRegistry.getDataModels()) {
            dataModels.add(new ItemStack(dataModel));
        }
        DigitalWillInjectorRecipe.addRecipe(dataModels);
        registry.addRecipes(DigitalWillInjectorRecipe.recipes, willInjectorCategory.getUid());
        willInjectorCategory.addCatalysts(registry);
    }

    private void addDigitalAgonizerRecipes(IModRegistry registry) {
        NonNullList<ItemStack> dataModels = NonNullList.create();
        for (ItemDataModel dataModel : DMLRegistry.getDataModels()) {
            dataModels.add(new ItemStack(dataModel));
        }
        DigitalAgonizerRecipe.addRecipe(dataModels);
        registry.addRecipes(DigitalAgonizerRecipe.recipes, agonizerCategory.getUid());
        agonizerCategory.addCatalysts(registry);
    }
}
