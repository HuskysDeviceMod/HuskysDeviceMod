package io.github.vampirestudios.gadget.proxy;

import io.github.vampirestudios.gadget.DeviceConfig;
import io.github.vampirestudios.gadget.HuskyGadgetMod;
import io.github.vampirestudios.gadget.Reference;
import io.github.vampirestudios.gadget.api.AppInfo;
import io.github.vampirestudios.gadget.api.ApplicationManager;
import io.github.vampirestudios.gadget.api.app.Application;
import io.github.vampirestudios.gadget.api.print.IPrint;
import io.github.vampirestudios.gadget.api.print.PrintingManager;
import io.github.vampirestudios.gadget.block.BlockColored;
import io.github.vampirestudios.gadget.core.BaseDevice;
import io.github.vampirestudios.gadget.core.client.ClientNotification;
import io.github.vampirestudios.gadget.init.GadgetBlocks;
import io.github.vampirestudios.gadget.init.GadgetItems;
import io.github.vampirestudios.gadget.item.ItemColored;
import io.github.vampirestudios.gadget.programs.system.SystemApplication;
import io.github.vampirestudios.gadget.tileentity.*;
import io.github.vampirestudios.gadget.tileentity.render.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.ItemBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.github.vampirestudios.gadget.Reference.MOD_ID;
import static io.github.vampirestudios.gadget.init.GadgetBlocks.*;
import static io.github.vampirestudios.gadget.init.GadgetItems.flash_drives;

public class ClientProxy extends CommonProxy implements IResourceManagerReloadListener {

    @Override
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
    }

    @Override
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaptop.class, new LaptopRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrinter.class, new PrinterRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPaper.class, new PaperRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRouter.class, new RouterRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityScreen.class, new ScreenRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityOfficeChair.class, new OfficeChairRenderer());

        BaseDevice.addWallpaper(new ResourceLocation(MOD_ID, "textures/gui/wallpapers/default.png"));
        for(int i = 1; i > 17; i++) {
            BaseDevice.addWallpaper(new ResourceLocation(MOD_ID, String.format("textures/gui/wallpapers/wallpaper_%d.png", i)));
        }

