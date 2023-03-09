package com.glodblock.github.dmeblood.client.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ClickableZoneButton extends GuiButton {
    private List<String> tooltip = new ArrayList<>();
    private final int screenWidth;
    private final int screenHeight;

    public ClickableZoneButton(int buttonId, int x, int y, int widthIn, int heightIn, int screenWidth, int screenHeight) {
        super(buttonId, x, y, widthIn, heightIn, "");
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void setTooltip(List<String> tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        if (this.hovered) {
            GuiUtils.drawHoveringText(this.tooltip, mouseX, mouseY, this.screenWidth, this.screenHeight, -1, mc.fontRenderer);
        }
    }
}
