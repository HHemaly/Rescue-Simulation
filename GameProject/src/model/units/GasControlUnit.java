package model.units;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.DisasterException;
import exceptions.IncompatibleTargetException;
import exceptions.UnitException;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class GasControlUnit extends FireUnit{
	
	
	public GasControlUnit(String id, Address location, int stepsPerCycle,WorldListener worldListener){
		super(id,location,stepsPerCycle,worldListener);
		
	}
	
	public void treat() {
		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) {
			jobsDone();
			return;
		} else if (target.getGasLevel() > 0) 
			target.setGasLevel(target.getGasLevel() - 10);

		if (target.getGasLevel() == 0)
			jobsDone();

	}
	
	@Override
	public boolean canTreat(Rescuable r) {
		// TODO Auto-generated method stub
		return ((ResidentialBuilding)r).getGasLevel()>0;
	}

}
