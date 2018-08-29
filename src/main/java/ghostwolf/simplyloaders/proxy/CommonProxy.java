package ghostwolf.simplyloaders.proxy;

import java.io.File;

import ghostwolf.simplyloaders.Config;
import ghostwolf.simplyloaders.Reference;
import ghostwolf.simplyloaders.init.ModBlocks;
import ghostwolf.simplyloaders.init.ModTileEntities;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber
public abstract class CommonProxy {
	
    public static Configuration config;
	
    public void preInit(FMLPreInitializationEvent e) {
    	File directory = e.getModConfigurationDirectory();
    	config = new Configuration(new File(directory.getPath(), Reference.ConfigFile));
    	Config.readConfig();
	
		ModBlocks.init();
		ModTileEntities.init();
    }

	public void init(FMLInitializationEvent e) {
        // nothing to do, but this method must be present.
    }
	
    public void postInit(FMLPostInitializationEvent e) {
    	if (config.hasChanged()) {
			config.save();
        }
    }

}
