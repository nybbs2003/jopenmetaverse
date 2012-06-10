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
        public final class ModifyBlockBlock extends PacketBlock
        {
            public byte Action;
            public byte BrushSize;
            public float Seconds;
            public float Height;

            @Override
			public int getLength()
            {
                get
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
                    Action = (byte)bytes[i++];
                    BrushSize = (byte)bytes[i++];
                    Seconds = Utils.BytesToFloat(bytes, i); i += 4;
                    Height = Utils.BytesToFloat(bytes, i); i += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = Action;
                bytes[i++] = BrushSize;
                Utils.FloatToBytes(Seconds, bytes, i); i += 4;
                Utils.FloatToBytes(Height, bytes, i); i += 4;
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
                get
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
                    LocalID = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
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
                Utils.IntToBytes(LocalID, bytes, i); i += 4;
                Utils.FloatToBytes(West, bytes, i); i += 4;
                Utils.FloatToBytes(South, bytes, i); i += 4;
                Utils.FloatToBytes(East, bytes, i); i += 4;
                Utils.FloatToBytes(North, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class ModifyBlockExtendedBlock extends PacketBlock
        {
            public float BrushSize;

            @Override
			public int getLength()
            {
                get
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
                    BrushSize = Utils.BytesToFloat(bytes, i); i += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.FloatToBytes(BrushSize, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 12;
                length += AgentData.getLength();
                length += ModifyBlock.length;
                for (int j = 0; j < ParcelData.length; j++)
                    length += ParcelData[j].length;
                for (int j = 0; j < ModifyBlockExtended.length; j++)
                    length += ModifyBlockExtended[j].length;
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
                packetEnd = Helpers.ZeroDecode(bytes, packetEnd + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            AgentData.FromBytes(bytes, i);
            ModifyBlock.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(ParcelData == null || ParcelData.length != -1) {
                ParcelData = new ParcelDataBlock[count];
                for(int j = 0; j < count; j++)
                { ParcelData[j] = new ParcelDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ParcelData[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
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
            int count = (int)bytes[i++];
            if(ParcelData == null || ParcelData.length != count) {
                ParcelData = new ParcelDataBlock[count];
                for(int j = 0; j < count; j++)
                { ParcelData[j] = new ParcelDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ParcelData[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
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
            for (int j = 0; j < ParcelData.length; j++) { length += ParcelData[j].length; }
            length++;
            for (int j = 0; j < ModifyBlockExtended.length; j++) { length += ModifyBlockExtended[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            ModifyBlock.ToBytes(bytes, i);
            bytes[i++] = (byte)ParcelData.length;
            for (int j = 0; j < ParcelData.length; j++) { ParcelData[j].ToBytes(bytes, i); }
            bytes[i++] = (byte)ModifyBlockExtended.length;
            for (int j = 0; j < ModifyBlockExtended.length; j++) { ModifyBlockExtended[j].ToBytes(bytes, i); }
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

                i = ModifyBlockExtendedStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < ModifyBlockExtended.length) {
                    int blockLength = ModifyBlockExtended[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++ModifyBlockExtendedCount;
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

                packet[length++] = (byte)ModifyBlockExtendedCount;
                for (i = ModifyBlockExtendedStart; i < ModifyBlockExtendedStart + ModifyBlockExtendedCount; i++) { ModifyBlockExtended[i].ToBytes(packet, ref length); }
                ModifyBlockExtendedStart += ModifyBlockExtendedCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                ParcelDataStart < ParcelData.length ||
                ModifyBlockExtendedStart < ModifyBlockExtended.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
