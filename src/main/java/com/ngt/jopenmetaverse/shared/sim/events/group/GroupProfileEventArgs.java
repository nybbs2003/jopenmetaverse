package com.ngt.jopenmetaverse.shared.sim.events.group;

import com.ngt.jopenmetaverse.shared.sim.GroupManager.Group;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

public class GroupProfileEventArgs extends EventArgs
    {
        private  Group m_Group;

        /// <summary>Get the group profile</summary>
        public Group getGroup() {return m_Group; }

        /// <summary>Construct a new instance of the GroupProfileEventArgs class</summary>
        /// <param name="group">The group profile</param>
        public GroupProfileEventArgs(Group group)
        {
            this.m_Group = group;
        }
    }

    /// <summary>
    /// Provides notification of a group invitation request sent by another Avatar
    /// </summary>
    /// <remarks>The <see cref="GroupInvitation"/> invitation is raised when another avatar makes an offer for our avatar
    /// to join a group.</remarks>
    