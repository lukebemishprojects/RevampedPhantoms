package revamped_phantoms.mixin;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemCooldowns.CooldownInstance.class)
public interface CooldownInstanceGetter {
    @Accessor
    int getStartTime();
    @Accessor
    int getEndTime();
    @Invoker("<init>")
    static ItemCooldowns.CooldownInstance invokeNew(int i, int j) {
        throw new AssertionError("Untransformed accessor");
    }
}
