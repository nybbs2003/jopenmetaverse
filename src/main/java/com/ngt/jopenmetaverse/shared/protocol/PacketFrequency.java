package com.ngt.jopenmetaverse.shared.protocol;


    /// <summary>
    /// 
    /// </summary>
    public enum PacketFrequency 
    {
        /// <summary></summary>
        Low ((byte) 0),
        /// <summary></summary>
        Medium ((byte) 1),
        /// <summary></summary>
        High ((byte) 2);
        
        private byte index;
        PacketFrequency(byte index)
    		{
    			this.index = index;
    		}     

    		public byte getIndex()
    		{
    			return index;
    		}
        
    }