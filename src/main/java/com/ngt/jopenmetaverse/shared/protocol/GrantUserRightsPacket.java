package com.ngt.jopenmetaverse.shared.protocol;


    public final class GrantUserRightsPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;

            @Override
			public int getLength()
            {
                get
                {
                    return 32;
                }
            }

            public AgentDataBlock() { }
            public AgentDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AgentID.FromBytes(bytes, i); i += 16;
                    SessionID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AgentID.ToBytes(bytes, i); i += 16;
                SessionID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class RightsBlock extends PacketBlock
        {
            public UUID AgentRelated;
            public int RelatedRights;

            @Override
			public int getLength()
            {
                get
                {
                    return 20;
                }
            }

            public RightsBlock() { }
            public RightsBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AgentRelated.FromBytes(bytes, i); i += 16;
                    RelatedRights = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AgentRelated.ToBytes(bytes, i); i += 16;
                Utils.IntToBytes(RelatedRights, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += AgentData.getLength();
                for (int j = 0; j < Rights.length; j++)
                    length += Rights[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public RightsBlock[] Rights;

        public GrantUserRightsPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.GrantUserRights;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 320;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            Rights = null;
        }

        public GrantUserRightsPacket(byte[] bytes, int[] i) 
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
                packetEnd = Helpers.ZeroDecode(bytes, packetEnd + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            AgentData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(Rights == null || Rights.length != -1) {
                Rights = new RightsBlock[count];
                for(int j = 0; j < count; j++)
                { Rights[j] = new RightsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Rights[j].FromBytes(bytes, i); }
        }

        public GrantUserRightsPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            AgentData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(Rights == null || Rights.length != count) {
                Rights = new RightsBlock[count];
                for(int j = 0; j < count; j++)
                { Rights[j] = new RightsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Rights[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < Rights.length; j++) { length += Rights[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i++] = (byte)Rights.length;
            for (int j = 0; j < Rights.length; j++) { Rights[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            System.Collections.Generic.List<byte[]> packets = new System.Collections.Generic.List<byte[]>();
            int i = 0;
            int fixedLength = 10;

            byte[] ackBytes = null;
            int acksLength = 0;
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, ref acksLength);
            }

            fixedLength += AgentData.getLength();
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int RightsStart = 0;
            do
            {
                int variableLength = 0;
                int RightsCount = 0;

                i = RightsStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < Rights.length) {
                    int blockLength = Rights[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++RightsCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)RightsCount;
                for (i = RightsStart; i < RightsStart + RightsCount; i++) { Rights[i].ToBytes(packet, ref length); }
                RightsStart += RightsCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                RightsStart < Rights.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
