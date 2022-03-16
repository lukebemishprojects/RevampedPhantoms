package com.github.lukebemish.revamped_phantoms;

import com.github.lukebemish.revamped_phantoms.client.ShockwaveRenderer;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;

public class EntityRendererSetup {
    public static void setupModels() {
        EnvExecutor.runInEnv(Env.CLIENT, ()->()->{
            EntityRendererRegistry.register(RevampedPhantoms.SHOCKWAVE::get, ShockwaveRenderer::new);
        });
    }
}
