package model.people;

import model.disasters.Disaster;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.SOSListener;
import model.events.WorldListener;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;
import view.MainWindow;

public class Citizen implements Simulatable , Rescuable{
	
	private CitizenState state ;
	private Disaster disaster ;
	private Address location;
	private String nationalID;
	private String name;
	private int age;
	private int hp;
	private int bloodLoss ;
	private int toxicity;
	private SOSListener emergencyService;
	private WorldListener worldListener;
	
	public Citizen(Address location, String nationalID, String name, int age,WorldListener worldListener){
		state=CitizenState.SAFE ;
	    this.location=location;
		this.nationalID=nationalID;
	   this.name=name;
	   this.age=age;
		hp=100;
		bloodLoss=0 ;
		toxicity=0;
		this.worldListener = worldListener;
	}

	public CitizenState getState() {
		return state;
	}

	public void setState(CitizenState state) {
		this.state = state;
	}

	public Disaster getDisaster() {
		return disaster;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getNationalID() {
		return nationalID;
	}

	public String getName() {
		return name;
	}


	public int getAge() {
		return age;
	}
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		if(hp>100)
			this.hp = 100;
		else if(hp<=0) {
			this.hp=0;
		    this.setState(CitizenState.DECEASED);
		}else
			this.hp = hp;
	}

	public int getBloodLoss() {
		return bloodLoss;
	}

	public void setBloodLoss(int bloodLoss) {
		if(bloodLoss<0) {
			this.bloodLoss=0;
		}else if(bloodLoss>=100) {
			this.bloodLoss=100;
			this.setHp(0);
		}else {
			this.bloodLoss=bloodLoss;
		}
	}

	public int getToxicity() {
		return toxicity;
	}

	public void setToxicity(int toxicity) {
		if(toxicity<0) {
			this.toxicity=0;
		}else if(toxicity>=100) {
			this.toxicity=100;
			this.setHp(0);
		}else {
			this.toxicity=toxicity;
		}
	}
	
    public void struckBy(Disaster d) {
		this.disaster = d;
		this.setState(CitizenState.IN_TROUBLE);
		emergencyService.receiveSOSCall(this);
	}
    
	public void setEmergencyService(SOSListener emergencyService) {
		this.emergencyService = emergencyService;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public void setWorldListener(WorldListener worldListener) {
		this.worldListener = worldListener;
	}
    
    public void cycleStep() {
    		  if((this.getBloodLoss()>0 && this.getBloodLoss()<30)) {
    			  this.setHp(this.getHp()-5);
    		  }else if(this.getBloodLoss()>=30 && this.getBloodLoss()<70) {
    			  this.setHp(this.getHp()-10);
    		  }else if((this.getBloodLoss()>=70)) {
    				this.setHp(this.getHp()-15);
    		  }

    		  if((this.getToxicity()>0 && this.getToxicity()<30)) {
    			  this.setHp(this.getHp()-5);
    		  }else if(this.getToxicity()>=30 && this.getToxicity()<70) {
    			  this.setHp(this.getHp()-10);
    		  }else if(this.getToxicity()>=70) {
    				this.setHp(this.getHp()-15);
    		  }
	}

	public String updateInfo() {
		String s = "Citizen Information:\n-------------------------------------------------"+
	               "\nName: "+ name+
	               "\nAge: "+ age+
	               "\nNationalID: "+ nationalID+
	               "\nLocation: X= "+ getLocation().getX() +", Y= " + getLocation().getY()+
	               "\nHp: "+hp +
	               "\nBloodLoss: " + bloodLoss+
	               "\nToxicity: "+ toxicity+
	               "\nCitizen State: "+ state;
		if(getDisaster()!=null) {
			if(getDisaster() instanceof Injury)
			   s+="\nDisaster: Injury";
			if(getDisaster() instanceof Infection)
				   s+="\nDisaster: Infection";
		}
		 return s;
		// TODO Auto-generated method stub
	}
    
}

