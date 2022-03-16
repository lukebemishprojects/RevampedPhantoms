package com.github.lukebemish.revamped_phantoms.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;

import java.util.Random;

public class SpawningUtils {
    public static boolean shouldSpawn(EntityType<? extends Monster> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, Random random) {
        return Monster.checkMonsterSpawnRules(type,level,reason,pos,random) && !level.collidesWithSuffocatingBlock(null, new AABB(pos, pos.atY(level.getMaxBuildHeight()).offset(1,0,1)));
    }
}
