package com.glodblock.github.dmeblood.proxy;

import com.glodblock.github.dmeblood.DeepMobLearningBM;
import com.glodblock.github.dmeblood.ModConstants;
import com.glodblock.github.dmeblood.common.data.JSONLoader;
import com.glodblock.github.dmeblood.integration.tconstruct.MaterialDef;
import com.glodblock.github.dmeblood.network.PacketLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.io.File;

public class CommonProxy {

    public final SimpleNetworkWrapper netHandler = NetworkRegistry.INSTANCE.newSimpleChannel(ModConstants.MODID);

    public void preInit(FMLPreInitializationEvent event) {
        JSONLoader.setRoot(new File(event.getModConfigurationDirectory(), "dme_bloodmagic"));
        PacketLoader.init();
        if (ModConstants.TINKER_CONSTRUCT) {
            MaterialDef.init();
        }
    }

    public static void openTileEntityGui(World world, EntityPlayer player, int id, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        ItemStack mainHandItem = player.getHeldItem(EnumHand.MAIN_HAND);
        ItemStack offHandItem = player.getHeldItem(EnumHand.OFF_HAND);
        if(mainHandItem.getItem().getToolClasses(mainHandItem).contains("wrench") || offHandItem.getItem().getToolClasses(offHandItem).contains("wrench")) {
            return;
        }
        player.openGui(DeepMobLearningBM.instance, id, player.world, pos.getX(), pos.getY(), pos.getZ());
    }

    public void registerItemRenderer(Item item, ResourceLocation location, int meta) {}

    public void spawnParticle(World world, double x, double y, double z, double mx, double my, double mz) {}
}
