package com.glodblock.github.dmeblood.util;

import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.orb.BloodOrb;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class BloodUtil {

    private static final HashMap<Integer, Integer> orbTierMap = new HashMap<>();
    static {
        for (Map.Entry<ResourceLocation, BloodOrb> e : RegistrarBloodMagic.BLOOD_ORBS.getEntries()) {
            BloodOrb orb = e.getValue();
            orbTierMap.put(orb.getTier(), orb.getCapacity());
        }
    }

    public static int getOrbCapByTier(int tier) {
        if (orbTierMap.containsKey(tier)) {
            return orbTierMap.get(tier);
        }
        return 0;
    }

}
