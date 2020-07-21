package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.DisasterException;
import model.people.Citizen;

public class Injury extends Disaster{
	public Injury (int cycle, Citizen target) {
		super(cycle,target);
	}
	
	public void strike() throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException {
		super.strike();
		((Citizen)this.getTarget()).setBloodLoss(((Citizen)this.getTarget()).getBloodLoss()+30);
	}
	
	public void cycleStep() {
		((Citizen)this.getTarget()).setBloodLoss(((Citizen)this.getTarget()).getBloodLoss()+10);
	}
}
