package com.ngt.jopenmetaverse.shared.sim.events.avm;

import com.ngt.jopenmetaverse.shared.sim.AvatarManager.ClassifiedAd;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

 public class ClassifiedInfoReplyEventArgs extends EventArgs
    {
        private UUID m_ClassifiedID;
        private ClassifiedAd m_Classified;

        public UUID getClassifiedID() {return m_ClassifiedID; }
        public ClassifiedAd getClassified() {return m_Classified; }


        public ClassifiedInfoReplyEventArgs(UUID classifiedID, ClassifiedAd Classified)
        {
            this.m_ClassifiedID = classifiedID;
            this.m_Classified = Classified;
        }
    }