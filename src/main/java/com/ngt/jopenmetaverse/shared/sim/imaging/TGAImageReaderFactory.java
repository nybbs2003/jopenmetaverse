package com.ngt.jopenmetaverse.shared.sim.imaging;


public class TGAImageReaderFactory {
	protected static TGAImageReaderMachine  tGAImageReaderMachine =null;
	
	public static void setTGAImageReaderMachine(TGAImageReaderMachine t)
	{
		tGAImageReaderMachine = t;
	}
	
	public static ITGAImageReader getInstance()
	{
		return tGAImageReaderMachine.createInstance();
	}
	
	public abstract static class TGAImageReaderMachine
	{
		public abstract ITGAImageReader createInstance();
	}	
}
