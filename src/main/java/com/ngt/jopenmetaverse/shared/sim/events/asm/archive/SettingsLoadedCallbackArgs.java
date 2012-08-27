package com.ngt.jopenmetaverse.shared.sim.events.asm.archive;

import com.ngt.jopenmetaverse.shared.sim.asset.archiving.RegionSettings;


public class SettingsLoadedCallbackArgs {
	String regionName;
	RegionSettings settings;
	
	public SettingsLoadedCallbackArgs() {
		super();
	}
	
	public SettingsLoadedCallbackArgs(String regionName, RegionSettings settings) {
		super();
		this.regionName = regionName;
		this.settings = settings;
	}
	
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public RegionSettings getSettings() {
		return settings;
	}
	public void setSettings(RegionSettings settings) {
		this.settings = settings;
	}
}
