package com.glodblock.github.dmeblood.util;

import WayofTime.bloodmagic.soul.IDemonWillWeapon;
import com.glodblock.github.dmeblood.common.data.DataSet;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Random;

public final class SentientWeapon {

    private SentientWeapon() {}

    public static boolean isValidWeapon(ItemStack stack) {
        return stack.getItem() instanceof IDemonWillWeapon;
    }

    public static void damageWeapon(ItemStack stack, Random rand) {
        if (stack.attemptDamageItem(1, rand, null)) {
            stack.setCount(0);
        }
    }

    // Follows BM's origin formula
    public static double getWill(ItemStack stack, ItemStack model, Random rand) {
        NBTTagCompound nbt = stack.getTagCompound();
        double d1 = 0;
        double d2 = 0;
        double multi = DataSet.willData.getTypeModifier(model);
        double loot = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack) + 1;
        if (nbt != null) {
            d1 = nbt.getDouble("soulSwordDrop");
            d2 = nbt.getDouble("soulSwordStaticDrop");
        }
        return loot * (d1 * rand.nextDouble() + d2) * multi;
    }

}
