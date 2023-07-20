package com.glodblock.github.dmeblood.common.tile;

import com.glodblock.github.dmeblood.DeepMobLearningBM;
import com.glodblock.github.dmeblood.ModConfig;
import com.glodblock.github.dmeblood.network.packets.SPacketStateUpdate;
import mustapelto.deepmoblearning.common.energy.DMLEnergyStorage;
import mustapelto.deepmoblearning.common.inventory.ItemHandlerBase;
import mustapelto.deepmoblearning.common.inventory.ItemHandlerDataModel;
import mustapelto.deepmoblearning.common.tiles.CraftingState;
import mustapelto.deepmoblearning.common.util.ItemStackHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nonnull;

public abstract class TileMachine extends TileEntity implements ITickable, IContainerProvider {

    private static final int DISK_SAVE = 100;
    protected final ItemHandlerBase dataModel = new ItemHandlerDataModel();
    protected final DMLEnergyStorage energyCap = new DMLEnergyStorage(ModConfig.getRFCapacity(), ModConfig.getRFInput()) {
        protected void onEnergyChanged() {
            TileMachine.this.markDirty();
        }
    };
    protected int saveTicks = 0;
    protected int progress = 0;
    protected int maxProgress = 0;
    protected int FEt = 0;
    protected CraftingState state = CraftingState.IDLE;

    public abstract void onRunningTick();

    public abstract void onExistingTick();

    public abstract void processOutput();

    public abstract boolean beginProcess();

    @Override
    public CraftingState getState() {
        return this.state;
    }

    @Override
    public void setState(CraftingState state) {
        this.state = state;
        IBlockState block = this.world.getBlockState(this.getPos());
        this.world.notifyBlockUpdate(this.getPos(), block, block, 3);
    }

    @Override
    public final void update() {
        this.saveTicks ++;
        if (this.progress == 0) {
            if (this.beginProcess()) {
                if (this.isServer()) {
                    this.updateCraftState(CraftingState.RUNNING);
                }
            } else {
                if (this.isServer()) {
                    this.updateCraftState();
                }
            }
        } else {
            if (this.isServer()) {
                this.updateCraftState();
            }
        }
        if (this.maxProgress != 0 && this.consumeEnergy()) {
            this.onRunningTick();
            this.progress ++;
            if (this.progress >= this.maxProgress) {
                this.progress = 0;
                this.maxProgress = 0;
                this.processOutput();
            }
        }
        this.onExistingTick();
        this.doStaggeredDiskSave();
    }

    public boolean consumeEnergy() {
        if (this.FEt > 0) {
            if (this.energyCap.getEnergyStored() > this.FEt) {
                this.energyCap.voidEnergy(this.FEt);
                return true;
            }
            return false;
        }
        return false;
    }

    public void updateCraftState() {
        if (this.state != this.getCurrentState()) {
            this.state = this.getCurrentState();
            DeepMobLearningBM.proxy.netHandler.sendToAllTracking(
                    new SPacketStateUpdate(this, this.state),
                    new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 10.0)
            );
        }
    }

    public void updateCraftState(CraftingState state) {
        if (this.state != state) {
            this.state = state;
            DeepMobLearningBM.proxy.netHandler.sendToAllTracking(
                    new SPacketStateUpdate(this, this.state),
                    new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 10.0)
            );
        }
    }

    protected abstract CraftingState getCurrentState();

    public ItemStack getDataModelStack() {
        return this.dataModel.getStackInSlot(0);
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
        return new SPacketUpdateTileEntity(getPos(), 3, this.writeToNBT(new NBTTagCompound()));
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
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("progress", this.progress);
        compound.setTag("dataModel", this.dataModel.serializeNBT());
        compound.setInteger("state", this.state.getIndex());
        compound.setInteger("maxProgress", this.maxProgress);
        compound.setInteger("FEt", this.FEt);
        this.energyCap.writeToNBT(compound);
        return compound;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.progress = compound.hasKey("progress", Constants.NBT.TAG_INT) ? compound.getInteger("progress") : 0;
        this.dataModel.deserializeNBT(compound.getCompoundTag("dataModel"));
        this.state = CraftingState.byIndex(compound.getInteger("state"));
        this.maxProgress = compound.hasKey("maxProgress", Constants.NBT.TAG_INT) ? compound.getInteger("maxProgress") : 0;
        this.FEt = compound.hasKey("FEt", Constants.NBT.TAG_INT) ? compound.getInteger("FEt") : 0;
        this.energyCap.readFromNBT(compound);
        if (this.maxProgress == 0 && this.progress != 0) {
            // Fix deadlock
            this.progress = 0;
        }
    }

    @Override
    public boolean shouldRefresh(@Nonnull World world, @Nonnull BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    private void doStaggeredDiskSave() {
        if (this.isServer() && this.saveTicks >= DISK_SAVE) {
            this.markDirty();
            this.saveTicks = 0;
        }
    }

    public int getProgress() {
        return this.progress;
    }

    public int getMaxProgress() {
        return this.maxProgress;
    }

    public int getFEt() {
        return this.FEt;
    }

    public boolean hasDataModel() {
        return ItemStackHelper.isDataModel(getDataModelStack());
    }

    public boolean isServer() {
        return !this.world.isRemote;
    }

    public void stop() {
        this.progress = 0;
        this.maxProgress = 0;
    }

}
