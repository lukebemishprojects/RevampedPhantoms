package revamped_phantoms.mixin;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Phantom;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = {"net/minecraft/world/entity/monster/Phantom$PhantomSweepAttackGoal"})
public abstract class SweepAttackMixin extends Goal {
    @Redirect(method = {"tick"},
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/monster/Phantom;attackPhase:Lnet/minecraft/world/entity/monster/Phantom$AttackPhase;",
                    opcode = Opcodes.PUTFIELD))
    private void revamped_phantoms_setAttackMode(Phantom owner, Phantom.AttackPhase phase) {
        if (owner.getTarget() != null && owner.getTarget().isFallFlying() && !((owner.horizontalCollision || owner.hurtTime > 0))) {
            ((IPhantomMixin)owner).setAttackPhase(Phantom.AttackPhase.SWOOP);
            return;
        }
        ((IPhantomMixin)owner).setAttackPhase(phase);
    }
}
