package jorensm.woodchopper;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class ChopperUtil {
	
	/**
	 * Get the block `offset` units away from specified block
	 */
	public static Block getOffsetBlock(Block block, Vector offset, World world) {
		Location offset_location = block.getLocation().add(offset);
		return world.getBlockAt(offset_location);
	}
	
	/**
	 * Gets blocks whose type strings contain provided string, within given radius around location
	 */
	public static ArrayList<Block> getBlocksInRadius(String type_string, World world, Location location, int radius) {
		ArrayList<Block> found_blocks = new ArrayList<Block>();
        for (int y = 1; y > -radius; y--) {
            for (int x = 1; x > -radius; x--) {
                for (int z = 1; z > -radius; z--) {
                    Block scan = world
                            .getBlockAt(
                            (int) location
                            .getX()
                            + x,
                            (int) location
                            .getY()
                            + y,
                            (int) location
                            .getZ()
                            + z);
                    if(scan.getType().name().contains(type_string)) {
                    	found_blocks.add(scan);
                    }
                }
            }
        }
 
        return found_blocks;
    }
	
	/**
	 * Get nearest block to location out of an array of blocks
	 */
	public static Block getNearestBlock(ArrayList<Block> blocks, Location location) {
		if(blocks.size() == 0) {
			throw new Error("Blocks count is 0");
		}
		Block nearest_block = blocks.get(0);
		double nearest_distance = location.distance(nearest_block.getLocation());
		
		for(int i = 0; i < blocks.size(); i++) {
			Block current_block = blocks.get(i);
			
			double curr_distance = location.distance(current_block.getLocation());
			
			if(curr_distance < nearest_distance) {
				nearest_block = current_block;
				nearest_distance = curr_distance;
			}
		}
		
		return nearest_block;
	}
	
	/**
	 * Gets the blocks of a tree based on a passed wood block
	 */
	public static ArrayList<Block> getTreeBlocks(Block log_block, World world) {
		if(!log_block.getType().name().contains("LOG")) {
			Bukkit.broadcastMessage("Specified block is not a log block!");
			throw new Error("Specified block is not a log block!");
		}
		
		ArrayList<Block> found_blocks = getBlocksInRadius("LOG", world, log_block.getLocation(), 5);
		
		return found_blocks;
	}
	
	/**
	 * Returns random int within range
	 */
	public static int randomIntRange(int start, int end) {
	    Random random = new Random();
	    int number = random.nextInt((end - start) + 1) + start; // see explanation below
	    return number;
	}

}
