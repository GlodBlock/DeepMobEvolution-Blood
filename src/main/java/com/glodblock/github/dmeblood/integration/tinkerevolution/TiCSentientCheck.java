package com.glodblock.github.dmeblood.integration.tinkerevolution;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.ToolHelper;
import xyz.phanta.tconevo.init.TconEvoTraits;

import java.util.Random;

public class TiCSentientCheck {

    public static boolean isSentientWeapon(ItemStack weapon) {
        return ToolHelper.getTraits(weapon).contains(TconEvoTraits.TRAIT_WILLFUL);
    }

    public static boolean isTiCWeapon(ItemStack weapon) {
        return !ToolHelper.getTraits(weapon).isEmpty();
    }

    public static void damageTiCWeapon(ItemStack weapon, Random rand) {
        if (weapon.attemptDamageItem(1, rand, null)) {
            NBTTagCompound tag = TagUtil.getToolTag(weapon);
            tag.setBoolean("Broken", true);
            TagUtil.setToolTag(weapon, tag);
        }
    }

}
