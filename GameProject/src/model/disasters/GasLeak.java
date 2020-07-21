package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.DisasterException;
import model.infrastructure.ResidentialBuilding;

public class GasLeak extends Disaster{
	public GasLeak(int cycle, ResidentialBuilding target) {
		super(cycle,target);
	}
	
	public void strike() throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException{
		super.strike();
		((ResidentialBuilding)this.getTarget()).setGasLevel(((ResidentialBuilding)this.getTarget()).getGasLevel()+10);
	}
	
	public void cycleStep() {
		((ResidentialBuilding)this.getTarget()).setGasLevel(((ResidentialBuilding)this.getTarget()).getGasLevel()+15);
	}
}
