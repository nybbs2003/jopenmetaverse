package com.ngt.jopenmetaverse.shared.protocol;


    public final class OnlineNotificationPacket extends Packet
    {
        /// <exclude/>
        public final class AgentBlockBlock extends PacketBlock
        {
            public UUID AgentID;

            @Override
			public int getLength()
            {
                get
                {
                    return 16;
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
                    AgentID.FromBytes(bytes, i); i += 16;
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
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                for (int j = 0; j < AgentBlock.length; j++)
                    length += AgentBlock[j].length;
                return length;
            }
        }
        public AgentBlockBlock[] AgentBlock;

        public OnlineNotificationPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.OnlineNotification;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 322;
            header.Reliable = true;
            AgentBlock = null;
        }

        public OnlineNotificationPacket(byte[] bytes, int[] i) 
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
            int count = (int)bytes[i++];
            if(AgentBlock == null || AgentBlock.length != -1) {
                AgentBlock = new AgentBlockBlock[count];
                for(int j = 0; j < count; j++)
                { AgentBlock[j] = new AgentBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AgentBlock[j].FromBytes(bytes, i); }
        }

        public OnlineNotificationPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            int count = (int)bytes[i++];
            if(AgentBlock == null || AgentBlock.length != count) {
                AgentBlock = new AgentBlockBlock[count];
                for(int j = 0; j < count; j++)
                { AgentBlock[j] = new AgentBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AgentBlock[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length++;
            for (int j = 0; j < AgentBlock.length; j++) { length += AgentBlock[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            bytes[i++] = (byte)AgentBlock.length;
            for (int j = 0; j < AgentBlock.length; j++) { AgentBlock[j].ToBytes(bytes, i); }
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

            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int AgentBlockStart = 0;
            do
            {
                int variableLength = 0;
                int AgentBlockCount = 0;

                i = AgentBlockStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < AgentBlock.length) {
                    int blockLength = AgentBlock[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++AgentBlockCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)AgentBlockCount;
                for (i = AgentBlockStart; i < AgentBlockStart + AgentBlockCount; i++) { AgentBlock[i].ToBytes(packet, ref length); }
                AgentBlockStart += AgentBlockCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                AgentBlockStart < AgentBlock.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
