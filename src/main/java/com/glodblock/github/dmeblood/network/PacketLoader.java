package com.glodblock.github.dmeblood.network;

import com.glodblock.github.dmeblood.DeepMobLearningBM;
import com.glodblock.github.dmeblood.network.packets.CHighlightAltarMessage;
import com.glodblock.github.dmeblood.network.packets.SPacketStateUpdate;
import net.minecraftforge.fml.relauncher.Side;

public class PacketLoader {

    public static void init() {
        int id = 0;
        DeepMobLearningBM.proxy.netHandler.registerMessage(new SPacketStateUpdate.Handler(), SPacketStateUpdate.class, id ++, Side.CLIENT);
        DeepMobLearningBM.proxy.netHandler.registerMessage(new CHighlightAltarMessage.Handler(), CHighlightAltarMessage.class, id ++, Side.SERVER);
    }

}
