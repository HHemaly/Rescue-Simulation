package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.DisasterException;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;

public class Infection extends Disaster{
	
	public Infection (int cycle, Citizen target) {
		super(cycle,target);
	}
	
	public void strike() throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException {
		super.strike();
		((Citizen)this.getTarget()).setToxicity(((Citizen)this.getTarget()).getToxicity()+25);
	}
	
	public void cycleStep() {
		((Citizen)this.getTarget()).setToxicity(((Citizen)this.getTarget()).getToxicity()+15);
	}
}
