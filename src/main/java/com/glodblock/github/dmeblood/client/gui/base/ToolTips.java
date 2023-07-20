package com.glodblock.github.dmeblood.client.gui.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ToolTips extends GuiLabel {
    private static final AtomicInteger id = new AtomicInteger();
    private final List<String> tooltip = new ArrayList<>();
    private final int color;

    public ToolTips(FontRenderer render, int id, int x, int y, int width, int height, int color) {
        super(render, id, x, y, width, height, color);
        this.color = color;
    }

    public ToolTips(FontRenderer render, int x, int y, int width, int height) {
        this(render, id.getAndIncrement(), x, y, width, height, 0xFFFFFFF);
    }

    public void setTooltip(List<String> tooltip) {
        this.tooltip.clear();
        this.tooltip.addAll(tooltip);
    }


    @Override
    public void drawLabel(Minecraft mc, int mouseX, int mouseY) {
        float red = (float) (this.color >> 16 & 255) / 255.0F;
        float blue = (float)(this.color >> 8 & 255) / 255.0F;
        float green = (float)(this.color & 255) / 255.0F;
        float alpha = (float)(this.color >> 24 & 255) / 255.0F;
        GlStateManager.color(red, blue, green, alpha);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        boolean hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        GuiScreen gui = mc.currentScreen;
        if (hovered && gui != null) {
            GuiUtils.drawHoveringText(this.tooltip, mouseX, mouseY > 8 ? mouseY - 8 : 3, gui.width, gui.height, -1, mc.fontRenderer);
        }
    }
}
