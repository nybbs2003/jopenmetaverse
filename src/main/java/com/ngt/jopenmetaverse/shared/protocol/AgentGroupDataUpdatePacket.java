package com.ngt.jopenmetaverse.shared.protocol;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class AgentGroupDataUpdatePacket extends Packet
    {
        /// <exclude/>
        public static final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
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
            }

        }

        /// <exclude/>
        public static final class GroupDataBlock extends PacketBlock
        {
            public UUID GroupID;
            public BigInteger GroupPowers;
            public boolean AcceptNotices;
            public UUID GroupInsigniaID;
            public int Contribution;
		/** Unsigned Byte */ 
		public byte[] GroupName;

            @Override
			public int getLength()
            {
                                {
                    int length = 46;
                    if (GroupName != null) { length += GroupName.length; }
                    return length;
                }
            }

            public GroupDataBlock() { }
            public GroupDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    GroupID.FromBytes(bytes, i[0]); i[0] += 16;
                    GroupPowers = Utils.bytesToULong(bytes, i[0]); i[0] += 8;
                    AcceptNotices = (bytes[i[0]++] != 0) ? true : false;
                    GroupInsigniaID.FromBytes(bytes, i[0]); i[0] += 16;
                    Contribution = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    GroupName = new byte[length];
                    Utils.arraycopy(bytes, i[0], GroupName, 0, length); i[0] +=  length;
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
                Utils.ulongToBytes(GroupPowers, bytes, i[0]); i[0] += 8;
                bytes[i[0]++] = (byte)((AcceptNotices) ? 1 : 0);
                GroupInsigniaID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.intToBytes(Contribution, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)GroupName.length;
                Utils.arraycopy(GroupName, 0, bytes, i[0], GroupName.length); i[0] +=  GroupName.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                for (int j = 0; j < GroupData.length; j++)
                    length += GroupData[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public GroupDataBlock[] GroupData;

        public AgentGroupDataUpdatePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.AgentGroupDataUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 389;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            GroupData = null;
        }

        public AgentGroupDataUpdatePacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            if(GroupData == null || GroupData.length != -1) {
                GroupData = new GroupDataBlock[count];
                for(int j = 0; j < count; j++)
                { GroupData[j] = new GroupDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { GroupData[j].FromBytes(bytes, i); }
        }

        public AgentGroupDataUpdatePacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            if(GroupData == null || GroupData.length != count) {
                GroupData = new GroupDataBlock[count];
                for(int j = 0; j < count; j++)
                { GroupData[j] = new GroupDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { GroupData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < GroupData.length; j++) { length += GroupData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)GroupData.length;
            for (int j = 0; j < GroupData.length; j++) { GroupData[j].ToBytes(bytes, i); }
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

            int GroupDataStart = 0;
            do
            {
                int variableLength = 0;
                int GroupDataCount = 0;

              i[0] =GroupDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < GroupData.length) {
                    int blockLength = GroupData[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++GroupDataCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)GroupDataCount;
                for (i[0] = GroupDataStart; i[0] < GroupDataStart + GroupDataCount; i[0]++) { GroupData[i[0]].ToBytes(packet, length); }
                GroupDataStart += GroupDataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                GroupDataStart < GroupData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
