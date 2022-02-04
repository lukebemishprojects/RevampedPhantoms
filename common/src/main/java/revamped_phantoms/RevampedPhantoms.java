package revamped_phantoms;

import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import revamped_phantoms.entity.Shockwave;

import java.util.function.Supplier;

public class RevampedPhantoms {
    public static final String MOD_ID = "revamped_phantoms";
    private static final ResourceLocation shockwave_id = new ResourceLocation(MOD_ID, "shockwave");
    // We can use this if we don't want to use DeferredRegister
    public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(MOD_ID, Registry.MOB_EFFECT_REGISTRY);
    public static final RegistrySupplier<MobEffect> STUNNED_EFFECT = MOB_EFFECTS.register("stunned", () ->
            new StunnedEffect(MobEffectCategory.HARMFUL, 0x795a94).addAttributeModifier(Attributes.MOVEMENT_SPEED, "9b23836a-f1c3-3e21-98e4-7ae212063415", -0.15f,AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(MOD_ID, Registry.ENTITY_TYPE_REGISTRY);
    public static final RegistrySupplier<EntityType<Shockwave>> SHOCKWAVE = ENTITY_TYPES.register(shockwave_id, () -> EntityType.Builder.of((EntityType<Shockwave> et, Level l) -> new Shockwave(et, l), MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(4).updateInterval(10).build(shockwave_id.toString()));

    public static void init() {
        MOB_EFFECTS.register();
        ENTITY_TYPES.register();
    }
}
