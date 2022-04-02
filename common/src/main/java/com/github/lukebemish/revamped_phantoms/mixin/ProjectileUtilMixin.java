package com.github.lukebemish.revamped_phantoms.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin {
    @Unique private static double revamped_phantoms$cached_D;
    @Unique private static Entity revamped_phantoms$cached_E;
    @Unique private static Vec3   revamped_phantoms$cached_V;

    @Inject(method = "getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;",
            at=@At("HEAD"))
    private static void revamped_phantoms$reset(Entity shooter, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter, double distance, CallbackInfoReturnable<EntityHitResult> cir) {
        revamped_phantoms$cached_D = 0D;
        revamped_phantoms$cached_E = null;
        revamped_phantoms$cached_V = null;
    }

    @Inject(method="getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;",
            at=@At(value = "INVOKE_ASSIGN",target = "Lnet/minecraft/world/phys/AABB;clip(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;)Ljava/util/Optional;",shift = At.Shift.AFTER),locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void revamped_phantoms$allowPhantomAttack(Entity shooter, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter, double distance, CallbackInfoReturnable<EntityHitResult> cir, Level level, double d, Entity entity, Vec3 vec3, Iterator<Entity> var12, Entity entity2, AABB aABB, Optional<Vec3> optional) {
        if (entity2.getRootVehicle() == shooter.getRootVehicle() && entity2.getType()== EntityType.PHANTOM) {
            if (optional.isPresent()) {
                Vec3 vec32 = (Vec3)optional.get();
                double e = startVec.distanceToSqr(vec32);
                if ((e < d || d == 0D)&&(e < revamped_phantoms$cached_D || revamped_phantoms$cached_D==0D)) {
                    revamped_phantoms$cached_D = e;
                    revamped_phantoms$cached_E = entity2;
                    revamped_phantoms$cached_V = vec32;
                }
            }
        }
    }

    @ModifyArgs(method = "getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;",
                at=@At(value = "INVOKE",target = "Lnet/minecraft/world/phys/EntityHitResult;<init>(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;)V"))
    private static void revamped_phantoms$modifyHit(Args args, Entity shooter, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter, double distance) {
        Vec3 orig = args.get(1);
        if (revamped_phantoms$cached_D != 0D && startVec.distanceToSqr(orig) > revamped_phantoms$cached_D) {
            args.set(0,revamped_phantoms$cached_E);
            args.set(1,revamped_phantoms$cached_V);
        }
    }

    @Inject(method = "getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;",
            at=@At("RETURN"),cancellable = true)
    private static void revamped_phantoms$modifyNullResult(Entity shooter, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter, double distance, CallbackInfoReturnable<EntityHitResult> cir) {
        if (cir.getReturnValue() == null && revamped_phantoms$cached_D != 0D) {
            cir.setReturnValue(new EntityHitResult(revamped_phantoms$cached_E,revamped_phantoms$cached_V));
        }
    }
}
