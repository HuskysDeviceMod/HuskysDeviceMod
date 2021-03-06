package io.github.vampirestudios.gadget.core.network.task;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import io.github.vampirestudios.gadget.api.task.Task;
import io.github.vampirestudios.gadget.core.network.NetworkDevice;
import io.github.vampirestudios.gadget.core.network.Router;
import io.github.vampirestudios.gadget.tileentity.TileEntityNetworkDevice;

import java.util.Collection;

public class TaskGetDevices extends Task {
    private BlockPos devicePos;
    private Class<? extends TileEntityNetworkDevice> targetDeviceClass;

    private Collection<NetworkDevice> foundDevices;

    public TaskGetDevices() {
        super("get_network_devices");
    }

    public TaskGetDevices(BlockPos devicePos) {
        this();
        this.devicePos = devicePos;
    }

    public TaskGetDevices(BlockPos devicePos, Class<? extends TileEntityNetworkDevice> targetDeviceClass) {
        this();
        this.devicePos = devicePos;
        this.targetDeviceClass = targetDeviceClass;
    }

    @Override
    public void prepareRequest(NBTTagCompound nbt) {
        nbt.setLong("devicePos", devicePos.toLong());
        if (targetDeviceClass != null) {
            nbt.setString("targetClass", targetDeviceClass.getName());
        }
    }

    @Override
    public void processRequest(NBTTagCompound nbt, World world, EntityPlayer player) {
        BlockPos devicePos = BlockPos.fromLong(nbt.getLong("devicePos"));
        Class targetDeviceClass = null;
        try {
            Class targetClass = Class.forName(nbt.getString("targetClass"));
            if (TileEntityNetworkDevice.class.isAssignableFrom(targetClass)) {
                targetDeviceClass = targetClass;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        TileEntity tileEntity = world.getTileEntity(devicePos);
        if (tileEntity instanceof TileEntityNetworkDevice) {
            TileEntityNetworkDevice TileEntityNetworkDevice = (TileEntityNetworkDevice) tileEntity;
            if (TileEntityNetworkDevice.isConnected()) {
                Router router = TileEntityNetworkDevice.getRouter();
                if (router != null) {
                    if (targetDeviceClass != null) {
                        foundDevices = router.getConnectedDevices(world, targetDeviceClass);
                    } else {
                        foundDevices = router.getConnectedDevices(world);
                    }
                    this.setSuccessful();
                }
            }
        }
    }

    @Override
    public void prepareResponse(NBTTagCompound nbt) {
        if (this.isSucessful()) {
            NBTTagList deviceList = new NBTTagList();
            foundDevices.forEach(device -> deviceList.appendTag(device.toTag(true)));
            nbt.setTag("network_devices", deviceList);
        }
    }

    @Override
    public void processResponse(NBTTagCompound nbt) {

    }
}