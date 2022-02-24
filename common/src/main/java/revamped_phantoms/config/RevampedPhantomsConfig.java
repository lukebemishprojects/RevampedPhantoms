package revamped_phantoms.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import dev.architectury.platform.Platform;
import revamped_phantoms.RevampedPhantoms;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RevampedPhantomsConfig {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Path CONFIG_PATH = Platform.getConfigFolder();
    public static final String FULL_PATH = CONFIG_PATH.toString() + "/"+ RevampedPhantoms.MOD_ID+".json";

    private boolean phantomsAttackAnimals = true;
    private boolean phantomsAttackVillagers = true;
    private float phantomElytraPursueModifier = 8.0f;
    private boolean phantomsStunPrey = true;
    private int ticksBetweenStunAttempts = 20*10;
    private int ticksStunDuration = 6*20;
    private boolean phantomsGrabPrey = true;
    private boolean doDaylightSpawns = true;
    private List<String> phantom_attack_blacklist = new ArrayList<>();

    private static RevampedPhantomsConfig load() {
        RevampedPhantomsConfig config = new RevampedPhantomsConfig();
        try {
            checkExistence();
            config = GSON.fromJson(new FileReader(FULL_PATH), RevampedPhantomsConfig.class);
            save(config);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            RevampedPhantoms.LOGGER.error("Could not load config (it may be out of date). Using default config.");
        }
        return config;
    }

    public static RevampedPhantomsConfig get() {
        RevampedPhantomsConfig config = load();
        if (config.phantomElytraPursueModifier < 1)
            config.phantomElytraPursueModifier = 1f;
        if (config.phantomElytraPursueModifier > 32)
            config.phantomElytraPursueModifier = 32f;
        return config;
    }

    public static void save(RevampedPhantomsConfig config) {
        try {
            checkExistence();
            FileWriter writer = new FileWriter(FULL_PATH);
            GSON.toJson(config, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkExistence() throws IOException {
        if (!Files.exists(CONFIG_PATH)) Files.createDirectories(CONFIG_PATH);
        if (!Files.exists(Paths.get(FULL_PATH))) {
            Files.createFile(Paths.get(FULL_PATH));
            save(new RevampedPhantomsConfig());
        }
    }

    public boolean isPhantomsStunPrey() {
        return phantomsStunPrey;
    }

    public boolean isPhantomsGrabPrey() {
        return phantomsGrabPrey;
    }

    public float getPhantomElytraPursueModifier() {
        return phantomElytraPursueModifier;
    }

    public boolean isPhantomsAttackVillagers() {
        return phantomsAttackVillagers;
    }

    public boolean isPhantomsAttackAnimals() {
        return phantomsAttackAnimals;
    }

    public boolean isDoDaylightSpawns() {
        return doDaylightSpawns;
    }

    public int getTicksBetweenStunAttempts() {
        return ticksBetweenStunAttempts;
    }

    public int getTicksStunDuration() {
        return ticksStunDuration;
    }

    public List<String> getPhantomAttackBlacklist() {
        return phantom_attack_blacklist;
    }
}
