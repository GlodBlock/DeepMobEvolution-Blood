package com.glodblock.github.dmeblood.network.packets;

import com.glodblock.github.dmeblood.common.tile.IContainerProvider;
import io.netty.buffer.ByteBuf;
import mustapelto.deepmoblearning.common.tiles.CraftingState;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class SPacketStateUpdate implements IMessage {

    private BlockPos pos;
    private CraftingState state;

    public SPacketStateUpdate() {
        //NO-OP
    }

    public SPacketStateUpdate(TileEntity te, CraftingState state) {
        this.pos = te.getPos();
        this.state = state;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.state = CraftingState.byIndex(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
        buf.writeInt(this.state.getIndex());
    }

    public static class Handler implements IMessageHandler<SPacketStateUpdate, IMessage> {
        @Nullable
        @Override
        public IMessage onMessage(SPacketStateUpdate message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);
                if (te instanceof IContainerProvider) {
                    ((IContainerProvider) te).setState(message.state);
                }
            });
            return null;
        }
    }

}
