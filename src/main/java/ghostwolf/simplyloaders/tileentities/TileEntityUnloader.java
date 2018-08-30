package ghostwolf.simplyloaders.tileentities;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityUnloader extends TileEntityLoaderBase {
	
	@Override 
	public void tick() {
		IItemHandler myInventory = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);  
		EntityMinecart cart = getChestCart (); //gets the minecart
		if (cart != null) {
			IItemHandler cartInventory = cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, minecartSide.getOpposite()); //gets the carts inventory
			if (cartInventory != null) {
				if (chestHasItem(cartInventory)) {
					updateRedstone(false);
					moveItem(cartInventory, myInventory);
				} else {
					updateRedstone(true);
				}
			} else {
				//carts has no chest 
				updateRedstone(true);
			}
		}
	}

}
