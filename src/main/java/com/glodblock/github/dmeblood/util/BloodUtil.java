package com.glodblock.github.dmeblood.util;

import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.orb.BloodOrb;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class BloodUtil {

    public static int getOrbCapByTier(int tier) {
        for (Map.Entry<ResourceLocation, BloodOrb> e : RegistrarBloodMagic.BLOOD_ORBS.getEntries()) {
            BloodOrb orb = e.getValue();
            if (orb.getTier() == tier) {
                return orb.getCapacity();
            }
        }
        return 0;
    }

}
