package dev.lukebemish.revampedphantoms.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.lukebemish.revampedphantoms.PhantomGoals;
import dev.lukebemish.revampedphantoms.RevampedPhantoms;
import dev.lukebemish.revampedphantoms.utils.HasSharedGoals;
import dev.lukebemish.revampedphantoms.utils.SharedGoalHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Phantom.class)
public abstract class PhantomMixin extends Mob implements HasSharedGoals {
    @Unique
    private final SharedGoalHolder revamped_phantoms$goalHolder = new SharedGoalHolder();

    protected PhantomMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @SuppressWarnings("DataFlowIssue")
    @Inject(method = "registerGoals", at=@At("HEAD"))
    private void revamped_phantoms$registerGoals(CallbackInfo ci) {
        Phantom phantom = (Phantom)((Object)this);
        if (RevampedPhantoms.instance().platform.config().phantomsGrabPrey()) {
            this.goalSelector.addGoal(2, new PhantomGoals.GrabPreyGoal(phantom));
        }
        if (RevampedPhantoms.instance().platform.config().phantomsGrabPrey()) {
            this.goalSelector.addGoal(1, new PhantomGoals.DropPreyGoal(phantom));
        }
        if (RevampedPhantoms.instance().platform.config().phantomsStunPrey()) {
            this.goalSelector.addGoal(1, new PhantomGoals.StunPreyGoal(phantom));
        }
        if (RevampedPhantoms.instance().platform.config().phantomsAttackAnimals()) {
            this.targetSelector.addGoal(1, new PhantomGoals.LivestockTargetGoal(phantom));
        }
        if (RevampedPhantoms.instance().platform.config().phantomsAttackVillagers()) {
            this.targetSelector.addGoal(1, new PhantomGoals.VillagerTargetGoal(phantom));
        }
    }

    @Override
    public SharedGoalHolder revamped_phantoms$getGoalHolder() {
        return revamped_phantoms$goalHolder;
    }

    @ModifyReturnValue(method = "getPassengerAttachmentPoint(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/EntityDimensions;F)Lorg/joml/Vector3f;", at = @At("RETURN"))
    private Vector3f revamped_phantoms$attachmentPoint(Vector3f old, Entity entity, EntityDimensions dimensions, float scale) {
        return old.add(0, -(dimensions.height * 0.675F + entity.getBbHeight()), 0);
    }
}
