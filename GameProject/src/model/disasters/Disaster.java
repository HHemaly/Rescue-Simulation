package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.DisasterException;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.*;

abstract public class Disaster implements Simulatable{
	
	private int startCycle ;
	private Rescuable target ;
	private boolean active ;
	
	public Disaster( int startCycle , Rescuable target ) {
		this.startCycle=startCycle;
		this.target=target;
		active=false;	
}

	public int getStartCycle() {
		return startCycle;
	}

	public Rescuable getTarget() {
		return target;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void strike() throws CitizenAlreadyDeadException,BuildingAlreadyCollapsedException{
		if(this.getTarget() instanceof ResidentialBuilding) {
			if(((ResidentialBuilding)this.getTarget()).getStructuralIntegrity()==0) {
				throw new BuildingAlreadyCollapsedException(this, "Disaster should not be applied on a collapsed building");
			}
		}else if(this.getTarget() instanceof Citizen) {
			if(((Citizen)this.getTarget()).getState()==CitizenState.DECEASED) {
				throw new CitizenAlreadyDeadException(this,"Disaster should not be applied on a deceased citizen");
			}
		}
			
		this.setActive(true);
		this.target.struckBy(this);
	}
	
    abstract public void cycleStep();
    
    
}
