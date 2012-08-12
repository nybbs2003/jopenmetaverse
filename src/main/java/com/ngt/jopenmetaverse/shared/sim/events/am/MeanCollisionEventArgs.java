package com.ngt.jopenmetaverse.shared.sim.events.am;

import java.util.Date;

import com.ngt.jopenmetaverse.shared.sim.AgentManager.MeanCollisionType;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>
	/// Data sent from a simulator indicating a collision with your agent
	/// </summary>
	public class MeanCollisionEventArgs extends EventArgs
	{
		private  MeanCollisionType m_Type;
		private  UUID m_Aggressor;
		private  UUID m_Victim;
		private  float m_Magnitude;
		private  Date m_Time;

		/// <summary>Get the Type of collision</summary>
		public MeanCollisionType getType() {return m_Type;}
		/// <summary>Get the ID of the agent or object that collided with your agent</summary>
		public UUID getAggressor() {return m_Aggressor;}
		/// <summary>Get the ID of the agent that was attacked</summary>
		public UUID getVictim() {return m_Victim;}
		/// <summary>A value indicating the strength of the collision</summary>
		public float getMagnitude() {return m_Magnitude;}
		/// <summary>Get the time the collision occurred</summary>
		public Date getTime() {return m_Time;}

		/// <summary>
		/// Construct a new instance of the MeanCollisionEventArgs class
		/// </summary>
		/// <param name="type">The type of collision that occurred</param>
		/// <param name="perp">The ID of the agent or object that perpetrated the agression</param>
		/// <param name="victim">The ID of the Victim</param>
		/// <param name="magnitude">The strength of the collision</param>
		/// <param name="time">The Time the collision occurred</param>
		public MeanCollisionEventArgs(MeanCollisionType type, UUID perp, UUID victim,
				float magnitude, Date time)
		{
			this.m_Type = type;
			this.m_Aggressor = perp;
			this.m_Victim = victim;
			this.m_Magnitude = magnitude;
			this.m_Time = time;
		}
	}
