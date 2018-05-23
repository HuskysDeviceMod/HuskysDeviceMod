package net.thegaminghuskymc.gadgetmod.programs.system.task;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.thegaminghuskymc.gadgetmod.api.app.annontation.DeviceTask;
import net.thegaminghuskymc.gadgetmod.api.task.Task;
import net.thegaminghuskymc.gadgetmod.api.utils.BankUtil;
import net.thegaminghuskymc.gadgetmod.programs.system.object.Account;
import net.thegaminghuskymc.gadgetmod.util.InventoryUtil;

import static net.thegaminghuskymc.gadgetmod.Reference.MOD_ID;

@DeviceTask(modId = MOD_ID, taskId = "bank_deposit")
public class TaskDeposit extends Task {
    private int amount;

    private TaskDeposit() {}

    public TaskDeposit(int amount) {
        this();
        this.amount = amount;
    }

    @Override
    public void prepareRequest(NBTTagCompound nbt) {
        nbt.setInteger("amount", this.amount);
    }

    @Override
    public void processRequest(NBTTagCompound nbt, World world, EntityPlayer player) {
        int amount = nbt.getInteger("amount");
        if (InventoryUtil.removeItemWithAmount(player, Items.EMERALD, amount)) {
            Account account = BankUtil.INSTANCE.getAccount(player);
            if (account.deposit(amount)) {
                this.amount = account.getBalance();
                this.setSuccessful();
            }
        }
    }

    @Override
    public void prepareResponse(NBTTagCompound nbt) {
        nbt.setInteger("balance", this.amount);
    }

    @Override
    public void processResponse(NBTTagCompound nbt) {
    }

}