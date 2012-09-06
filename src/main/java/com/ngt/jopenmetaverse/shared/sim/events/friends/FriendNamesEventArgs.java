package com.ngt.jopenmetaverse.shared.sim.events.friends;

import java.util.Map;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>Contains Friend Names</summary>
    public class FriendNamesEventArgs extends  EventArgs
    {
        private  Map<UUID, String> m_Names;

        /// <summary>A dictionary where the Key is the ID of the Agent, 
        /// and the Value is a String containing their name</summary>
        public Map<UUID, String> getNames() {return m_Names;}

        /// <summary>
        /// Construct a new instance of the FriendNamesEventArgs class
        /// </summary>
        /// <param name="names">A dictionary where the Key is the ID of the Agent, 
        /// and the Value is a String containing their name</param>
        public FriendNamesEventArgs(Map<UUID, String> names)
        {
            this.m_Names = names;
        }
    }