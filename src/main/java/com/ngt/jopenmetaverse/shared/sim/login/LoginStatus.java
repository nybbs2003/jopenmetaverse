package com.ngt.jopenmetaverse.shared.sim.login;

public enum LoginStatus {
	/// <summary></summary>
	Failed ((byte)-1),
	/// <summary></summary>
	None ((byte)0),
	/// <summary></summary>
	ConnectingToLogin ((byte)1),
	/// <summary></summary>
	ReadingResponse ((byte)2),
	/// <summary></summary>
	ConnectingToSim ((byte)3),
	/// <summary></summary>
	Redirecting ((byte)4),
	/// <summary></summary>
	Success ((byte)5);

	private byte index;
	LoginStatus(byte index)
	{
		this.index = index;
	}     

	public byte getIndex()
	{
		return index;
	}
}
