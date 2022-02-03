package revamped_phantoms.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Random;

public class Utils {
    public static boolean shouldSpawn(EntityType<? extends Monster> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, Random random) {
        return Monster.checkMonsterSpawnRules(type,level,reason,pos,random) && level.getSkyDarken() >= 4;
    }
}
