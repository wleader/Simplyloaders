package ghostwolf.simplyloaders.blocks;


import java.util.List;

import ghostwolf.simplyloaders.Config;
import ghostwolf.simplyloaders.ModGuiHandler;
import ghostwolf.simplyloaders.SimplyloadersMod;
import ghostwolf.simplyloaders.tileentities.TileEntityLoader;
import ghostwolf.simplyloaders.tileentities.TileEntityLoaderBase;
import net.minecraft.block.properties.PropertyInteger;
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

public class BlockLoader extends BlockLoaderBase {

	public static final String BLOCK_ID = "loader";
	
	public static final PropertyInteger inputSide = PropertyInteger.create("inputside", 0, 6);
	public static final PropertyInteger outputSide = PropertyInteger.create("outputside", 0, 6);
	
	public BlockLoader() {
		super(BLOCK_ID);
	}
	
	@Override
	public void onActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand
            , EnumFacing side, float hitX, float hitY, float hitZ) {
		  TileEntity te = world.getTileEntity(pos);
          if (te instanceof TileEntityLoaderBase) {
       	   if (player.isSneaking()) {
       		   ((TileEntityLoaderBase) te).setInputSide(side);
       	   } else {
       		   ((TileEntityLoaderBase) te).setOutputSide(side);
       	   }
          }
          
          player.openGui(SimplyloadersMod.instance, ModGuiHandler.LOADER, world, pos.getX(), pos.getY(), pos.getZ());
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityLoader();
	}
	
	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		TileEntity te = blockAccess.getTileEntity(pos);
	    if (te instanceof TileEntityLoaderBase) {
	        if (((TileEntityLoaderBase) te).isEmittingRedstone ) {
        		if (Config.LoaderEmitsToAllNearbyBlocks) {
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
		tooltip.add("\u00A77" + "Place loader under rails, to load items into passing carts");
		tooltip.add("\u00A77" + "Place chest below loader, with the items you want to load");
		tooltip.add("");
		tooltip.add("\u00A77" + "Right click with empty hand to change output");
		tooltip.add("\u00A77" + "Shift-Right click with empty hand to change input");
	}
	
}
