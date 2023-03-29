package com.glodblock.github.dmeblood.network.packets;

import com.glodblock.github.dmeblood.common.container.ContainerDigitalAgonizer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CHighlightAltarMessage implements IMessage {
    public CHighlightAltarMessage() {}

    @Override
    public void fromBytes(ByteBuf byteBuf) {}

    @Override
    public void toBytes(ByteBuf byteBuf) {}

    public static class Handler implements IMessageHandler<CHighlightAltarMessage, IMessage> {

        @Override
        public IMessage onMessage(CHighlightAltarMessage message, MessageContext ctx) {
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
