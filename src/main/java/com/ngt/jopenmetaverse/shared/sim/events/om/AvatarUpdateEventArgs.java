package com.ngt.jopenmetaverse.shared.sim.events.om;

import com.ngt.jopenmetaverse.shared.sim.Avatar;
import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;


/// <summary>Provides data for the <see cref="ObjectManager.AvatarUpdate"/> event</summary>
/// <remarks><para>The <see cref="ObjectManager.AvatarUpdate"/> event occurs when the simulator sends
/// an <see cref="ObjectUpdatePacket"/> containing Avatar data</para>    
/// <para>Note 1: The <see cref="ObjectManager.AvatarUpdate"/> event will not be raised when the object is an Avatar</para>
/// <para>Note 2: It is possible for the <see cref="ObjectManager.AvatarUpdate"/> to be 
/// raised twice for the same avatar if for example the avatar moved to a new simulator, then returned to the current simulator</para>
/// </remarks>
/// <example>
/// The following code example uses the <see cref="AvatarUpdateEventArgs.Avatar"/> property to make a request for the top picks
/// using the <see cref="AvatarManager.RequestAvatarPicks"/> method in the <see cref="AvatarManager"/> class to display the names
/// of our own agents picks listings on the <see cref="Console"/> window.
/// <code>
///     // subscribe to the AvatarUpdate event to get our information
///     Client.Objects.AvatarUpdate += Objects_AvatarUpdate;
///     Client.Avatars.AvatarPicksReply += Avatars_AvatarPicksReply;
///     
///     private void Objects_AvatarUpdate(Object sender, AvatarUpdateEventArgs e)
///     {
///         // we only want our own data
///         if (e.Avatar.LocalID == Client.Self.LocalID)
///         {    
///             // Unsubscribe from the avatar update event to prevent a loop
///             // where we continually request the picks every time we get an update for ourselves
///             Client.Objects.AvatarUpdate -= Objects_AvatarUpdate;
///             // make the top picks request through AvatarManager
///             Client.Avatars.RequestAvatarPicks(e.Avatar.ID);
///         }
///     }
///
///     private void Avatars_AvatarPicksReply(Object sender, AvatarPicksReplyEventArgs e)
///     {
///         // we'll unsubscribe from the AvatarPicksReply event since we now have the data 
///         // we were looking for
///         Client.Avatars.AvatarPicksReply -= Avatars_AvatarPicksReply;
///         // loop through the dictionary and extract the names of the top picks from our profile
///         foreach (var pickName in e.Picks.Values)
///         {
///             Console.WriteLine(pickName);
///         }
///     }
/// </code>
/// </example>
/// <seealso cref="ObjectManager.ObjectUpdate"/>
/// <seealso cref="PrimEventArgs"/>
public class AvatarUpdateEventArgs extends EventArgs
{
    private  Simulator m_Simulator;
    private  Avatar m_Avatar;
    //ushort
    private  int m_TimeDilation;
    private  boolean m_IsNew;

    /// <summary>Get the simulator the object originated from</summary>
    public Simulator getSimulator() {return m_Simulator;}
    /// <summary>Get the <see cref="Avatar"/> data</summary>
    public Avatar getAvatar() {return m_Avatar;}
    /// <summary>Get the simulator time dilation</summary>
    public int getTimeDilation() {return m_TimeDilation;}
    /// <summary>true if the <see cref="Avatar"/> did not exist in the dictionary before this update (always true if avatar tracking has been disabled)</summary>
    public boolean getIsNew() {return m_IsNew;}

    /// <summary>
    /// Construct a new instance of the AvatarUpdateEventArgs class
    /// </summary>
    /// <param name="simulator">The simulator the packet originated from</param>
    /// <param name="avatar">The <see cref="Avatar"/> data</param>
    /// <param name="timeDilation">The simulator time dilation</param>
    /// <param name="isNew">The avatar was not in the dictionary before this update</param>
    public AvatarUpdateEventArgs(Simulator simulator, Avatar avatar, int timeDilation, boolean isNew)
    {
        this.m_Simulator = simulator;
        this.m_Avatar = avatar;
        this.m_TimeDilation = timeDilation;
        this.m_IsNew = isNew;
    }
}
