package dev.lukebemish.revampedphantoms.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.lukebemish.revampedphantoms.PhantomGoals;
import dev.lukebemish.revampedphantoms.RevampedPhantoms;
import dev.lukebemish.revampedphantoms.utils.Accessors;
import dev.lukebemish.revampedphantoms.utils.HasSharedGoals;
import dev.lukebemish.revampedphantoms.utils.SharedGoalHolder;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Mixin(Phantom.class)
public abstract class PhantomMixin extends Mob implements HasSharedGoals {
	@Unique private final SharedGoalHolder revamped_phantoms$goalHolder = new SharedGoalHolder();

	protected PhantomMixin(EntityType<? extends Mob> entityType, Level level) {
		super(entityType, level);
	}

	@SuppressWarnings({"DataFlowIssue", "UnreachableCode"})
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

	@SuppressWarnings("UnreachableCode")
	@ModifyExpressionValue(
		method = "getDefaultDimensions(Lnet/minecraft/world/entity/Pose;)Lnet/minecraft/world/entity/EntityDimensions;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/FlyingMob;getDefaultDimensions(Lnet/minecraft/world/entity/Pose;)Lnet/minecraft/world/entity/EntityDimensions;"
		)
	)
	private EntityDimensions revamped_phantoms$getDefaultDimensions(EntityDimensions original) {
		var attachments = Accessors.getAttachments(original.attachments());
		Map<EntityAttachment, List<Vec3>> map = new EnumMap<>(EntityAttachment.class);
		attachments.forEach((k, v) -> map.put(k, new ArrayList<>(v)));
		map.put(EntityAttachment.PASSENGER, EntityAttachment.Fallback.AT_FEET.create(original.width(), original.height()));
		var builder = EntityAttachments.builder();
		map.forEach((k, v) -> v.forEach(p -> builder.attach(k, p)));
		return original.withAttachments(builder);
	}
}
