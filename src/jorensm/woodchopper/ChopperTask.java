package jorensm.woodchopper;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class ChopperTask extends BukkitRunnable {
	
	/**
	 * Chopper task that gets called semi-regularly
	 */
	
	ArrayList<ChopperEntity> choppers;
	World world;
	
	ChopperTask(ArrayList<ChopperEntity> _choppers, World _world) {
		world = _world;
		choppers = _choppers;
	}
	
	@Override
	public void run() {
		Bukkit.broadcastMessage("running chopper task");
		
		for(int i = 0; i < choppers.size(); i++) {
			Bukkit.broadcastMessage("Chopper #" + String.valueOf(i));
			ChopperEntity chopper = choppers.get(i);
			
			boolean is_near = chopper.isNearTargetTree();
			
			//If the currently scanned tree has been chopped, scan for new trees and move to them
			if(chopper.scanned_tree.size() == 0 && chopper.scanForLogs()) {
				chopper.moveToScannedTree();
			}
			
			//If chopper is near the target tree, chop it.
			if(is_near) {
				Bukkit.broadcastMessage("Is near tree");
				chopper.chopScannedTree();
			}
		}
	}
}
