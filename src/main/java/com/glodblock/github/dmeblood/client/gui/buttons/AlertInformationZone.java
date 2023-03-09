package com.glodblock.github.dmeblood.client.gui.buttons;

import com.glodblock.github.dmeblood.ModConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class AlertInformationZone extends GuiButton {
    private List<String> tooltip = new ArrayList<>();
    private final int screenWidth;
    private final int screenHeight;
    protected static final ResourceLocation TEXTURE = new ResourceLocation(ModConstants.MODID,"textures/gui/buttons/buttons.png");

    public AlertInformationZone(int buttonId, int x, int y, int widthIn, int heightIn, int screenWidth, int screenHeight) {
        super(buttonId, x, y, widthIn, heightIn, "");
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void setTooltip(List<String> tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        mc.getTextureManager().bindTexture(TEXTURE);
        RenderHelper.disableStandardItemLighting();
        drawTexturedModalRect(this.x, this.y, 0, 0, this.width, this.height);

        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        if (this.hovered) {
            GuiUtils.drawHoveringText(this.tooltip, mouseX, mouseY, this.screenWidth, this.screenHeight, -1, mc.fontRenderer);
        }
    }
}
