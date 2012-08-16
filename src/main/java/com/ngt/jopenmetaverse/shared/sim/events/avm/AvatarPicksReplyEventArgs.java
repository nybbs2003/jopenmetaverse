package com.ngt.jopenmetaverse.shared.sim.events.avm;


import java.util.Map;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;
 public class AvatarPicksReplyEventArgs extends EventArgs
    {
        private UUID m_AvatarID;
        private Map<UUID, String> m_Picks;

        /// <summary>Get the ID of the agent</summary>
        public UUID getAvatarID() {return m_AvatarID; }
        public Map<UUID, String> getPicks() {return m_Picks; }

        public AvatarPicksReplyEventArgs(UUID avatarid, Map<UUID, String> picks)
        {
            this.m_AvatarID = avatarid;
            this.m_Picks = picks;
        }
    }