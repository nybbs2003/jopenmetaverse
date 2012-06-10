package com.ngt.jopenmetaverse.shared.protocol;


    public final class StartLurePacket extends Packet
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
        public final class InfoBlock extends PacketBlock
        {
            public byte LureType;
            public byte[] Message;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 2;
                    if (Message != null) { length += Message.length; }
                    return length;
                }
            }

            public InfoBlock() { }
            public InfoBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    LureType = (byte)bytes[i++];
                    length = bytes[i++];
                    Message = new byte[length];
                    Buffer.BlockCopy(bytes, i, Message, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = LureType;
                bytes[i++] = (byte)Message.length;
                Buffer.BlockCopy(Message, 0, bytes, i, Message.length); i += Message.length;
            }

        }

        /// <exclude/>
        public final class TargetDataBlock extends PacketBlock
        {
            public UUID TargetID;

            @Override
			public int getLength()
            {
                get
                {
                    return 16;
                }
            }

            public TargetDataBlock() { }
            public TargetDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    TargetID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                TargetID.ToBytes(bytes, i); i += 16;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += AgentData.getLength();
                length += Info.length;
                for (int j = 0; j < TargetData.length; j++)
                    length += TargetData[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public InfoBlock Info;
        public TargetDataBlock[] TargetData;

        public StartLurePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.StartLure;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 70;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            Info = new InfoBlock();
            TargetData = null;
        }

        public StartLurePacket(byte[] bytes, int[] i) 
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
            Info.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(TargetData == null || TargetData.length != -1) {
                TargetData = new TargetDataBlock[count];
                for(int j = 0; j < count; j++)
                { TargetData[j] = new TargetDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { TargetData[j].FromBytes(bytes, i); }
        }

        public StartLurePacket(Header head, byte[] bytes, int[] i)
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
            Info.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(TargetData == null || TargetData.length != count) {
                TargetData = new TargetDataBlock[count];
                for(int j = 0; j < count; j++)
                { TargetData[j] = new TargetDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { TargetData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += Info.length;
            length++;
            for (int j = 0; j < TargetData.length; j++) { length += TargetData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            Info.ToBytes(bytes, i);
            bytes[i++] = (byte)TargetData.length;
            for (int j = 0; j < TargetData.length; j++) { TargetData[j].ToBytes(bytes, i); }
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
            fixedLength += Info.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            Info.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int TargetDataStart = 0;
            do
            {
                int variableLength = 0;
                int TargetDataCount = 0;

                i = TargetDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < TargetData.length) {
                    int blockLength = TargetData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++TargetDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)TargetDataCount;
                for (i = TargetDataStart; i < TargetDataStart + TargetDataCount; i++) { TargetData[i].ToBytes(packet, ref length); }
                TargetDataStart += TargetDataCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                TargetDataStart < TargetData.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
