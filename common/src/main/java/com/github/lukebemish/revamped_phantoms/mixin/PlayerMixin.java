package com.github.lukebemish.revamped_phantoms.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.github.lukebemish.revamped_phantoms.RevampedPhantoms;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    private PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
        throw new AssertionError(); //why is this called?
    }

    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At("RETURN"), cancellable = true)
    private void revamped_phantoms_stunned_delaying(CallbackInfoReturnable<Float> ci) {
        if (((Player)((Object)this)).hasEffect(RevampedPhantoms.STUNNED_EFFECT.get())) {
            float multiplier = 4.0f;
            float orig = ci.getReturnValue();
            ci.setReturnValue(orig * multiplier);
        }
    }

    @Inject(method="wantsToStopRiding", at = @At("RETURN"), cancellable = true)
    private void revamped_phantoms$wantsToStopRiding(CallbackInfoReturnable<Boolean> cir) {
        if (this.getVehicle()!=null && this.getVehicle().getType()==EntityType.PHANTOM) {
            cir.setReturnValue(false);
        }
    }
}
