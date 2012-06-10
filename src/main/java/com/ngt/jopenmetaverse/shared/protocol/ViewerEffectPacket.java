package com.ngt.jopenmetaverse.shared.protocol;


    public final class ViewerEffectPacket extends Packet
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
        public final class EffectBlock extends PacketBlock
        {
            public UUID ID;
            public UUID AgentID;
            public byte Type;
            public float Duration;
            public byte[] Color;
            public byte[] TypeData;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 42;
                    if (TypeData != null) { length += TypeData.length; }
                    return length;
                }
            }

            public EffectBlock() { }
            public EffectBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    ID.FromBytes(bytes, i); i += 16;
                    AgentID.FromBytes(bytes, i); i += 16;
                    Type = (byte)bytes[i++];
                    Duration = Utils.BytesToFloat(bytes, i); i += 4;
                    Color = new byte[4];
                    Buffer.BlockCopy(bytes, i, Color, 0, 4); i += 4;
                    length = bytes[i++];
                    TypeData = new byte[length];
                    Buffer.BlockCopy(bytes, i, TypeData, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ID.ToBytes(bytes, i); i += 16;
                AgentID.ToBytes(bytes, i); i += 16;
                bytes[i++] = Type;
                Utils.FloatToBytes(Duration, bytes, i); i += 4;
                Buffer.BlockCopy(Color, 0, bytes, i, 4);i += 4;
                bytes[i++] = (byte)TypeData.length;
                Buffer.BlockCopy(TypeData, 0, bytes, i, TypeData.length); i += TypeData.length;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 9;
                length += AgentData.getLength();
                for (int j = 0; j < Effect.length; j++)
                    length += Effect[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public EffectBlock[] Effect;

        public ViewerEffectPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ViewerEffect;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Medium;
            header.ID = 17;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            Effect = null;
        }

        public ViewerEffectPacket(byte[] bytes, int[] i) 
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
            if(Effect == null || Effect.length != -1) {
                Effect = new EffectBlock[count];
                for(int j = 0; j < count; j++)
                { Effect[j] = new EffectBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Effect[j].FromBytes(bytes, i); }
        }

        public ViewerEffectPacket(Header head, byte[] bytes, int[] i)
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
            if(Effect == null || Effect.length != count) {
                Effect = new EffectBlock[count];
                for(int j = 0; j < count; j++)
                { Effect[j] = new EffectBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Effect[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 8;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < Effect.length; j++) { length += Effect[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i++] = (byte)Effect.length;
            for (int j = 0; j < Effect.length; j++) { Effect[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            System.Collections.Generic.List<byte[]> packets = new System.Collections.Generic.List<byte[]>();
            int i = 0;
            int fixedLength = 8;

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

            int EffectStart = 0;
            do
            {
                int variableLength = 0;
                int EffectCount = 0;

                i = EffectStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < Effect.length) {
                    int blockLength = Effect[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++EffectCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)EffectCount;
                for (i = EffectStart; i < EffectStart + EffectCount; i++) { Effect[i].ToBytes(packet, ref length); }
                EffectStart += EffectCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                EffectStart < Effect.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
