package com.ngt.jopenmetaverse.shared.sim.events.asm;

import com.ngt.jopenmetaverse.shared.sim.AssetManager.ImageType;
import com.ngt.jopenmetaverse.shared.types.UUID;

 /// <summary>
    /// 
    /// </summary>
    public class ImageRequest
    {
        public UUID ImageID;
        public ImageType Type;
        public float Priority;
        public int DiscardLevel;

        /// <summary>
        /// 
        /// </summary>
        /// <param name="imageid"></param>
        /// <param name="type"></param>
        /// <param name="priority"></param>
        /// <param name="discardLevel"></param>
        public ImageRequest(UUID imageid, ImageType type, float priority, int discardLevel)
        {
            ImageID = imageid;
            Type = type;
            Priority = priority;
            DiscardLevel = discardLevel;
        }

    }