package dev.lukebemish.revampedphantoms.entity;

import dev.lukebemish.revampedphantoms.RevampedPhantoms;
import java.util.List;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class Shockwave extends AbstractHurtingProjectile {
	public Shockwave(EntityType<Shockwave> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	protected float getInertia() {
		return 1.4f;
	}

	public Shockwave(Level level, LivingEntity livingEntity, double d, double e, double f) {
		super(RevampedPhantoms.instance().shockwave.get(), livingEntity, d, e, f, level);
	}

	public float getDistFromInitial() {
		if (getOwner() == null) {
			return 1.0f;
		}
		return Math.min(getOwner().distanceTo(this)/5.0f+1.0f, 4.0f);
	}

	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);
		if (result.getType() == HitResult.Type.ENTITY && this.ownedBy(((EntityHitResult)result).getEntity())) {
			return;
		}
		if (!this.level().isClientSide) {
			List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0, 4.0, 4.0));
			if (!list.isEmpty()) {
				Entity thrower = this.getOwner();
				if (thrower == null) {
					thrower = this;
				}
				DamageSource damageSource = new DamageSource(
					this.level().registryAccess()
						.registryOrThrow(Registries.DAMAGE_TYPE)
						.getHolderOrThrow(DamageTypes.MOB_PROJECTILE),
					this,
					thrower
				);
				for (LivingEntity livingEntity : list) {
					if (!(livingEntity instanceof Phantom)) {
						livingEntity.addEffect(new MobEffectInstance(RevampedPhantoms.instance().stunned.get(), RevampedPhantoms.instance().platform.config().ticksStunDuration(), 0, false, false));
						livingEntity.hurt(damageSource, 1);
						if (thrower instanceof LivingEntity livingThrower) {
							livingThrower.setLastHurtMob(livingEntity);
						}
					}
				}
			}
			this.discard();
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide()) {
			if (this.position().y > this.level().getMaxBuildHeight() + 1000) {
				// welp, I'm sure there's a better approach but whatever, it works
				this.discard();
			}
		}
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	public boolean hurt(DamageSource damageSource, float amount) {
		return false;
	}

	@Override
	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.MYCELIUM;
	}

	@Override
	protected boolean shouldBurn() {
		return false;
	}
}
