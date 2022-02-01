package revamped_phantoms.mixin;

import net.minecraft.world.entity.monster.Phantom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Phantom.class)
public interface PhantomInterMixin {
    @Accessor
    Phantom.AttackPhase getAttackPhase();
    @Accessor("attackPhase")
    void setAttackPhase(Phantom.AttackPhase i);
}
