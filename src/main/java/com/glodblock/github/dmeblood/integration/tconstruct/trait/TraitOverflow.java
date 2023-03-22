package com.glodblock.github.dmeblood.integration.tconstruct.trait;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitOverflow extends AbstractTrait {

    public TraitOverflow() {
        super("overflow", 0x25f4eb);
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
        if (wasHit && target.isEntityAlive() && random.nextDouble() > 0.75) {
            target.setHealth(target.getHealth());
        }
    }

}
