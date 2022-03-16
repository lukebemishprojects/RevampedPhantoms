package com.github.lukebemish.revamped_phantoms.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Random;

@Mixin(Entity.class)
public interface IEntityMixin {
    @Accessor
    Random getRandom();
}
