package ghostwolf.simplyloaders.blocks;

import java.util.List;

import ghostwolf.simplyloaders.Config;
import ghostwolf.simplyloaders.ModGuiHandler;
import ghostwolf.simplyloaders.SimplyloadersMod;
import ghostwolf.simplyloaders.tileentities.TileEntityLoaderBase;
import ghostwolf.simplyloaders.tileentities.TileEntityUnloader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockUnloader extends BlockLoaderBase {
	
	public static final String BLOCK_ID = "unloader";
	
	public BlockUnloader() {
		super(BLOCK_ID);
	}

	@Override
	public void onActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand
            , EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityLoaderBase) {
     	   if (player.isSneaking()) {
     		   ((TileEntityLoaderBase) te).setOutputSide(side);
     	   } else {
     		   ((TileEntityLoaderBase) te).setInputSide(side);     	      
     	   }
        }
        
        player.openGui(SimplyloadersMod.instance, ModGuiHandler.LOADER, world, pos.getX(), pos.getY(), pos.getZ());
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityUnloader();
	}
	
	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		
		TileEntity te = blockAccess.getTileEntity(pos);
	    if (te instanceof TileEntityLoaderBase) {
	        if (((TileEntityLoaderBase) te).isEmittingRedstone ) {
        		if (Config.UnloaderEmitsToAllNearbyBlocks) {
	        		return 15;
	        	} else {
	        		if ( side.getOpposite() == ((TileEntityLoaderBase) te).getOutputSide()) {
	        			return 15;
	        		}
	        	}
	        } 
	    } 
		return 0;
	}
	
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add("\u00A77" + "Place unloader under rails, to unload items from passing carts");
		tooltip.add("\u00A77" + "Place chest below unloader, to store the unloaded items in");
		tooltip.add("");
		tooltip.add("\u00A77" + "Right click with empty hand to change output");
		tooltip.add("\u00A77" + "Shift-Right click with empty hand to change input");
	}
}
