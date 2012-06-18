package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class ParcelClaimPacket extends Packet
    {
        /// <exclude/>
        public static final class AgentDataBlock extends PacketBlock
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
        public static final class DataBlock extends PacketBlock
        {
            public UUID GroupID;
            public boolean IsGroupOwned;
            public boolean Final;

            @Override
			public int getLength()
            {
                                {
                    return 18;
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
                try
                {
                    GroupID.FromBytes(bytes, i[0]); i[0] += 16;
                    IsGroupOwned = (bytes[i[0]++] != 0) ? true : false;
                    Final = (bytes[i[0]++] != 0) ? true : false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                GroupID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)((IsGroupOwned) ? 1 : 0);
                bytes[i[0]++] = (byte)((Final) ? 1 : 0);
            }

        }

        /// <exclude/>
        public static final class ParcelDataBlock extends PacketBlock
        {
            public float West;
            public float South;
            public float East;
            public float North;

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public ParcelDataBlock() { }
            public ParcelDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
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
                Utils.floatToBytes(West, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(South, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(East, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(North, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                length += Data.getLength();
                for (int j = 0; j < ParcelData.length; j++)
                    length += ParcelData[j].getLength();
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

        public ParcelClaimPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            Data.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(ParcelData == null || ParcelData.length != -1) {
                ParcelData = new ParcelDataBlock[count];
                for(int j = 0; j < count; j++)
                { ParcelData[j] = new ParcelDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ParcelData[j].FromBytes(bytes, i); }
        }

        public ParcelClaimPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            Data.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
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
            for (int j = 0; j < ParcelData.length; j++) { length += ParcelData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            Data.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)ParcelData.length;
            for (int j = 0; j < ParcelData.length; j++) { ParcelData[j].ToBytes(bytes, i); }
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

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)ParcelDataCount;
                for (i[0] = ParcelDataStart; i[0] < ParcelDataStart + ParcelDataCount; i[0]++) { ParcelData[i[0]].ToBytes(packet, length); }
                ParcelDataStart += ParcelDataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                ParcelDataStart < ParcelData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
