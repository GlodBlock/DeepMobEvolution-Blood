package com.glodblock.github.dmeblood.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface IContainerProvider {
    Object getContainer(TileEntity entity, EntityPlayer player, World world, int x, int y, int z);
    Object getGui(TileEntity entity, EntityPlayer player, World world, int x, int y, int z);
}
