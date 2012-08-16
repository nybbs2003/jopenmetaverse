package com.ngt.jopenmetaverse.shared.sim.events.avm;

import java.util.Map;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

 public class AvatarPickerReplyEventArgs extends EventArgs
    {
        private UUID m_QueryID;
        private Map<UUID, String> m_Avatars;

        public UUID getQueryID() {return m_QueryID; }
        public Map<UUID, String> getAvatars() {return m_Avatars; }

        public AvatarPickerReplyEventArgs(UUID queryID, Map<UUID, String> avatars)
        {
            this.m_QueryID = queryID;
            this.m_Avatars = avatars;
        }
    }