package net.thegaminghuskymc.gadgetmod.core.operation_systems;

import net.thegaminghuskymc.gadgetmod.api.operating_system.OperatingSystem;
import net.thegaminghuskymc.gadgetmod.core.TaskBar;

public class PixelOS implements OperatingSystem {

    @Override
    public String getOSName() {
        return "PixelOS";
    }

    @Override
    public String getOSVersion() {
        return "0.0.1";
    }

    @Override
    public TaskBar getTaskBar() {
        return null;
    }

}
