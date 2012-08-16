package com.ngt.jopenmetaverse.shared.sim.events.avm;

import java.util.Map;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

  public class UUIDNameReplyEventArgs extends EventArgs
    {
        private Map<UUID, String> m_Names;

        public Map<UUID, String> getNames() {return m_Names; }

        public UUIDNameReplyEventArgs(Map<UUID, String> names)
        {
            this.m_Names = names;
        }
    }