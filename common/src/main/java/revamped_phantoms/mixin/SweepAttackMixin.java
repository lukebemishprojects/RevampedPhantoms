package revamped_phantoms.mixin;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Phantom;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import revamped_phantoms.RevampedPhantoms;
import revamped_phantoms.utils.IHasSharedGoals;

@Mixin(targets = {"net/minecraft/world/entity/monster/Phantom$PhantomSweepAttackGoal"})
public abstract class SweepAttackMixin extends Goal {
    @Shadow
    @Final
    Phantom field_7333;

    @Redirect(method = {"tick"},
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/monster/Phantom;attackPhase:Lnet/minecraft/world/entity/monster/Phantom$AttackPhase;",
                    opcode = Opcodes.PUTFIELD))
    private void revamped_phantoms_setAttackMode(Phantom owner, Phantom.AttackPhase phase) {
        if (owner.getTarget() != null && owner.getTarget().isFallFlying() && !((owner.horizontalCollision || owner.hurtTime > 0))) {
            ((IPhantomMixin)owner).setAttackPhase(Phantom.AttackPhase.SWOOP);
            if (RevampedPhantoms.getConfig().isPhantomsGrabPrey()) {
                ((IHasSharedGoals) owner).revamped_phantoms_getGoalHolder().shouldGrab = true;
            }
            return;
        }
        ((IPhantomMixin)owner).setAttackPhase(phase);
    }

    @Inject(method = "tick", at=@At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Phantom;doHurtTarget(Lnet/minecraft/world/entity/Entity;)Z"))
    private void revamped_phantoms_onHurtsTarget(CallbackInfo ci) {
        if (RevampedPhantoms.getConfig().isPhantomsGrabPrey()) {
            ((IHasSharedGoals) field_7333).revamped_phantoms_getGoalHolder().shouldGrab = true;
        }
    }

    @Inject(method = "canUse", at=@At("RETURN"), cancellable = true)
    private void revamped_phantoms_canUse(CallbackInfoReturnable<Boolean> ci) {
        boolean shouldOnlyCarry = field_7333.getTarget() instanceof Animal;
        boolean isFlying = field_7333.getTarget() != null && field_7333.getTarget().isFallFlying();
        boolean old = ci.getReturnValue();
        ci.setReturnValue(old && (!(((IHasSharedGoals) field_7333).revamped_phantoms_getGoalHolder().shouldGrab || shouldOnlyCarry)) || isFlying);
    }
}
