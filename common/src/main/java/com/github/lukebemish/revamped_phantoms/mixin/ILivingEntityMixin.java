package com.github.lukebemish.revamped_phantoms.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface ILivingEntityMixin {
    @Accessor
    int getUseItemRemaining();
    @Accessor("useItemRemaining")
    void setUseItemRemaining(int i);

}
