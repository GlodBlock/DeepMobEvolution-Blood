package com.glodblock.github.dmeblood.common.inventory;

import com.glodblock.github.dmeblood.util.Catalyst;
import mustapelto.deepmoblearning.common.inventory.ItemHandlerBase;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class CatalystInputHandler extends ItemHandlerBase {
    public CatalystInputHandler() {
        super();
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (Catalyst.isValidCatalyst(stack)) {
            return super.insertItem(slot, stack, simulate);
        } else {
            return stack;
        }
    }
}
