package com.glodblock.github.dmeblood.common.inventory;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.iface.IMultiWillTool;
import WayofTime.bloodmagic.item.soul.ItemSoulGem;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.soul.IDemonWill;
import WayofTime.bloodmagic.soul.IDemonWillGem;
import mustapelto.deepmoblearning.common.inventory.ItemHandlerBase;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SoulGemInputHandler extends ItemHandlerBase {

    public SoulGemInputHandler() {
        super();
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.getItem() instanceof ItemSoulGem) {
            return super.insertItem(slot, stack, simulate);
        } else {
            return stack;
        }
    }

    @Nullable
    public EnumDemonWillType getWillType() {
        ItemStack stack = this.getStackInSlot(0);
        if (stack.getItem() instanceof IMultiWillTool) {
            return ((IMultiWillTool) stack.getItem()).getCurrentType(stack);
        }
        return null;
    }

    public void fillGem(double willAmount, EnumDemonWillType type) {
        ItemStack gemStack = this.getStackInSlot(0);
        IDemonWillGem gem = (IDemonWillGem) gemStack.getItem();
        IDemonWill soul = (IDemonWill) RegistrarBloodMagicItems.MONSTER_SOUL;
        ItemStack soulStack = soul.createWill(type.ordinal(), willAmount);
        gem.fillDemonWillGem(gemStack, soulStack);
    }

    public void genSoul(double willAmount, EnumDemonWillType type) {
        IDemonWill soul = (IDemonWill) RegistrarBloodMagicItems.MONSTER_SOUL;
        this.setStackInSlot(0, soul.createWill(type.ordinal(), willAmount));
    }

}
