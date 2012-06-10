package com.ngt.jopenmetaverse.shared.protocol;


    public final class ObjectFlagUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;
            public uint ObjectLocalID;
            public bool UsePhysics;
            public bool IsTemporary;
            public bool IsPhantom;
            public bool CastsShadows;

            @Override
			public int getLength()
            {
                get
                {
                    return 40;
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
                    ObjectLocalID = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    UsePhysics = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    IsTemporary = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    IsPhantom = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    CastsShadows = (bytes[i++] != 0) ? (bool)true : (bool)false;
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
                Utils.UIntToBytes(ObjectLocalID, bytes, i); i += 4;
                bytes[i++] = (byte)((UsePhysics) ? 1 : 0);
                bytes[i++] = (byte)((IsTemporary) ? 1 : 0);
                bytes[i++] = (byte)((IsPhantom) ? 1 : 0);
                bytes[i++] = (byte)((CastsShadows) ? 1 : 0);
            }

        }

        /// <exclude/>
        public final class ExtraPhysicsBlock extends PacketBlock
        {
            public byte PhysicsShapeType;
            public float Density;
            public float Friction;
            public float Restitution;
            public float GravityMultiplier;

            @Override
			public int getLength()
            {
                get
                {
                    return 17;
                }
            }

            public ExtraPhysicsBlock() { }
            public ExtraPhysicsBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    PhysicsShapeType = (byte)bytes[i++];
                    Density = Utils.BytesToFloat(bytes, i); i += 4;
                    Friction = Utils.BytesToFloat(bytes, i); i += 4;
                    Restitution = Utils.BytesToFloat(bytes, i); i += 4;
                    GravityMultiplier = Utils.BytesToFloat(bytes, i); i += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = PhysicsShapeType;
                Utils.FloatToBytes(Density, bytes, i); i += 4;
                Utils.FloatToBytes(Friction, bytes, i); i += 4;
                Utils.FloatToBytes(Restitution, bytes, i); i += 4;
                Utils.FloatToBytes(GravityMultiplier, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += AgentData.getLength();
                for (int j = 0; j < ExtraPhysics.length; j++)
                    length += ExtraPhysics[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public ExtraPhysicsBlock[] ExtraPhysics;

        public ObjectFlagUpdatePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ObjectFlagUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 94;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            ExtraPhysics = null;
        }

        public ObjectFlagUpdatePacket(byte[] bytes, int[] i) 
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
            if(ExtraPhysics == null || ExtraPhysics.length != -1) {
                ExtraPhysics = new ExtraPhysicsBlock[count];
                for(int j = 0; j < count; j++)
                { ExtraPhysics[j] = new ExtraPhysicsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ExtraPhysics[j].FromBytes(bytes, i); }
        }

        public ObjectFlagUpdatePacket(Header head, byte[] bytes, int[] i)
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
            if(ExtraPhysics == null || ExtraPhysics.length != count) {
                ExtraPhysics = new ExtraPhysicsBlock[count];
                for(int j = 0; j < count; j++)
                { ExtraPhysics[j] = new ExtraPhysicsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ExtraPhysics[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < ExtraPhysics.length; j++) { length += ExtraPhysics[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i++] = (byte)ExtraPhysics.length;
            for (int j = 0; j < ExtraPhysics.length; j++) { ExtraPhysics[j].ToBytes(bytes, i); }
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

            int ExtraPhysicsStart = 0;
            do
            {
                int variableLength = 0;
                int ExtraPhysicsCount = 0;

                i = ExtraPhysicsStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < ExtraPhysics.length) {
                    int blockLength = ExtraPhysics[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++ExtraPhysicsCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)ExtraPhysicsCount;
                for (i = ExtraPhysicsStart; i < ExtraPhysicsStart + ExtraPhysicsCount; i++) { ExtraPhysics[i].ToBytes(packet, ref length); }
                ExtraPhysicsStart += ExtraPhysicsCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                ExtraPhysicsStart < ExtraPhysics.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
