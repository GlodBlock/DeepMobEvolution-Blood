package com.glodblock.github.dmeblood.common.items;

import WayofTime.bloodmagic.tile.TileAltar;
import com.glodblock.github.dmeblood.ModConfig;
import com.glodblock.github.dmeblood.common.blocks.BlockDigitalAgonizer;
import com.glodblock.github.dmeblood.common.tile.TileEntityDigitalAgonizer;
import mustapelto.deepmoblearning.common.util.NBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemAltarLinker extends ItemBase {
    private final int linkingRange = 25;

    public ItemAltarLinker() {
        super("altar_linker", 1);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, List<String> list, @Nonnull ITooltipFlag flagIn) {
        list.add(I18n.format("tooltip.altar_linker.0", this.linkingRange));
        if (hasTargetAgonizer(stack)) {
            BlockPos pos = BlockPos.fromLong(getTargetAgonizerPos(stack));
            list.add(I18n.format("tooltip.altar_linker.1", pos.getX(), pos.getY(), pos.getZ()));
            list.add(I18n.format("tooltip.altar_linker.2"));
            list.add(I18n.format("tooltip.altar_linker.3"));
        } else {
            list.add(I18n.format("tooltip.altar_linker.4"));
            list.add(I18n.format("tooltip.altar_linker.5"));
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, EntityPlayer player, @Nonnull EnumHand handIn) {
        if (!player.world.isRemote && !player.isSneaking()) {
            removeTargetAccelerator(player.getHeldItem(handIn));
            player.sendStatusMessage(new TextComponentTranslation("text.altar_linker.clear"), true);
        }
        return super.onItemRightClick(worldIn, player, handIn);
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack agonizerLinker = player.getHeldItem(hand);
        if (!player.world.isRemote) {
            if (player.isSneaking()) {
                if (isBlockDigitalAgonizerAtPos(world, pos)) {
                    setTargetAgonizer(agonizerLinker, pos);
                    player.sendStatusMessage(new TextComponentTranslation("text.altar_linker.set"), true);
                } else if (isValidAltar(world, pos)) {
                    if (hasTargetAgonizer(agonizerLinker)) {
                        BlockPos agonizerPos = BlockPos.fromLong(getTargetAgonizerPos(agonizerLinker));

                        if (!ModConfig.isMultipleAgonizersAllowed) {
                            if (isAltarLinked(world, pos, agonizerPos)) {
                                player.sendStatusMessage(new TextComponentTranslation("text.altar_linker.error.0"), true);
                                return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
                            }
                        }

                        if (getBlockDistance(pos, agonizerPos) <= linkingRange) {
                            if (isBlockDigitalAgonizerAtPos(world, agonizerPos)) {
                                TileEntityDigitalAgonizer tile = getAgonizerFromPos(world, agonizerPos);
                                tile.setAltarPos(pos);
                                tile.updateState(true);
                                player.sendStatusMessage(new TextComponentTranslation("text.altar_linker.success"), true);
                            }
                        } else {
                            player.sendStatusMessage(new TextComponentTranslation("text.altar_linker.error.1"), true);
                        }
                    }
                }
            }
        }
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    private boolean isAltarLinked(World world, BlockPos altarPos, BlockPos currentAgonizerPos) {
        Iterable<BlockPos> altarSurroundings = BlockPos.getAllInBox(
            new BlockPos(altarPos.getX() - 50, altarPos.getY() - 50, altarPos.getZ() - 50),
            new BlockPos(altarPos.getX() + 50, altarPos.getY() + 50, altarPos.getZ() + 50)
        );

        for (BlockPos blockPos : altarSurroundings) {
            if(isBlockDigitalAgonizerAtPos(world, blockPos) && !blockPos.equals(currentAgonizerPos)){
                TileEntityDigitalAgonizer tile = getAgonizerFromPos(world, blockPos);
                if(isValidAltar(world, tile.getAltarPos())) {
                    return true;
                }
            }
        }
        return false;
    }

    // E-Distance
    private double getBlockDistance(BlockPos pos, BlockPos pos2) {
        double x = Math.pow((pos.getX() - pos2.getX()), 2);
        double y = Math.pow((pos.getY() - pos2.getY()), 2);
        double z = Math.pow((pos.getZ() - pos2.getZ()), 2);

        return Math.sqrt(x + y + z);
    }

    private boolean isBlockDigitalAgonizerAtPos(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof BlockDigitalAgonizer;
    }

    private boolean isValidAltar(World world, BlockPos pos) {
        return world.getTileEntity(pos) instanceof TileAltar;
    }

    private TileEntityDigitalAgonizer getAgonizerFromPos(World world, BlockPos pos) {
        return (TileEntityDigitalAgonizer) world.getTileEntity(pos);
    }

    private boolean hasTargetAgonizer(ItemStack stack) {
        return NBTHelper.hasKey(stack, "targetAgonizer");
    }

    private void removeTargetAccelerator(ItemStack stack) {
        NBTHelper.removeKey(stack, "targetAgonizer");
    }

    private long getTargetAgonizerPos(ItemStack stack) {
        if (stack.getTagCompound() != null) {
            return stack.getTagCompound().getLong("targetAgonizer");
        }
        return 0;
    }

    private void setTargetAgonizer(ItemStack stack, BlockPos pos) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setLong("targetAgonizer", pos.toLong());
    }
}
