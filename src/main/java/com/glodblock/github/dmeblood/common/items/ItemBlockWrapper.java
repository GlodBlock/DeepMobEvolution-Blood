package com.glodblock.github.dmeblood.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import java.util.Objects;

public class ItemBlockWrapper extends ItemBlock {

    public ItemBlockWrapper(Block block) {
        super(block);
        this.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
    }

}
