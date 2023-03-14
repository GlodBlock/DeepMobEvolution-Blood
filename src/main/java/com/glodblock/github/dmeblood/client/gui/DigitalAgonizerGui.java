package com.glodblock.github.dmeblood.client.gui;

import WayofTime.bloodmagic.altar.BloodAltar;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import com.glodblock.github.dmeblood.DeepMobLearningBM;
import com.glodblock.github.dmeblood.ModConfig;
import com.glodblock.github.dmeblood.ModConstants;
import com.glodblock.github.dmeblood.client.gui.base.ToolTips;
import com.glodblock.github.dmeblood.client.gui.buttons.AlertInformationZone;
import com.glodblock.github.dmeblood.client.gui.buttons.ClickableZoneButton;
import com.glodblock.github.dmeblood.client.gui.buttons.ZoneButton;
import com.glodblock.github.dmeblood.common.container.ContainerDigitalAgonizer;
import com.glodblock.github.dmeblood.common.network.HighlightAltarMessage;
import com.glodblock.github.dmeblood.common.tile.TileEntityDigitalAgonizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;

public class DigitalAgonizerGui extends MachineGui<TileEntityDigitalAgonizer> {

    private static final int WIDTH =  200;
    private static final int HEIGHT = 178;
    private ZoneButton sacRuneZone;
    private ClickableZoneButton altarButton;
    private AlertInformationZone alertButton;
    private static final NumberFormat F = NumberFormat.getNumberInstance(Locale.ENGLISH);
    private ToolTips catalyst;
    private ToolTips progress;


    public DigitalAgonizerGui(TileEntityDigitalAgonizer tile, InventoryPlayer inventory, World world) {
        super(tile, new ContainerDigitalAgonizer(tile, inventory, world));
        this.xSize = WIDTH;
        this.ySize = HEIGHT;
        this.base = new ResourceLocation(ModConstants.MODID, "textures/gui/digital_agonizer_gui.png");
    }

    @Override
    public void initGui() {
        super.initGui();
        this.sacRuneZone = new ZoneButton(0, getGuiLeft() + 120, getGuiTop() - 8, 16, 16, this.width, this.height);
        this.altarButton = new ClickableZoneButton(1, getGuiLeft() + 130, getGuiTop() + 35, 16, 16, this.width, this.height);
        this.alertButton = new AlertInformationZone(2, getGuiLeft() + 114, getGuiTop() + 79, 16, 16, this.width, this.height);
        this.catalyst = new ToolTips(this.fontRenderer,getGuiLeft() + 66,getGuiTop() + 31,16,2);
        this.progress = new ToolTips(this.fontRenderer,getGuiLeft() + 88,getGuiTop() + 39,36,6);
        this.buttonList.add(this.sacRuneZone);
        this.buttonList.add(this.altarButton);
        this.labelList.add(this.catalyst);
        this.labelList.add(this.progress);
    }

