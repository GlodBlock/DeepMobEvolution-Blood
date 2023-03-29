package com.glodblock.github.dmeblood.common.blocks;

import com.glodblock.github.dmeblood.DeepMobLearningBM;
import com.glodblock.github.dmeblood.ModConstants;
import com.glodblock.github.dmeblood.common.tile.IContainerProvider;
import com.glodblock.github.dmeblood.proxy.CommonProxy;
import mustapelto.deepmoblearning.common.tiles.CraftingState;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BlockMachine<T extends IContainerProvider> extends Block implements ITileEntityProvider {

    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private static final PropertyEnum<CraftingState> CRAFTING_STATE = PropertyEnum.create("state", CraftingState.class);
    @SuppressWarnings("FieldCanBeLocal")
    private final String name;
    private final Class<T> tile;

    public BlockMachine(String name, Class<T> tileClazz) {
        super(Material.IRON);
        this.name = name;
        this.tile = tileClazz;
        setTranslationKey(ModConstants.MODID + "." + this.name);
        setCreativeTab(DeepMobLearningBM.creativeTab);
        setRegistryName(this.name);
        setHardness(4f);
        setResistance(10.0f);
        setLightLevel(1F);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(@Nonnull IBlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(@Nonnull IBlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState()
                .withProperty(FACING, placer.getHorizontalFacing().getOpposite())
                .withProperty(CRAFTING_STATE, CraftingState.IDLE);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, CRAFTING_STATE);
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            CommonProxy.openTileEntityGui(world, player, this.getInstance().getID(), pos);
        }
        return true;
    }

    @Override
    public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (this.tile.isInstance(tile)) {
            IItemHandler inventory = ((IContainerProvider) tile).getInnerInventory();
            if (inventory != null) {
                for (int i = 0; i < inventory.getSlots(); i++) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                    item.setDefaultPickupDelay();
                    world.spawnEntity(item);
                }
            }
        }
        super.breakBlock(world, pos, state);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(@Nonnull CreativeTabs tab, NonNullList<ItemStack> list) {
        list.add(new ItemStack(this));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int i) {
        return (TileEntity) this.getInstance();
    }

    @SuppressWarnings({"deprecation", "unchecked"})
    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        TileEntity te;
        if (worldIn instanceof ChunkCache) {
            te = ((ChunkCache) worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
        } else {
            te = worldIn.getTileEntity(pos);
        }
        if (this.tile.isInstance(te)) {
            return state.withProperty(CRAFTING_STATE, ((T) te).getState());
        }
        return state;
    }

    public T getInstance() {
        try {
            return this.tile.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Fail to generate instance from " + this.tile.getName());
        }
    }

}
