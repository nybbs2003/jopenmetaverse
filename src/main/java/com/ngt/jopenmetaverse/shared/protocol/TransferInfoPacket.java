package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class TransferInfoPacket extends Packet
    {
        /// <exclude/>
        public static final class TransferInfoBlock extends PacketBlock
        {
            public UUID TransferID = new UUID();
            public int ChannelType;
            public int TargetType;
            public int Status;
            public int Size;
		/** Unsigned Byte */ 
		public byte[] Params;

            @Override
			public int getLength()
            {
                                {
                    int length = 34;
                    if (Params != null) { length += Params.length; }
                    return length;
                }
            }

            public TransferInfoBlock() { }
            public TransferInfoBlock(byte[] bytes, int[] i) throws MalformedDataException
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
                    TargetType = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    Status = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    Size = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    length = Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    Params = new byte[length];
                    Utils.arraycopy(bytes, i[0], Params, 0, length); i[0] +=  length;
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
                Utils.intToBytes(TargetType, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(Status, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(Size, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)(Params.length % 256);
                bytes[i[0]++] = (byte)((Params.length >> 8) % 256);
                Utils.arraycopy(Params, 0, bytes, i[0], Params.length); i[0] +=  Params.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += TransferInfo.getLength();
                return length;
            }
        }
        public TransferInfoBlock TransferInfo;

        public TransferInfoPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.TransferInfo;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 154;
            header.Reliable = true;
            header.Zerocoded = true;
            TransferInfo = new TransferInfoBlock();
        }

        public TransferInfoPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            TransferInfo.FromBytes(bytes, i);
        }

        public TransferInfoPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            TransferInfo.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += TransferInfo.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            TransferInfo.ToBytes(bytes, i);
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
