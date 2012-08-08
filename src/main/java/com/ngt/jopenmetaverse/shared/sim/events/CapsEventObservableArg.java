package com.ngt.jopenmetaverse.shared.sim.events;

import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.interfaces.IMessage;

public class CapsEventObservableArg 
{
	String capsKey;
	IMessage message;
	Simulator simulator;
		
	public CapsEventObservableArg() {
		super();
	}
	public CapsEventObservableArg(String capsKey, IMessage message,
			Simulator simulator) {
		super();
		this.capsKey = capsKey;
		this.message = message;
		this.simulator = simulator;
	}
	public String getCapsKey() {
		return capsKey;
	}
	public void setCapsKey(String capsKey) {
		this.capsKey = capsKey;
	}
	public IMessage getMessage() {
		return message;
	}
	public void setMessage(IMessage message) {
		this.message = message;
	}
	public Simulator getSimulator() {
		return simulator;
	}
	public void setSimulator(Simulator simulator) {
		this.simulator = simulator;
	}	
}
