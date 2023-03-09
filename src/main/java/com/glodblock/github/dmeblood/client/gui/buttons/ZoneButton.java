package com.glodblock.github.dmeblood.client.gui.buttons;

import net.minecraft.client.audio.SoundHandler;

import javax.annotation.Nonnull;

public class ZoneButton extends ClickableZoneButton {
    public ZoneButton(int buttonId, int x, int y, int widthIn, int heightIn, int screenWidth, int screenHeight) {
        super(buttonId, x, y, widthIn, heightIn, screenWidth, screenHeight);
    }

    @Override
    public void playPressSound(@Nonnull SoundHandler soundHandlerIn) {

    }
}
