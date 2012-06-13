package com.ngt.jopenmetaverse.shared.protocol;


    public final class ObjectDeGrabPacket extends Packet
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
            public AgentDataBlock(byte[] bytes, int[] i)
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
            public long LocalID;

            @Override
			public int getLength()
            {
                                {
                    return 4;
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
                    LocalID = Utils.bytesToUInt(bytes); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.uintToBytes(LocalID, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public final class SurfaceInfoBlock extends PacketBlock
        {
            public Vector3 UVCoord;
            public Vector3 STCoord;
            public int FaceIndex;
            public Vector3 Position;
            public Vector3 Normal;
            public Vector3 Binormal;

            @Override
			public int getLength()
            {
                                {
                    return 64;
                }
            }

            public SurfaceInfoBlock() { }
            public SurfaceInfoBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    UVCoord.FromBytes(bytes, i[0]); i[0] += 12;
                    STCoord.FromBytes(bytes, i[0]); i[0] += 12;
                    FaceIndex = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    Position.FromBytes(bytes, i[0]); i[0] += 12;
                    Normal.FromBytes(bytes, i[0]); i[0] += 12;
                    Binormal.FromBytes(bytes, i[0]); i[0] += 12;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                UVCoord.ToBytes(bytes, i[0]); i[0] += 12;
                STCoord.ToBytes(bytes, i[0]); i[0] += 12;
                Utils.intToBytes(FaceIndex, bytes, i[0]); i[0] += 4;
                Position.ToBytes(bytes, i[0]); i[0] += 12;
                Normal.ToBytes(bytes, i[0]); i[0] += 12;
                Binormal.ToBytes(bytes, i[0]); i[0] += 12;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                length += ObjectData.length;
                for (int j = 0; j < SurfaceInfo.length; j++)
                    length += SurfaceInfo[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public ObjectDataBlock ObjectData;
        public SurfaceInfoBlock[] SurfaceInfo;

        public ObjectDeGrabPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ObjectDeGrab;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 119;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            ObjectData = new ObjectDataBlock();
            SurfaceInfo = null;
        }

        public ObjectDeGrabPacket(byte[] bytes, int[] i) 
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
            AgentData.FromBytes(bytes, i);
            ObjectData.FromBytes(bytes, i);
            int count = (int)bytes[i[0]++];
            if(SurfaceInfo == null || SurfaceInfo.length != -1) {
                SurfaceInfo = new SurfaceInfoBlock[count];
                for(int j = 0; j < count; j++)
                { SurfaceInfo[j] = new SurfaceInfoBlock(); }
            }
            for (int j = 0; j < count; j++)
            { SurfaceInfo[j].FromBytes(bytes, i); }
        }

        public ObjectDeGrabPacket(Header head, byte[] bytes, int[] i)
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
            ObjectData.FromBytes(bytes, i);
            int count = (int)bytes[i[0]++];
            if(SurfaceInfo == null || SurfaceInfo.length != count) {
                SurfaceInfo = new SurfaceInfoBlock[count];
                for(int j = 0; j < count; j++)
                { SurfaceInfo[j] = new SurfaceInfoBlock(); }
            }
            for (int j = 0; j < count; j++)
            { SurfaceInfo[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += ObjectData.length;
            length++;
            for (int j = 0; j < SurfaceInfo.length; j++) { length += SurfaceInfo[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            ObjectData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)SurfaceInfo.length;
            for (int j = 0; j < SurfaceInfo.length; j++) { SurfaceInfo[j].ToBytes(bytes, i); }
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
            fixedLength += ObjectData.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            ObjectData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int SurfaceInfoStart = 0;
            do
            {
                int variableLength = 0;
                int SurfaceInfoCount = 0;

              i[0] =SurfaceInfoStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < SurfaceInfo.length) {
                    int blockLength = SurfaceInfo[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++SurfaceInfoCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)SurfaceInfoCount;
                for (i[0] = SurfaceInfoStart; i[0] < SurfaceInfoStart + SurfaceInfoCount; i[0]++) { SurfaceInfo[i[0]].ToBytes(packet, length); }
                SurfaceInfoStart += SurfaceInfoCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                SurfaceInfoStart < SurfaceInfo.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
