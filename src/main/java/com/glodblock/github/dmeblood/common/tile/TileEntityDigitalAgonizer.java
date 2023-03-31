package com.glodblock.github.dmeblood.common.tile;

import WayofTime.bloodmagic.altar.AltarUpgrade;
import WayofTime.bloodmagic.altar.BloodAltar;
import WayofTime.bloodmagic.block.enums.BloodRuneType;
import WayofTime.bloodmagic.tile.TileAltar;
import com.glodblock.github.dmeblood.DeepMobLearningBM;
import com.glodblock.github.dmeblood.ModConfig;
import com.glodblock.github.dmeblood.client.gui.DigitalAgonizerGui;
import com.glodblock.github.dmeblood.common.container.ContainerDigitalAgonizer;
import com.glodblock.github.dmeblood.common.inventory.CatalystInputHandler;
import com.glodblock.github.dmeblood.util.Catalyst;
import com.glodblock.github.dmeblood.util.EssenceHelper;
import mustapelto.deepmoblearning.common.inventory.ItemHandlerBase;
import mustapelto.deepmoblearning.common.tiles.CraftingState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;

public class TileEntityDigitalAgonizer extends TileMachine {

    private final ItemHandlerBase input = new CatalystInputHandler();
    private int highlightingTicks = 0;
    private int catalystOperations = 0;
    private int catalystOperationsMax = 0;
    private BlockPos altarPos = BlockPos.fromLong(0);
    private int numOfSacrificeRunes = 0;
    private double multiplier = 1.0;

    @Override
    public void onRunningTick() {
        if (this.isServer()) {
            if (!hasDataModel() || !isValidDataModelTier()) {
                this.stop();
            }
        }
    }

    @Override
    public void onExistingTick() {
        if (this.isServer()) {
            if (this.highlightingTicks > 0) {
                this.highlightingTicks --;
                if (this.highlightingTicks == 0) {
                    this.updateState(true);
                }
            }
            if (this.catalystOperations == 0) {
                this.consumeCatalyst();
            }
            if (this.getAltarTank() == null) {
                this.setAltarPos(BlockPos.fromLong(0));
            }
        } else {
            if (this.highlightingTicks > 0) {
                ThreadLocalRandom rand = ThreadLocalRandom.current();
                if (getAltarTank() != null) {
                    DeepMobLearningBM.proxy.spawnParticle(
                            this.world,
                            getAltarPos().getX() + 0.5D + rand.nextDouble(-0.33D, 0.33D),
                            getAltarPos().getY() + 1.2D,
                            getAltarPos().getZ() + 0.5D + rand.nextDouble(-0.33D, 0.33D),
                            rand.nextDouble(-0.02D, 0.02D),
                            0,
                            rand.nextDouble(-0.02D, 0.02D)
                    );
                }
            }
        }
    }

    @Override
    public void processOutput() {
        if (this.isServer()) {
            BloodAltar altar = getAltarTank();
            if (altar != null) {
                altar.fillMainTank(getFillAmount());
            }
            if (this.catalystOperations > 0) {
                this.catalystOperations--;
            } else {
                this.multiplier = 1.0;
            }
        }
    }

    @Override
    public boolean beginProcess() {
        if (this.isServer()) {
            if (!altarIsFull() && hasDataModel() && isValidDataModelTier()) {
                this.maxProgress = 60;
                this.FEt = ModConfig.getAgonizerRFCost();
                return true;
            }
        }
        return false;
    }

    @Override
    protected CraftingState getCurrentState() {
        if (this.maxProgress > 0 && this.energyCap.getEnergyStored() >= this.FEt) {
            return CraftingState.RUNNING;
        }
        if (altarIsFull() || !hasDataModel()) {
            return CraftingState.IDLE;
        }
        return CraftingState.ERROR;
    }

    public double getSacrificeMultiplier() {
        return getAltarTank() != null ? getAltarTank().getSacrificeMultiplier() : 0;
    }

    public void updateSacrificeRuneCount() {
        BloodAltar altar = getAltarTank();
        if (altar != null) {
            AltarUpgrade altarUpgrade = altar.getUpgrade();
            if (altarUpgrade != null) {
                this.numOfSacrificeRunes = altarUpgrade.getLevel(BloodRuneType.SACRIFICE);
            } else {
                this.numOfSacrificeRunes = 0;
            }
        } else {
            this.numOfSacrificeRunes = 0;
        }
    }

