package com.glodblock.github.dmeblood.common.container;

import com.glodblock.github.dmeblood.common.inventory.slot.SlotDigitalAgonizer;
import com.glodblock.github.dmeblood.common.tile.TileEntityDigitalAgonizer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class ContainerDigitalAgonizer extends DMEBMContainer<TileEntityDigitalAgonizer> {
    public static final int DATA_MODEL_SLOT = 0;
    public static final int CATALYST_SLOT = 1;

    private final IItemHandler inventory;
    private final World world;

    public ContainerDigitalAgonizer(TileEntityDigitalAgonizer te, InventoryPlayer inventory, World world) {
        super(te, inventory);
        this.world = world;
        this.inventory = this.tile.getInnerInventory();
        this.tile.updateState(true);
        addSlotsToHandler();
        addInventorySlots();
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (!this.world.isRemote) {
            this.tile.updateState(false);
            this.tile.updateSacrificeRuneCount();
        }
    }

    @Override
    protected void addSlotsToHandler() {
        addSlotToContainer(new SlotDigitalAgonizer(this.inventory, DATA_MODEL_SLOT, 92, 79));
        addSlotToContainer(new SlotDigitalAgonizer(this.inventory, CATALYST_SLOT, 66, 34));
    }
}
