package com.ngt.jopenmetaverse.shared.sim.events.am;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

/// <summary>Contains the currency balance</summary>
public class BalanceEventArgs extends EventArgs
{
	private int m_Balance;

	/// <summary>
	/// Get the currenct balance
	/// </summary>
	public int getBalance() {  return m_Balance; } 

	/// <summary>
	/// Construct a new BalanceEventArgs object
	/// </summary>
	/// <param name="balance">The currenct balance</param>
	public BalanceEventArgs(int balance)
	{
		this.m_Balance = balance;
	}
}
