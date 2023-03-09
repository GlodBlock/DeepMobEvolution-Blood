package com.glodblock.github.dmeblood;

import mustapelto.deepmoblearning.common.util.MathHelper;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.*;

@SuppressWarnings("WeakerAccess")
@Mod.EventBusSubscriber
@Config(modid = ModConstants.MODID)
public class ModConfig {
    @SubscribeEvent
    public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(ModConstants.MODID)) {
            ConfigManager.sync(ModConstants.MODID, Config.Type.INSTANCE);
        }
    }

    @Config.Name("Essence Multiplier Settings")
    public static CatalystMultiplierSubCategory essenceMultiplierSubCat = new CatalystMultiplierSubCategory();

    @Config.Name("Essence Base Amounts")
    public static EssenceBaseAmountsSubCategory essenceAmountSubCat = new EssenceBaseAmountsSubCategory();

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
        "Default: false"
    })
    @Config.Name("Allow multiple agonizers linked with one Altar")
    public static boolean isMultipleAgonizersAllowed = false;

    public static class EssenceBaseAmountsSubCategory {
        @Config.Comment({"Default: 50"})
        @Config.Name("Tier 1 Data Model - Essence per operation")
        @Config.RangeInt(min = 1, max = 2000)
        public int tierOneEssenceAmount = 50;

        @Config.Comment({"Default: 75"})
        @Config.Name("Tier 2 Data Model - Essence per operation")
        @Config.RangeInt(min = 1, max = 2000)
        public int tierTwoEssenceAmount = 75;

        @Config.Comment({"Default: 150"})
        @Config.Name("Tier 3 Data Model - Essence per operation")
        @Config.RangeInt(min = 1, max = 2000)
        public int tierThreeEssenceAmount = 150;

        @Config.Comment({"Default: 300"})
        @Config.Name("Tier 4 Data Model - Essence per operation")
        @Config.RangeInt(min = 1, max = 2000)
        public int tierFourEssenceAmount = 300;

        public int getTierEssenceAmount(int tier) {
            switch (tier) {
                case 1: return MathHelper.clamp(tierOneEssenceAmount, 1, 2000);
                case 2: return MathHelper.clamp(tierTwoEssenceAmount, 1, 2000);
                case 3: return MathHelper.clamp(tierThreeEssenceAmount, 1, 2000);
                case 4: return MathHelper.clamp(tierFourEssenceAmount, 1, 2000);
                default: return 1;
            }
        }
    }

    public static class CatalystMultiplierSubCategory {
        @Config.Comment({"Default: 5.0"})
        @Config.Name("#1 Multiplier of Corrupted Glitch Heart")
        @Config.RangeDouble(min = 1.0, max = 10.0)
        public double heartCatalystMultiplier = 5.0;
        public double getHeartCatalystMultiplier() {
            return MathHelper.clamp(heartCatalystMultiplier, 1.0, 10.0);
        }

        @Config.Comment({"Default: 2.2"})
        @Config.Name("#2 Multiplier of Overworldian Living matter")
        @Config.RangeDouble(min = 1.0, max = 10.0)
        public double overworldianCatalystMultiplier = 2.2;
        public double getOverworldianCatalystMultiplier() {
            return MathHelper.clamp(overworldianCatalystMultiplier, 1.0, 10.0);
        }

        @Config.Comment({"Default: 2.4"})
        @Config.Name("#3 Multiplier of Hellish Living matter")
        @Config.RangeDouble(min = 1.0, max = 10.0)
        public double hellishCatalystMultiplier = 2.4;
        public double getHellishCatalystMultiplier() {
            return MathHelper.clamp(hellishCatalystMultiplier, 1.0, 10.0);
        }

        @Config.Comment({"Default: 2.7"})
        @Config.Name("#4 Multiplier of Extraterrestrial Living matter")
        @Config.RangeDouble(min = 1.0, max = 10.0)
        public double extraterrestrialCatalystMultiplier = 2.7;
        public double getExtraterrestrialCatalystMultiplier() {
            return MathHelper.clamp(extraterrestrialCatalystMultiplier, 1.0, 10.0);
        }

        @Config.Comment({"Default: 2.5"})
        @Config.Name("#5 Multiplier of Twilight Living matter")
        @Config.RangeDouble(min = 1.0, max = 10.0)
        public double twilightCatalystMultiplier = 2.5;
        public double getTwilightCatalystMultiplier() {
            return MathHelper.clamp(twilightCatalystMultiplier, 1.0, 10.0);
        }
    }


}
