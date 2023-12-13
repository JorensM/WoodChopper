package jorensm.woodchopper;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Chest;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

/* 
 * Wrapper for a chicken entity with custom wood-chopping behavior
 */
public class ChopperEntity {
	Mob entity; //The entity of the chopper
	
	LivingEntity target; //Target entity that the chopper will follow. Since Bukkit doesn't have pathfinding support, have to use this hack.
	
	World world;
	
	String id;
	
	Inventory inventory;
	
	WoodChopper plugin;
	
	int cooldown = 1;
	
	int[] yield = {1, 1};
	
	int current_cooldown = 0;
	
	ChopperType type;
	
	ChopperEntity(WoodChopper _plugin, World _world, ChopperType _type) {
		Bukkit.broadcastMessage("Constructing Chopper");
		type = _type;
		plugin = _plugin;
		id = UUID.randomUUID().toString();
		inventory = Bukkit.createInventory(null, 9, "Chopper Inventory");
		world = _world;
		
		if(type == ChopperType.EMERALD) {
			cooldown = 2;
		} else if (type == ChopperType.GOLD) {
			cooldown = 3;
			yield[1] = 4;
		}
	}
	
	ArrayList<Block> scanned_tree = new ArrayList<Block>();
	
	boolean is_chopping = false;
	
	/*
	 * Spawn entity in the world
	 */
	void spawn(Location location) {
		Bukkit.broadcastMessage("Spawning entity");
		Entity temp_entity = world.spawnEntity(location, EntityType.ZOMBIE);
		if(temp_entity instanceof Mob) {
			entity = (Mob) temp_entity;
		} else {
			Bukkit.broadcastMessage("Could not cast entity");
		}
		
		entity.setVisualFire(false);
		entity.setInvulnerable(true);
		entity.setCustomName("Wood Chopper");
		entity.setCustomNameVisible(true);
		entity.setMetadata("chopper_id", new FixedMetadataValue(plugin, id));
		
		Entity temp_target = world.spawnEntity(location, EntityType.CHICKEN);
		if(temp_target instanceof LivingEntity) {
			target = (LivingEntity) temp_target;
		} else {
			Bukkit.broadcastMessage("Could not cast entity target");
		}
		target.setInvisible(true);
		target.setInvulnerable(true);
		target.setAI(false);
		target.setGravity(false);
		int distance = 6;
		Bukkit.broadcastMessage("Setting target " + String.valueOf(distance) + " blocks away");
		target.teleport(location.add(new Vector(6, 0, 0)));
		entity.setTarget(target);
		
		Bukkit.broadcastMessage("Target is " + entity.getTarget().getName());
		
	}
	
	/**
	 * Scans the surroundings for log blocks.
	 * Adds scanned tree to scanned_tree variable
	 * Returns true if there was a tree found, false otherwise
	 */
	boolean scanForLogs() {
		Bukkit.broadcastMessage("Scanning for logs");
		ArrayList<Block> found_blocks = ChopperUtil.getBlocksInRadius("LOG", world, entity.getLocation(), 16);
		Block nearest_block = ChopperUtil.getNearestBlock(found_blocks, entity.getLocation());
		scanned_tree = ChopperUtil.getTreeBlocks(nearest_block, world);
		if(scanned_tree.size() > 0) {
			return true;
		}
		return false;
	}
	
	boolean isNearTargetTree() {
		int radius = 4;
		boolean is_near = false;
		for(int i = 0; i < scanned_tree.size(); i++) {
			Block tree_block = scanned_tree.get(i);
			
			if(tree_block.getLocation().distance(entity.getLocation()) < radius) {
				is_near = true;
				break;
			}
		}
		
		return is_near;
	}
	
	void chopScannedTree() {
		Bukkit.broadcastMessage("Chopping scanned tree");
		current_cooldown += 1;
		if(current_cooldown < cooldown) {
			Bukkit.broadcastMessage("Skipping chop because of cooldown");
			return;
		}
		if(scanned_tree.size() > 0) {
			Block block = scanned_tree.remove(0);
			
			ItemStack stack = new ItemStack(block.getType(), ChopperUtil.randomIntRange(yield[0], yield[1]));
			
			block.setType(Material.AIR);
			
			inventory.addItem(stack);
		} else {
			Bukkit.broadcastMessage("No tree to chop");
		}
	}
	
	void moveToScannedTree() {
		moveTo(scanned_tree.get(0).getLocation());
	}
	
	void moveTo(Location location) {
		target.teleport(location);
	}
	
	void setIsChopping(boolean _chopping) {
		is_chopping = _chopping;
	}
	
	Inventory getInventory() {
		return inventory;
	}
	
	boolean getIsChopping() {
		return is_chopping;
	}

}
