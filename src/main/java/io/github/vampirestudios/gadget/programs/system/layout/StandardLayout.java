package io.github.vampirestudios.gadget.programs.system.layout;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import io.github.vampirestudios.gadget.api.app.Application;
import io.github.vampirestudios.gadget.api.app.IIcon;
import io.github.vampirestudios.gadget.api.app.Layout;
import io.github.vampirestudios.gadget.api.app.component.Button;
import io.github.vampirestudios.gadget.api.app.emojie_packs.Icons;
import io.github.vampirestudios.gadget.core.BaseDevice;

import javax.annotation.Nullable;
import java.awt.*;

public class StandardLayout extends Layout {
    protected Application app;
    private String title;
    private Layout previous;
    private IIcon icon;

    public StandardLayout(String title, int width, int height, Application app, @Nullable Layout previous) {
        super(width, height);
        this.title = title;
        this.app = app;
        this.previous = previous;
    }

    @Override
    public void init() {
        if (previous != null) {
            Button btnBack = new Button(2, 2, Icons.ARROW_LEFT);
            btnBack.setClickListener((mouseX, mouseY, mouseButton) ->
            {
                if (mouseButton == 0) {
                    app.setCurrentLayout(previous);
                }
            });
            this.addComponent(btnBack);
        }
    }

    @Override
    public void render(BaseDevice laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean windowActive, float partialTicks) {
        Color color = new Color(BaseDevice.getSystem().getSettings().getColourScheme().getSecondApplicationBarColour());
        Gui.drawRect(x - 1, y, x + width + 1, y + 20, color.getRGB());
        Gui.drawRect(x - 1, y + 20, x + width + 1, y + 21, color.darker().getRGB());

        if (previous == null && icon != null) {
            icon.draw(mc, x + 5, y + 5);
        }
        mc.fontRenderer.drawString(title, x + 5 + (previous != null || icon != null ? 16 : 0), y + 7, Color.WHITE.getRGB(), true);

        super.render(laptop, mc, x, y, mouseX, mouseY, windowActive, partialTicks);
    }

    public void setIcon(IIcon icon) {
        this.icon = icon;
    }
}