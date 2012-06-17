package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class TransferPacketPacket extends Packet
    {
        /// <exclude/>
        public final class TransferDataBlock extends PacketBlock
        {
            public UUID TransferID;
            public int ChannelType;
            public int Packet;
            public int Status;
		/** Unsigned Byte */ 
		public byte[] Data;

            @Override
			public int getLength()
            {
                                {
                    int length = 30;
                    if (Data != null) { length += Data.length; }
                    return length;
                }
            }

            public TransferDataBlock() { }
            public TransferDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    TransferID.FromBytes(bytes, i[0]); i[0] += 16;
                    ChannelType = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    Packet = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    Status = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    length = Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    Data = new byte[length];
                    Utils.arraycopy(bytes, i[0], Data, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                TransferID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.intToBytes(ChannelType, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(Packet, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(Status, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)(Data.length % 256);
                bytes[i[0]++] = (byte)((Data.length >> 8) % 256);
                Utils.arraycopy(Data, 0, bytes, i[0], Data.length); i[0] +=  Data.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 7;
                length += TransferData.getLength();
                return length;
            }
        }
        public TransferDataBlock TransferData;

        public TransferPacketPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.TransferPacket;
            this.header =  new Header();
            header.Frequency = PacketFrequency.High;
            header.ID = 17;
            header.Reliable = true;
            TransferData = new TransferDataBlock();
        }

        public TransferPacketPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            TransferData.FromBytes(bytes, i);
        }

        public TransferPacketPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            TransferData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += TransferData.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            TransferData.ToBytes(bytes, i);
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
