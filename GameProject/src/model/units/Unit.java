package model.units;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.DisasterException;
import exceptions.IncompatibleTargetException;
import exceptions.UnitException;
import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.SOSResponder;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

abstract public class Unit implements Simulatable,SOSResponder{
	private String unitID;
	private UnitState state;
	private Address location;
	private Rescuable target;
	private int distanceToTarget;
	private int stepsPerCycle;
	private WorldListener worldListener;

	
	public Unit(String id, Address location, int stepsPerCycle,WorldListener worldListener) {
       unitID=id;
	   state= UnitState.IDLE ;
	   this.location=location;
	   this.stepsPerCycle=stepsPerCycle;
	   this.worldListener = worldListener;
	}

	public String getUnitID() {
		return unitID;
	}

	public UnitState getState() {
		return state;
	}

	public void setState(UnitState state) {
		this.state = state;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public Rescuable getTarget() {
		return target;
	}

	public int getStepsPerCycle() {
		return stepsPerCycle;
	}
	
	public WorldListener getWorldListener() {
		return worldListener;
	}

	public void setWorldListener(WorldListener worldListener) {
		this.worldListener = worldListener;
	}
	
	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}
	
	public void cycleStep() {
		if (state == UnitState.IDLE)
			return;
		if (distanceToTarget > 0) {
			distanceToTarget = distanceToTarget - stepsPerCycle;
			if (distanceToTarget <= 0) {
				distanceToTarget = 0;
				Address t = target.getLocation();
				worldListener.assignAddress(this, t.getX(), t.getY());
			}
		} else {
			state = UnitState.TREATING;
			treat();
		}
	}
	
	public abstract void treat();
	
	
	@Override
	public void respond(Rescuable r) throws UnitException,DisasterException {
		if(this instanceof Evacuator) {
			if(!(r instanceof ResidentialBuilding)) {
				throw new IncompatibleTargetException(this,r,"You must choose a Building");
			}
			if(!canTreat(r)) {
				throw new CannotTreatException(this,r,"The Building is already Safe");
			}
			
			if(((ResidentialBuilding)r).getStructuralIntegrity()==0) {
				throw new BuildingAlreadyCollapsedException(r.getDisaster(), "The Building is already Collapsed");
			}
		}else if(this instanceof FireTruck) {
			if(!(r instanceof ResidentialBuilding)) {
				throw new IncompatibleTargetException(this,r,"You must choose a Building");
			}
			if(!canTreat(r)) {
				throw new CannotTreatException(this,r,"The Building is already Safe");
			}
			
			if(((ResidentialBuilding)r).getStructuralIntegrity()==0) {
				throw new BuildingAlreadyCollapsedException(r.getDisaster(), "The Building is already Collapsed");
			}
			
			if(this.getState()!=UnitState.IDLE) {
				this.getTarget().getDisaster().setActive(true);
			}
		}else if(this instanceof GasControlUnit) {
			if(!(r instanceof ResidentialBuilding)) {
				throw new IncompatibleTargetException(this,r,"You must choose a Building");
			}
			
			if(!canTreat(r)) {
				throw new CannotTreatException(this,r,"The Building is already Safe");
			}
			
			if(((ResidentialBuilding)r).getStructuralIntegrity()==0) {
				throw new BuildingAlreadyCollapsedException(r.getDisaster(), "The Building is already Collapsed");
			}
		}
		if (target != null && state == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);

	}

	public void reactivateDisaster() {
		Disaster curr = target.getDisaster();
		curr.setActive(true);
	}

	public void finishRespond(Rescuable r) {
		target = r;
		state = UnitState.RESPONDING;
		Address t = r.getLocation();
		distanceToTarget = Math.abs(t.getX() - location.getX())
				+ Math.abs(t.getY() - location.getY());

	}
	
	public void jobsDone(){
				this.target = null;
				this.setState(UnitState.IDLE);
	}
	
	abstract public boolean canTreat(Rescuable r);
	
	public String updateInfo() {
		String s = "Unit Information:\n-------------------------------------------------"+
	               "\nUnitID: "+unitID;
	               if(this instanceof FireTruck)
	            	   s+= "\nUnit Type: Fire Truck";
	               else if(this instanceof Evacuator)
	            	   s+= "\nUnit Type: Evacuator";
	               else if(this instanceof DiseaseControlUnit)
	            	   s+= "\nUnit Type: Disease Control Unit";
	               else if(this instanceof GasControlUnit)
	            	   s+= "\nUnit Type: Gas Control Unit";
	               else if(this instanceof Ambulance)
	            	   s+= "\nUnit Type: Ambulance";
	               s+="\nLocation: X= "+ getLocation().getX() +", Y= " + getLocation().getY()+
	               "\nSteps per cycle: "+stepsPerCycle;
	               if(this.getTarget()!=null) {
	            	   if(this.getTarget() instanceof Citizen) {
	            		   s+="\nTarget: Citizen"+
	            	          " ,Location: X= "+this.getTarget().getLocation().getX()+", Y= "+this.getTarget().getLocation().getY();
	            	   }else if(this.getTarget() instanceof ResidentialBuilding) {
	            		   s+="\nTarget: Building"+
	 	            	          " ,Location: X= "+this.getTarget().getLocation().getX()+", Y= "+this.getTarget().getLocation().getY();
	            	   }
	               }
	              s+= "\nUnit State: "+ state;
	               if(this instanceof Evacuator) {
	            	   s+="\nNumber of the passengers: "+((Evacuator)this).getPassengers().size();
		if(((Evacuator)this).getPassengers().size()!=0) {
			s+= "\n\nThe Passengers in the Evacuator:\n-------------------------------------------------";
			for(int i=0;i<((Evacuator)this).getPassengers().size();i++) {
				s+="\n"+((Evacuator)this).getPassengers().get(i).updateInfo();
			}
		}
	               }
		 return s;
		// TODO Auto-generated method stub
	}
}
