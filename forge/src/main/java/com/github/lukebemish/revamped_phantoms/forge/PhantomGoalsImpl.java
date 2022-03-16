package com.github.lukebemish.revamped_phantoms.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;

public class PhantomGoalsImpl {
    public static ResourceLocation getEntityRl(Entity e) {
        return ForgeRegistries.ENTITIES.getKey(e.getType());
    }
}
