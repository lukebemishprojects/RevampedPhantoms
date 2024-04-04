package dev.lukebemish.revampedphantoms.mixin;

import dev.lukebemish.revampedphantoms.RevampedPhantoms;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Phantom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(targets = {"net/minecraft/world/entity/monster/Phantom$PhantomMoveControl"})
public abstract class MoveControlMixin extends MoveControl {

    public MoveControlMixin(Mob mob) {
        super(mob);
    }

    @ModifyArg(method="tick()V", at=@At(value = "INVOKE",
        target = "Lnet/minecraft/util/Mth;approach(FFF)F"), index=1)
    private float revamped_phantoms$modifyPhantomMaxSpeed(float original) {
        Phantom phantom = (Phantom) this.mob;
        if (phantom.getTarget() != null && phantom.getTarget().isFallFlying()) {
            return original * RevampedPhantoms.instance().platform.config().phantomElytraPursueModifier();
        }
        return original;
    }
}
