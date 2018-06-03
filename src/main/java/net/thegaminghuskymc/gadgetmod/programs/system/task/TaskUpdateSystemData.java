package net.thegaminghuskymc.gadgetmod.programs.system.task;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.thegaminghuskymc.gadgetmod.api.task.Task;
import net.thegaminghuskymc.gadgetmod.tileentity.TileEntityBaseDevice;

public class TaskUpdateSystemData extends Task {
    private BlockPos pos;
    private NBTTagCompound data;

    public TaskUpdateSystemData() {}

    public TaskUpdateSystemData(BlockPos pos, NBTTagCompound data) {
        this();
        this.pos = pos;
        this.data = data;
    }

    @Override
    public void prepareRequest(NBTTagCompound tag) {
        tag.setLong("pos", pos.toLong());
        tag.setTag("data", this.data);
    }

    @Override
    public void processRequest(NBTTagCompound tag, World world, EntityPlayer player) {
        BlockPos pos = BlockPos.fromLong(tag.getLong("pos"));
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityBaseDevice) {
            TileEntityBaseDevice laptop = (TileEntityBaseDevice) tileEntity;
            laptop.setSystemData(tag.getCompoundTag("data"));
        }
        this.setSuccessful();
    }

    @Override
    public void prepareResponse(NBTTagCompound tag) {

    }

    @Override
    public void processResponse(NBTTagCompound tag) {

    }
}