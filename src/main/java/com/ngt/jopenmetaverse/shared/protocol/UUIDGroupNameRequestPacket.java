package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class UUIDGroupNameRequestPacket extends Packet
    {
        /// <exclude/>
        public static final class UUIDNameBlockBlock extends PacketBlock
        {
            public UUID ID = new UUID();

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public UUIDNameBlockBlock() { }
            public UUIDNameBlockBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                for (int j = 0; j < UUIDNameBlock.length; j++)
                    length += UUIDNameBlock[j].getLength();
                return length;
            }
        }
        public UUIDNameBlockBlock[] UUIDNameBlock;

        public UUIDGroupNameRequestPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.UUIDGroupNameRequest;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 237;
            header.Reliable = true;
            UUIDNameBlock = null;
        }

        public UUIDGroupNameRequestPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(UUIDNameBlock == null || UUIDNameBlock.length != -1) {
                UUIDNameBlock = new UUIDNameBlockBlock[count];
                for(int j = 0; j < count; j++)
                { UUIDNameBlock[j] = new UUIDNameBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { UUIDNameBlock[j].FromBytes(bytes, i); }
        }

        public UUIDGroupNameRequestPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(UUIDNameBlock == null || UUIDNameBlock.length != count) {
                UUIDNameBlock = new UUIDNameBlockBlock[count];
                for(int j = 0; j < count; j++)
                { UUIDNameBlock[j] = new UUIDNameBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { UUIDNameBlock[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length++;
            for (int j = 0; j < UUIDNameBlock.length; j++) { length += UUIDNameBlock[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)UUIDNameBlock.length;
            for (int j = 0; j < UUIDNameBlock.length; j++) { UUIDNameBlock[j].ToBytes(bytes, i); }
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

            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int UUIDNameBlockStart = 0;
            do
            {
                int variableLength = 0;
                int UUIDNameBlockCount = 0;

              i[0] =UUIDNameBlockStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < UUIDNameBlock.length) {
                    int blockLength = UUIDNameBlock[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++UUIDNameBlockCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)UUIDNameBlockCount;
                for (i[0] = UUIDNameBlockStart; i[0] < UUIDNameBlockStart + UUIDNameBlockCount; i[0]++) { UUIDNameBlock[i[0]].ToBytes(packet, length); }
                UUIDNameBlockStart += UUIDNameBlockCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                UUIDNameBlockStart < UUIDNameBlock.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