//        BaseDevice.addWallpaper(new ResourceLocation(MOD_ID, "textures/gui/laptop_wallpaper_1.png"));
//        BaseDevice.addWallpaper(new ResourceLocation(MOD_ID, "textures/gui/laptop_wallpaper_2.png"));
//        BaseDevice.addWallpaper(new ResourceLocation(MOD_ID, "textures/gui/laptop_wallpaper_3.png"));
//        BaseDevice.addWallpaper(new ResourceLocation(MOD_ID, "textures/gui/laptop_wallpaper_4.png"));
//        BaseDevice.addWallpaper(new ResourceLocation(MOD_ID, "textures/gui/laptop_wallpaper_5.png"));
//        BaseDevice.addWallpaper(new ResourceLocation(MOD_ID, "textures/gui/laptop_wallpaper_6.png"));
//        BaseDevice.addWallpaper(new ResourceLocation(MOD_ID, "textures/gui/laptop_wallpaper_7.png"));
//        BaseDevice.addWallpaper(new ResourceLocation(MOD_ID, "textures/gui/laptop_wallpaper_8.png"));

        /*BaseDevice.addTheme(new ResourceLocation(MOD_ID, "themes/test_theme.json"));

        File folder = Paths.get(Minecraft.getMinecraft().mcDataDir.getAbsolutePath(), Reference.MOD_ID, "wallpapers").toFile();
        if (!folder.exists()) {
            folder.mkdir();
        }
        File[] files = folder.listFiles((dir, name) -> name.matches("wallpaper_.*.png"));
        if (files != null) {
            for (File f : files) {

                BufferedImage img = null;
                try {
                    if (!f.exists()) f.createNewFile();
                    img = ImageIO.read(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BaseDevice.addWallpaper(Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("wallpapers", new DynamicTexture(img)));
            }
        }*/

        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
        IItemColor easterEgg = (stack, tintIndex) -> tintIndex < 2 && stack.hasTagCompound() ? Objects.requireNonNull(stack.getTagCompound()).getInteger("color" + tintIndex) : 0xFFFFFF;
        itemColors.registerItemColorHandler(easterEgg, GadgetItems.easter_egg);

        BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
        IBlockColor easterEggBlock = (state, worldIn, pos, tintIndex) -> {
            TileEntity te = Objects.requireNonNull(worldIn).getTileEntity(Objects.requireNonNull(pos));
            if (te instanceof TileEntityEasterEgg) {
                return ((TileEntityEasterEgg) te).getColor(tintIndex);
            }
            return 0xFFFFFF;
        };
        blockColors.registerBlockColorHandler(easterEggBlock, GadgetBlocks.easter_egg);

        ItemColors items = Minecraft.getMinecraft().getItemColors();
        BlockColors blocks = Minecraft.getMinecraft().getBlockColors();

        IItemColor handlerItems = (s, t) -> t == 0 ? ((ItemColored) s.getItem()).color.getColorValue() : 0xFFFFFF;
        items.registerItemColorHandler(handlerItems, flash_drives);
        items.registerItemColorHandler((stack, tintIndex) -> blocks.colorMultiplier(((ItemBlock) stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex),
                gaming_chairs);
        items.registerItemColorHandler((stack, tintIndex) -> blocks.colorMultiplier(((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata()), null, null, tintIndex),
                laptops);
        items.registerItemColorHandler((stack, tintIndex) -> blocks.colorMultiplier(((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata()), null, null, tintIndex),
                monitors);
        items.registerItemColorHandler((stack, tintIndex) -> blocks.colorMultiplier(((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata()), null, null, tintIndex),
                ethernet_wall_outlets);
        items.registerItemColorHandler((stack, tintIndex) -> blocks.colorMultiplier(((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata()), null, null, tintIndex),
                robots);
        items.registerItemColorHandler((stack, tintIndex) -> blocks.colorMultiplier(((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata()), null, null, tintIndex),
                gaming_desks);
        /*items.registerItemColorHandler((stack, tintIndex) -> blocks.colorMultiplier(((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata()), null, null, tintIndex),
                benchmark_stations);*/
        items.registerItemColorHandler((stack, tintIndex) -> blocks.colorMultiplier(((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata()), null, null, tintIndex),
                external_harddrives);
        /*items.registerItemColorHandler((stack, tintIndex) -> blocks.colorMultiplier(((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata()), null, null, tintIndex),
                playstation_4_pros);
        items.registerItemColorHandler((stack, tintIndex) -> blocks.colorMultiplier(((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata()), null, null, tintIndex),
                threede_printers);*/
        items.registerItemColorHandler((stack, tintIndex) -> blocks.colorMultiplier(((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata()), null, null, tintIndex),
                desktops);
        items.registerItemColorHandler((stack, tintIndex) -> blocks.colorMultiplier(((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata()), null, null, tintIndex),
                routers);

        IBlockColor handlerBlocks = (s, w, p, t) -> t == 0 ? ((BlockColored) s.getBlock()).color.getColorValue() : 0xFFFFFF;
        blocks.registerBlockColorHandler(handlerBlocks, gaming_chairs);
        blocks.registerBlockColorHandler(handlerBlocks, laptops);
        blocks.registerBlockColorHandler(handlerBlocks, monitors);
        blocks.registerBlockColorHandler(handlerBlocks, ethernet_wall_outlets);
        blocks.registerBlockColorHandler(handlerBlocks, robots);
        blocks.registerBlockColorHandler(handlerBlocks, gaming_desks);
//        blocks.registerBlockColorHandler(handlerBlocks, benchmark_stations);
        blocks.registerBlockColorHandler(handlerBlocks, external_harddrives);
//        blocks.registerBlockColorHandler(handlerBlocks, playstation_4_pros);
//        blocks.registerBlockColorHandler(handlerBlocks, threede_printers);
        blocks.registerBlockColorHandler(handlerBlocks, desktops);
        blocks.registerBlockColorHandler(handlerBlocks, routers);

    }

    @Override
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        generateIconAtlas();
        generateBannerAtlas();
    }

    private void generateIconAtlas()
    {
        final int ICON_SIZE = 14;
        int index = 0;

        BufferedImage atlas = new BufferedImage(ICON_SIZE * 16, ICON_SIZE * 16, BufferedImage.TYPE_INT_ARGB);
        Graphics g = atlas.createGraphics();

        try
        {
            BufferedImage icon = TextureUtil.readBufferedImage(ClientProxy.class.getResourceAsStream("/assets/" + Reference.MOD_ID + "/textures/app/icon/missing.png"));
            g.drawImage(icon, 0, 0, ICON_SIZE, ICON_SIZE, null);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        index++;

        for(AppInfo info : ApplicationManager.getAllApplications())
        {
            if(info.getIcon() == null)
                continue;

            ResourceLocation identifier = info.getId();
            ResourceLocation iconResource = new ResourceLocation(info.getIcon());
            String path = "/assets/" + iconResource.getNamespace() + "/" + iconResource.getPath();
            try
            {
                InputStream input = ClientProxy.class.getResourceAsStream(path);
                if(input != null)
                {
                    BufferedImage icon = TextureUtil.readBufferedImage(input);
                    if(icon.getWidth() != ICON_SIZE || icon.getHeight() != ICON_SIZE)
                    {
                        HuskyGadgetMod.logger().error("Incorrect icon size for " + identifier.toString() + " (Must be 14 by 14 pixels)");
                        continue;
                    }
                    int iconU = (index % 16) * ICON_SIZE;
                    int iconV = (index / 16) * ICON_SIZE;
                    g.drawImage(icon, iconU, iconV, ICON_SIZE, ICON_SIZE, null);
                    updateIcon(info, iconU, iconV);
                    index++;
                }
                else
                {
                    HuskyGadgetMod.logger().error("Icon for application '" + identifier.toString() +  "' could not be found at '" + path + "'");
                }
            }
            catch(Exception e)
            {
                HuskyGadgetMod.logger().error("Unable to load icon for " + identifier.toString());
            }
        }

        g.dispose();
        Minecraft.getMinecraft().getTextureManager().loadTexture(BaseDevice.ICON_TEXTURES, new DynamicTexture(atlas));
    }

    private void generateBannerAtlas() {
        final int BANNER_WIDTH = 250;
        final int BANNER_HEIGHT = 40;
        int index = 0;

        BufferedImage atlas = new BufferedImage(BANNER_WIDTH * 16, BANNER_HEIGHT * 16, BufferedImage.TYPE_INT_ARGB);
        Graphics g = atlas.createGraphics();

        try {
            BufferedImage icon = TextureUtil.readBufferedImage(ClientProxy.class.getResourceAsStream("/assets/" + MOD_ID + "/textures/app/banner/banner_default.png"));
            g.drawImage(icon, 0, 0, BANNER_WIDTH, BANNER_HEIGHT, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        index++;

        for (AppInfo info : ApplicationManager.getAllApplications()) {
            if (info.getBanner() == null)
                continue;

            ResourceLocation identifier = info.getId();
            ResourceLocation iconResource = new ResourceLocation(info.getBanner());
            String path = "/assets/" + iconResource.getNamespace() + "/" + iconResource.getPath();
            try {
                InputStream input = ClientProxy.class.getResourceAsStream(path);
                if (input != null) {
                    BufferedImage banner = TextureUtil.readBufferedImage(input);
                    if (banner.getWidth() != BANNER_WIDTH || banner.getHeight() != BANNER_HEIGHT) {
                        HuskyGadgetMod.logger().error("Incorrect banner size for " + identifier.toString() + " (Must be 250 by 40 pixels)");
                        continue;
                    }
                    int bannerU = (index % 16) * BANNER_WIDTH;
                    int bannerV = (index / 16) * BANNER_HEIGHT;
                    g.drawImage(banner, bannerU, bannerV, BANNER_WIDTH, BANNER_HEIGHT, null);
                    updateBanner(info, bannerU, bannerV);
                    index++;
                } else {
                    HuskyGadgetMod.logger().error("Banner for application '" + identifier.toString() + "' could not be found at '" + path + "'");
                }
            } catch (Exception e) {
                HuskyGadgetMod.logger().error("Unable to load banner for " + identifier.toString());
            }
        }

        g.dispose();
        Minecraft.getMinecraft().getTextureManager().loadTexture(BaseDevice.BANNER_TEXTURES, new DynamicTexture(atlas));
    }

    private void updateBanner(AppInfo info, int bannerU, int bannerV) {
        ReflectionHelper.setPrivateValue(AppInfo.class, info, bannerU, "bannerU");
        ReflectionHelper.setPrivateValue(AppInfo.class, info, bannerV, "bannerV");
    }

    private void updateIcon(AppInfo info, int iconU, int iconV)
    {
        ReflectionHelper.setPrivateValue(AppInfo.class, info, iconU, "iconU");
        ReflectionHelper.setPrivateValue(AppInfo.class, info, iconV, "iconV");
    }

    @Nullable
    @Override
    public Application registerApplication(ResourceLocation identifier, Class<? extends Application> clazz)
    {
        if("minecraft".equals(identifier.getPath()))
        {
            throw new IllegalArgumentException("Invalid identifier domain");
        }

        try
        {
            Application application = clazz.newInstance();
            java.util.List<Application> APPS = ReflectionHelper.getPrivateValue(BaseDevice.class, null, "APPLICATIONS");
            APPS.add(application);

            Field field = Application.class.getDeclaredField("info");
            field.setAccessible(true);

            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(application, generateAppInfo(identifier, clazz));

            return application;
        }
        catch(InstantiationException | IllegalAccessException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private AppInfo generateAppInfo(ResourceLocation identifier, Class<? extends Application> clazz)
    {
        AppInfo info = new AppInfo(identifier, SystemApplication.class.isAssignableFrom(clazz));
        info.reload();
        return info;
    }

    @Override
    public boolean registerPrint(ResourceLocation identifier, Class<? extends IPrint> classPrint)
    {
        try {
            Constructor<? extends IPrint> constructor = classPrint.getConstructor();
            IPrint print = constructor.newInstance();
            Class<? extends IPrint.Renderer> classRenderer = print.getRenderer();
            try {
                IPrint.Renderer renderer = classRenderer.newInstance();
                Map<String, IPrint.Renderer> idToRenderer = ReflectionHelper.getPrivateValue(PrintingManager.class, null, "registeredRenders");
                if(idToRenderer == null) {
                    idToRenderer = new HashMap<>();
                    ReflectionHelper.setPrivateValue(PrintingManager.class, null, idToRenderer, "registeredRenders");
                }
                idToRenderer.put(identifier.toString(), renderer);
            }
            catch(InstantiationException e) {
                HuskyGadgetMod.logger().error("The print renderer '" + classRenderer.getName() + "' is missing an empty constructor and could not be registered!");
                return false;
            }
            return true;
        }
        catch(Exception e) {
            HuskyGadgetMod.logger().error("The print '" + classPrint.getName() + "' is missing an empty constructor and could not be registered!");
        }
        return false;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        if(ApplicationManager.getAllApplications().size() > 0) {
            ApplicationManager.getAllApplications().forEach(AppInfo::reload);
            generateIconAtlas();
        }
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        allowedApps = null;
        DeviceConfig.restore();
    }

    @Override
    public void showNotification(NBTTagCompound tag) {
        ClientNotification notification = ClientNotification.loadFromTag(tag);
        notification.push();
    }

}
