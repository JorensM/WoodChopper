package jorensm.woodchopper;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class ChopperTask extends BukkitRunnable {
	
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
			
			if(chopper.scanned_tree.size() == 0 && chopper.scanForLogs()) {
				chopper.moveToScannedTree();
			}
			
			if(is_near) {
				Bukkit.broadcastMessage("Is near tree");
				chopper.chopScannedTree();
			}
		}
	}
}
