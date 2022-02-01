package revamped_phantoms.mixin;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import revamped_phantoms.RevampedPhantoms;

import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Mixin(Phantom.class)
public abstract class PhantomMixin extends Mob {
    @Shadow
    Phantom.AttackPhase attackPhase;
    @Shadow
    Vec3 moveTargetPoint;

    private double preyGrabbedY;
    private int ticksSinceGrabbed;

    @Inject(method = "registerGoals", at=@At("HEAD"))
    protected void revamped_phantoms_registerGoals(CallbackInfo ci) {
        this.goalSelector.addGoal(2, new PhantomMixin.GrabPreyGoal());
        this.goalSelector.addGoal(1, new PhantomMixin.DropPreyGoal());
        this.goalSelector.addGoal(1, new PhantomMixin.StunPreyGoal());
        this.targetSelector.addGoal(1, new PhantomMixin.LivestockTargetGoal());
        this.targetSelector.addGoal(2, new PhantomMixin.VillagerTargetGoal());
    }

    class LivestockTargetGoal extends Goal {
        private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0);
        private int nextScanTick = PhantomMixin.LivestockTargetGoal.reducedTickDelay(20);

        @Override
        public boolean canUse() {
            if (this.nextScanTick > 0) {
                --this.nextScanTick;
                return false;
            }
            this.nextScanTick = PhantomMixin.LivestockTargetGoal.reducedTickDelay(60);
            List<Animal> list = PhantomMixin.this.level.getNearbyEntities(Animal.class, this.attackTargeting, PhantomMixin.this, PhantomMixin.this.getBoundingBox().inflate(32.0, 64.0, 32.0));
            if (!list.isEmpty()) {
                list.sort(Comparator.comparing((Entity entity) -> entity.getY()).reversed());
                for (Animal animal : list) {
                    if (!PhantomMixin.this.canAttack(animal, TargetingConditions.DEFAULT)) continue;
                    if (animal.isPassenger() || animal.isLeashed() || animal instanceof Cat) {
                        continue;
                    }
                    PhantomMixin.this.setTarget(animal);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingEntity = PhantomMixin.this.getTarget();
            if (livingEntity != null) {
                return PhantomMixin.this.canAttack(livingEntity, TargetingConditions.DEFAULT);
            }
            return false;
        }
    }
    class VillagerTargetGoal extends Goal {
        private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0);
        private int nextScanTick = PhantomMixin.VillagerTargetGoal.reducedTickDelay(20);

        @Override
        public boolean canUse() {
            if (this.nextScanTick > 0) {
                --this.nextScanTick;
                return false;
            }
            this.nextScanTick = PhantomMixin.VillagerTargetGoal.reducedTickDelay(60);
            List<Villager> list = PhantomMixin.this.level.getNearbyEntities(Villager.class, this.attackTargeting, PhantomMixin.this, PhantomMixin.this.getBoundingBox().inflate(32.0, 64.0, 32.0));
            if (!list.isEmpty()) {
                list.sort(Comparator.comparing((Entity entity) -> entity.getY()).reversed());
                for (Villager villager : list) {
                    if (!PhantomMixin.this.canAttack(villager, TargetingConditions.DEFAULT)) continue;
                    if (villager.isPassenger()) {
                        continue;
                    }
                    PhantomMixin.this.setTarget(villager);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingEntity = PhantomMixin.this.getTarget();
            if (livingEntity != null) {
                return PhantomMixin.this.canAttack(livingEntity, TargetingConditions.DEFAULT);
            }
            return false;
        }
    }

    class StunPreyGoal extends Goal {
        private boolean isScaredOfCat;
        private int catSearchTick;

        private int stunTick;

        @Override
        public boolean canUse() {
            return PhantomMixin.this.getTarget() != null && PhantomMixin.this.attackPhase == Phantom.AttackPhase.SWOOP;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingEntity = PhantomMixin.this.getTarget();
            if (livingEntity == null) {
                return false;
            }
            if (!livingEntity.isAlive()) {
                return false;
            }
            if (livingEntity instanceof Player) {
                Player player = (Player) livingEntity;
                if (livingEntity.isSpectator() || player.isCreative()) {
                    return false;
                }
            }
            if (!this.canUse()) {
                return false;
            }
            if (PhantomMixin.this.tickCount > this.catSearchTick) {
                this.catSearchTick = PhantomMixin.this.tickCount + 20;
                List<Cat> list = PhantomMixin.this.level.getEntitiesOfClass(Cat.class, PhantomMixin.this.getBoundingBox().inflate(16.0), EntitySelector.ENTITY_STILL_ALIVE);
                for (Cat cat : list) {
                    cat.hiss();
                }
                this.isScaredOfCat = !list.isEmpty();
            }
            return !this.isScaredOfCat;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = PhantomMixin.this.getTarget();
            if (livingEntity == null) {
                return;
            }
            // stun target
            if (PhantomMixin.this.hasLineOfSight(livingEntity)) {
                if (PhantomMixin.this.tickCount > stunTick && PhantomMixin.this.distanceTo(livingEntity) < 20) {
                    //particle?
                    if (!PhantomMixin.this.level.isClientSide()) {
                        /*SimpleParticleType particleoptions = ParticleTypes.HEART;
                        Vec3 pos = PhantomMixin.this.getPosition(1.0f).add(PhantomMixin.this.getLookAngle().multiply(1.5f,1.5f,1.5f));
                        Vec3 dist = livingEntity.getPosition(1.0f).subtract(pos);
                        dist = dist.normalize().multiply(0.1,0.1,0.1);
                        dist = Vec3.ZERO;
                        PhantomMixin.this.level.addParticle(particleoptions, pos.x, pos.y, pos.z, dist.x, dist.y, dist.z);*/
                        PhantomMixin.this.playSound(SoundEvents.CAT_HISS, 10.0f, 0.95f + PhantomMixin.this.random.nextFloat() * 0.1f);
                        if (!livingEntity.hasEffect(RevampedPhantoms.STUNNED_EFFECT.get())) {
                            livingEntity.addEffect(new MobEffectInstance(RevampedPhantoms.STUNNED_EFFECT.get(), 6 * 20, 0));
                        }
                    }
                    //stun here
                    stunTick = PhantomMixin.this.tickCount + 20*10;
                }
            }
        }
    }

    class GrabPreyGoal extends Goal {
        private boolean isScaredOfCat;
        private int catSearchTick;
        @Override
        public boolean canUse() {
            return PhantomMixin.this.getTarget() != null && PhantomMixin.this.attackPhase == Phantom.AttackPhase.SWOOP
                    && !PhantomMixin.this.getTarget().isPassenger() && !PhantomMixin.this.getTarget().isFallFlying();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingEntity = PhantomMixin.this.getTarget();
            if (livingEntity == null) {
                return false;
            }
            if (!livingEntity.isAlive()) {
                return false;
            }
            if (livingEntity.isPassenger()) {
                return false;
            }
            if (livingEntity instanceof Player) {
                Player player = (Player)livingEntity;
                if (livingEntity.isSpectator() || player.isCreative()) {
                    return false;
                }
            }
            if (!this.canUse()) {
                return false;
            }
            if (PhantomMixin.this.tickCount > this.catSearchTick) {
                this.catSearchTick = PhantomMixin.this.tickCount + 20;
                List<Cat> list = PhantomMixin.this.level.getEntitiesOfClass(Cat.class, PhantomMixin.this.getBoundingBox().inflate(16.0), EntitySelector.ENTITY_STILL_ALIVE);
                for (Cat cat : list) {
                    cat.hiss();
                }
                this.isScaredOfCat = !list.isEmpty();
            }
            return !this.isScaredOfCat;
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
            PhantomMixin.this.setTarget(null);
            PhantomMixin.this.attackPhase = Phantom.AttackPhase.CIRCLE;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = PhantomMixin.this.getTarget();
            if (livingEntity == null) {
                return;
            }
            PhantomMixin.this.moveTargetPoint = new Vec3(livingEntity.getX(), livingEntity.getY(0.5), livingEntity.getZ());
            if (PhantomMixin.this.getBoundingBox().inflate(0.2f).intersects(livingEntity.getBoundingBox()) && !livingEntity.hasPassenger(PhantomMixin.this)) {
                //Try to carry off
                livingEntity.startRiding(PhantomMixin.this);
                PhantomMixin.this.preyGrabbedY = PhantomMixin.this.getY();
                PhantomMixin.this.ticksSinceGrabbed = PhantomMixin.this.tickCount + 20*10;
                PhantomMixin.this.attackPhase = Phantom.AttackPhase.CIRCLE;
            } else if (PhantomMixin.this.horizontalCollision || PhantomMixin.this.hurtTime > 0 || livingEntity.hasPassenger(PhantomMixin.this)) {
                PhantomMixin.this.attackPhase = Phantom.AttackPhase.CIRCLE;
            }
        }
    }

    class DropPreyGoal extends Goal {
        @Override
        public boolean canUse() {
            return PhantomMixin.this.getFirstPassenger() != null;
        }

        @Override
        public void tick() {
            Entity entity = PhantomMixin.this.getFirstPassenger();
            if (entity == null) {
                return;
            } else if (PhantomMixin.this.getY() > 30.0+PhantomMixin.this.preyGrabbedY || PhantomMixin.this.tickCount > PhantomMixin.this.ticksSinceGrabbed) {
                entity.stopRiding();
            }
        }
    }



    //unimportant stuff...
    protected PhantomMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
        throw new AssertionError();
    }
}
