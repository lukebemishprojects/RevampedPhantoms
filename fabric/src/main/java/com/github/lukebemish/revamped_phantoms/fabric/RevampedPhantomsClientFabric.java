package com.github.lukebemish.revamped_phantoms.fabric;

import com.github.lukebemish.revamped_phantoms.EntityRendererSetup;
import com.github.lukebemish.revamped_phantoms.RevampedPhantomsClient;
import net.fabricmc.api.ClientModInitializer;

public class RevampedPhantomsClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RevampedPhantomsClient.init();
        EntityRendererSetup.setupModels();
    }
}
