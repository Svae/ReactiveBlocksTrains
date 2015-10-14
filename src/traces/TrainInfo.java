package traces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.Tuple;

public class TrainInfo {

	private long ID;
	private List<Integer> occupiedSleepers;
	private List<Integer> updatedSleepers;
	private HashMap<Integer, Boolean> deltaUpdate;
	
	public HashMap<Integer, Boolean> getDelta(){
		return deltaUpdate;
	}
	
	
	public void setUpdatedSleepers(List<Integer> update){
		updatedSleepers = update;
		updateDelta();
	}
	
	private void updateDelta(){
		deltaUpdate = new HashMap<Integer, Boolean>();
		for (Integer sleeper : updatedSleepers) {
			if(!occupiedSleepers.contains(sleeper)){
				deltaUpdate.put(sleeper, true);
			}
		}
		for (Integer sleeper : occupiedSleepers) {
			if(!updatedSleepers.contains(sleeper)){
				deltaUpdate.put(sleeper, false);
			}
		}
		occupiedSleepers = updatedSleepers;
	}
	
	public List<Integer> occupiedSleepers(){
		return occupiedSleepers;
	}
	
	
}
