package model.units;

import model.events.WorldListener;
import model.people.Citizen;
import simulation.Address;

abstract public class MedicalUnit extends Unit{
	private int healingAmount;
	private int treatmentAmount;
	
	public MedicalUnit(String id, Address location, int stepsPerCycle,WorldListener worldListener){
		super(id,location,stepsPerCycle, worldListener);
		healingAmount = 10;
		treatmentAmount = 10;
	}
	
	public int getTreatmentAmount() {
		return treatmentAmount;
	}
	
	public void heal() {
		if(((Citizen)this.getTarget()).getHp()<100) {
			((Citizen)super.getTarget()).setHp(((Citizen)super.getTarget()).getHp()+healingAmount);
			if(((Citizen)this.getTarget()).getHp()>=100) {
				this.jobsDone();
			}
		}
	}
}
