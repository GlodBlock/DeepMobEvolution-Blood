package com.glodblock.github.dmeblood.common.inventory;

import WayofTime.bloodmagic.iface.IMultiWillTool;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import com.glodblock.github.dmeblood.util.SentientWeapon;
import mustapelto.deepmoblearning.common.inventory.ItemHandlerBase;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SentientInputHandler extends ItemHandlerBase {

    public SentientInputHandler() {
        super();
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (SentientWeapon.isValidWeapon(stack)) {
            return super.insertItem(slot, stack, simulate);
        } else {
            return stack;
        }
    }

    @Nullable
    public EnumDemonWillType getWillType() {
        ItemStack stack = this.getStackInSlot(0);
        if (stack.isEmpty() || !SentientWeapon.isValidWeapon(stack)) {
            return null;
        }
        if (stack.getItem() instanceof IMultiWillTool) {
            return ((IMultiWillTool) stack.getItem()).getCurrentType(stack);
        }
        return EnumDemonWillType.DEFAULT;
    }
}