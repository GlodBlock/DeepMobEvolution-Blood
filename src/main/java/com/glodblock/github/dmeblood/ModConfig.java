package com.glodblock.github.dmeblood;

import mustapelto.deepmoblearning.common.util.MathHelper;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.*;

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
        "Max: 10,000",
        "Default: 128"
    })
    @Config.Name("RF tick cost of the Digital Agonizer")
    @Config.RangeInt(min = 1, max = 10000)
    public static int agonizerRFCost = 128;
    public static int getAgonizerRFCost() {
        return MathHelper.clamp(agonizerRFCost, 1, 10000);
    }

    @Config.Comment({
            "Max: 10,000",
            "Default: 256"
    })
    @Config.Name("RF tick cost of the Digital Will Injector")
    @Config.RangeInt(min = 1, max = 10000)
    public static int willInjectorRFCost = 256;
    public static int getWillInjectorRFCost() {
        return MathHelper.clamp(willInjectorRFCost, 1, 10000);
    }

    @Config.Comment({
        "Default: true"
    })
    @Config.Name("Allow multiple agonizers linked with one Altar")
    public static boolean isMultipleAgonizersAllowed = true;

}