    private void consumeCatalyst() {
        Catalyst catalyst = Catalyst.getCatalyst(getCatalystStack());
        if (catalyst != null) {
            this.catalystOperations = catalyst.getOperations();
            this.catalystOperationsMax = catalyst.getOperations();
            this.multiplier = catalyst.getMultiplier();
            getCatalystStack().shrink(1);
        }
    }

    public BlockPos getAltarPos() {
        return this.altarPos;
    }

    public void setAltarPos(BlockPos pos) {
        this.altarPos = pos;
    }

    public BloodAltar getAltarTank() {
        if (this.altarPos != null) {
            TileEntity tile = this.world.getTileEntity(this.altarPos);
            if(tile instanceof TileAltar) {
                return (BloodAltar) tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
            } else {
                return null;
            }
        }
        return null;
    }

    private boolean altarIsFull() {
        return getAltarTank() == null || (getAltarTank().getFluidAmount() + getFillAmount()) >= getAltarTank().getCapacity();
    }

    public void setHighlightingTicks(int highlightingTicks) {
        this.highlightingTicks = highlightingTicks;
        updateState(true);
    }

    public int getNumOfSacrificeRunes() {
        return this.numOfSacrificeRunes;
    }

    private ItemStack getCatalystStack() {
        return this.input.getStackInSlot(0);
    }

    public boolean isValidDataModelTier() {
        return EssenceHelper.getFluidBaseAmount(getDataModelStack()) > 0;
    }

    public int getFillAmount() {
        return EssenceHelper.getFillAmount(getDataModelStack(), (getMultiplier() + getSacrificeMultiplier()));
    }

    public int getCatalystOperations() {
        return this.catalystOperations;
    }

    public int getCatalystOperationsMax() {
        return this.catalystOperationsMax;
    }

    public double getMultiplier() {
        return this.multiplier;
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("highlightingTicks", this.highlightingTicks);
        compound.setInteger("catalystOperations", this.catalystOperations);
        compound.setInteger("catalystOperationsMax", this.catalystOperationsMax);
        compound.setDouble("mutliplier", this.multiplier);
        compound.setLong("altarPos", this.altarPos.toLong());
        compound.setInteger("numOfSacrificeRunes", this.numOfSacrificeRunes);
        compound.setTag("input", this.input.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.highlightingTicks = compound.hasKey("highlightingTicks", Constants.NBT.TAG_INT) ? compound.getInteger("highlightingTicks") : 0;
        this.catalystOperations = compound.hasKey("catalystOperations", Constants.NBT.TAG_INT) ? compound.getInteger("catalystOperations") : 0;
        this.catalystOperationsMax = compound.hasKey("catalystOperationsMax", Constants.NBT.TAG_INT) ? compound.getInteger("catalystOperationsMax") : 0;
        this.multiplier = compound.hasKey("mutliplier", Constants.NBT.TAG_DOUBLE) ? compound.getDouble("mutliplier") : 1.0;
        this.numOfSacrificeRunes = compound.hasKey("numOfSacrificeRunes", Constants.NBT.TAG_INT) ? compound.getInteger("numOfSacrificeRunes") : 0;
        this.altarPos = compound.hasKey("altarPos", Constants.NBT.TAG_LONG) ? BlockPos.fromLong(compound.getLong("altarPos")) : null;
        this.input.deserializeNBT(compound.getCompoundTag("input"));
    }

    @Override
    public boolean hasCapability(@Nonnull Capability capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.input);
        } if (capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(energyCap);
        } else {
            return super.getCapability(capability, facing);
        }
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public IItemHandler getInnerInventory() {
        return new CombinedInvWrapper(this.dataModel, this.input);
    }

    @Override
    public ContainerDigitalAgonizer getContainer(TileEntity entity, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerDigitalAgonizer((TileEntityDigitalAgonizer) world.getTileEntity(new BlockPos(x, y, z)), player.inventory, world);
    }

    @Override
    public DigitalAgonizerGui getGui(TileEntity entity, EntityPlayer player, World world, int x, int y, int z) {
        return new DigitalAgonizerGui((TileEntityDigitalAgonizer) world.getTileEntity(new BlockPos(x, y, z)), player.inventory, world);
    }

}
