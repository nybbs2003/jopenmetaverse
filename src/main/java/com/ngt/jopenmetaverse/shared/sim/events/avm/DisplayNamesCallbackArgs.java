package com.ngt.jopenmetaverse.shared.sim.events.avm;

import com.ngt.jopenmetaverse.shared.sim.AvatarManager.AgentDisplayName;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class DisplayNamesCallbackArgs {
	boolean success;
	AgentDisplayName[] names;
	UUID[] badIDs;
	
	public DisplayNamesCallbackArgs(boolean success, AgentDisplayName[] names,
			UUID[] badIDs) {
		super();
		this.success = success;
		this.names = names;
		this.badIDs = badIDs;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public AgentDisplayName[] getNames() {
		return names;
	}
	public void setNames(AgentDisplayName[] names) {
		this.names = names;
	}
	public UUID[] getBadIDs() {
		return badIDs;
	}
	public void setBadIDs(UUID[] badIDs) {
		this.badIDs = badIDs;
	}
}
