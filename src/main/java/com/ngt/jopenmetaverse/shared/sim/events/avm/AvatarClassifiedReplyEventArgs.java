package com.ngt.jopenmetaverse.shared.sim.events.avm;

import java.util.Map;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

 public class AvatarClassifiedReplyEventArgs extends EventArgs
    {
        private UUID m_AvatarID;
        private Map<UUID, String> m_Classifieds;

        /// <summary>Get the ID of the avatar</summary>
        public UUID getAvatarID() {return m_AvatarID; }
        public Map<UUID, String> getClassifieds() {return m_Classifieds; }

        public AvatarClassifiedReplyEventArgs(UUID avatarid, Map<UUID, String> classifieds)
        {
            this.m_AvatarID = avatarid;
            this.m_Classifieds = classifieds;
        }
    }