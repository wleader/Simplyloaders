package ghostwolf.simplyloaders.tileentities;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityLoader extends TileEntityLoaderBase {
	
	@Override 
	public void tick() {
		TileEntity chest = getChest(); //checks if there is an inventory below the loader
		if (chest != null) {
			IItemHandler chestInventory = chest.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inputSide.getOpposite());
			if (chestInventory != null) { //gets the chests inventory	
				if (chestHasItem(chestInventory)) {
					
					updateRedstone(false);
					EntityMinecart cart = getChestCart (); //gets the minecart
					if (cart != null) {
						IItemHandler cartInventory = cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, outputSide.getOpposite()); //gets the carts inventory
						if (cartInventory != null ) {
								moveItem(chestInventory, cartInventory);
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
	
}
