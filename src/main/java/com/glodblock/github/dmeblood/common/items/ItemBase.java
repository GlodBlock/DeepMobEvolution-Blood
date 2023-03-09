package com.glodblock.github.dmeblood.common.items;

import com.glodblock.github.dmeblood.DeepMobLearningBM;
import com.glodblock.github.dmeblood.ModConstants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemBase extends Item {
    public String itemName;

    public ItemBase(String name, int stackSize) {
        setTranslationKey(ModConstants.MODID + "." + name);
        setCreativeTab(DeepMobLearningBM.creativeTab);
        setRegistryName(name);
        setMaxStackSize(stackSize);
        this.itemName = name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
        if (isInCreativeTab(tab)) {
            list.add(new ItemStack(this));
        }
    }
}
