package ghostwolf.simplyloaders.tileentities;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityLoader extends TileEntityLoaderBase {
	
	@Override 
	public void tick() {
		IItemHandler myInventory = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null); 
		if (myInventory != null) {
			if (chestHasItem(myInventory)) {
				updateRedstone(false);
				EntityMinecart cart = getChestCart (); //gets the minecart
				if (cart != null) {
					IItemHandler cartInventory = cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, minecartSide.getOpposite()); //gets the carts inventory
					if (cartInventory != null ) {
						moveItem(myInventory, cartInventory);
					} else {
						//carts has no chest 
						updateRedstone(true);
					}
				}
			} else {
				//chest is empty enable signal
				updateRedstone(true);
			}
		}
	}
	
}
