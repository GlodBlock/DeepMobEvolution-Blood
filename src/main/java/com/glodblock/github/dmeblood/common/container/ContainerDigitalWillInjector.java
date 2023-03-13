package com.glodblock.github.dmeblood.common.container;

import com.glodblock.github.dmeblood.common.inventory.slot.SlotDigitalWillInjector;
import com.glodblock.github.dmeblood.common.tile.TileEntityDigitalWillInjector;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class ContainerDigitalWillInjector extends DMEBMContainer<TileEntityDigitalWillInjector> {

    public static final int DATA_MODEL_SLOT = 0;
    public static final int WEAPON_SLOT = 1;
    public static final int GEM_SLOT = 2;

    private final IItemHandler inventory;
    private final World world;

    public ContainerDigitalWillInjector(TileEntityDigitalWillInjector te, InventoryPlayer inventory, World world) {
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
        }
    }

    @Override
    protected void addSlotsToHandler() {
        addSlotToContainer(new SlotDigitalWillInjector(this.inventory, DATA_MODEL_SLOT, 92, 79));
        addSlotToContainer(new SlotDigitalWillInjector(this.inventory, WEAPON_SLOT, 66, 34));
        addSlotToContainer(new SlotDigitalWillInjector(this.inventory, GEM_SLOT, 129, 34));
    }

}
