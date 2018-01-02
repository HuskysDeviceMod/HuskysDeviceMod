package net.thegaminghuskymc.gadgetmod.block;

import net.minecraft.block.BlockColored;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.thegaminghuskymc.gadgetmod.HuskyGadgetMod;
import net.thegaminghuskymc.gadgetmod.Reference;
import net.thegaminghuskymc.gadgetmod.tileentity.TileEntityRoofLights;
import net.thegaminghuskymc.gadgetmod.util.Colorable;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockRoofLights extends BlockColorable implements ITileEntityProvider {

    public BlockRoofLights() {
        super(Material.ANVIL);
        this.setCreativeTab(HuskyGadgetMod.deviceDecoration);
        this.setUnlocalizedName("roof_lights");
        this.setRegistryName(Reference.MOD_ID, "roof_lights");
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityRoofLights();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityRoofLights) {
            TileEntityRoofLights externalHarddrive = (TileEntityRoofLights) tileEntity;

            NBTTagCompound tileEntityTag = new NBTTagCompound();
            externalHarddrive.writeToNBT(tileEntityTag);
            tileEntityTag.removeTag("x");
            tileEntityTag.removeTag("y");
            tileEntityTag.removeTag("z");
            tileEntityTag.removeTag("color");
            tileEntityTag.removeTag("powered");
            tileEntityTag.removeTag("lightColour");

            NBTTagCompound compound = new NBTTagCompound();
            compound.setTag("BlockEntityTag", tileEntityTag);

            ItemStack drop = new ItemStack(Item.getItemFromBlock(this));
            drop.setTagCompound(compound);

            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
        return state.withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof Colorable) {
            Colorable colorable = (Colorable) tileEntity;
            state = state.withProperty(BlockColored.COLOR, colorable.getColor());
        }
        return state;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, BlockColored.COLOR);
    }

}