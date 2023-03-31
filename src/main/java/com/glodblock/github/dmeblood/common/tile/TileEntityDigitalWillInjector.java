package com.glodblock.github.dmeblood.common.tile;

import WayofTime.bloodmagic.soul.EnumDemonWillType;
import com.glodblock.github.dmeblood.ModConfig;
import com.glodblock.github.dmeblood.client.gui.DigitalWillInjectorGui;
import com.glodblock.github.dmeblood.common.container.ContainerDigitalWillInjector;
import com.glodblock.github.dmeblood.common.data.DataSet;
import com.glodblock.github.dmeblood.common.inventory.SentientInputHandler;
import com.glodblock.github.dmeblood.common.inventory.SoulGemInputHandler;
import com.glodblock.github.dmeblood.util.EssenceHelper;
import com.glodblock.github.dmeblood.util.SentientWeapon;
import mustapelto.deepmoblearning.common.tiles.CraftingState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityDigitalWillInjector extends TileMachine {

    private final SentientInputHandler weapon = new SentientInputHandler();
    private final SoulGemInputHandler gem = new SoulGemInputHandler();

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public IItemHandler getInnerInventory() {
        return new CombinedInvWrapper(this.dataModel, this.weapon, this.gem);
    }

    @Override
    public void onRunningTick() {
        if (this.isServer()) {
            if (!((this.weapon.getWillType() == this.gem.getWillType() && this.gem.canFill(this.getFinalOutput(),this.gem.getWillType()) ||
                    this.gem.getWillType() == null && this.gem.isEmpty())) ||
                    this.weapon.getWillType() == null ||
                    this.getModelModifier() <= 0 ||
                    this.getTierOutput() <= 0) {
                this.stop();
            }
        }
    }

    @Override
    public void onExistingTick() {
        // NO-OP
    }

    @Override
    public void processOutput() {
        if (this.isServer()) {
            if (this.weapon.getWillType() != null) {
                if (this.weapon.getWillType() == this.gem.getWillType() && this.gem.canFill(this.getFinalOutput(), this.gem.getWillType())) {
                    this.gem.fillGem(this.getFinalOutput(), this.weapon.getWillType());
                    SentientWeapon.damageWeapon(this.weapon.getStackInSlot(0), this.world.rand);
                } else if (this.gem.getWillType() == null && this.gem.isEmpty()) {
                    this.gem.genSoul(this.getFinalOutput(), this.weapon.getWillType());
                    SentientWeapon.damageWeapon(this.weapon.getStackInSlot(0), this.world.rand);
                }
            }
        }
    }

    @Override
    public boolean beginProcess() {
        if (this.isServer()) {
            if (this.weapon.getWillType() != null &&
                (this.weapon.getWillType() == this.gem.getWillType() && this.gem.canFill(this.getFinalOutput(),this.gem.getWillType()) ||
                 this.gem.getWillType() == null && this.gem.isEmpty()) &&
                this.getModelModifier() > 0 &&
                this.getTierOutput() > 0) {
                this.maxProgress = 60;
                this.FEt = ModConfig.getWillInjectorRFCost();
                return true;
            }
        }
        return false;
    }

    @Override
    public ContainerDigitalWillInjector getContainer(TileEntity entity, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerDigitalWillInjector((TileEntityDigitalWillInjector) world.getTileEntity(new BlockPos(x, y, z)), player.inventory, world);
    }

    @Override
    public DigitalWillInjectorGui getGui(TileEntity entity, EntityPlayer player, World world, int x, int y, int z) {
        return new DigitalWillInjectorGui((TileEntityDigitalWillInjector) world.getTileEntity(new BlockPos(x, y, z)), player.inventory, world);
    }

    @Override
    protected CraftingState getCurrentState() {
        if (this.maxProgress > 0 && this.energyCap.getEnergyStored() >= this.FEt) {
            return CraftingState.RUNNING;
        }
        if (getDataModelStack().isEmpty()) {
            return CraftingState.IDLE;
        }
        return CraftingState.ERROR;
    }

    private double getFinalOutput() {
        double base = SentientWeapon.getWill(this.weapon.getStackInSlot(0), this.getDataModelStack(), this.world.rand);
        double tier = this.getTierOutput();
        return base * tier;
    }

    public ItemStack getDataModelStack() {
        return this.dataModel.getStackInSlot(0);
    }

    public double getModelModifier() {
        return DataSet.willData.getTypeModifier(this.getDataModelStack());
    }

    public double getTierOutput() {
        return DataSet.willTierData.getWillOutput(EssenceHelper.getModelTier(this.getDataModelStack()));
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("weapon", this.weapon.serializeNBT());
        compound.setTag("gem", this.gem.serializeNBT());
        return compound;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.weapon.deserializeNBT(compound.getCompoundTag("weapon"));
        this.gem.deserializeNBT(compound.getCompoundTag("gem"));
    }

    @Override
    public boolean hasCapability(@Nonnull Capability capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new CombinedInvWrapper(this.weapon, this.gem));
        } if (capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(energyCap);
        } else {
            return super.getCapability(capability, facing);
        }
    }

    public EnumDemonWillType getWeaponWillType() {
        return this.weapon.getWillType();
    }

    public EnumDemonWillType getGemWillType() {
        return this.gem.getWillType();
    }

}
