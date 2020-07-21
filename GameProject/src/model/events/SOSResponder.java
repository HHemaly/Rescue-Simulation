package model.events;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.DisasterException;
import exceptions.IncompatibleTargetException;
import exceptions.UnitException;
import simulation.Rescuable;

public interface SOSResponder {
	public void respond(Rescuable r) throws UnitException, DisasterException;
}
