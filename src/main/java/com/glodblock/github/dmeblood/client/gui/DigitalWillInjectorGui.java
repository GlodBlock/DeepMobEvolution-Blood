package com.glodblock.github.dmeblood.client.gui;

import com.glodblock.github.dmeblood.ModConfig;
import com.glodblock.github.dmeblood.ModConstants;
import com.glodblock.github.dmeblood.client.gui.base.ToolTips;
import com.glodblock.github.dmeblood.client.gui.buttons.AlertInformationZone;
import com.glodblock.github.dmeblood.common.container.ContainerDigitalWillInjector;
import com.glodblock.github.dmeblood.common.tile.TileEntityDigitalWillInjector;
import mustapelto.deepmoblearning.common.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DigitalWillInjectorGui extends MachineGui<TileEntityDigitalWillInjector> {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 178;
    private AlertInformationZone alertButton;
    private ToolTips progress;

    public DigitalWillInjectorGui(TileEntityDigitalWillInjector tile, InventoryPlayer inventory, World world) {
        super(tile, new ContainerDigitalWillInjector(tile, inventory, world));
        this.xSize = WIDTH;
        this.ySize = HEIGHT;
        this.base = new ResourceLocation(ModConstants.MODID, "textures/gui/digital_will_injector_gui.png");
    }

    @Override
    public void initGui() {
        super.initGui();
        this.alertButton = new AlertInformationZone(0, getGuiLeft() + 114, getGuiTop() + 79, 16, 16, this.width, this.height);
        this.progress = new ToolTips(this.fontRenderer,this.guiLeft + 88,this.guiTop + 39,36,6);
        this.labelList.add(progress);
    }

    @Override
    protected void handleButtonClick(GuiButton guiButton) {
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(base);

        int x = mouseX - this.guiLeft;
        int y = mouseY - this.guiTop;

        List<String> energyTooltip = new ArrayList<>();
        if (17 <= y && y < 66) {
            if (52 <= x && x < 59) {
                // Tooltip for energy
                energyTooltip.add(F.format(this.energyStorage.getEnergyStored()) + "/" + F.format(this.energyStorage.getMaxEnergyStored()) + " RF");
                energyTooltip.add(I18n.format("gui.digital_will_injector.energy", F.format(ModConfig.getWillInjectorRFCost())));
                drawHoveringText(energyTooltip, x, y);
            }
        }

        List<String> issueTooltips = new ArrayList<>();

        issueTooltips.add(I18n.format("gui.digital_will_injector.issue.0"));
        if (this.tile.getDataModelStack().isEmpty()) {
            issueTooltips.add(I18n.format("gui.digital_will_injector.issue.1"));
        }
        if (!this.tile.getDataModelStack().isEmpty() && this.tile.getModelModifier() <= 0) {
            issueTooltips.add(I18n.format("gui.digital_will_injector.issue.2"));
        }
        if (!this.tile.getDataModelStack().isEmpty() && this.tile.getTierOutput() <= 0) {
            issueTooltips.add(I18n.format("gui.digital_will_injector.issue.3"));
        }
        if (this.tile.getWeaponWillType() == null) {
            issueTooltips.add(I18n.format("gui.digital_will_injector.issue.4"));
        } else {
            if (this.tile.getGemWillType() != null && this.tile.getGemWillType() != this.tile.getWeaponWillType()) {
                issueTooltips.add(I18n.format("gui.digital_will_injector.issue.5"));
            }
        }
        this.alertButton.setTooltip(issueTooltips);
        this.progress.setTooltip(Collections.singletonList(I18n.format("gui.progress", this.tile.getProgress(), 60)));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int left = getGuiLeft();
        int top = getGuiTop();

        // Draw the main GUI
        Minecraft.getMinecraft().getTextureManager().bindTexture(base);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(left + 46, top + 12, 0, 0, 107, 59);

        // Draw crafting progress
        if (this.tile.getGemWillType() != null) {
            int craftingBarWidth = (int) (((float) this.tile.getProgress() / 60 * 36));
            drawTexturedModalRect(left + 88,  top + 39, 25, 59 + 6 * this.tile.getGemWillType().ordinal(), craftingBarWidth, 6);
        }

        // Draw data model slot
        drawTexturedModalRect(left + 91, top + 78, 7, 59, 18, 18);

        // Draw current energy
        int energyBarHeight = MathHelper.clamp((int) ((float) energyStorage.getEnergyStored() / (energyStorage.getMaxEnergyStored() - ModConfig.getAgonizerRFCost()) * 49), 0, 49);
        int energyBarOffset = 49 - energyBarHeight;
        drawTexturedModalRect(left + 52,  top + 17 + energyBarOffset, 0, 59, 7, energyBarHeight);

        // Draw player inventory
        Minecraft.getMinecraft().getTextureManager().bindTexture(defaultGui);
        drawTexturedModalRect( left + 12, top + 106, 0, 0, 176, 90);

        this.buttonList.forEach(guiButton -> guiButton.drawButton(this.mc, mouseX, mouseY, this.mc.getRenderPartialTicks()));
        if(this.tile.getTierOutput() <= 0 || this.tile.getModelModifier() <= 0 || this.tile.getWeaponWillType() == null || (this.tile.getGemWillType() != null && this.tile.getGemWillType() != this.tile.getWeaponWillType())) {
            this.alertButton.drawButton(this.mc, mouseX, mouseY, this.mc.getRenderPartialTicks());
        }
    }

}
