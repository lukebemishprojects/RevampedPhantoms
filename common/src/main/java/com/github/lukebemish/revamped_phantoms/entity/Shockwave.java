package com.github.lukebemish.revamped_phantoms.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import com.github.lukebemish.revamped_phantoms.RevampedPhantoms;

import java.util.List;

public class Shockwave extends AbstractHurtingProjectile {
    public Shockwave(EntityType<Shockwave> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected float getInertia() {
        return 1.4f;
    }

    public Shockwave(Level level, LivingEntity livingEntity, double d, double e, double f) {
        super(RevampedPhantoms.SHOCKWAVE.get(), livingEntity, d, e, f, level);
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
        if (!this.level.isClientSide) {
            List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0, 4.0, 4.0));
            if (!list.isEmpty()) {
                for (LivingEntity livingEntity : list) {
                    if (!(livingEntity instanceof Phantom)) {
                        livingEntity.addEffect(new MobEffectInstance(RevampedPhantoms.STUNNED_EFFECT.get(), RevampedPhantoms.getConfig().getTicksStunDuration(), 0, false, false));
                        if (this.getOwner() != null && this.getOwner() instanceof LivingEntity owner) {
                            livingEntity.hurt(DamageSource.mobAttack(owner), 1);
                        }
                    }
                }
            }
            this.discard();
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
