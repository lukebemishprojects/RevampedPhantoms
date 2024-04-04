package dev.lukebemish.revampedphantoms;

import dev.lukebemish.revampedphantoms.entity.Shockwave;
import dev.lukebemish.revampedphantoms.utils.Accessors;
import dev.lukebemish.revampedphantoms.utils.HasSharedGoals;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public final class PhantomGoals {
    private PhantomGoals() {}

    public static class LivestockTargetGoal extends Goal {
        private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0).selector((LivingEntity e) ->
            !e.getType().is(RevampedPhantoms.PHANTOM_ATTACK_BLACKLIST)
        );
        private int nextScanTick = PhantomGoals.LivestockTargetGoal.reducedTickDelay(20);

        final private Phantom self;
        public LivestockTargetGoal(Phantom p) {
            self = p;
        }

        @Override
        public boolean canUse() {
            if (this.nextScanTick > 0) {
                --this.nextScanTick;
                return false;
            }
            this.nextScanTick = PhantomGoals.LivestockTargetGoal.reducedTickDelay(60);
            List<Animal> list = self.level().getNearbyEntities(Animal.class, this.attackTargeting, self, self.getBoundingBox().inflate(32.0, 64.0, 32.0));
            if (!list.isEmpty()) {
                list.sort(Comparator.comparing((Entity entity) -> entity.getY()).reversed());
                for (Animal animal : list) {
                    if (!self.canAttack(animal, TargetingConditions.DEFAULT)) continue;
                    if (animal.isPassenger() || animal.isLeashed() || animal instanceof Cat) {
                        continue;
                    }
                    self.setTarget(animal);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingEntity = self.getTarget();
            if (livingEntity != null) {
                return self.canAttack(livingEntity, TargetingConditions.DEFAULT);
            }
            return false;
        }
    }
    public static class VillagerTargetGoal extends Goal {
        private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0);
        private int nextScanTick = PhantomGoals.VillagerTargetGoal.reducedTickDelay(20);

        final private Phantom self;
        public VillagerTargetGoal(Phantom p) {
            self = p;
        }

        @Override
        public boolean canUse() {
            if (this.nextScanTick > 0) {
                --this.nextScanTick;
                return false;
            }
            this.nextScanTick = PhantomGoals.VillagerTargetGoal.reducedTickDelay(60);
            List<Villager> list = self.level().getNearbyEntities(Villager.class, this.attackTargeting, self, self.getBoundingBox().inflate(32.0, 64.0, 32.0));
            if (!list.isEmpty()) {
                list.sort(Comparator.comparing((Entity entity) -> entity.getY()).reversed());
                for (Villager villager : list) {
                    if (!self.canAttack(villager, TargetingConditions.DEFAULT)) continue;
                    if (villager.isPassenger()) {
                        continue;
                    }
                    self.setTarget(villager);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingEntity = self.getTarget();
            if (livingEntity != null) {
                return self.canAttack(livingEntity, TargetingConditions.DEFAULT);
            }
            return false;
        }
    }

    public static class StunPreyGoal extends Goal {
        private boolean isScaredOfCat;
        private int catSearchTick;

        private int stunTick;

        final private Phantom self;
        public StunPreyGoal(Phantom p) {
            self = p;
        }

        @SuppressWarnings("ConstantValue")
        @Override
        public boolean canUse() {
            return self.getTarget() != null && Accessors.getAttackPhase(self) == Accessors.getSwoopPhase();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingEntity = self.getTarget();
            if (livingEntity == null) {
                return false;
            }
            if (!livingEntity.isAlive()) {
                return false;
            }
            if (livingEntity instanceof Player player) {
                if (player.isSpectator() || player.isCreative()) {
                    return false;
                }
            }
            if (!this.canUse()) {
                return false;
            }
            if (self.tickCount > this.catSearchTick) {
                this.catSearchTick = self.tickCount + 20;
                List<Cat> list = self.level().getEntitiesOfClass(Cat.class, self.getBoundingBox().inflate(16.0), EntitySelector.ENTITY_STILL_ALIVE);
                for (Cat cat : list) {
                    cat.hiss();
                }
                this.isScaredOfCat = !list.isEmpty();
            }
            return !this.isScaredOfCat;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = self.getTarget();
            if (livingEntity == null) {
                return;
            }
            // stun target
            if (self.hasLineOfSight(livingEntity)) {
                if (self.tickCount > stunTick && self.distanceTo(livingEntity) < 20) {
                    //projectile
                    if (!self.level().isClientSide()) {
                        Vec3 pos = self.getEyePosition();
                        double l = pos.x;
                        double m = pos.y;
                        double n = pos.z;
                        double o = self.getTarget().getX() - l;
                        double p = self.getTarget().getY(0.5) - m;
                        double q = self.getTarget().getZ() - n;
                        if (!self.isSilent()) {
                            self.playSound(SoundEvents.PHANTOM_SWOOP, 10.0f, 1.5f + self.getRandom().nextFloat() * 0.1f);
                        }
                        Shockwave shockwave = new Shockwave(self.level(), self, o, p, q);
                        shockwave.moveTo(l, m, n, 0.0f, 0.0f);
                        self.level().addFreshEntity(shockwave);
                    }
                    //stun here
                    stunTick = self.tickCount + RevampedPhantoms.instance().platform.config().ticksBetweenStunAttempts();
                }
            }
        }
    }

    public static class GrabPreyGoal extends Goal {

        final private Phantom self;
        public GrabPreyGoal(Phantom p) {
            self = p;
        }

        private boolean isScaredOfCat;
        private int catSearchTick;

        @SuppressWarnings("ConstantValue")
        @Override
        public boolean canUse() {
            boolean shouldOnlyCarry = self.getTarget() instanceof Animal;
            return self.getTarget() != null
                    && Accessors.getAttackPhase(self) == Accessors.getSwoopPhase()
                    && self.getFirstPassenger() == null
                    && !self.getTarget().isPassenger()
                    && !self.getTarget().isFallFlying()
                    && (((HasSharedGoals)self).revamped_phantoms$getGoalHolder().shouldGrab || shouldOnlyCarry);
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingEntity = self.getTarget();
            if (livingEntity == null) {
                return false;
            }
            if (!livingEntity.isAlive()) {
                return false;
            }
            if (livingEntity.isPassenger()) {
                return false;
            }
            if (livingEntity instanceof Player player) {
                if (livingEntity.isSpectator() || player.isCreative()) {
                    return false;
                }
            }
            if (!this.canUse()) {
                return false;
            }
            if (self.tickCount > this.catSearchTick) {
                this.catSearchTick = self.tickCount + 20;
                List<Cat> list = self.level().getEntitiesOfClass(Cat.class, self.getBoundingBox().inflate(16.0), EntitySelector.ENTITY_STILL_ALIVE);
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
            self.setTarget(null);
            Accessors.setAttackPhase(self, Accessors.getCirclePhase());
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = self.getTarget();
            if (livingEntity == null) {
                return;
            }
            Accessors.setMoveTargetPoint(self, new Vec3(livingEntity.getX(), livingEntity.getY(0.5), livingEntity.getZ()));
            if (self.getBoundingBox().inflate(0.2f).intersects(livingEntity.getBoundingBox()) && !livingEntity.hasPassenger(self)) {
                //Try to carry off
                livingEntity.startRiding(self);
                ((HasSharedGoals)self).revamped_phantoms$getGoalHolder().preyGrabbedY = self.getY();
                ((HasSharedGoals)self).revamped_phantoms$getGoalHolder().ticksSinceGrabbed = self.tickCount + 20*10;
                ((HasSharedGoals)self).revamped_phantoms$getGoalHolder().shouldGrab = false;
                Accessors.setAttackPhase(self, Accessors.getCirclePhase());
            } else if (self.horizontalCollision || self.hurtTime > 0 || livingEntity.hasPassenger(self)) {
                Accessors.setAttackPhase(self, Accessors.getCirclePhase());
            }
        }
    }

    public static class DropPreyGoal extends Goal {

        final private Phantom self;
        public DropPreyGoal(Phantom p) {
            self = p;
        }

        @Override
        public boolean canUse() {
            return self.getFirstPassenger() != null;
        }

        @Override
        public void tick() {
            Entity entity = self.getFirstPassenger();
            if (entity != null && (self.getY() > 30.0+((HasSharedGoals)self).revamped_phantoms$getGoalHolder().preyGrabbedY || self.tickCount > ((HasSharedGoals)self).revamped_phantoms$getGoalHolder().ticksSinceGrabbed)) {
                entity.stopRiding();
            }
        }
    }
}
