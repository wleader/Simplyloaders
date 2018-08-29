package ghostwolf.simplyloaders.init;

import ghostwolf.simplyloaders.Reference;
import ghostwolf.simplyloaders.tileentities.TileEntityLoader;
import ghostwolf.simplyloaders.tileentities.TileEntityUnloader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {

	public static void init () {
		// TODO - figure out what the correct replacement code for this  deprecated method is.
		// Its probably a @SubscribeEvent on a RegistryEvent.Register<EntityEntry> ? 
		GameRegistry.registerTileEntity(TileEntityUnloader.class, Reference.MOD_ID + ":TileEntityUnloader");
		GameRegistry.registerTileEntity(TileEntityLoader.class, Reference.MOD_ID + ":TileEntityLoader");
	}
}
