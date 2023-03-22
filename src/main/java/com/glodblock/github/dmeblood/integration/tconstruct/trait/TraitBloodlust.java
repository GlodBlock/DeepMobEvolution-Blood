package com.glodblock.github.dmeblood.integration.tconstruct.trait;

import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import com.glodblock.github.dmeblood.util.BloodUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitBloodlust extends AbstractTrait {

    public TraitBloodlust() {
        super("bloodlust", 0x821b17);
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
        World world = target.getEntityWorld();
        if (wasHit && !target.isEntityAlive() && !world.isRemote && player instanceof EntityPlayer) {
            SoulNetwork bloodNet = NetworkHelper.getSoulNetwork((EntityPlayer) player);
            int maxCap = BloodUtil.getOrbCapByTier(bloodNet.getOrbTier());
            bloodNet.add(new SoulTicket((int) (target.getMaxHealth() * 50)), maxCap);
        }
    }

}
