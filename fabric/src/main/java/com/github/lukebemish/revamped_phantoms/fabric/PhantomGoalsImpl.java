package com.github.lukebemish.revamped_phantoms.fabric;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class PhantomGoalsImpl {
    public static ResourceLocation getEntityRl(Entity e) {
        return Registry.ENTITY_TYPE.getKey(e.getType());
    }
}
