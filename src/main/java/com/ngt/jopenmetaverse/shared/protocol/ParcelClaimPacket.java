package com.ngt.jopenmetaverse.shared.protocol;


    public final class ParcelClaimPacket extends Packet
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
        public final class DataBlock extends PacketBlock
        {
            public UUID GroupID;
            public bool IsGroupOwned;
            public bool Final;

            @Override
			public int getLength()
            {
                get
                {
                    return 18;
                }
            }

            public DataBlock() { }
            public DataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    GroupID.FromBytes(bytes, i); i += 16;
                    IsGroupOwned = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    Final = (bytes[i++] != 0) ? (bool)true : (bool)false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                GroupID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)((IsGroupOwned) ? 1 : 0);
                bytes[i++] = (byte)((Final) ? 1 : 0);
            }

        }

        /// <exclude/>
        public final class ParcelDataBlock extends PacketBlock
        {
            public float West;
            public float South;
            public float East;
            public float North;

            @Override
			public int getLength()
            {
                get
                {
                    return 16;
                }
            }

            public ParcelDataBlock() { }
            public ParcelDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    West = Utils.BytesToFloat(bytes, i); i += 4;
                    South = Utils.BytesToFloat(bytes, i); i += 4;
                    East = Utils.BytesToFloat(bytes, i); i += 4;
                    North = Utils.BytesToFloat(bytes, i); i += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.FloatToBytes(West, bytes, i); i += 4;
                Utils.FloatToBytes(South, bytes, i); i += 4;
                Utils.FloatToBytes(East, bytes, i); i += 4;
                Utils.FloatToBytes(North, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += AgentData.getLength();
                length += Data.getLength();
                for (int j = 0; j < ParcelData.length; j++)
                    length += ParcelData[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public DataBlock Data;
        public ParcelDataBlock[] ParcelData;

        public ParcelClaimPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ParcelClaim;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 209;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            Data = new DataBlock();
            ParcelData = null;
        }

        public ParcelClaimPacket(byte[] bytes, int[] i) 
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
            Data.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(ParcelData == null || ParcelData.length != -1) {
                ParcelData = new ParcelDataBlock[count];
                for(int j = 0; j < count; j++)
                { ParcelData[j] = new ParcelDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ParcelData[j].FromBytes(bytes, i); }
        }

        public ParcelClaimPacket(Header head, byte[] bytes, int[] i)
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
            Data.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(ParcelData == null || ParcelData.length != count) {
                ParcelData = new ParcelDataBlock[count];
                for(int j = 0; j < count; j++)
                { ParcelData[j] = new ParcelDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ParcelData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += Data.getLength();
            length++;
            for (int j = 0; j < ParcelData.length; j++) { length += ParcelData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            Data.ToBytes(bytes, i);
            bytes[i++] = (byte)ParcelData.length;
            for (int j = 0; j < ParcelData.length; j++) { ParcelData[j].ToBytes(bytes, i); }
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
            fixedLength += Data.getLength();
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            Data.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int ParcelDataStart = 0;
            do
            {
                int variableLength = 0;
                int ParcelDataCount = 0;

                i = ParcelDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < ParcelData.length) {
                    int blockLength = ParcelData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++ParcelDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)ParcelDataCount;
                for (i = ParcelDataStart; i < ParcelDataStart + ParcelDataCount; i++) { ParcelData[i].ToBytes(packet, ref length); }
                ParcelDataStart += ParcelDataCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                ParcelDataStart < ParcelData.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
