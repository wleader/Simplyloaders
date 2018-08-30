package ghostwolf.simplyloaders.tileentities;


import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import ghostwolf.simplyloaders.Config;
import ghostwolf.simplyloaders.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileEntityLoaderBase extends TileEntity implements ICapabilityProvider, ITickable{
	
	public boolean isEmittingRedstone = false;
	public int transferRate = Config.LoaderTransferRate;
	
	private ItemStackHandler itemStackHandler;
	
	public TileEntityLoaderBase() {
		itemStackHandler = new ItemStackHandler(9);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return (oldState.getBlock() != newState.getBlock());
	}
	
	public EnumFacing minecartSide = EnumFacing.NORTH;
		
//	public TileEntity getChestRemove () {
//		
//		if (inputSide == null) {
//			return null;
//		}
//		
//		TileEntity Te = null;
//		
//		if (inputSide == EnumFacing.UP) {
//			 Te = getWorld().getTileEntity(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()));
//		} else if (inputSide == EnumFacing.DOWN) {
//			 Te = getWorld().getTileEntity(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()));
//		} else if (inputSide == EnumFacing.NORTH) {
//			 Te = getWorld().getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1));
//		} else if (inputSide == EnumFacing.SOUTH) {
//			 Te = getWorld().getTileEntity(new BlockPos(pos.getX(), pos.getY() , pos.getZ() + 1));
//		}else if (inputSide == EnumFacing.WEST) {
//			 Te = getWorld().getTileEntity(new BlockPos(pos.getX() - 1, pos.getY() , pos.getZ()));
//		} else {
//			Te = getWorld().getTileEntity(new BlockPos(pos.getX() + 1, pos.getY() , pos.getZ()));
//		}
//		
//		if (Te != null) {
//			IItemHandler itemHandler = Te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inputSide.getOpposite());
//			if (itemHandler != null) {
//				return Te;
//			}
//		}
//		if (! isEmittingRedstone) {
//			updateRedstone(true);
//		}
//		return null;
//	}
	
	public boolean chestHasItem (IItemHandler inventory) {
		for (int i = 0; i < inventory.getSlots(); i++) {
			ItemStack inslot = inventory.getStackInSlot(i);
			if (inslot != null && ! inslot.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public EntityMinecart getChestCart () {
		if (minecartSide == null) {
			return null;
		}
		AxisAlignedBB box = new AxisAlignedBB(getPos(),getPos());

		if (minecartSide == EnumFacing.UP) {
			box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1);
		} else if (minecartSide == EnumFacing.DOWN) {
			box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() - 1, pos.getZ() + 1);
		} else if (minecartSide == EnumFacing.NORTH) {
			box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() -1);
		} else if (minecartSide == EnumFacing.SOUTH) {
			box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 2);
		}else if (minecartSide == EnumFacing.WEST) {
			box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() - 1, pos.getY() + 1 , pos.getZ() + 1);
		} else  {
			box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 2, pos.getY() + 1, pos.getZ() + 1);
		}
	

		List<EntityMinecart> L = getWorld().getEntitiesWithinAABB(EntityMinecart.class, box);
		if (! L.isEmpty()) {
			EntityMinecart cart = L.get(0);
			return cart;
		}
		return null;
	}
	
	//chests represents input chest and cart represents output chest so its moves items from value1 to value2
	public void moveItem (IItemHandler inventory, IItemHandler cart) {
		
		boolean itemMoved = false;
		for (int i = 0; i < inventory.getSlots(); i++) {
			if (! inventory.getStackInSlot(i).isEmpty()) {
				// found item
				
				ItemStack extractedItems = inventory.extractItem(i, transferRate, true);
				//Attempt to add the item to the cart until its successful, if its fails assume cart is full and enable redstone
				for (int x = 0; x < cart.getSlots(); x++) {
					ItemStack insertedItems = cart.insertItem(x, extractedItems, true);
					if (insertedItems.isEmpty()) {
						//insertion successful proceed to add item
						ItemStack exI = inventory.extractItem(i, transferRate, false);
						cart.insertItem(x, exI, false);
						itemMoved = true;
						break;
					}
				} 
			} if (itemMoved) {
				//if an item was moved  break loop to prevent multiple stacks being  transfered per tick
				break;
			} 
		} if (! itemMoved) {
			//cart is full
			updateRedstone(true);
		}
	}
	
	public void updateRedstone (boolean value) {
		if (! getWorld().isRemote) {
			if (isEmittingRedstone != value) {
				isEmittingRedstone = value;
				updateNeighbors();
			}
		}
	}

	@Override
	public void update() {
		if (! getWorld().isRemote) {
			tick();
		}
	}
	
	//override this for update function
	public void tick () {
		
	}
	
	public void setTransferRate (int rate) {
		transferRate = rate;
	}
	
	public void setMinecartSide (EnumFacing side) {
		if (side != minecartSide) {
			minecartSide = side;
			updateNeighbors();
			markDirty();
			setBlockState();
		}

	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
	    super.readFromNBT(compound);
	    if (compound.getString("minecartside") != "none") {
	    	minecartSide = EnumFacing.byName(compound.getString("minecartside"));
	    } else {
	       	minecartSide = null;
	    }
	        
	    itemStackHandler.deserializeNBT(compound.getCompoundTag("ItemStackHandler"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
	    super.writeToNBT(compound);
	    if (minecartSide != null) {
	        compound.setString("minecartside", minecartSide.toString());
	    } else {
	    	compound.setString("minecartside", "none"); 
	    }
	       
	        
	    compound.setTag("ItemStackHandler", itemStackHandler.serializeNBT());
	        	        
	    return compound;
	}
	    
    public EnumFacing getMinecartSide () {
    	return minecartSide;
    }
	    
    private void updateNeighbors () {
    	getWorld().notifyNeighborsOfStateChange(getPos(), ModBlocks.loader, false);
    }
	    
	public int minecartSideToInt () {
    	if (minecartSide == null) {
    		return 0;
    	} else if (minecartSide == EnumFacing.UP) {
    		return 1;
    	} else if (minecartSide == EnumFacing.DOWN) {
    		return 2;
    	} else if (minecartSide == EnumFacing.EAST) {
    		return 3;
    	} else if (minecartSide == EnumFacing.WEST) {
    		return 4;
    	} else if (minecartSide == EnumFacing.SOUTH) {
    		return 5;
    	} else if (minecartSide == EnumFacing.NORTH) {
    		return 6;
    	}
    	
    	return 0;
    }
	    
    public void setBlockState() {    	
    	IBlockState state = getWorld().getBlockState(getPos());
    	getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }
    
    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
    	readFromNBT(tag);
    	setBlockState();
    	world.markBlockRangeForRenderUpdate(pos, pos);
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
    	return writeToNBT(new NBTTagCompound());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    	super.onDataPacket(net, pkt);
    	handleUpdateTag(pkt.getNbtCompound());
    }
 
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
         return new SPacketUpdateTileEntity(getPos(), 1, getUpdateTag());
    }
	    
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
    	if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
    		return true;
    	}
    	
    	return super.hasCapability(capability, facing);
    }

    
	@Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
    	if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
    
    		@SuppressWarnings("unchecked")
    		T result = (T)itemStackHandler;
    		
    		return result;
    	}
    	return super.getCapability(capability, facing);
    }
	    
}
