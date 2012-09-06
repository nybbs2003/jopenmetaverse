package com.ngt.jopenmetaverse.shared.sim.events.sound;

import java.math.BigInteger;

import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3;

/// <summary>Provides data for the <see cref="SoundManager.SoundTrigger"/> event</summary>
/// <remarks><para>The <see cref="SoundManager.SoundTrigger"/> event occurs when the simulator forwards
/// a request made by yourself or another agent to play either an asset sound or a built in sound</para>
/// 
/// <para>Requests to play sounds where the <see cref="SoundTriggerEventArgs.SoundID"/> is not one of the built-in
/// <see cref="Sounds"/> will require sending a request to download the sound asset before it can be played</para>
/// </remarks>
/// <example>
/// The following code example uses the <see cref="SoundTriggerEventArgs.OwnerID"/>, <see cref="SoundTriggerEventArgs.SoundID"/> 
/// and <see cref="SoundTriggerEventArgs.Gain"/>
/// properties to display some information on a sound request on the <see cref="Console"/> window.
/// <code>
///     // subscribe to the event
///     Client.Sound.SoundTrigger += Sound_SoundTrigger;
///
///     // play the pre-defined BELL_TING sound
///     Client.Sound.SendSoundTrigger(Sounds.BELL_TING);
///     
///     // handle the response data
///     private void Sound_SoundTrigger(object sender, SoundTriggerEventArgs e)
///     {
///         Console.WriteLine("{0} played the sound {1} at volume {2}",
///             e.OwnerID, e.SoundID, e.Gain);
///     }    
/// </code>
/// </example>

public class SoundTriggerEventArgs extends EventArgs
  {
      private  Simulator m_Simulator;
      private  UUID m_SoundID;
      private  UUID m_OwnerID;
      private  UUID m_ObjectID;
      private  UUID m_ParentID;
      private  float m_Gain;
      //ulong
      private  BigInteger m_RegionHandle;
      private  Vector3 m_Position;

      /// <summary>Simulator where the event originated</summary>
      public Simulator getSimulator() {return m_Simulator; }
      /// <summary>Get the sound asset id</summary>
      public UUID getSoundID() {return m_SoundID; }
      /// <summary>Get the ID of the owner</summary>
      public UUID getOwnerID() {return m_OwnerID; }
      /// <summary>Get the ID of the Object</summary>
      public UUID getObjectID() {return m_ObjectID; }
      /// <summary>Get the ID of the objects parent</summary>
      public UUID getParentID() {return m_ParentID; }
      /// <summary>Get the volume level</summary>
      public float getGain() {return m_Gain; }
      /// <summary>Get the regionhandle</summary>
      //ulong
      public BigInteger getRegionHandle() {return m_RegionHandle; }
      /// <summary>Get the source position</summary>
      public Vector3 getPosition() {return m_Position; }

      /// <summary>
      /// Construct a new instance of the SoundTriggerEventArgs class
      /// </summary>
      /// <param name="sim">Simulator where the event originated</param>
      /// <param name="soundID">The sound asset id</param>
      /// <param name="ownerID">The ID of the owner</param>
      /// <param name="objectID">The ID of the object</param>
      /// <param name="parentID">The ID of the objects parent</param>
      /// <param name="gain">The volume level</param>
      /// <param name="regionHandle">The regionhandle</param>
      /// <param name="position">The source position</param>
      public SoundTriggerEventArgs(Simulator sim, UUID soundID, 
    		  UUID ownerID, UUID objectID, UUID parentID, float gain, BigInteger regionHandle, Vector3 position)
      {
          this.m_Simulator = sim;
          this.m_SoundID = soundID;
          this.m_OwnerID = ownerID;
          this.m_ObjectID = objectID;
          this.m_ParentID = parentID;
          this.m_Gain = gain;
          this.m_RegionHandle = regionHandle;
          this.m_Position = position;
      }
  }