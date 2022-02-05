package revamped_phantoms.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import revamped_phantoms.RevampedPhantoms;
import revamped_phantoms.utils.IHasSharedGoals;
import revamped_phantoms.PhantomGoals;
import revamped_phantoms.utils.SharedGoalHolder;

@Mixin(Phantom.class)
public abstract class PhantomMixin extends Mob implements IHasSharedGoals {
    @Shadow
    Phantom.AttackPhase attackPhase;
    @Shadow
    Vec3 moveTargetPoint;

    @Unique
    private SharedGoalHolder revamped_phantoms_goalHolder;

    @Inject(method = "<init>", at=@At("RETURN"))
    private void revamped_phantoms_setup(CallbackInfo ci) {
        revamped_phantoms_goalHolder = new SharedGoalHolder();
    }

    @Inject(method = "registerGoals", at=@At("HEAD"))
    private void revamped_phantoms_registerGoals(CallbackInfo ci) {
        Phantom phantom = (Phantom)((Object)this);
        if (RevampedPhantoms.getConfig().isPhantomsGrabPrey()) {
            this.goalSelector.addGoal(2, new PhantomGoals.GrabPreyGoal(phantom));
        }
        if (RevampedPhantoms.getConfig().isPhantomsGrabPrey()) {
            this.goalSelector.addGoal(1, new PhantomGoals.DropPreyGoal(phantom));
        }
        if (RevampedPhantoms.getConfig().isPhantomsStunPrey()) {
            this.goalSelector.addGoal(1, new PhantomGoals.StunPreyGoal(phantom));
        }
        if (RevampedPhantoms.getConfig().isPhantomsAttackAnimals()) {
            this.targetSelector.addGoal(1, new PhantomGoals.LivestockTargetGoal(phantom));
        }
        if (RevampedPhantoms.getConfig().isPhantomsAttackVillagers()) {
            this.targetSelector.addGoal(1, new PhantomGoals.VillagerTargetGoal(phantom));
        }
    }

    @Override
    public SharedGoalHolder getGoalHolder() {
        return revamped_phantoms_goalHolder;
    }

    //unimportant stuff...
    private PhantomMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
        throw new AssertionError();
    }
}
