package com.ngt.jopenmetaverse.shared.sim.imaging;

public class OpenJPEGFactory 
{
	private static OpenJPEGMachine openJPEGMachine;
	
	public static void setOpenJPEGMachine(OpenJPEGMachine machine) {
		openJPEGMachine = machine;
	}

	public static IOpenJPEG getIntance() {
		return openJPEGMachine.getInstance();
	}

	public abstract static class OpenJPEGMachine
	{
		public abstract IOpenJPEG getInstance();
	}
}
