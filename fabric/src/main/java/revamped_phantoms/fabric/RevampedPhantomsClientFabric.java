package revamped_phantoms.fabric;

import net.fabricmc.api.ClientModInitializer;
import revamped_phantoms.EntityRendererSetup;
import revamped_phantoms.RevampedPhantomsClient;

public class RevampedPhantomsClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RevampedPhantomsClient.init();
        EntityRendererSetup.setupModels();
    }
}
