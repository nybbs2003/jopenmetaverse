package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class ParcelDeedToGroupPacket extends Packet
    {
        
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID agentID;
            public UUID sessionID;

            @Override
			public int getLength()
            {
                    return 32;
            }

            public AgentDataBlock() { }
            public AgentDataBlock(byte[] bytes, int[] i)
            {
                try {
					FromBytes(bytes, i);
				} catch (MalformedDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    agentID.FromBytes(bytes, i[0]); i[0] += 16;
                    sessionID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                agentID.ToBytes(bytes, i[0]); i[0] += 16;
                sessionID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        public final class DataBlock extends PacketBlock
        {
            public UUID groupID;
            public int localID;

            @Override
			public int getLength()
            {
                    return 20;
            }

            public DataBlock() { }
            public DataBlock(byte[] bytes, int[] i)
            {
                try {
					FromBytes(bytes, i);
				} catch (MalformedDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    groupID.FromBytes(bytes, i[0]); i[0] += 16;
                    localID = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                groupID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.intToBytes(localID, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                int length = 10;
                length += agentData.getLength();
                length += data.getLength();
                return length;
        }
        public AgentDataBlock agentData;
        public DataBlock data;

        public ParcelDeedToGroupPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ParcelDeedToGroup;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 207;
            header.Reliable = true;
            agentData = new AgentDataBlock();
            data = new DataBlock();
        }

        public ParcelDeedToGroupPacket(byte[] bytes, int[] i) 
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(bytes, i, packetEnd, null);
        }

        @Override
		public void FromBytes(byte[] bytes, int[] i, int[] packetEnd, byte[] zeroBuffer)
        {
            header.FromBytes(bytes, i, packetEnd);
            if (header.Zerocoded && zeroBuffer != null)
            {
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            try {
				agentData.FromBytes(bytes, i);
				data.FromBytes(bytes, i);
			} catch (MalformedDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }

        public ParcelDeedToGroupPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            
            try {
            	agentData.FromBytes(bytes, i);
            	data.FromBytes(bytes, i);
			} catch (MalformedDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += agentData.getLength();
            length += data.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[1];
            i[0]= 0;
            header.ToBytes(bytes, i);
            agentData.ToBytes(bytes, i);
            data.ToBytes(bytes, i);
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            return new byte[][] { ToBytes() };
        }
    }