package model.infrastructure;

import java.util.ArrayList;

import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.SOSListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public class ResidentialBuilding implements Simulatable , Rescuable  {
	
	private Address location;
	private int structuralIntegrity;
	private int fireDamage;
	private int gasLevel;
	private int foundationDamage;
	private ArrayList<Citizen> occupants;
	private Disaster disaster;
	private SOSListener emergencyService;
	
	public ResidentialBuilding( Address location ) {
		this.location=location;
		structuralIntegrity=100;
		fireDamage=0;
		gasLevel=0;
		occupants= new ArrayList<Citizen>();
		foundationDamage=0;	
	}


	public Address getLocation() {
		return location;
	}

	public int getStructuralIntegrity() {
		return structuralIntegrity;
	}

	public void setStructuralIntegrity(int structuralIntegrity) {
		if(structuralIntegrity<=0) {
			this.structuralIntegrity=0;
			for(int i=0;i<this.occupants.size();i++) {
				this.occupants.get(i).setHp(0);
			}
		}else
		 this.structuralIntegrity = structuralIntegrity;
	}

	public int getFireDamage() {
		return fireDamage;
	}

	public void setFireDamage(int fireDamage) {
		if(fireDamage<0) {
			fireDamage=0;
		}else if(fireDamage>100) {
			fireDamage = 100;
		}
		this.fireDamage = fireDamage;
	}

	public int getGasLevel() {
		return gasLevel;
	}

	public void setGasLevel(int gasLevel) {
		if(gasLevel<0) {
			this.gasLevel = 0;
		}else if(gasLevel>=100) {
			this.gasLevel=100;
			for(int i=0;i<occupants.size();i++) {
				occupants.get(i).setHp(0);
			}

		}else
		    this.gasLevel = gasLevel;

	}

	public int getFoundationDamage() {
		return foundationDamage;
	}

	public void setFoundationDamage(int foundationDamage) {
		if(foundationDamage>=100) {
			this.foundationDamage=100;
			this.setStructuralIntegrity(0);
		}else
		    this.foundationDamage = foundationDamage;
	}

	public ArrayList<Citizen> getOccupants() {
		return occupants;
	}
	public Disaster getDisaster() {
		return disaster;
	}
	
	public void setEmergencyService(SOSListener emergencyService) {
		this.emergencyService = emergencyService;
	}
	
	public void struckBy(Disaster d) {
		this.disaster = d;
		emergencyService.receiveSOSCall(this);
	}
	
    public void cycleStep() {
		if(this.getFoundationDamage() > 0) {
			int i = (int) (Math.random()*6) +5;
			this.setStructuralIntegrity(this.getStructuralIntegrity()-i);
		}if(this.getFireDamage()>0 && this.getFireDamage()<30) {
			this.setStructuralIntegrity(this.getStructuralIntegrity()-3);
		}if(this.getFireDamage()>=30 && this.getFireDamage()<70) {
			this.setStructuralIntegrity(this.getStructuralIntegrity()-5);
		}if(this.getFireDamage()>=70) {
			this.setStructuralIntegrity(this.getStructuralIntegrity()-7);
		}
	}
    
    public boolean areDeceased() {
    	for(int i=0;i<this.occupants.size();i++) {
    		if(this.occupants.get(i).getState()!=CitizenState.DECEASED) {
    			return false;
    		}
    	}
    	return true;
    }


	public String updateInfo() {
		System.out.println(this.gasLevel);
		String s = "Building Information:\n-------------------------------------------------"+
	               "\nLocation: X= "+ getLocation().getX() +", Y= " + getLocation().getY()+
	               "\nStructural Integrity: "+structuralIntegrity +
	               "\nFire Damage: " + fireDamage+
	               "\nGas Level: "+ gasLevel+
	               "\nFoundation Damage: "+ foundationDamage+
	               "\nNumber of Occupants: "+getOccupants().size();
		if(getDisaster()!=null) {
			if(getDisaster() instanceof GasLeak)
			   s+="\nDisaster: Gas Leak";
			if(getDisaster() instanceof Fire)
			   s+="\nDisaster: Fire";
			if(getDisaster() instanceof Collapse)
			   s+="\nDisaster: Collapse";
		}
		if(getOccupants().size()!=0) {
			s+= "\n\nThe Occupants in the building:\n-------------------------------------------------";
			for(int i=0;i<getOccupants().size();i++) {
				s+="\n"+getOccupants().get(i).updateInfo()+"\n";
			}
		}
		 return s;
		// TODO Auto-generated method stub
	}


	public Object[] getNames(ArrayList<Citizen> occupants2) {
		Object[] res = new Object[occupants2.size()];
		for(int i=0;i<occupants2.size();i++) {
			res[i] = occupants2.get(i).getName();
		}
		return res;
		// TODO Auto-generated method stub
	}


	public Citizen getCitizen(String s) {
		// TODO Auto-generated method stub
		for(int i=0;i<occupants.size();i++) {
			if(occupants.get(i).getName().equals(s)) {
				return occupants.get(i);
			}
		}
		return null;
	}
    
}
