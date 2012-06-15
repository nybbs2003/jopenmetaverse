package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class ReplyTaskInventoryPacket extends Packet
    {
        /// <exclude/>
        public final class InventoryDataBlock extends PacketBlock
        {
            public UUID TaskID;
            public short Serial;
            public byte[] Filename;

            @Override
			public int getLength()
            {
                                {
                    int length = 19;
                    if (Filename != null) { length += Filename.length; }
                    return length;
                }
            }

            public InventoryDataBlock() { }
            public InventoryDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    TaskID.FromBytes(bytes, i[0]); i[0] += 16;
                    Serial = (short)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Filename = new byte[length];
                    Utils.arraycopy(bytes, i[0], Filename, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                TaskID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)(Serial % 256);
                bytes[i[0]++] = (byte)((Serial >> 8) % 256);
                bytes[i[0]++] = (byte)Filename.length;
                Utils.arraycopy(Filename, 0, bytes, i[0], Filename.length); i[0] +=  Filename.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += InventoryData.length;
                return length;
            }
        }
        public InventoryDataBlock InventoryData;

        public ReplyTaskInventoryPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ReplyTaskInventory;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 290;
            header.Reliable = true;
            header.Zerocoded = true;
            InventoryData = new InventoryDataBlock();
        }

        public ReplyTaskInventoryPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            InventoryData.FromBytes(bytes, i);
        }

        public ReplyTaskInventoryPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            InventoryData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += InventoryData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            InventoryData.ToBytes(bytes, i);
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            return new byte[][] { ToBytes() };
        }
    }

    /// <exclude/>
