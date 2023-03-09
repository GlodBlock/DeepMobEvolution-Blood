package com.glodblock.github.dmeblood.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import com.glodblock.github.dmeblood.common.tile.TileEntityDigitalAgonizer;

import javax.annotation.Nonnull;

public class ContainerDigitalAgonizer extends Container {
    public static final int DATA_MODEL_SLOT = 0;
    public static final int CATALYST_SLOT = 1;

    private final IItemHandler inventory;
    public TileEntityDigitalAgonizer tile;
    private final EntityPlayer player;
    private final World world;

    public ContainerDigitalAgonizer(TileEntityDigitalAgonizer te, InventoryPlayer inventory, World world) {
        this.player = inventory.player;
        this.world = world;
        this.tile = te;
        this.inventory = this.tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
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

    private void addSlotsToHandler() {
        addSlotToContainer(new SlotDigitalAgonizer(this.inventory, DATA_MODEL_SLOT, 92, 79));
        addSlotToContainer(new SlotDigitalAgonizer(this.inventory, CATALYST_SLOT, 66, 34));
    }

    private void addInventorySlots() {
        // Bind actionbar
        for (int row = 0; row < 9; row++) {
            Slot slot = new Slot(this.player.inventory, row, 20 + row * 18, 172);
            addSlotToContainer(slot);
        }

        // 3 Top rows, starting with the bottom one
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                int x = 20 + column * 18;
                int y = 114 + row * 18;
                int index = column + row * 9 + 9;
                Slot slot = new Slot(this.player.inventory, index, x, y);
                addSlotToContainer(slot);
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(@Nonnull EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();

            int containerSlots = this.inventorySlots.size() - player.inventory.mainInventory.size();

            if (index < containerSlots) {
                if (!mergeItemStack(stack, containerSlots, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(stack, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        this.tile.markDirty();
        this.player.inventory.markDirty();
        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return !player.isSpectator();
    }
}
