package com.ngt.jopenmetaverse.shared.protocol;


    public final class FindAgentPacket extends Packet
    {
        /// <exclude/>
        public final class AgentBlockBlock extends PacketBlock
        {
            public UUID Hunter;
            public UUID Prey;
            public uint SpaceIP;

            @Override
			public int getLength()
            {
                get
                {
                    return 36;
                }
            }

            public AgentBlockBlock() { }
            public AgentBlockBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Hunter.FromBytes(bytes, i); i += 16;
                    Prey.FromBytes(bytes, i); i += 16;
                    SpaceIP = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Hunter.ToBytes(bytes, i); i += 16;
                Prey.ToBytes(bytes, i); i += 16;
                Utils.UIntToBytes(SpaceIP, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class LocationBlockBlock extends PacketBlock
        {
            public double GlobalX;
            public double GlobalY;

            @Override
			public int getLength()
            {
                get
                {
                    return 16;
                }
            }

            public LocationBlockBlock() { }
            public LocationBlockBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    GlobalX = Utils.BytesToDouble(bytes, i); i += 8;
                    GlobalY = Utils.BytesToDouble(bytes, i); i += 8;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.DoubleToBytes(GlobalX, bytes, i); i += 8;
                Utils.DoubleToBytes(GlobalY, bytes, i); i += 8;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += AgentBlock.length;
                for (int j = 0; j < LocationBlock.length; j++)
                    length += LocationBlock[j].length;
                return length;
            }
        }
        public AgentBlockBlock AgentBlock;
        public LocationBlockBlock[] LocationBlock;

        public FindAgentPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.FindAgent;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 256;
            header.Reliable = true;
            AgentBlock = new AgentBlockBlock();
            LocationBlock = null;
        }

        public FindAgentPacket(byte[] bytes, int[] i) 
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
            AgentBlock.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(LocationBlock == null || LocationBlock.length != -1) {
                LocationBlock = new LocationBlockBlock[count];
                for(int j = 0; j < count; j++)
                { LocationBlock[j] = new LocationBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { LocationBlock[j].FromBytes(bytes, i); }
        }

        public FindAgentPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            AgentBlock.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(LocationBlock == null || LocationBlock.length != count) {
                LocationBlock = new LocationBlockBlock[count];
                for(int j = 0; j < count; j++)
                { LocationBlock[j] = new LocationBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { LocationBlock[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentBlock.length;
            length++;
            for (int j = 0; j < LocationBlock.length; j++) { length += LocationBlock[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentBlock.ToBytes(bytes, i);
            bytes[i++] = (byte)LocationBlock.length;
            for (int j = 0; j < LocationBlock.length; j++) { LocationBlock[j].ToBytes(bytes, i); }
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

            fixedLength += AgentBlock.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentBlock.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int LocationBlockStart = 0;
            do
            {
                int variableLength = 0;
                int LocationBlockCount = 0;

                i = LocationBlockStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < LocationBlock.length) {
                    int blockLength = LocationBlock[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++LocationBlockCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)LocationBlockCount;
                for (i = LocationBlockStart; i < LocationBlockStart + LocationBlockCount; i++) { LocationBlock[i].ToBytes(packet, ref length); }
                LocationBlockStart += LocationBlockCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                LocationBlockStart < LocationBlock.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
