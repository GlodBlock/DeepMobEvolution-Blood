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
import mustapelto.deepmoblearning.common.energy.DMLEnergyStorage;
import mustapelto.deepmoblearning.common.inventory.ItemHandlerBase;
import mustapelto.deepmoblearning.common.inventory.ItemHandlerDataModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityDigitalWillInjector extends TileEntity implements ITickable, IContainerProvider {

    private final ItemHandlerBase dataModel = new ItemHandlerDataModel();
    private final SentientInputHandler weapon = new SentientInputHandler();
    private final SoulGemInputHandler gem = new SoulGemInputHandler();
    private final DMLEnergyStorage energyCap = new DMLEnergyStorage(100000, 25600) {
        protected void onEnergyChanged() {
            TileEntityDigitalWillInjector.this.markDirty();
        }
    };
    private int saveTicks = 0;
    private int progress = 0;

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public IItemHandler getInnerInventory() {
        return new CombinedInvWrapper(this.dataModel, this.weapon, this.gem);
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
    public void update() {
        if (!this.world.isRemote) {
            this.saveTicks++;
            if (canContinueCraft()) {
                this.progress++;
                this.energyCap.voidEnergy(ModConfig.getWillInjectorRFCost());
                if (this.progress % 60 == 0) {
                    this.output();
                    this.progress = 0;
                }
            } else {
                this.progress = 0;
            }
            doStaggeredDiskSave(100);
        }
    }

    private void output() {
        if (this.weapon.getWillType() != null) {
            if (this.weapon.getWillType() == this.gem.getWillType()) {
                this.gem.fillGem(this.getFinalOutput(), this.weapon.getWillType());
                SentientWeapon.damageWeapon(this.weapon.getStackInSlot(0), this.world.rand);
            } else if (this.gem.getWillType() == null) {
                this.gem.genSoul(this.getFinalOutput(), this.weapon.getWillType());
                SentientWeapon.damageWeapon(this.weapon.getStackInSlot(0), this.world.rand);
            }
        }
    }

    private double getFinalOutput() {
        double base = SentientWeapon.getWill(this.weapon.getStackInSlot(0), this.getDataModelStack(), this.world.rand);
        double tier = this.getTierOutput();
        return base * tier;
    }

    private boolean canContinueCraft() {
        return this.weapon.getWillType() != null &&
                (this.weapon.getWillType() == this.gem.getWillType() || this.gem.getWillType() == null) &&
                this.energyCap.getEnergyStored() > ModConfig.getWillInjectorRFCost() &&
                this.getModelModifier() > 0 &&
                this.getTierOutput() > 0;
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

    private void doStaggeredDiskSave(int divisor) {
        if (this.saveTicks % divisor == 0) {
            this.markDirty();
            this.saveTicks = 0;
        }
    }

    @Override
    public void updateState(boolean markDirty) {
        IBlockState state = this.world.getBlockState(getPos());
        this.world.notifyBlockUpdate(getPos(), state, state, 3);
        if(markDirty) {
            markDirty();
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 3, writeToNBT(new NBTTagCompound()));
    }

    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(@Nonnull NetworkManager net, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("progress", this.progress);
        compound.setTag("dataModel", this.dataModel.serializeNBT());
        compound.setTag("weapon", this.weapon.serializeNBT());
        compound.setTag("gem", this.gem.serializeNBT());
        this.energyCap.writeToNBT(compound);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.progress = compound.hasKey("progress", Constants.NBT.TAG_INT) ? compound.getInteger("progress") : 0;
        this.dataModel.deserializeNBT(compound.getCompoundTag("dataModel"));
        this.weapon.deserializeNBT(compound.getCompoundTag("weapon"));
        this.gem.deserializeNBT(compound.getCompoundTag("gem"));
        this.energyCap.readFromNBT(compound);
        super.readFromNBT(compound);
    }

    @Override
    public boolean shouldRefresh(@Nonnull World world, @Nonnull BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
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

    public int getProgress() {
        return this.progress;
    }

}
