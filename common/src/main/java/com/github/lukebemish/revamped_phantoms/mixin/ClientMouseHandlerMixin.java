package com.github.lukebemish.revamped_phantoms.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.Options;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.github.lukebemish.revamped_phantoms.RevampedPhantoms;

@Mixin(value = MouseHandler.class, priority = 499)
public class ClientMouseHandlerMixin {
/*
    @Redirect(method = {"turnPlayer"},
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;smoothCamera:Z", opcode = Opcodes.GETFIELD))
    private boolean revamped_phantoms_replaceSmoothCamera(Options options) {
        if (Minecraft.getInstance().player != null) {
            if (Minecraft.getInstance().player.hasEffect(RevampedPhantoms.STUNNED_EFFECT.get())) {
                return true;
            }
        }
        return options.smoothCamera;
    }
 */
    @Unique
    public boolean revamped_phantoms$cachedSmoothCamera;

    @Inject(method = {"turnPlayer"},
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;smoothCamera:Z", opcode = Opcodes.GETFIELD, shift = At.Shift.BEFORE))
    private void revamped_phantoms_smoothCamHead(CallbackInfo ci) {
        Options options = Minecraft.getInstance().options;
        revamped_phantoms$cachedSmoothCamera = options.smoothCamera;
        options.smoothCamera = revamped_phantoms$cachedSmoothCamera ||
                (Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(RevampedPhantoms.STUNNED_EFFECT.get()));
    }

    @Inject(method = {"turnPlayer"},
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;smoothCamera:Z", opcode = Opcodes.GETFIELD, shift = At.Shift.AFTER))
    private void revamped_phantoms_smoothCamReturn(CallbackInfo ci) {
        Minecraft.getInstance().options.smoothCamera = revamped_phantoms$cachedSmoothCamera;
    }
}
