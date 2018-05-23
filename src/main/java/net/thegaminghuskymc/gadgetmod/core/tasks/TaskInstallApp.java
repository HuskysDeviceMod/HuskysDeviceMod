package net.thegaminghuskymc.gadgetmod.core.tasks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.thegaminghuskymc.gadgetmod.api.AppInfo;
import net.thegaminghuskymc.gadgetmod.api.app.annontation.DeviceTask;
import net.thegaminghuskymc.gadgetmod.api.task.Task;
import net.thegaminghuskymc.gadgetmod.tileentity.TileEntityBaseDevice;

import static net.thegaminghuskymc.gadgetmod.Reference.MOD_ID;

/**
 * Author: MrCrayfish
 */
@DeviceTask(modId = MOD_ID, taskId = "install_app")
public class TaskInstallApp extends Task
{
    private String appId;
    private BlockPos laptopPos;
    private boolean install;

    private TaskInstallApp() {}

    public TaskInstallApp(AppInfo info, BlockPos laptopPos, boolean install)
    {
        this();
        this.appId = info.getFormattedId();
        this.laptopPos = laptopPos;
        this.install = install;
    }

    @Override
    public void prepareRequest(NBTTagCompound nbt)
    {
        nbt.setString("appId", appId);
        nbt.setLong("pos", laptopPos.toLong());
        nbt.setBoolean("install", install);
    }

    @Override
    public void processRequest(NBTTagCompound nbt, World world, EntityPlayer player)
    {
        String appId = nbt.getString("appId");
        TileEntity tileEntity = world.getTileEntity(BlockPos.fromLong(nbt.getLong("pos")));
        if(tileEntity instanceof TileEntityBaseDevice)
        {
            TileEntityBaseDevice laptop = (TileEntityBaseDevice) tileEntity;
            NBTTagCompound systemData = laptop.getSystemData();
            NBTTagList tagList = systemData.getTagList("InstalledApps", Constants.NBT.TAG_STRING);

            System.out.println("Before the task: ");
            for(int i = 0; i < tagList.tagCount(); i++) {
            	System.out.println("\t- " + tagList.getStringTagAt(i));
            }
            
            if(nbt.getBoolean("install"))
            {
                for(int i = 0; i < tagList.tagCount(); i++)
                {
                    if(tagList.getStringTagAt(i).equals(appId))
                    {
                        return;
                    }
                }
                tagList.appendTag(new NBTTagString(appId));
                this.setSuccessful();
            }
            else
            {
                for(int i = 0; i < tagList.tagCount(); i++)
                {
                    if(tagList.getStringTagAt(i).equals(appId))
                    {
                        tagList.removeTag(i);
                        this.setSuccessful();
                    }
                }
            }
            systemData.setTag("InstalledApps", tagList);
            
            System.out.println("After the task: ");
            for(int i = 0; i < tagList.tagCount(); i++) {
            	System.out.println("\t- " + tagList.getStringTagAt(i));
            }
        }
    }

    @Override
    public void prepareResponse(NBTTagCompound nbt)
    {

    }

    @Override
    public void processResponse(NBTTagCompound nbt)
    {

    }
}