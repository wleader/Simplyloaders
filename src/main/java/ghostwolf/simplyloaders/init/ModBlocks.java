package ghostwolf.simplyloaders.init;

import ghostwolf.simplyloaders.Reference;
import ghostwolf.simplyloaders.blocks.BlockLoader;
import ghostwolf.simplyloaders.blocks.BlockUnloader;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class ModBlocks {
	
	@GameRegistry.ObjectHolder(Reference.MOD_ID + ":" + BlockUnloader.BLOCK_ID)
	public static BlockUnloader unloader;
	
	@GameRegistry.ObjectHolder(Reference.MOD_ID + ":" + BlockLoader.BLOCK_ID)
	public static BlockLoader loader;
		
	public static void init () {
		unloader = new BlockUnloader();
		loader = new BlockLoader();
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(loader);
		event.getRegistry().registerAll(unloader);
	}

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(new ItemBlock(loader).setRegistryName(loader.getRegistryName()));
		event.getRegistry().registerAll(new ItemBlock(unloader).setRegistryName(unloader.getRegistryName()));
	}
	
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		registerRender(Item.getItemFromBlock(loader));
		registerRender(Item.getItemFromBlock(unloader));
	}
	
	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation( item.getRegistryName(), "inventory"));
	}
}
