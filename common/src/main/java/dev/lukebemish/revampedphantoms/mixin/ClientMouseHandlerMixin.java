package dev.lukebemish.revampedphantoms.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.lukebemish.revampedphantoms.RevampedPhantoms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = MouseHandler.class, priority = 499)
public class ClientMouseHandlerMixin {
    @Unique
    public boolean revamped_phantoms$cachedSmoothCamera;

    @ModifyExpressionValue(
        method = "turnPlayer()V",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;smoothCamera:Z", opcode = Opcodes.GETFIELD)
    )
    private boolean revamped_phantoms$smoothCam(boolean smoothCamera) {
        return smoothCamera || (Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(RevampedPhantoms.instance().stunned.get()));
    }
}
