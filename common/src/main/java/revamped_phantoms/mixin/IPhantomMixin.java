package revamped_phantoms.mixin;

import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Phantom.class)
public interface IPhantomMixin extends IEntityMixin {
    @Accessor
    Phantom.AttackPhase getAttackPhase();
    @Accessor("attackPhase")
    void setAttackPhase(Phantom.AttackPhase phase);
    @Accessor
    Vec3 getMoveTargetPoint();
    @Accessor("moveTargetPoint")
    void setMoveTargetPoint(Vec3 p);
}
