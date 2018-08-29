package ghostwolf.simplyloaders.blocks;

import ghostwolf.simplyloaders.Reference;
import ghostwolf.simplyloaders.SimplyloadersMod;
import ghostwolf.simplyloaders.tileentities.TileEntityLoaderBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class BlockLoaderBase extends Block  {
	
	public static final PropertyInteger inputSide = PropertyInteger.create("inputside", 0, 6);
	public static final PropertyInteger outputSide = PropertyInteger.create("outputside", 0, 6);

	public BlockLoaderBase(String name) {
		super(Material.IRON);
		setHarvestLevel("pickaxe", 0);
		setUnlocalizedName(Reference.MOD_ID + "." + name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.TRANSPORTATION);
		setHardness(1.2F);
		setResistance(10F);
		setSoundType(SoundType.METAL);	
		
		setDefaultState(blockState.getBaseState());
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}
	
	@Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    	TileEntity te = worldIn.getTileEntity(pos);
     	if (te instanceof TileEntityLoaderBase) {
     		return state.withProperty(inputSide, ((TileEntityLoaderBase) te).inputSideToInt() ).withProperty(outputSide, ((TileEntityLoaderBase) te).outputSideToInt());
 
     	} else {
     		return state;
     	}
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, inputSide, outputSide);
    }
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
       updateState(state, pos, world);
    }
    
   @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
       updateState(state, pos, worldIn);
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
    	return EnumBlockRenderType.MODEL;
    }
    
    public void updateState (IBlockState state, BlockPos pos, World world) {
    		TileEntity te = world.getTileEntity(pos);
         	if (te instanceof TileEntityLoaderBase) {
         		world.setBlockState(pos, getActualState(state, world, pos) ) ;
         	}
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
    		EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

	   if (! worldIn.isRemote) {
		   // handle stuff server side
		   onActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	   }
	   
	   return true;
    }
    
    public void onActivated (World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
    		EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    	//override this to handle on clicked
    }
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    	TileEntity te = worldIn.getTileEntity(pos);
    	IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    	int slots = itemHandler.getSlots();
    	for(int slot = 0; slot < slots -1; slot++) {
    		ItemStack stack = itemHandler.getStackInSlot(slot);
    		InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
    	}
    	
    	super.breakBlock(worldIn, pos, state);
    }
    
}
