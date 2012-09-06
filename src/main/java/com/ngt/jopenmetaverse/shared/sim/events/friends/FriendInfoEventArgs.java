package com.ngt.jopenmetaverse.shared.sim.events.friends;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.sim.friends.FriendInfo;

/// <summary>Contains information on a member of our friends list</summary>
    public class FriendInfoEventArgs extends  EventArgs
    {
        private  FriendInfo m_Friend;

        /// <summary>Get the FriendInfo</summary>
        public FriendInfo getFriend() {return m_Friend;}

        /// <summary>
        /// Construct a new instance of the FriendInfoEventArgs class
        /// </summary>
        /// <param name="friend">The FriendInfo</param>
        public FriendInfoEventArgs(FriendInfo friend)
        {
            this.m_Friend = friend;
        }
    }