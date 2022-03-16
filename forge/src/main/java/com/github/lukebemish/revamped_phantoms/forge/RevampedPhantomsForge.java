package com.github.lukebemish.revamped_phantoms.forge;

import com.github.lukebemish.revamped_phantoms.EntityRendererSetup;
import com.github.lukebemish.revamped_phantoms.RevampedPhantoms;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RevampedPhantoms.MOD_ID)
public class RevampedPhantomsForge {
    public RevampedPhantomsForge() {
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(RevampedPhantoms.MOD_ID, modbus);
        RevampedPhantoms.init();
        EntityRendererSetup.setupModels();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modbus.addListener(RevampedPhantomsClientForge::init));
    }
}
