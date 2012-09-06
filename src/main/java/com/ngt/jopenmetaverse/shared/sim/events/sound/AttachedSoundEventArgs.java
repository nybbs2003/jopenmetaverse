package com.ngt.jopenmetaverse.shared.sim.events.sound;

import java.util.EnumSet;

import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.EnumsPrimitive.SoundFlags;
import com.ngt.jopenmetaverse.shared.types.UUID;


/// <summary>Provides data for the <see cref="SoundManager.AttachedSound"/> event</summary>
  /// <remarks>The <see cref="SoundManager.AttachedSound"/> event occurs when the simulator sends
  /// the sound data which emits from an agents attachment</remarks>
  /// <example>
  /// The following code example shows the process to subscribe to the <see cref="SoundManager.AttachedSound"/> event
  /// and a stub to handle the data passed from the simulator
  /// <code>
  ///     // Subscribe to the AttachedSound event
  ///     Client.Sound.AttachedSound += Sound_AttachedSound;
  ///     
  ///     // process the data raised in the event here
  ///     private void Sound_AttachedSound(object sender, AttachedSoundEventArgs e)
  ///     {
  ///         // ... Process AttachedSoundEventArgs here ...
  ///     }
  /// </code>
  /// </example>
  public class AttachedSoundEventArgs extends EventArgs
  {
      private  Simulator m_Simulator;
      private  UUID m_SoundID;
      private  UUID m_OwnerID;
      private  UUID m_ObjectID;
      private  float m_Gain;
      private  EnumSet<SoundFlags> m_Flags;

      /// <summary>Simulator where the event originated</summary>
      public Simulator getSimulator() {return m_Simulator; }
      /// <summary>Get the sound asset id</summary>
      public UUID getSoundID() {return m_SoundID; }
      /// <summary>Get the ID of the owner</summary>
      public UUID getOwnerID() {return m_OwnerID; }
      /// <summary>Get the ID of the Object</summary>
      public UUID getObjectID() {return m_ObjectID; }
      /// <summary>Get the volume level</summary>
      public float getGain() {return m_Gain; }
      /// <summary>Get the <see cref="SoundFlags"/></summary>
      public EnumSet<SoundFlags> getFlags() {return m_Flags; }

      /// <summary>
      /// Construct a new instance of the SoundTriggerEventArgs class
      /// </summary>
      /// <param name="sim">Simulator where the event originated</param>
      /// <param name="soundID">The sound asset id</param>
      /// <param name="ownerID">The ID of the owner</param>
      /// <param name="objectID">The ID of the object</param>
      /// <param name="gain">The volume level</param>
      /// <param name="flags">The <see cref="SoundFlags"/></param>
      public AttachedSoundEventArgs(Simulator sim, UUID soundID, UUID ownerID, UUID objectID, float gain, EnumSet<SoundFlags> flags)
      {
          this.m_Simulator = sim;
          this.m_SoundID = soundID;
          this.m_OwnerID = ownerID;
          this.m_ObjectID = objectID;
          this.m_Gain = gain;
          this.m_Flags = flags;
      }
  }
  