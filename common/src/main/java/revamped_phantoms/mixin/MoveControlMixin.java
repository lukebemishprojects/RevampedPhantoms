package revamped_phantoms.mixin;

import net.minecraft.world.entity.monster.Phantom;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import revamped_phantoms.RevampedPhantoms;


@Mixin(targets = {"net/minecraft/world/entity/monster/Phantom$PhantomMoveControl"})
public class MoveControlMixin {
    @Shadow
    @Final
    Phantom field_7330;

    @ModifyArg(method="tick", at=@At(value = "INVOKE",
            target = "Lnet/minecraft/util/Mth;approach(FFF)F"), index=1)
    private float revamped_phantoms$modifyPhantomMaxSpeed(float original) {
        if (field_7330.getTarget() != null && field_7330.getTarget().isFallFlying()) {
            return original*RevampedPhantoms.getConfig().getPhantomElytraPursueModifier();
        }
        return original;
    }
}
