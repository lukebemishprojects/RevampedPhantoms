package revamped_phantoms.mixin;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Phantom;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.ModifyConstant;


@Mixin(targets = {"net/minecraft/world/entity/monster/Phantom$PhantomMoveControl"})
public class MoveControlMixin {
    @Shadow
    @Final
    Phantom this$0;

    @ModifyConstant(method="tick", constant = @Constant(floatValue = 1.8f),
            target={@Desc(value="degreesDifferenceAbs", owner=Mth.class, ret=Float.class, args={Float.class,Float.class})})
    private float revamped_phantoms_modifyPhantomMaxSpeed(float original) {
        if (this$0.getTarget() != null && this$0.getTarget().isFallFlying()) {
            return original*8;
        }
        return original;
    }
}
