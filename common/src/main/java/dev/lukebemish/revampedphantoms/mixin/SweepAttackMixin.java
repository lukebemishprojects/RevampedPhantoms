package dev.lukebemish.revampedphantoms.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.lukebemish.revampedphantoms.RevampedPhantoms;
import dev.lukebemish.revampedphantoms.utils.Accessors;
import dev.lukebemish.revampedphantoms.utils.HasSharedGoals;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Phantom;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = {"net/minecraft/world/entity/monster/Phantom$PhantomSweepAttackGoal"})
public abstract class SweepAttackMixin extends Goal {
    @Unique
    Phantom revamped_phantoms$captured;

    @Inject(
        method = "<init>(Lnet/minecraft/world/entity/monster/Phantom;)V",
        at = @At("RETURN")
    )
    private void revamped_phantoms_init(Phantom phantom, CallbackInfo ci) {
        revamped_phantoms$captured = phantom;
    }

    @WrapOperation(
        method = "tick()V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/monster/Phantom;attackPhase:Lnet/minecraft/world/entity/monster/Phantom$AttackPhase;",
            opcode = Opcodes.PUTFIELD
        )
    )
    private void revamped_phantoms$setAttackMode(Phantom owner, @Coerce Object phase, Operation<Void> operation) {
        if (owner.getTarget() != null && owner.getTarget().isFallFlying() && !((owner.horizontalCollision || owner.hurtTime > 0))) {
            operation.call(owner, Accessors.getSwoopPhase());
            if (RevampedPhantoms.instance().platform.config().phantomsGrabPrey()) {
                ((HasSharedGoals) owner).revamped_phantoms$getGoalHolder().shouldGrab = true;
            }
            return;
        }
        operation.call(owner, phase);
    }

    @Inject(
        method = "tick()V",
        at=@At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/monster/Phantom;doHurtTarget(Lnet/minecraft/world/entity/Entity;)Z"
        )
    )
    private void revamped_phantoms$onHurtsTarget(CallbackInfo ci) {
        if (RevampedPhantoms.instance().platform.config().phantomsGrabPrey()) {
            ((HasSharedGoals) revamped_phantoms$captured).revamped_phantoms$getGoalHolder().shouldGrab = true;
        }
    }

    @ModifyReturnValue(method = "canUse()Z", at=@At("RETURN"))
    private boolean revamped_phantoms_canUse(boolean old) {
        if (old) {
            boolean shouldOnlyCarry = revamped_phantoms$captured.getTarget() instanceof Animal;
            return (!(((HasSharedGoals) revamped_phantoms$captured).revamped_phantoms$getGoalHolder().shouldGrab || shouldOnlyCarry));
        }
        return revamped_phantoms$captured.getTarget() != null && revamped_phantoms$captured.getTarget().isFallFlying();
    }
}
