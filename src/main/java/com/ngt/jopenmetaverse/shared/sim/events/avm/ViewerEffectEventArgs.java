package com.ngt.jopenmetaverse.shared.sim.events.avm;

import com.ngt.jopenmetaverse.shared.sim.AgentManager.EffectType;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3d;

 public class ViewerEffectEventArgs extends EventArgs
    {
        private EffectType m_Type;
        private UUID m_SourceID;
        private UUID m_TargetID;
        private Vector3d m_TargetPosition;
        private float m_Duration;
        private UUID m_EffectID;

        public EffectType getType() {return m_Type; }
        public UUID getSourceID() {return m_SourceID; }
        public UUID getTargetID() {return m_TargetID; }
        public Vector3d getTargetPosition() {return m_TargetPosition; }
        public float getDuration() {return m_Duration; }
        public UUID getEffectID() {return m_EffectID; }

        public ViewerEffectEventArgs(EffectType type, UUID sourceID, UUID targetID, Vector3d targetPos, float duration, UUID id)
        {
            this.m_Type = type;
            this.m_SourceID = sourceID;
            this.m_TargetID = targetID;
            this.m_TargetPosition = targetPos;
            this.m_Duration = duration;
            this.m_EffectID = id;
        }
    }