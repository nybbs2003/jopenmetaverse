package com.ngt.jopenmetaverse.shared.sim.events.group;

import java.util.Map;

import com.ngt.jopenmetaverse.shared.sim.GroupManager.Group;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class GroupNamesEventArgs extends EventArgs
    {
        private Map<UUID, String> m_GroupNames;

        /// <summary>Get the Group Names dictionary</summary>
        public Map<UUID, String> getGroupNames() {return m_GroupNames; }

        /// <summary>Construct a new instance of the GroupNamesEventArgs class</summary>
        /// <param name="groupNames">The Group names dictionary</param>
        public GroupNamesEventArgs(Map<UUID, String> groupNames)
        {
            this.m_GroupNames = groupNames;
        }
    }
