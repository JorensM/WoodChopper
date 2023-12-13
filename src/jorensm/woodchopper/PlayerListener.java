package jorensm.woodchopper;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener {
	
	World world;
	WoodChopper plugin;
	
	PlayerListener(WoodChopper _plugin) {
		plugin = _plugin;
		world = Bukkit.getWorlds().get(0);
	}
	
	private boolean isBit(Material block_type) {
		if(
			block_type == Material.GOLD_BLOCK ||
			block_type == Material.DIAMOND_BLOCK ||
			block_type == Material.EMERALD_BLOCK
		) {
			return true;
		}
		return false;
	}
	
	/**
	 * Activates chopper at given position. Removes the bit and chest blocks and spawns the chopper
	 */
	private void activateChopper(Location bit_location) {
		Bukkit.broadcastMessage("Activating chopper");
		Bukkit.broadcastMessage("Removing blocks");
		Block bit_block = world.getBlockAt(bit_location);
		
		ChopperType chopper_type = ChopperType.DIAMOND;
		
		if (bit_block.getType() == Material.EMERALD_BLOCK) {
			chopper_type = ChopperType.EMERALD;
		} else if (bit_block.getType() == Material.GOLD_BLOCK) {
			chopper_type = ChopperType.GOLD;
		}
		
		bit_block.setType(Material.AIR);
		
		Block chest_block = ChopperUtil.getOffsetBlock(bit_block, new Vector(0, 1, 0), world);
		chest_block.setType(Material.AIR);
		plugin.spawnChopper(world, bit_location, chopper_type);
	}
	
	/**
	 * Checks when a chopper structure has been built to activate the chopper
	 */
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Bukkit.getLogger().info("Placed block");
		Bukkit.broadcastMessage("Placed block");
		
		Block block = event.getBlockPlaced();
		Material block_type = event.getBlockPlaced().getType();
		
		if(block_type == Material.CHEST) {
			Bukkit.broadcastMessage("Placed chest");
			Block block_below = ChopperUtil.getOffsetBlock(block, new Vector(0, -1, 0), world);
			Bukkit.broadcastMessage("Block below is " + block_below.getType().name());
			if(isBit(block_below.getType())) {
				activateChopper(block_below.getLocation());
			}
		} else if (isBit(block_type)) {
			Bukkit.broadcastMessage("Placed bit");
			Block block_above = ChopperUtil.getOffsetBlock(block, new Vector(0, 1, 0), world);
			Bukkit.broadcastMessage("Block above is " + block_above.getType().name());
			if(block_above.getType() == Material.CHEST) {
				activateChopper(block.getLocation());
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Bukkit.broadcastMessage("Interacting with entity");
		
		Entity entity = event.getRightClicked();
		Player player = event.getPlayer();
		
		if(entity.hasMetadata("chopper_id")) {
			Bukkit.broadcastMessage("Interacting with Chopper");
			
			String chopper_id = entity.getMetadata("chopper_id").get(0).asString();
			
			ChopperEntity chopper = plugin.getChopperById(chopper_id);
			
			player.openInventory(chopper.getInventory());
		}
	}
}
