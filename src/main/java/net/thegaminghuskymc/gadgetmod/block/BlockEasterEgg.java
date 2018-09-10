package net.thegaminghuskymc.gadgetmod.block;

import net.hdt.huskylib2.block.BlockMod;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.thegaminghuskymc.gadgetmod.init.GadgetItems;
import net.thegaminghuskymc.gadgetmod.tileentity.TileEntityEasterEgg;

public class BlockEasterEgg extends BlockMod implements ITileEntityProvider, IHGMBlock {

    public BlockEasterEgg() {
        super("easter_egg", Material.CARPET);
        this.setHardness(-1.0f);
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        super.onBlockClicked(worldIn, pos, playerIn);
        if (!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityEasterEgg) {
                TileEntityEasterEgg eggte = (TileEntityEasterEgg) te;
                ItemStack egg = new ItemStack(GadgetItems.easter_egg);
                NBTTagCompound nbt = eggte.writeColorsToNBT(new NBTTagCompound());
                egg.setTagCompound(nbt);
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), egg));
            }
            worldIn.destroyBlock(pos, false);
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityEasterEgg(worldIn.isRemote ? null : worldIn.rand);
    }

}
