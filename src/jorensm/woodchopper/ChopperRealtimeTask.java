package jorensm.woodchopper;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class ChopperRealtimeTask extends BukkitRunnable {
	
	/*
	 * Chopper realtime tasks that should be run every tick or almost every tick.
	 */
	
	ArrayList<ChopperEntity> choppers;
	World world;
	
	ChopperRealtimeTask(ArrayList<ChopperEntity> _choppers, World _world) {
		world = _world;
		choppers = _choppers;
	}
	
	@Override
	public void run() {
		for(int i = 0; i < choppers.size(); i++) {
			ChopperEntity chopper = choppers.get(i);
			
			//Spawn particles according to chopper's type
			if(chopper.type == ChopperType.EMERALD) {
				world.spawnParticle(Particle.GLOW, chopper.entity.getLocation(), 2);
			} else if(chopper.type == ChopperType.GOLD) {
				world.spawnParticle(Particle.PORTAL, chopper.entity.getLocation(), 2);
			}
			
			
			//Check if chopper is near a mushroom and despawn it if so
			ArrayList<Block> mushrooms_nearby = ChopperUtil.getBlocksInRadius("MUSHROOM", world, chopper.entity.getLocation(), 1);
			if(mushrooms_nearby.size() > 0) {
				chopper.entity.remove();
			}
		}
	}
}