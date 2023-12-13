package jorensm.woodchopper;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class WoodChopper extends JavaPlugin {
	
	/*
	 * Main plugin class
	 */
	
	Logger log;
	
	ArrayList<ChopperEntity> choppers = new ArrayList<ChopperEntity>();
	
	World world;
	
	// Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	world = Bukkit.getWorlds().get(0);
    	log = Bukkit.getLogger();
    	log.info("Plugin enabled");
    	log.info("Registering event listeners");
    	getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    	
    	
    	//Create Runnables
    	ChopperTask chopper_task = new ChopperTask(choppers, world);
    	ChopperRealtimeTask chopper_realtime_task = new ChopperRealtimeTask(choppers, world);
    	chopper_task.runTaskTimer(this, 0, 40);
    	chopper_realtime_task.runTaskTimer(this, 0, 10);
    	
    }
    
    /**
     * Retrieve chopper by its ID
     * @param id
     * @return
     */
    ChopperEntity getChopperById(String id) {
    	for(int i = 0; i < choppers.size(); i++) {
    		ChopperEntity chopper = choppers.get(i);
    		if(chopper.id == id) {
    			return chopper;
    		}
    	}
    	
    	throw new Error("Could not find Chopper by ID " + id);
    }
    
    /**
     * Spawn chopper at location with provided type
     */
    public void spawnChopper(World world, Location location, ChopperType type) {
    	ChopperEntity chopper = new ChopperEntity(this, world, type);
		chopper.spawn(location);
		choppers.add(chopper);
    }
    
    // Fired when plugin is disabled
    @Override
    public void onDisable() {

    }
}
