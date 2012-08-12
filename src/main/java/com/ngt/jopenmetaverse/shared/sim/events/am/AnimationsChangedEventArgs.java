package com.ngt.jopenmetaverse.shared.sim.events.am;

import com.ngt.jopenmetaverse.shared.sim.InternalDictionary;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>Data sent by the simulator to indicate the active/changed animations
	/// applied to your agent</summary>
	public class AnimationsChangedEventArgs extends EventArgs
	{
		private  InternalDictionary<UUID, Integer> m_Animations;

		/// <summary>Get the dictionary that contains the changed animations</summary>
		public InternalDictionary<UUID, Integer> getAnimations() {return m_Animations;}

		/// <summary>
		/// Construct a new instance of the AnimationsChangedEventArgs class
		/// </summary>
		/// <param name="agentAnimations">The dictionary that contains the changed animations</param>
		public AnimationsChangedEventArgs(InternalDictionary<UUID, Integer> agentAnimations)
		{
			this.m_Animations = agentAnimations;
		}

	}
