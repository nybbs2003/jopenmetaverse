package com.ngt.jopenmetaverse.shared.sim.events.appearance;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>Contains the Event data returned from the data server from an RebakeAvatarTextures</summary>
    public class RebakeAvatarTexturesEventArgs extends EventArgs
    {
        private  UUID m_textureID;

        /// <summary>The ID of the Texture Layer to bake</summary>
        public UUID getTextureID() {  return m_textureID; } 

        /// <summary>
        /// Triggered when the simulator sends a request for this agent to rebake
        /// its appearance
        /// </summary>
        /// <param name="textureID">The ID of the Texture Layer to bake</param>
        public RebakeAvatarTexturesEventArgs(UUID textureID)
        {
            this.m_textureID = textureID;
        }

    }