package io.github.vampirestudios.gadget.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityMonitor extends TileEntityColoredDevice {

    @SideOnly(Side.CLIENT)
    public float rotation;
    private String name = "Monitor";
    private boolean powered = false, hasComputerConnected = false;

    @Override
    public String getDeviceName() {
        return name;
    }

    @Override
    public void update() {
        if (world.isRemote) {
            if (!powered) {
                if (rotation > 0) {
                    rotation -= 10F;
                }
            } else {
                if (rotation < 110) {
                    rotation += 10F;
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("powered")) {
            this.powered = compound.getBoolean("powered");
        }
        if (compound.hasKey("device_name", Constants.NBT.TAG_STRING)) {
            this.name = compound.getString("device_name");
        }
        if (compound.hasKey("has_computer_connected")) {
            this.hasComputerConnected = compound.getBoolean("has_computer_connected");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("powered", powered);
        compound.setString("device_name", name);
        compound.setBoolean("has_computer_connected", hasComputerConnected);

        return compound;
    }

    @Override
    public NBTTagCompound writeSyncTag() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("powered", powered);
        tag.setString("device_name", name);
        tag.setBoolean("has_computer_connected", hasComputerConnected);
        return tag;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 16384;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public void powerUnpower() {
        powered = !powered;
        pipeline.setBoolean("powered", powered);
        sync();
    }

    public boolean isPowered() {
        return powered;
    }

    public boolean isComputerConnected() {
        return hasComputerConnected;
    }

}
