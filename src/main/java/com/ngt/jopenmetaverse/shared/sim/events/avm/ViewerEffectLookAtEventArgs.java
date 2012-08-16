package com.ngt.jopenmetaverse.shared.sim.events.avm;

import com.ngt.jopenmetaverse.shared.sim.AgentManager.LookAtType;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3d;

public class ViewerEffectLookAtEventArgs extends EventArgs
{
    private UUID m_SourceID;
    private UUID m_TargetID;
    private Vector3d m_TargetPosition;
    private LookAtType m_LookType;
    private float m_Duration;
    private UUID m_EffectID;


    public UUID getSourceID() {return m_SourceID; }
    public UUID getTargetID() {return m_TargetID; }
    public Vector3d getTargetPosition() {return m_TargetPosition; }
    public LookAtType getLookType() {return m_LookType; }
    public float getDuration() {return m_Duration; }
    public UUID getEffectID() {return m_EffectID; }

    public ViewerEffectLookAtEventArgs(UUID sourceID, UUID targetID, Vector3d targetPos, LookAtType lookType, float duration, UUID id)
    {
        this.m_SourceID = sourceID;
        this.m_TargetID = targetID;
        this.m_TargetPosition = targetPos;
        this.m_LookType = lookType;
        this.m_Duration = duration;
        this.m_EffectID = id;
    }
}