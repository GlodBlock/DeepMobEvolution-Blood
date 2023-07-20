package com.glodblock.github.dmeblood;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("WeakerAccess")
@Mod.EventBusSubscriber
@Config(modid = ModConstants.MODID, name = "dme_bloodmagic/main")
public class ModConfig {
    @SubscribeEvent
    public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(ModConstants.MODID)) {
            ConfigManager.sync(ModConstants.MODID, Config.Type.INSTANCE);
        }
    }

    @Config.Comment({
        "Min: 1",
        "Default: 128"
    })
    @Config.Name("RF tick cost of the Digital Agonizer")
    @Config.RangeInt(min = 1)
    public static int agonizerRFCost = 128;
    public static int getAgonizerRFCost() {
        return Math.max(agonizerRFCost, 1);
    }

    @Config.Comment({
            "Min: 1",
            "Default: 100,000"
    })
    @Config.Name("Machine RF capacity")
    @Config.RangeInt(min = 1)
    public static int capacity = 100000;
    public static int getRFCapacity() {
        return Math.max(capacity, 1);
    }

    @Config.Comment({
            "Min: 1",
            "Default: 25,600"
    })
    @Config.Name("Machine maximum RF/t input")
    @Config.RangeInt(min = 1)
    public static int RFInput = 25600;
    public static int getRFInput() {
        return Math.max(RFInput, 1);
    }

    @Config.Comment({
            "Min: 1",
            "Default: 256"
    })
    @Config.Name("RF tick cost of the Digital Will Injector")
    @Config.RangeInt(min = 1)
    public static int willInjectorRFCost = 256;
    public static int getWillInjectorRFCost() {
        return Math.max(willInjectorRFCost, 1);
    }

    @Config.Comment({
        "Default: true"
    })
    @Config.Name("Allow multiple agonizers linked with one Altar")
    public static boolean isMultipleAgonizersAllowed = true;

}
