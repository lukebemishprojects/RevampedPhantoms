package revamped_phantoms;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import revamped_phantoms.client.ShockwaveRenderer;

public class EntityRendererSetup {
    public static void setupModels() {
        EnvExecutor.runInEnv(Env.CLIENT, ()->()->{
            EntityRendererRegistry.register(RevampedPhantoms.SHOCKWAVE::get, ShockwaveRenderer::new);
        });
    }
}
