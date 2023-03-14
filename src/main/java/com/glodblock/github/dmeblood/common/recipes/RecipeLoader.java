package com.glodblock.github.dmeblood.common.recipes;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import com.glodblock.github.dmeblood.common.Registry;
import mustapelto.deepmoblearning.common.DMLRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class RecipeLoader {

    public static void run() {
        BloodMagicAPI.INSTANCE.getRecipeRegistrar().addBloodAltar(
                Ingredient.fromItem(DMLRegistry.ITEM_GLITCH_INGOT),
                new ItemStack(Registry.itemBloodIngot, 1),
                3,
                10000,
                20,
                20
        );
    }

}
