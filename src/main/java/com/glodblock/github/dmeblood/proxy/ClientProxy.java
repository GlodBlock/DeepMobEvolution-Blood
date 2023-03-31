package com.glodblock.github.dmeblood.proxy;

import com.glodblock.github.dmeblood.ModConstants;
import com.glodblock.github.dmeblood.client.particles.ParticleIndicator;
import com.glodblock.github.dmeblood.integration.tconstruct.MaterialDef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        if (ModConstants.TINKER_CONSTRUCT) {
            MaterialDef.initClient();
        }
    }

    @Override
    public void registerItemRenderer(Item item, ResourceLocation location, int meta) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(location, "inventory"));
    }

    @Override
    public void spawnParticle(World world, double x, double y, double z, double mx, double my, double mz) {
        Particle particle = new ParticleIndicator(world, x, y, z, mx, my, mz, 1.3F);
        particle.setRBGColorF(1.0F, 0.3F, 0.3F);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }
}
