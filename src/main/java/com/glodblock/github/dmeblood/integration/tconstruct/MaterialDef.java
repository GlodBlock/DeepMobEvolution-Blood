package com.glodblock.github.dmeblood.integration.tconstruct;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;
import slimeknights.tconstruct.library.fluid.FluidMolten;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

import javax.annotation.Nonnull;

public class MaterialDef {

    public static final Material BLOOD_INGOT = new Material("blood_infused_glitch", 0xde2412);

    public static void init() {
        setFluid(BLOOD_INGOT);
        BLOOD_INGOT.setCastable(true);
        BLOOD_INGOT.setCraftable(false);
        BLOOD_INGOT.setRepresentativeItem("ingotBloodInfusedGlitch");
        BLOOD_INGOT.addTrait(TraitDef.TRAIT_BLOODLUST);
        BLOOD_INGOT.addTrait(TraitDef.TRAIT_GLITCH);
        BLOOD_INGOT.addTrait(TraitDef.TRAIT_OVERFLOW, "head");
        TinkerRegistry.addMaterialStats(BLOOD_INGOT, new HeadMaterialStats(450, 19.2F, 6.66F, HarvestLevels.COBALT));
        TinkerRegistry.addMaterialStats(BLOOD_INGOT, new HandleMaterialStats(1.1F, 0));
        TinkerRegistry.addMaterialStats(BLOOD_INGOT, new ExtraMaterialStats(130));
        TinkerRegistry.addMaterialStats(BLOOD_INGOT, new BowMaterialStats(0.66F, 1.2F, 6.66F));
        TinkerRegistry.addMaterial(BLOOD_INGOT);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        BLOOD_INGOT.setRenderInfo(new MaterialRenderInfo.MultiColor(0x9b921c, 0xde2412, 0x9b921c));
    }

    private static void setFluid(@Nonnull Material material) {
        if (FluidRegistry.getFluid(material.identifier) == null) {
            Fluid fluid = new FluidMolten(material.identifier, material.materialTextColor);
            FluidRegistry.registerFluid(fluid);
            FluidRegistry.addBucketForFluid(fluid);
            material.setFluid(fluid);
        } else {
            material.setFluid(FluidRegistry.getFluid(material.identifier));
        }
    }

    public static void initRecipe() {
        TinkerSmeltery.registerOredictMeltingCasting(BLOOD_INGOT.getFluid(), "BloodInfusedGlitch");
        TinkerSmeltery.registerToolpartMeltingCasting(BLOOD_INGOT);
    }

}
