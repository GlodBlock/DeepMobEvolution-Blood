package com.glodblock.github.dmeblood.common.inventory;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.iface.IMultiWillTool;
import WayofTime.bloodmagic.item.soul.ItemSoulGem;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.soul.IDemonWill;
import WayofTime.bloodmagic.soul.IDemonWillGem;
import mustapelto.deepmoblearning.common.inventory.ItemHandlerBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SoulGemInputHandler extends ItemHandlerBase {
    private final int slotIndex = 0;

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
        ItemStack stack = this.getStackInSlot(slotIndex);
        if (stack.getItem() instanceof IMultiWillTool) {
            return ((IMultiWillTool) stack.getItem()).getCurrentType(stack);
        }
        return null;
    }

    public void fillGem(double willAmount, EnumDemonWillType type) {
        ItemStack gemStack = this.getStackInSlot(slotIndex);
        IDemonWillGem gem = (IDemonWillGem) gemStack.getItem();
        IDemonWill soul = (IDemonWill) RegistrarBloodMagicItems.MONSTER_SOUL;
        ItemStack soulStack = soul.createWill(type.ordinal(), willAmount);
        gem.fillDemonWillGem(gemStack, soulStack);
    }

    public void genSoul(double willAmount, EnumDemonWillType type) {
        IDemonWill soul = (IDemonWill) RegistrarBloodMagicItems.MONSTER_SOUL;
        this.setStackInSlot(slotIndex, soul.createWill(type.ordinal(), willAmount));
    }

    public boolean canFill(double willAmount, EnumDemonWillType type) {
        ItemStack gemStack = this.getGemStack();
        if (gemStack.isEmpty()) return false;
        Item gem = gemStack.getItem();
        if (gem instanceof ItemSoulGem) {
            return ((ItemSoulGem) gem).fillWill(type, gemStack, willAmount, false) > 0;
        }
        return false;
    }

    public ItemStack getGemStack() {
        ItemStack stack = this.getStackInSlot(slotIndex);
        if (stack.getItem() instanceof IDemonWillGem){
            return stack;
        }
        return ItemStack.EMPTY;
    }
    public boolean isEmpty() {
        return this.getStackInSlot(slotIndex).isEmpty();
    }

}
