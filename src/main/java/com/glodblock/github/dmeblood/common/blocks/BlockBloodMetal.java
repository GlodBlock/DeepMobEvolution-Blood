package com.glodblock.github.dmeblood.common.blocks;

import com.glodblock.github.dmeblood.DeepMobLearningBM;
import com.glodblock.github.dmeblood.ModConstants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockBloodMetal extends Block {

    public BlockBloodMetal() {
        super(Material.IRON);
        setTranslationKey(ModConstants.MODID + ".block_blood_infused_glitch_ingot");
        setCreativeTab(DeepMobLearningBM.creativeTab);
        setRegistryName("block_blood_infused_glitch_ingot");
        setHardness(12f);
        setResistance(10.0f);
        setLightLevel(1F);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        if (rand.nextDouble() > 0.7 && !world.getBlockState(pos.up()).isOpaqueCube()) {
            DeepMobLearningBM.proxy.spawnParticle(
                    world,
                    pos.getX() + rand.nextDouble(),
                    pos.getY() + 1.2D,
                    pos.getZ() + rand.nextDouble(),
                    rand.nextDouble() * 0.04 - 0.02,
                    0,
                    rand.nextDouble() * 0.04 - 0.02
            );
        }
    }
}
