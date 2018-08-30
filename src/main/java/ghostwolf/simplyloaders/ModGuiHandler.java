package ghostwolf.simplyloaders;

import ghostwolf.simplyloaders.blocks.ContainerBase;
import ghostwolf.simplyloaders.blocks.GuiBase;
import ghostwolf.simplyloaders.init.ModBlocks;
import ghostwolf.simplyloaders.tileentities.TileEntityLoaderBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {

	public static final int LOADER = 0;
	public static final int UNLOADER = 1;
	
	
	@Override
	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case LOADER:
				return new ContainerBase(player.inventory, (TileEntityLoaderBase)world.getTileEntity(new BlockPos(x, y, z)));
			case UNLOADER:
				return new ContainerBase(player.inventory, (TileEntityLoaderBase)world.getTileEntity(new BlockPos(x, y, z)));			
			default:
				return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case LOADER:
				return new GuiBase(getServerGuiElement(ID, player, world, x, y, z), player.inventory, ModBlocks.loader.getUnlocalizedName());
			case UNLOADER:
				return new GuiBase(getServerGuiElement(ID, player, world, x, y, z), player.inventory, ModBlocks.unloader.getUnlocalizedName());			
			default:
				return null;
		}
	}
}
