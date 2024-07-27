package z3roco01.quickvillagers;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickVillagers implements ModInitializer {
    public static final String modid = "quick_villagers";
    public static final Logger logger = LoggerFactory.getLogger(modid);

    @Override
    public void onInitialize() {
        logger.info("init :3");
        QuickVillagerComponents.register();
    }
}