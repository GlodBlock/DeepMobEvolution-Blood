package com.glodblock.github.dmeblood.common;

import com.glodblock.github.dmeblood.DeepMobLearningBM;
import com.glodblock.github.dmeblood.ModConstants;
import com.glodblock.github.dmeblood.common.tile.TileEntityDigitalAgonizer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import com.glodblock.github.dmeblood.common.blocks.BlockDigitalAgonizer;
import com.glodblock.github.dmeblood.common.items.ItemAltarLinker;

import java.util.Objects;

public class Registry {
    private static final BlockDigitalAgonizer blockDigitalAgonizer = new BlockDigitalAgonizer();
    public static Item blockDigitalAgonizerItem = new ItemBlock(blockDigitalAgonizer).setRegistryName(Objects.requireNonNull(blockDigitalAgonizer.getRegistryName()));
    public static ItemAltarLinker itemAltarLinker = new ItemAltarLinker();

    private static final NonNullList<Block> blocks = NonNullList.create();
    private static final NonNullList<Item> itemBlocks = NonNullList.create();
    private static final NonNullList<Item> items = NonNullList.create();

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        if(ModConstants.BLOOD_MAGIC) {
            blocks.add(blockDigitalAgonizer);
            blocks.forEach(registry::register);
            registerTileEntities();
        }
    }

    public static void registerItems(IForgeRegistry<Item> registry) {
        if(ModConstants.BLOOD_MAGIC) {
            itemBlocks.add(blockDigitalAgonizerItem);
            items.add(itemAltarLinker);
        }
        itemBlocks.forEach(registry::register);
        items.forEach(registry::register);
    }

    private static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityDigitalAgonizer.class, new ResourceLocation(ModConstants.MODID, "digital_agonizer"));
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
