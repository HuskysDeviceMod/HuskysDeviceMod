package io.github.vampirestudios.gadget.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import io.github.vampirestudios.gadget.api.print.IPrint;
import io.github.vampirestudios.gadget.object.Bounds;
import io.github.vampirestudios.gadget.tileentity.TileEntityPaper;
import io.github.vampirestudios.gadget.util.CollisionHelper;

import javax.annotation.Nullable;

public class BlockPaper extends BlockFacing implements ITileEntityProvider {

    private static final Bounds SELECTION_BOUNDS = new Bounds(15 * 0.0625, 0.0, 0.0, 16 * 0.0625, 16 * 0.0625, 16 * 0.0625);
    private static final AxisAlignedBB SELECTION_BOX_NORTH = CollisionHelper.getBlockBounds(EnumFacing.NORTH, SELECTION_BOUNDS);
    private static final AxisAlignedBB SELECTION_BOX_EAST = CollisionHelper.getBlockBounds(EnumFacing.EAST, SELECTION_BOUNDS);
    private static final AxisAlignedBB SELECTION_BOX_SOUTH = CollisionHelper.getBlockBounds(EnumFacing.SOUTH, SELECTION_BOUNDS);
    private static final AxisAlignedBB SELECTION_BOX_WEST = CollisionHelper.getBlockBounds(EnumFacing.WEST, SELECTION_BOUNDS);
    private static final AxisAlignedBB[] SELECTION_BOUNDING_BOX = {SELECTION_BOX_SOUTH, SELECTION_BOX_WEST, SELECTION_BOX_NORTH, SELECTION_BOX_EAST};

    public BlockPaper() {
        super("paper", Material.CLOTH);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return SELECTION_BOUNDING_BOX[state.getValue(FACING).getHorizontalIndex()];
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return null;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
        return state.withProperty(FACING, facing.getAxis() == EnumFacing.Axis.Y ? placer.getHorizontalFacing() : facing.getOpposite());
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityPaper) {
                TileEntityPaper paper = (TileEntityPaper) tileEntity;
                paper.nextRotation();
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityPaper) {
            TileEntityPaper paper = (TileEntityPaper) tileEntity;
            ItemStack drop = IPrint.generateItem(paper.getPrint());
            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityPaper();
    }

}