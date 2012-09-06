package com.ngt.jopenmetaverse.shared.sim.events.sound;

import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>Provides data for the <see cref="SoundManager.AttachedSoundGainChange"/> event</summary>
/// <remarks>The <see cref="SoundManager.AttachedSoundGainChange"/> event occurs when an attached sound
/// changes its volume level</remarks>
public class AttachedSoundGainChangeEventArgs extends EventArgs
  {
      private  Simulator m_Simulator;
      private  UUID m_ObjectID;
      private  float m_Gain;

      /// <summary>Simulator where the event originated</summary>
      public Simulator getSimulator() {return m_Simulator; }
      /// <summary>Get the ID of the Object</summary>
      public UUID getObjectID() {return m_ObjectID; }
      /// <summary>Get the volume level</summary>
      public float getGain() {return m_Gain; }

      /// <summary>
      /// Construct a new instance of the AttachedSoundGainChangedEventArgs class
      /// </summary>
      /// <param name="sim">Simulator where the event originated</param>
      /// <param name="objectID">The ID of the Object</param>
      /// <param name="gain">The new volume level</param>
      public AttachedSoundGainChangeEventArgs(Simulator sim, UUID objectID, float gain)
      {
          this.m_Simulator = sim;
          this.m_ObjectID = objectID;
          this.m_Gain = gain;
      }
  }
  