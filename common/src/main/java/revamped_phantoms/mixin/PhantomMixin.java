package revamped_phantoms.mixin;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import revamped_phantoms.IHasSharedGoals;
import revamped_phantoms.PhantomGoals;
import revamped_phantoms.SharedGoalHolder;

@Mixin(Phantom.class)
public abstract class PhantomMixin extends Mob implements IHasSharedGoals {
    @Shadow
    Phantom.AttackPhase attackPhase;
    @Shadow
    Vec3 moveTargetPoint;

    @Unique
    private SharedGoalHolder revamped_phantoms_goalHolder;

    @Inject(method = "<init>", at=@At("RETURN"))
    protected void revamped_phantoms_setup(CallbackInfo ci) {
        revamped_phantoms_goalHolder = new SharedGoalHolder();
    }

    @Inject(method = "registerGoals", at=@At("HEAD"))
    protected void revamped_phantoms_registerGoals(CallbackInfo ci) {
        Phantom phantom = (Phantom)((Object)this);
        this.goalSelector.addGoal(2, new PhantomGoals.GrabPreyGoal(phantom));
        this.goalSelector.addGoal(1, new PhantomGoals.DropPreyGoal(phantom));
        this.goalSelector.addGoal(1, new PhantomGoals.StunPreyGoal(phantom));
        this.targetSelector.addGoal(1, new PhantomGoals.LivestockTargetGoal(phantom));
        this.targetSelector.addGoal(2, new PhantomGoals.VillagerTargetGoal(phantom));
    }

    @Override
    public SharedGoalHolder getGoalHolder() {
        return revamped_phantoms_goalHolder;
    }

    //unimportant stuff...
    protected PhantomMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
        throw new AssertionError();
    }
}