    @Override
    protected void handleButtonClick(GuiButton guiButton) {
        if (guiButton == this.altarButton) {
            if (tile.getAltarTank() != null) {
                DeepMobLearningBM.network.sendToServer(new HighlightAltarMessage());
            }
        }
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
                energyTooltip.add(I18n.format("gui.digital_agonizer.energy", F.format(ModConfig.getAgonizerRFCost())));
                drawHoveringText(energyTooltip, x, y);
            }
        }

        List<String> runeTooltips = new ArrayList<>();
        List<String> altarTooltips = new ArrayList<>();
        List<String> issueTooltips = new ArrayList<>();

        if (this.tile.getAltarTank() != null) {
            BloodAltar altar = this.tile.getAltarTank();
            runeTooltips.add(I18n.format("gui.digital_agonizer.run.0", this.tile.getNumOfSacrificeRunes()));
            runeTooltips.add(I18n.format("gui.digital_agonizer.run.1", altar.getSacrificeMultiplier()));
            runeTooltips.add(I18n.format("gui.digital_agonizer.run.2", this.tile.getMultiplier()));
            runeTooltips.add(I18n.format("gui.digital_agonizer.run.3", F.format(this.tile.getMultiplier() + this.tile.getSacrificeMultiplier())));

            altarTooltips.add(I18n.format("gui.digital_agonizer.altar.0"));
            altarTooltips.add(I18n.format("gui.digital_agonizer.altar.1", this.tile.getAltarPos().getX(), this.tile.getAltarPos().getY(), this.tile.getAltarPos().getZ()));
            altarTooltips.add(I18n.format("gui.digital_agonizer.altar.2", F.format(altar.getFluidAmount()), F.format(altar.getCapacity())));
            altarTooltips.add("");
            altarTooltips.add(I18n.format("gui.digital_agonizer.altar.3"));
            altarTooltips.add(I18n.format("gui.digital_agonizer.altar.4"));
        } else {
            altarTooltips.add(I18n.format("gui.digital_agonizer.altar.5"));
            altarTooltips.add(I18n.format("gui.digital_agonizer.altar.6"));
            altarTooltips.add(I18n.format("gui.digital_agonizer.altar.7"));
        }

        issueTooltips.add(I18n.format("gui.digital_agonizer.issue.0"));
        if (!this.tile.hasDataModel()) {
            issueTooltips.add(I18n.format("gui.digital_agonizer.issue.1"));
        }
        if (this.tile.hasDataModel() && !this.tile.isValidDataModelTier()) {
            issueTooltips.add(I18n.format("gui.digital_agonizer.issue.2"));
        }
        if (this.tile.getAltarTank() == null) {
            issueTooltips.add(I18n.format("gui.digital_agonizer.issue.3"));
        }

        // tooltips
        this.progress.setTooltip(Collections.singletonList(I18n.format("gui.progress", this.tile.getProgress(), 60)));
        this.catalyst.setTooltip(Collections.singletonList(I18n.format("gui.digital_agonizer.catalyst_operations",this.tile.getCatalystOperations(),this.tile.getCatalystOperationsMax())));

        this.sacRuneZone.setTooltip(runeTooltips);
        this.altarButton.setTooltip(altarTooltips);
        this.alertButton.setTooltip(issueTooltips);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        int left = getGuiLeft();
        int top = getGuiTop();

        Minecraft.getMinecraft().getTextureManager().bindTexture(base);
        // Draw crafting progress
        int craftingBarWidth = (int) (((float) this.tile.getProgress() / 60 * 36));
        drawTexturedModalRect(left + 88,  top + 39, 25, 59, craftingBarWidth, 6);

        // Draw data model slot
        drawTexturedModalRect(left + 91, top + 78, 7, 59, 18, 18);

        // Draw Error icon or Altar
        if (this.tile.getAltarTank() == null) {
            drawTexturedModalRect(left + 129, top + 35, 60, 59, 15, 15);
        } else {
            drawItemStack(left + 130, top + 34, new ItemStack(RegistrarBloodMagicBlocks.ALTAR));
            // Draw sac runes
            drawItemStack(left + 120, top - 8, new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 3));
            drawString(this.fontRenderer,  this.tile.getNumOfSacrificeRunes() + "", left + 140, top - 4, 0xFFFFFF);
        }

        // Draw current multiplier
        drawCenteredString(this.fontRenderer, F.format(this.tile.getMultiplier() + this.tile.getSacrificeMultiplier()) + "x", left + 105, top + 28, 0xFFFFFF);

        if (this.tile.hasDataModel() && this.tile.isValidDataModelTier()) {
            drawCenteredString(this.fontRenderer,  this.tile.getFillAmount() + "mB", left + 105, top + 49, 0xFFFFFF);
        }

        // Draw catalyst operations
        drawGradientRect(left + 66, top + 31, left + 82, top + 33, 0xFF424242, 0xFF333333);
        int catalystWidth = (int) (((float) this.tile.getCatalystOperations() / tile.getCatalystOperationsMax() * 16));
        drawGradientRect(left + 66, top + 31, left + 66 + catalystWidth, top + 33, 0xFFa142f4, 0xFF852cd3);

        this.buttonList.forEach(guiButton -> guiButton.drawButton(this.mc, mouseX, mouseY, this.mc.getRenderPartialTicks()));
        if(!this.tile.hasDataModel() || (this.tile.hasDataModel() && !this.tile.isValidDataModelTier()) || this.tile.getAltarTank() == null) {
            this.alertButton.drawButton(this.mc, mouseX, mouseY, this.mc.getRenderPartialTicks());
        }
    }

}
