package com.glodblock.github.dmeblood.integration.tinkerevolution;

import com.glodblock.github.dmeblood.ModConstants;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.ToolHelper;
import xyz.phanta.tconevo.TconEvoConfig;
import xyz.phanta.tconevo.capability.EuStore;
import xyz.phanta.tconevo.init.TconEvoCaps;
import xyz.phanta.tconevo.init.TconEvoTraits;

import java.util.Random;

public class TiCSentientCheck {

    public static boolean isSentientWeapon(ItemStack weapon) {
        return !checkBroken(weapon) && ToolHelper.getTraits(weapon).contains(TconEvoTraits.TRAIT_WILLFUL);
    }

    public static boolean isTiCWeapon(ItemStack weapon) {
        return !ToolHelper.getTraits(weapon).isEmpty();
    }

    public static void damageTiCWeapon(ItemStack weapon, Random rand) {
        if (isElectricPower(weapon)) {
            IEnergyStorage buff = weapon.getCapability(CapabilityEnergy.ENERGY, null);
            if (buff != null) {
                buff.extractEnergy(getEnergyCost(), false);
                return;
            }
            if (ModConstants.TINKER_EVOLUTION) {
                EuStore euBuff = weapon.getCapability(TconEvoCaps.EU_STORE, null);
                if (euBuff != null) {
                    euBuff.extractEu(getEnergyCost(), false, true);
                }
            }
            return;
        }
        if (weapon.attemptDamageItem(getEnergyCost(), rand, null)) {
            NBTTagCompound tag = TagUtil.getToolTag(weapon);
            tag.setBoolean("Broken", true);
            TagUtil.setToolTag(weapon, tag);
        }
    }

    private static boolean checkBroken(ItemStack weapon) {
        if (isElectricPower(weapon)) {
            IEnergyStorage buff = weapon.getCapability(CapabilityEnergy.ENERGY, null);
            if (buff != null) {
                return buff.extractEnergy(getEnergyCost(), true) <= 0;
            }
            if (ModConstants.TINKER_EVOLUTION) {
                EuStore euBuff = weapon.getCapability(TconEvoCaps.EU_STORE, null);
                if (euBuff != null) {
                    return euBuff.getEuStored() < getEnergyCost();
                }
            }
        }
        NBTTagCompound tag = TagUtil.getToolTag(weapon);
        return tag.getBoolean("Broken");
    }

    private static int getEnergyCost() {
        if (ModConstants.TINKER_EVOLUTION) {
            return TconEvoConfig.general.modFluxedEnergyCostTools;
        }
        return 1;
    }

    private static boolean isElectricPower(ItemStack weapon) {
        return weapon.hasCapability(CapabilityEnergy.ENERGY, null) ||
                (ModConstants.TINKER_EVOLUTION && weapon.hasCapability(TconEvoCaps.EU_STORE, null));
    }

}
