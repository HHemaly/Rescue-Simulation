package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.DisasterException;
import model.infrastructure.ResidentialBuilding;

public class Collapse extends Disaster  {
	
	public Collapse(int cycle, ResidentialBuilding target) {
		super(cycle,target);
	}
	
	public void strike() throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException {
		super.strike();
		((ResidentialBuilding)this.getTarget()).setFoundationDamage(((ResidentialBuilding)this.getTarget()).getFoundationDamage()+10);
	}
	
	public void cycleStep() {
		((ResidentialBuilding)this.getTarget()).setFoundationDamage(((ResidentialBuilding)this.getTarget()).getFoundationDamage()+10);
	}
}
