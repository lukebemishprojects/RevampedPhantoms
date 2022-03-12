package revamped_phantoms.mixin;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Phantom;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import revamped_phantoms.RevampedPhantoms;


@Mixin(targets = {"net/minecraft/world/entity/monster/Phantom$PhantomMoveControl"})
public class MoveControlMixin {
    @Shadow(aliases = {"this$0","field_7330"})
    @Final
    Phantom this$0;

    @ModifyArgs(method="tick", at=@At(value = "INVOKE",
            target = "Lnet/minecraft/util/Mth;approach(FFF)F"))
    private void revamped_phantoms$modifyPhantomMaxSpeed(Args args) {
        float original = args.get(1);
        if (this$0.getTarget() != null && this$0.getTarget().isFallFlying()) {
            args.set(1, original*RevampedPhantoms.getConfig().getPhantomElytraPursueModifier());
        }
    }
}
