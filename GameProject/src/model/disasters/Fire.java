package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.DisasterException;
import model.infrastructure.ResidentialBuilding;

public class Fire extends Disaster{

	public Fire(int cycle, ResidentialBuilding target) {
		super(cycle,target);
		}
	
	public void strike() throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException {
		super.strike();
		((ResidentialBuilding)this.getTarget()).setFireDamage(((ResidentialBuilding)this.getTarget()).getFireDamage()+10);
	}
	
	public void cycleStep() {
		((ResidentialBuilding)this.getTarget()).setFireDamage(((ResidentialBuilding)this.getTarget()).getFireDamage()+10);
	}
}
