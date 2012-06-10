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
        public final class ObjectDataBlock extends PacketBlock
        {
            public uint LocalID;

            @Override
			public int getLength()
            {
                get
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
                    LocalID = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(LocalID, bytes, i); i += 4;
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
                get
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
                    UVCoord.FromBytes(bytes, i); i += 12;
                    STCoord.FromBytes(bytes, i); i += 12;
                    FaceIndex = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Position.FromBytes(bytes, i); i += 12;
                    Normal.FromBytes(bytes, i); i += 12;
                    Binormal.FromBytes(bytes, i); i += 12;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                UVCoord.ToBytes(bytes, i); i += 12;
                STCoord.ToBytes(bytes, i); i += 12;
                Utils.IntToBytes(FaceIndex, bytes, i); i += 4;
                Position.ToBytes(bytes, i); i += 12;
                Normal.ToBytes(bytes, i); i += 12;
                Binormal.ToBytes(bytes, i); i += 12;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += AgentData.getLength();
                length += ObjectData.length;
                for (int j = 0; j < SurfaceInfo.length; j++)
                    length += SurfaceInfo[j].length;
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
                packetEnd = Helpers.ZeroDecode(bytes, packetEnd + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            AgentData.FromBytes(bytes, i);
            ObjectData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
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
            int count = (int)bytes[i++];
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
            for (int j = 0; j < SurfaceInfo.length; j++) { length += SurfaceInfo[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            ObjectData.ToBytes(bytes, i);
            bytes[i++] = (byte)SurfaceInfo.length;
            for (int j = 0; j < SurfaceInfo.length; j++) { SurfaceInfo[j].ToBytes(bytes, i); }
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

                i = SurfaceInfoStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < SurfaceInfo.length) {
                    int blockLength = SurfaceInfo[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++SurfaceInfoCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)SurfaceInfoCount;
                for (i = SurfaceInfoStart; i < SurfaceInfoStart + SurfaceInfoCount; i++) { SurfaceInfo[i].ToBytes(packet, ref length); }
                SurfaceInfoStart += SurfaceInfoCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                SurfaceInfoStart < SurfaceInfo.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
