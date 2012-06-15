package com.ngt.jopenmetaverse.shared.protocol;


    public final class ModifyLandPacket extends Packet
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
        public final class ModifyBlockBlock extends PacketBlock
        {
            public byte Action;
            public byte BrushSize;
            public float Seconds;
            public float Height;

            @Override
			public int getLength()
            {
                                {
                    return 10;
                }
            }

            public ModifyBlockBlock() { }
            public ModifyBlockBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Action = (byte)bytes[i[0]++];
                    BrushSize = (byte)bytes[i[0]++];
                    Seconds = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    Height = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = Action;
                bytes[i[0]++] = BrushSize;
                Utils.floatToBytes(Seconds, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(Height, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public final class ParcelDataBlock extends PacketBlock
        {
            public int LocalID;
            public float West;
            public float South;
            public float East;
            public float North;

            @Override
			public int getLength()
            {
                                {
                    return 20;
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
                    LocalID = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    West = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    South = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    East = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    North = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.intToBytes(LocalID, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(West, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(South, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(East, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(North, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public final class ModifyBlockExtendedBlock extends PacketBlock
        {
            public float BrushSize;

            @Override
			public int getLength()
            {
                                {
                    return 4;
                }
            }

            public ModifyBlockExtendedBlock() { }
            public ModifyBlockExtendedBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    BrushSize = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.floatToBytes(BrushSize, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 12;
                length += AgentData.getLength();
                length += ModifyBlock.length;
                for (int j = 0; j < ParcelData.length; j++)
                    length += ParcelData[j].getLength();
                for (int j = 0; j < ModifyBlockExtended.length; j++)
                    length += ModifyBlockExtended[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public ModifyBlockBlock ModifyBlock;
        public ParcelDataBlock[] ParcelData;
        public ModifyBlockExtendedBlock[] ModifyBlockExtended;

        public ModifyLandPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ModifyLand;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 124;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            ModifyBlock = new ModifyBlockBlock();
            ParcelData = null;
            ModifyBlockExtended = null;
        }

        public ModifyLandPacket(byte[] bytes, int[] i) 
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
            ModifyBlock.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(ParcelData == null || ParcelData.length != -1) {
                ParcelData = new ParcelDataBlock[count];
                for(int j = 0; j < count; j++)
                { ParcelData[j] = new ParcelDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ParcelData[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(ModifyBlockExtended == null || ModifyBlockExtended.length != -1) {
                ModifyBlockExtended = new ModifyBlockExtendedBlock[count];
                for(int j = 0; j < count; j++)
                { ModifyBlockExtended[j] = new ModifyBlockExtendedBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ModifyBlockExtended[j].FromBytes(bytes, i); }
        }

        public ModifyLandPacket(Header head, byte[] bytes, int[] i)
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
            ModifyBlock.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(ParcelData == null || ParcelData.length != count) {
                ParcelData = new ParcelDataBlock[count];
                for(int j = 0; j < count; j++)
                { ParcelData[j] = new ParcelDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ParcelData[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(ModifyBlockExtended == null || ModifyBlockExtended.length != count) {
                ModifyBlockExtended = new ModifyBlockExtendedBlock[count];
                for(int j = 0; j < count; j++)
                { ModifyBlockExtended[j] = new ModifyBlockExtendedBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ModifyBlockExtended[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += ModifyBlock.length;
            length++;
            for (int j = 0; j < ParcelData.length; j++) { length += ParcelData[j].getLength(); }
            length++;
            for (int j = 0; j < ModifyBlockExtended.length; j++) { length += ModifyBlockExtended[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            ModifyBlock.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)ParcelData.length;
            for (int j = 0; j < ParcelData.length; j++) { ParcelData[j].ToBytes(bytes, i); }
            bytes[i[0]++] = (byte)ModifyBlockExtended.length;
            for (int j = 0; j < ModifyBlockExtended.length; j++) { ModifyBlockExtended[j].ToBytes(bytes, i); }
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
            fixedLength += ModifyBlock.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            ModifyBlock.ToBytes(fixedBytes, i);
            fixedLength += 2;

            int ParcelDataStart = 0;
            int ModifyBlockExtendedStart = 0;
            do
            {
                int variableLength = 0;
                int ParcelDataCount = 0;
                int ModifyBlockExtendedCount = 0;

              i[0] =ParcelDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < ParcelData.length) {
                    int blockLength = ParcelData[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++ParcelDataCount;
                    }
                    else { break; }
                    i[0]++;
                }

              i[0] =ModifyBlockExtendedStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < ModifyBlockExtended.length) {
                    int blockLength = ModifyBlockExtended[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++ModifyBlockExtendedCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)ParcelDataCount;
                for (i[0] = ParcelDataStart; i[0] < ParcelDataStart + ParcelDataCount; i[0]++) { ParcelData[i[0]].ToBytes(packet, length); }
                ParcelDataStart += ParcelDataCount;

                packet[length[0]++] = (byte)ModifyBlockExtendedCount;
                for (i[0] = ModifyBlockExtendedStart; i[0] < ModifyBlockExtendedStart + ModifyBlockExtendedCount; i[0]++) { ModifyBlockExtended[i[0]].ToBytes(packet, length); }
                ModifyBlockExtendedStart += ModifyBlockExtendedCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                ParcelDataStart < ParcelData.length ||
                ModifyBlockExtendedStart < ModifyBlockExtended.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
