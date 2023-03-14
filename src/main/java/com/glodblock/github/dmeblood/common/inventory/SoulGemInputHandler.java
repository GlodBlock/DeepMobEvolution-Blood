package com.glodblock.github.dmeblood.common.inventory;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.soul.ItemMonsterSoul;
import WayofTime.bloodmagic.item.soul.ItemSoulGem;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.soul.IDemonWill;
import mustapelto.deepmoblearning.common.inventory.ItemHandlerBase;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SoulGemInputHandler extends ItemHandlerBase {
    IDemonWill SOUL = (IDemonWill) RegistrarBloodMagicItems.MONSTER_SOUL;

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
        ItemStack stack = this.getGemStack();
        if (stack.getItem() instanceof ItemMonsterSoul) {
            return ((ItemMonsterSoul) stack.getItem()).getType(stack);
        }
        return null;
    }

    public void fillGem(double willAmount, EnumDemonWillType type) {
        ItemStack gemStack = this.getGemStack();
        IDemonWill gem = (IDemonWill) gemStack.getItem();
        this.setGemStack(willAmount + gem.getWill(type,gemStack),type);
    }

    public void genSoul(double willAmount, EnumDemonWillType type) {
        this.setGemStack(willAmount,type);
    }

    public ItemStack getGemStack(){
        return this.getStackInSlot(0);
    }

    public void setGemStack(double willAmount,EnumDemonWillType type){
        if(this.getGemStack() == null){
            this.setStackInSlot(0, SOUL.createWill(type.ordinal(), willAmount));
        }else{
            ItemStack gemStack = this.getGemStack();
            IDemonWill gem = (IDemonWill) gemStack.getItem();
            gem.setWill(type,gemStack,willAmount);
        }
    }
}
