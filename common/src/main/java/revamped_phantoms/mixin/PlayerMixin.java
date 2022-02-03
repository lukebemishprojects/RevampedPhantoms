package revamped_phantoms.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import revamped_phantoms.RevampedPhantoms;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At("RETURN"), cancellable = true)
    private void stunned_delaying(CallbackInfoReturnable<Float> ci) {
        if (((Player)((Object)this)).hasEffect(RevampedPhantoms.STUNNED_EFFECT.get())) {
            float multiplier = 4.0f;
            float orig = ci.getReturnValue();
            ci.setReturnValue(orig * multiplier);
        }
    }
}
