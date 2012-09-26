package com.ngt.jopenmetaverse.shared.sim.imaging;

public abstract class AbstractIBitmapImpl implements IBitmap{
	
	public byte[] exportTGA() throws Exception
	{
		//FIXME write platform specific optimized code
		ManagedImage mi = new ManagedImage(this);
		return mi.ExportTGA();
	}

	public byte[] exportRAW() throws Exception
	{
		//FIXME write platform specific optimized code
		ManagedImage mi = new ManagedImage(this);
		return mi.ExportRaw();
	}
	
	public int[] exportPixels() throws Exception
	{
		//FIXME write platform specific optimized code
		ManagedImage mi = new ManagedImage(this);
		return mi.ExportPixels();
	}	
}
