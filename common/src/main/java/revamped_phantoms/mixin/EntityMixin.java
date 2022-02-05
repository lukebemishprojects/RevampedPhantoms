package revamped_phantoms.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Phantom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import revamped_phantoms.RevampedPhantoms;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "getPassengersRidingOffset", at = @At("RETURN"), cancellable = true)
    private void revamped_phantoms_getPassengersRidingOffset(CallbackInfoReturnable<Double> ci) {
        if ((Entity)((Object)this) instanceof Phantom && RevampedPhantoms.getConfig().isPhantomsGrabPrey()) {
            ci.setReturnValue(ci.getReturnValue()-2);
        }
    }
}
