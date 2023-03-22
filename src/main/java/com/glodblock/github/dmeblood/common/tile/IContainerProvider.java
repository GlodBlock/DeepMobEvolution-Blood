package com.glodblock.github.dmeblood.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public interface IContainerProvider {

    int getID();

    /**
     * This inventory includes all item inventories in this tile.
     * @return All inventories
     */
    IItemHandler getInnerInventory();

    Object getContainer(TileEntity entity, EntityPlayer player, World world, int x, int y, int z);

    Object getGui(TileEntity entity, EntityPlayer player, World world, int x, int y, int z);

    void updateState(boolean markDirty);

}
