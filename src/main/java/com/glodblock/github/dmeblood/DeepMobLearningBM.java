package com.glodblock.github.dmeblood;

import com.glodblock.github.dmeblood.common.Registry;
import com.glodblock.github.dmeblood.common.data.DataSet;
import com.glodblock.github.dmeblood.common.data.JSONLoader;
import com.glodblock.github.dmeblood.common.network.HighlightAltarMessage;
import com.glodblock.github.dmeblood.common.tile.IContainerProvider;
import com.glodblock.github.dmeblood.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.io.File;

@Mod(modid = ModConstants.MODID, version = ModConstants.VERSION, dependencies = "required-after:deepmoblearning;required-after:bloodmagic;after:jei;after:twilightforest")
@Mod.EventBusSubscriber
public class DeepMobLearningBM {
    private int networkID = 0;

    @Mod.Instance(ModConstants.MODID)
    public static DeepMobLearningBM instance;

    @SidedProxy(
        clientSide="com.glodblock.github.dmeblood.proxy.ClientProxy",
        serverSide="com.glodblock.github.dmeblood.proxy.CommonProxy"
    )
    public static CommonProxy proxy;
    public static SimpleNetworkWrapper network;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(ModConstants.MODID);
        network.registerMessage(HighlightAltarMessage.Handler.class, HighlightAltarMessage.class, networkID++, Side.SERVER);
        JSONLoader.setRoot(new File(event.getModConfigurationDirectory(), "dme_bloodmagic"));
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        Registry.registerBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        Registry.registerItems(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        Registry.registerItemModels();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        DataSet.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new IGuiHandler() {
            public Object getServerGuiElement(int i, EntityPlayer player, World world, int x, int y, int z) {
                TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                return tile instanceof IContainerProvider ? ((IContainerProvider) tile).getContainer(tile, player, world, x, y, z) : null;
            }
            public Object getClientGuiElement(int i, EntityPlayer player, World world, int x, int y, int z) {
                TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                return tile instanceof IContainerProvider ? ((IContainerProvider) tile).getGui(tile, player, world, x, y, z) : null;
            }
        });
    }

    public static CreativeTabs creativeTab = new CreativeTabs(ModConstants.MODID) {
        @Nonnull
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Registry.blockDigitalAgonizerItem);
        }
    };
}
