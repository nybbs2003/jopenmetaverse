package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class MapBlockReplyPacket extends Packet
    {
        /// <exclude/>
        public static final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID = new UUID();
            public long Flags;

            @Override
			public int getLength()
            {
                                {
                    return 20;
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
                    Flags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.uintToBytes(Flags, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public static final class DataBlock extends PacketBlock
        {
        	/**
        	 * Unsigned Short
        	 */
            public int X;
            /**
             * Unsigned Short
             */
            public int Y;
		/** Unsigned Byte */ 
		public byte[] Name;
		/** Unsigned Byte */ 
		public byte Access;
            public long RegionFlags;
		/** Unsigned Byte */ 
		public byte WaterHeight;
		/** Unsigned Byte */ 
		public byte Agents;
            public UUID MapImageID = new UUID();

            @Override
			public int getLength()
            {
                                {
                    int length = 28;
                    if (Name != null) { length += Name.length; }
                    return length;
                }
            }

            public DataBlock() { }
            public DataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    X = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    Y = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i[0], Name, 0, length); i[0] +=  length;
                    Access = (byte)bytes[i[0]++];
                    RegionFlags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    WaterHeight = (byte)bytes[i[0]++];
                    Agents = (byte)bytes[i[0]++];
                    MapImageID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)(X % 256);
                bytes[i[0]++] = (byte)((X >> 8) % 256);
                bytes[i[0]++] = (byte)(Y % 256);
                bytes[i[0]++] = (byte)((Y >> 8) % 256);
                bytes[i[0]++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i[0], Name.length); i[0] +=  Name.length;
                bytes[i[0]++] = Access;
                Utils.uintToBytes(RegionFlags, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = WaterHeight;
                bytes[i[0]++] = Agents;
                MapImageID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                for (int j = 0; j < Data.length; j++)
                    length += Data[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public DataBlock[] Data;

        public MapBlockReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.MapBlockReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 409;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            Data = null;
        }

        public MapBlockReplyPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            if(Data == null || Data.length != -1) {
                Data = new DataBlock[count];
                for(int j = 0; j < count; j++)
                { Data[j] = new DataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Data[j].FromBytes(bytes, i); }
        }

        public MapBlockReplyPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            if(Data == null || Data.length != count) {
                Data = new DataBlock[count];
                for(int j = 0; j < count; j++)
                { Data[j] = new DataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Data[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < Data.length; j++) { length += Data[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)Data.length;
            for (int j = 0; j < Data.length; j++) { Data[j].ToBytes(bytes, i); }
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

            int DataStart = 0;
            do
            {
                int variableLength = 0;
                int DataCount = 0;

              i[0] =DataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < Data.length) {
                    int blockLength = Data[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++DataCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)DataCount;
                for (i[0] = DataStart; i[0] < DataStart + DataCount; i[0]++) { Data[i[0]].ToBytes(packet, length); }
                DataStart += DataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                DataStart < Data.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
