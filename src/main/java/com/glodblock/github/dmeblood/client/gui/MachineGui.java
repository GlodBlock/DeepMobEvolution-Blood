package com.glodblock.github.dmeblood.client.gui;

import com.glodblock.github.dmeblood.ModConfig;
import com.glodblock.github.dmeblood.common.tile.IContainerProvider;
import mustapelto.deepmoblearning.DMLConstants;
import mustapelto.deepmoblearning.common.energy.DMLEnergyStorage;
import mustapelto.deepmoblearning.common.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public abstract class MachineGui<T extends TileEntity & IContainerProvider> extends GuiContainer {

    protected static final NumberFormat F = NumberFormat.getNumberInstance(Locale.ENGLISH);
    protected static final ResourceLocation defaultGui = new ResourceLocation(DMLConstants.ModInfo.ID, "textures/gui/player_inventory.png");
    protected ResourceLocation base;
    protected final T tile;
    protected final DMLEnergyStorage energyStorage;

    public MachineGui(T tile, Container c) {
        super(c);
        this.tile = tile;
        this.energyStorage = (DMLEnergyStorage) tile.getCapability(CapabilityEnergy.ENERGY, null);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.buttonList.forEach(guiButton -> {
            if (guiButton.isMouseOver()) {
                handleButtonClick(guiButton);
            }
        });
    }

    abstract protected void handleButtonClick(GuiButton guiButton);

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int left = getGuiLeft();
        int top = getGuiTop();

        // Draw the main GUI
        Minecraft.getMinecraft().getTextureManager().bindTexture(base);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(left + 46, top + 12, 0, 0, 107, 59);

        // Draw current energy
        int energyBarHeight = MathHelper.clamp((int) ((float) energyStorage.getEnergyStored() / (energyStorage.getMaxEnergyStored() - ModConfig.getAgonizerRFCost()) * 49), 0, 49);
        int energyBarOffset = 49 - energyBarHeight;
        drawTexturedModalRect(left + 52,  top + 17 + energyBarOffset, 0, 59, 7, energyBarHeight);

        // Draw player inventory
        Minecraft.getMinecraft().getTextureManager().bindTexture(defaultGui);
        drawTexturedModalRect( left + 11, top + 105, 0, 0, 177, 91);
    }

    protected void drawItemStack(int x, int y, ItemStack stack) {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glPushMatrix();
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        this.zLevel = 1.0F;
        this.itemRender.zLevel = 1.0F;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();

        RenderHelper.disableStandardItemLighting();
    }

}
