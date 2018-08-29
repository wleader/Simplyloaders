package ghostwolf.simplyloaders.tileentities;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityUnloader extends TileEntityLoaderBase {
	
	@Override 
	public void tick() {
		
		TileEntity chest = getChest(); //checks if there is an inventory below the loader
		if (chest != null) {
			IItemHandler chestInventory = chest.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inputSide.getOpposite());
			if (chestInventory != null) { //gets the chests inventory
				
					EntityMinecart cart = getChestCart (); //gets the minecart
					if (cart != null) {
						IItemHandler cartInventory = cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, outputSide.getOpposite()); //gets the carts inventory
						if (cartInventory != null) {
							if (chestHasItem(cartInventory)) {
								updateRedstone(false);
								moveItem(cartInventory, chestInventory);
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
		
	}

}
