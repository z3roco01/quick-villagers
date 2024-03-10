package z3roco01.quickvillagers

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object QuickVillagers : ModInitializer {
	val modid = "quick_villagers"
    val logger = LoggerFactory.getLogger(modid)

	override fun onInitialize() {
		logger.info("init started :3")

		logger.info("init finished !! :D")
	}
}