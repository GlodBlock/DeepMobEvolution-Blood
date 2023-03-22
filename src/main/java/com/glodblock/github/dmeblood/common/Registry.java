package com.glodblock.github.dmeblood.common;

import com.glodblock.github.dmeblood.DeepMobLearningBM;
import com.glodblock.github.dmeblood.ModConstants;
import com.glodblock.github.dmeblood.common.blocks.BlockBloodMetal;
import com.glodblock.github.dmeblood.common.blocks.BlockDigitalAgonizer;
import com.glodblock.github.dmeblood.common.blocks.BlockDigitalWillInjector;
import com.glodblock.github.dmeblood.common.items.ItemAltarLinker;
import com.glodblock.github.dmeblood.common.items.ItemBlockWrapper;
import com.glodblock.github.dmeblood.common.items.ItemBloodIngot;
import com.glodblock.github.dmeblood.common.items.ItemBloodNugget;
import com.glodblock.github.dmeblood.common.tile.TileEntityDigitalAgonizer;
import com.glodblock.github.dmeblood.common.tile.TileEntityDigitalWillInjector;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

public class Registry {
    private static final BlockDigitalAgonizer blockDigitalAgonizer = new BlockDigitalAgonizer();
    private static final BlockDigitalWillInjector blockDigitalWillInjector = new BlockDigitalWillInjector();
    private static final BlockBloodMetal blockBloodIngot = new BlockBloodMetal();
    public static Item blockDigitalAgonizerItem = new ItemBlockWrapper(blockDigitalAgonizer);
    public static Item blockDigitalWillInjectorItem = new ItemBlockWrapper(blockDigitalWillInjector);
    public static Item blockBloodIngotItem = new ItemBlockWrapper(blockBloodIngot);
    public static ItemAltarLinker itemAltarLinker = new ItemAltarLinker();
    public static ItemBloodIngot itemBloodIngot = new ItemBloodIngot();
    public static ItemBloodNugget itemBloodNugget = new ItemBloodNugget();

    private static final NonNullList<Block> blocks = NonNullList.create();
    private static final NonNullList<Item> itemBlocks = NonNullList.create();
    private static final NonNullList<Item> items = NonNullList.create();

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        if (ModConstants.BLOOD_MAGIC) {
            blocks.add(blockDigitalAgonizer);
            blocks.add(blockDigitalWillInjector);
            blocks.add(blockBloodIngot);
            blocks.forEach(registry::register);
            registerTileEntities();
        }
    }

    public static void registerItems(IForgeRegistry<Item> registry) {
        if (ModConstants.BLOOD_MAGIC) {
            itemBlocks.add(blockDigitalAgonizerItem);
            itemBlocks.add(blockDigitalWillInjectorItem);
            itemBlocks.add(blockBloodIngotItem);
            items.add(itemAltarLinker);
            items.add(itemBloodIngot);
            items.add(itemBloodNugget);
        }
        itemBlocks.forEach(registry::register);
        items.forEach(registry::register);
        OreDictionary.registerOre("nuggetBloodInfusedGlitch", itemBloodNugget);
        OreDictionary.registerOre("ingotBloodInfusedGlitch", itemBloodIngot);
        OreDictionary.registerOre("blockBloodInfusedGlitch", blockBloodIngotItem);
    }

    private static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityDigitalAgonizer.class, new ResourceLocation(ModConstants.MODID, "digital_agonizer"));
        GameRegistry.registerTileEntity(TileEntityDigitalWillInjector.class, new ResourceLocation(ModConstants.MODID, "digital_will_injector"));
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemModels() {
        for (Block block : blocks) {
            DeepMobLearningBM.proxy.registerItemRenderer(Item.getItemFromBlock(block), block.getRegistryName(), 0);
        }

        for (Item item : items) {
            DeepMobLearningBM.proxy.registerItemRenderer(item, item.getRegistryName(), 0);
        }
    }
}
