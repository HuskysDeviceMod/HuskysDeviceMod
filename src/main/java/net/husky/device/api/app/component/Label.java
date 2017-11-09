package net.husky.device.api.app.component;

import codechicken.lib.colour.ColourRGBA;
import net.husky.device.api.app.Component;
import net.husky.device.core.Laptop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class Label extends Component {

	protected String text;
	protected int width;
	protected boolean shadow = true;
	protected double scale = 1;
	protected int alignment = ALIGN_LEFT;

	protected ColourRGBA textColor;

	protected ColourRGBA defaultTextColor = new ColourRGBA(255, 255, 255, 255);
	
	/**
	 * Default label constructor
	 * 
	 * @param text the text to display
	 * @param left how many pixels from the left
	 * @param top how many pixels from the top
	 */
	public Label(String text, int left, int top) 
	{
		super(left, top);
		this.text = text;
		setTextColour(defaultTextColor);
	}

	@Override
	public void render(Laptop laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean windowActive, float partialTicks) 
	{
		if (this.visible)
        {
			GlStateManager.pushMatrix();
			{
				GlStateManager.translate(xPosition, yPosition, 0);
				GlStateManager.scale(scale, scale, scale);
				if(alignment == ALIGN_RIGHT)
					GlStateManager.translate(-(mc.fontRenderer.getStringWidth(text) * scale), 0, 0);
				if(alignment == ALIGN_CENTER)
					GlStateManager.translate(-(mc.fontRenderer.getStringWidth(text) * scale) / (2 * scale), 0, 0);
				mc.fontRenderer.drawString(text, 0, 0, getTextColor().argb(), shadow);
			}
			GlStateManager.popMatrix();
        }
	}
	
	/**
	 * Sets the text in the label
	 * 
	 * @param text the text
	 */
	public void setText(String text) 
	{
		this.text = text;
	}
	
	/**
	 * Sets the text colour for this component
	 * 
	 * @param color the text colour
	 */
	public void setTextColour(ColourRGBA color)
	{
		this.textColor = color;
	}

    public ColourRGBA getTextColor() {
        return textColor;
    }

    /**
	 * Sets the whether shadow should show under the text
	 * 
	 * @param shadow if should render shadow
	 */
	public void setShadow(boolean shadow)
	{
		this.shadow = shadow;
	}
	
	/**
	 * Scales the text, essentially setting the font size. Minecraft
	 * does not support proper font resizing. The default scale is 1
	 * 
	 * @param scale the text scale
	 */
	public void setScale(double scale)
	{
		this.scale = scale;
	}
	
	/**
	 * Sets the alignment of the text. Use {@link Component#ALIGN_LEFT} or
	 * {@link Component#ALIGN_RIGHT} to set alignment.
	 * 
	 * @param alignment the alignment type
	 */
	public void setAlignment(int alignment)
	{
		this.alignment = alignment;
	}
}
