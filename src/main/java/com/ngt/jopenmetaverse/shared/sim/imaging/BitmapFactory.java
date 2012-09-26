package com.ngt.jopenmetaverse.shared.sim.imaging;

public class BitmapFactory{
	static BitmapFactoryMachine bitmapFactoryMachine;
	
	public static void setBitmapFactoryMachine(
			BitmapFactoryMachine bitmapFactoryMachine) {
		BitmapFactory.bitmapFactoryMachine = bitmapFactoryMachine;
	}

	public static IBitmapFactory getIntance() {
		return bitmapFactoryMachine.getInstance();
	}

	public abstract static class BitmapFactoryMachine
	{
		public abstract IBitmapFactory getInstance();
	}
}
