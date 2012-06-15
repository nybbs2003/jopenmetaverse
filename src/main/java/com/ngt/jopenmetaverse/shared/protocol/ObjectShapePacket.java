package com.ngt.jopenmetaverse.shared.protocol;


    public final class ObjectShapePacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;

            @Override
			public int getLength()
            {
                                {
                    return 32;
                }
            }

            public AgentDataBlock() { }
            public AgentDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
                SessionID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class ObjectDataBlock extends PacketBlock
        {
            public long ObjectLocalID;
            public byte PathCurve;
            public byte ProfileCurve;
            public ushort PathBegin;
            public ushort PathEnd;
            public byte PathScaleX;
            public byte PathScaleY;
            public byte PathShearX;
            public byte PathShearY;
            public sbyte PathTwist;
            public sbyte PathTwistBegin;
            public sbyte PathRadiusOffset;
            public sbyte PathTaperX;
            public sbyte PathTaperY;
            public byte PathRevolutions;
            public sbyte PathSkew;
            public ushort ProfileBegin;
            public ushort ProfileEnd;
            public ushort ProfileHollow;

            @Override
			public int getLength()
            {
                                {
                    return 27;
                }
            }

            public ObjectDataBlock() { }
            public ObjectDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ObjectLocalID = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    PathCurve = (byte)bytes[i[0]++];
                    ProfileCurve = (byte)bytes[i[0]++];
                    PathBegin = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    PathEnd = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    PathScaleX = (byte)bytes[i[0]++];
                    PathScaleY = (byte)bytes[i[0]++];
                    PathShearX = (byte)bytes[i[0]++];
                    PathShearY = (byte)bytes[i[0]++];
                    PathTwist = (sbyte)bytes[i[0]++];
                    PathTwistBegin = (sbyte)bytes[i[0]++];
                    PathRadiusOffset = (sbyte)bytes[i[0]++];
                    PathTaperX = (sbyte)bytes[i[0]++];
                    PathTaperY = (sbyte)bytes[i[0]++];
                    PathRevolutions = (byte)bytes[i[0]++];
                    PathSkew = (sbyte)bytes[i[0]++];
                    ProfileBegin = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    ProfileEnd = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    ProfileHollow = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.uintToBytes(ObjectLocalID, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = PathCurve;
                bytes[i[0]++] = ProfileCurve;
                bytes[i[0]++] = (byte)(PathBegin % 256);
                bytes[i[0]++] = (byte)((PathBegin >> 8) % 256);
                bytes[i[0]++] = (byte)(PathEnd % 256);
                bytes[i[0]++] = (byte)((PathEnd >> 8) % 256);
                bytes[i[0]++] = PathScaleX;
                bytes[i[0]++] = PathScaleY;
                bytes[i[0]++] = PathShearX;
                bytes[i[0]++] = PathShearY;
                bytes[i[0]++] = (byte)PathTwist;
                bytes[i[0]++] = (byte)PathTwistBegin;
                bytes[i[0]++] = (byte)PathRadiusOffset;
                bytes[i[0]++] = (byte)PathTaperX;
                bytes[i[0]++] = (byte)PathTaperY;
                bytes[i[0]++] = PathRevolutions;
                bytes[i[0]++] = (byte)PathSkew;
                bytes[i[0]++] = (byte)(ProfileBegin % 256);
                bytes[i[0]++] = (byte)((ProfileBegin >> 8) % 256);
                bytes[i[0]++] = (byte)(ProfileEnd % 256);
                bytes[i[0]++] = (byte)((ProfileEnd >> 8) % 256);
                bytes[i[0]++] = (byte)(ProfileHollow % 256);
                bytes[i[0]++] = (byte)((ProfileHollow >> 8) % 256);
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                for (int j = 0; j < ObjectData.length; j++)
                    length += ObjectData[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public ObjectDataBlock[] ObjectData;

        public ObjectShapePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ObjectShape;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 98;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            ObjectData = null;
        }

        public ObjectShapePacket(byte[] bytes, int[] i) 
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(bytes, i, packetEnd, null);
        }

        @Override
		public void FromBytes(byte[] bytes, int[] i, int[] packetEnd, byte[] zeroBuffer) throws MalformedDataException
        {
            header.FromBytes(bytes, i, packetEnd);
            if (header.Zerocoded && zeroBuffer != null)
            {
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            AgentData.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(ObjectData == null || ObjectData.length != -1) {
                ObjectData = new ObjectDataBlock[count];
                for(int j = 0; j < count; j++)
                { ObjectData[j] = new ObjectDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ObjectData[j].FromBytes(bytes, i); }
        }

        public ObjectShapePacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            AgentData.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(ObjectData == null || ObjectData.length != count) {
                ObjectData = new ObjectDataBlock[count];
                for(int j = 0; j < count; j++)
                { ObjectData[j] = new ObjectDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ObjectData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < ObjectData.length; j++) { length += ObjectData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)ObjectData.length;
            for (int j = 0; j < ObjectData.length; j++) { ObjectData[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            List<byte[]> packets = new ArrayList<byte[]>();
            int[] i = new int[]{0};
            int fixedLength = 10;

            byte[] ackBytes = null;
            int[] acksLength = new int[]{0};
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, acksLength);
            }

            fixedLength += AgentData.getLength();
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int ObjectDataStart = 0;
            do
            {
                int variableLength = 0;
                int ObjectDataCount = 0;

              i[0] =ObjectDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < ObjectData.length) {
                    int blockLength = ObjectData[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++ObjectDataCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)ObjectDataCount;
                for (i[0] = ObjectDataStart; i[0] < ObjectDataStart + ObjectDataCount; i[0]++) { ObjectData[i[0]].ToBytes(packet, length); }
                ObjectDataStart += ObjectDataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                ObjectDataStart < ObjectData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
