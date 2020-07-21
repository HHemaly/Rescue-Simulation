package model.units;

import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class Evacuator extends PoliceUnit {
	public Evacuator(String id, Address location, int stepsPerCycle,WorldListener worldListener, int maxCapacity){
		super(id,location,stepsPerCycle,worldListener,maxCapacity);
	}
	
	
	@Override
	public void treat() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0
				|| target.getOccupants().size() == 0) {
			jobsDone();
			return;
		}

		for (int i = 0; getPassengers().size() != getMaxCapacity()
				&& i < target.getOccupants().size(); i++) {
			getPassengers().add(target.getOccupants().remove(i));
			i--;
		}

		setDistanceToBase(target.getLocation().getX()
				+ target.getLocation().getY());

	}
	
	@Override
	public boolean canTreat(Rescuable r) {
		// TODO Auto-generated method stub
		return((ResidentialBuilding)r).getFoundationDamage()>0;
	}
	
	
	
}
