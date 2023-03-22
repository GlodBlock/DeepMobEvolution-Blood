package com.glodblock.github.dmeblood.integration.tconstruct.trait;

import mustapelto.deepmoblearning.common.DMLRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitGlitch extends AbstractTrait {

    public TraitGlitch() {
        super("glitch", 0x252ef4);
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
        World world = target.getEntityWorld();
        if (wasHit && !target.isEntityAlive() && !world.isRemote && random.nextDouble() > 0.9) {
            BlockPos pos = target.getPosition();
            int loot = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, tool) + 1;
            EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(DMLRegistry.ITEM_GLITCH_HEART, loot));
            item.setDefaultPickupDelay();
            world.spawnEntity(item);
        }
    }

}
