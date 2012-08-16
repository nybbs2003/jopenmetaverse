package com.ngt.jopenmetaverse.shared.sim.events.avm;

import com.ngt.jopenmetaverse.shared.sim.AvatarManager.ProfilePick;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

 public class PickInfoReplyEventArgs extends EventArgs
    {
        private UUID m_PickID;
        private ProfilePick m_Pick;

        public UUID getPickID() {return m_PickID; }
        public ProfilePick getPick() {return m_Pick; }


        public PickInfoReplyEventArgs(UUID pickid, ProfilePick pick)
        {
            this.m_PickID = pickid;
            this.m_Pick = pick;
        }
    }