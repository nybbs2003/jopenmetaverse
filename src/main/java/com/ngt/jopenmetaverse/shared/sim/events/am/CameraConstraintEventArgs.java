package com.ngt.jopenmetaverse.shared.sim.events.am;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.Vector4;

/// <summary>
/// Data sent from the simulator to an agent to indicate its view limits
/// </summary>
public class CameraConstraintEventArgs extends EventArgs
{
	private  Vector4 m_CollidePlane;

	/// <summary>Get the collision plane</summary>
	public Vector4 getCollidePlane() {return m_CollidePlane;}

	/// <summary>
	/// Construct a new instance of the CameraConstraintEventArgs class
	/// </summary>
	/// <param name="collidePlane">The collision plane</param>
	public CameraConstraintEventArgs(Vector4 collidePlane)
	{
		m_CollidePlane = collidePlane;
	}
}
