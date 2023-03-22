package com.glodblock.github.dmeblood.coremod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;

public class CoreModHooker {

    public static final String OWNER = "com/glodblock/github/dmeblood/coremod/CoreModHooker";

    public static EntityLivingBase getEntityBase(Entity targetEntity) {
        if (targetEntity instanceof MultiPartEntityPart) {
            IEntityMultiPart main = ((MultiPartEntityPart) targetEntity).parent;
            if (main instanceof EntityLivingBase) {
                return (EntityLivingBase) main;
            }
        }
        return null;
    }

}
