package com.ngt.jopenmetaverse.shared.sim.events.group;

import java.util.Map;

import com.ngt.jopenmetaverse.shared.sim.GroupManager.Group;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;
  /// <summary>Contains the current groups your agent is a member of</summary>
    public class CurrentGroupsEventArgs extends EventArgs
    {
        private Map<UUID, Group> m_Groups;

        /// <summary>Get the current groups your agent is a member of</summary>
        public Map<UUID, Group> getGroups() {return m_Groups; }

        /// <summary>Construct a new instance of the CurrentGroupsEventArgs class</summary>
        /// <param name="groups">The current groups your agent is a member of</param>
        public CurrentGroupsEventArgs(Map<UUID, Group> groups)
        {
            this.m_Groups = groups;
        }
    }
    
    /// <summary>A Dictionary of group names, where the Key is the groups ID and the value is the groups name</summary>
    