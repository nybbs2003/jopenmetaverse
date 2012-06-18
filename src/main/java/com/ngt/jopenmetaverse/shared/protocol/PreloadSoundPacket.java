package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class PreloadSoundPacket extends Packet
    {
        /// <exclude/>
        public static final class DataBlockBlock extends PacketBlock
        {
            public UUID ObjectID;
            public UUID OwnerID;
            public UUID SoundID;

            @Override
			public int getLength()
            {
                                {
                    return 48;
                }
            }

            public DataBlockBlock() { }
            public DataBlockBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ObjectID.FromBytes(bytes, i[0]); i[0] += 16;
                    OwnerID.FromBytes(bytes, i[0]); i[0] += 16;
                    SoundID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ObjectID.ToBytes(bytes, i[0]); i[0] += 16;
                OwnerID.ToBytes(bytes, i[0]); i[0] += 16;
                SoundID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 9;
                for (int j = 0; j < DataBlock.length; j++)
                    length += DataBlock[j].getLength();
                return length;
            }
        }
        public DataBlockBlock[] DataBlock;

        public PreloadSoundPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.PreloadSound;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Medium;
            header.ID = 15;
            header.Reliable = true;
            DataBlock = null;
        }

        public PreloadSoundPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            if(DataBlock == null || DataBlock.length != -1) {
                DataBlock = new DataBlockBlock[count];
                for(int j = 0; j < count; j++)
                { DataBlock[j] = new DataBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { DataBlock[j].FromBytes(bytes, i); }
        }

        public PreloadSoundPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            if(DataBlock == null || DataBlock.length != count) {
                DataBlock = new DataBlockBlock[count];
                for(int j = 0; j < count; j++)
                { DataBlock[j] = new DataBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { DataBlock[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 8;
            length++;
            for (int j = 0; j < DataBlock.length; j++) { length += DataBlock[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)DataBlock.length;
            for (int j = 0; j < DataBlock.length; j++) { DataBlock[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            List<byte[]> packets = new ArrayList<byte[]>();
            int[] i = new int[]{0};
            int fixedLength = 8;

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

            int DataBlockStart = 0;
            do
            {
                int variableLength = 0;
                int DataBlockCount = 0;

              i[0] =DataBlockStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < DataBlock.length) {
                    int blockLength = DataBlock[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++DataBlockCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)DataBlockCount;
                for (i[0] = DataBlockStart; i[0] < DataBlockStart + DataBlockCount; i[0]++) { DataBlock[i[0]].ToBytes(packet, length); }
                DataBlockStart += DataBlockCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                DataBlockStart < DataBlock.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
