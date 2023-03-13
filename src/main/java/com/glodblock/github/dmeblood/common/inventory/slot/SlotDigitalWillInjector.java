package com.glodblock.github.dmeblood.common.inventory.slot;

import WayofTime.bloodmagic.item.soul.ItemSoulGem;
import com.glodblock.github.dmeblood.common.container.ContainerDigitalWillInjector;
import com.glodblock.github.dmeblood.util.SentientWeapon;
import mustapelto.deepmoblearning.common.items.ItemDataModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotDigitalWillInjector extends SlotItemHandler {

    public SlotDigitalWillInjector(IItemHandler handler, int index, int x, int y) {
        super(handler, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        Item item = stack.getItem();
        switch(getSlotIndex()) {
            case ContainerDigitalWillInjector.DATA_MODEL_SLOT:
                return !stack.isEmpty() && item instanceof ItemDataModel;
            case ContainerDigitalWillInjector.WEAPON_SLOT:
                return !stack.isEmpty() && SentientWeapon.isValidWeapon(stack);
            case ContainerDigitalWillInjector.GEM_SLOT:
                return !stack.isEmpty() && item instanceof ItemSoulGem;
            default:
                return false;
        }
    }

    @Override
    public int getSlotStackLimit() {
        return 64;
    }
}