package ghostwolf.simplyloaders.blocks;

import ghostwolf.simplyloaders.Config;
import ghostwolf.simplyloaders.ModGuiHandler;
import ghostwolf.simplyloaders.SimplyloadersMod;
import ghostwolf.simplyloaders.tileentities.TileEntityLoaderBase;
import ghostwolf.simplyloaders.tileentities.TileEntityUnloader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockUnloader extends BlockLoaderBase {
	
	public BlockUnloader() {
		super("unloader");
	}

	@Override
	public void onActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand
            , EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityLoaderBase) {
     	   if (player.isSneaking()) {
     		   ((TileEntityLoaderBase) te).setMinecartSide(side);
     	   }
     	   else
     	   {
     		   player.openGui(SimplyloadersMod.instance, ModGuiHandler.UNLOADER, world, pos.getX(), pos.getY(), pos.getZ());
     	   }
        }
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
	        		if ( side.getOpposite() == ((TileEntityLoaderBase) te).getMinecartSide()) {
	        			return 15;
	        		}
	        	}
	        } 
	    } 
		return 0;
	}
}
