package model.units;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.DisasterException;
import exceptions.IncompatibleTargetException;
import exceptions.UnitException;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class Ambulance extends MedicalUnit {
	public Ambulance(String id, Address location, int stepsPerCycle,WorldListener worldListener){
		super(id,location,stepsPerCycle,worldListener);
	}
	
	public void treat() {
		getTarget().getDisaster().setActive(false);

		Citizen target = (Citizen) getTarget();
		if (target.getHp() == 0) {
			jobsDone();
			return;
		} else if (target.getBloodLoss() > 0) {
			target.setBloodLoss(target.getBloodLoss() - getTreatmentAmount());
			if (target.getBloodLoss() == 0)
				target.setState(CitizenState.RESCUED);
		}

		else if (target.getBloodLoss() == 0)

			heal();
		}
	
	
	public void respond(Rescuable r) throws UnitException, DisasterException {
		
			if(!(r instanceof Citizen)) {
				throw new IncompatibleTargetException(this,r,"You must choose a Citizen");
			}
			if(!canTreat(r)) {
				throw new CannotTreatException(this,r,"The Citizen is already Safe");
			}
			
			if(((Citizen)r).getHp()==0) {
				throw new CitizenAlreadyDeadException(r.getDisaster(), "The Citizen is already deceased");
			}
			
			
			if (getTarget() != null && ((Citizen) getTarget()).getBloodLoss() > 0
					&& getState() == UnitState.TREATING)
				reactivateDisaster();
			finishRespond(r);
			
	}


	@Override
	public boolean canTreat(Rescuable r) {
		// TODO Auto-generated method stub
		return ((Citizen)r).getBloodLoss()>0 &&((Citizen)r).getHp()<100;
	}
}
