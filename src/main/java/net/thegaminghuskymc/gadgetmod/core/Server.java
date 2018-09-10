package net.thegaminghuskymc.gadgetmod.core;

import net.thegaminghuskymc.gadgetmod.core.operation_systems.NeonOSServer;
import net.thegaminghuskymc.gadgetmod.tileentity.TileEntityBaseDevice;

public class Server extends BaseDevice {

    public Server() {
        super(new TileEntityBaseDevice("Server"), 2, new NeonOSServer());
    }

}
