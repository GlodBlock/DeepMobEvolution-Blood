package com.glodblock.github.dmeblood.common.network;

import com.glodblock.github.dmeblood.common.container.ContainerDigitalAgonizer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HighlightAltarMessage implements IMessage {
    public HighlightAltarMessage() {}

    @Override
    public void fromBytes(ByteBuf byteBuf) {}

    @Override
    public void toBytes(ByteBuf byteBuf) {}

    public static class Handler implements IMessageHandler<HighlightAltarMessage, IMessage> {

        @Override
        public IMessage onMessage(HighlightAltarMessage message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                if (player.openContainer instanceof ContainerDigitalAgonizer) {
                    ContainerDigitalAgonizer container = (ContainerDigitalAgonizer) player.openContainer;
                    container.tile.setHighlightingTicks(20 * 20);
                    player.sendStatusMessage(new TextComponentTranslation("text.altar_linker.highlight"), true);
                }
            });
            return null;
        }
    }
}
