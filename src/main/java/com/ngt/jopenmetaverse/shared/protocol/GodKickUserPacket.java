package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;

    public final class GodKickUserPacket extends Packet
    {
        /// <exclude/>
        public static final class UserInfoBlock extends PacketBlock
        {
            public UUID GodID = new UUID();
            public UUID GodSessionID = new UUID();
            public UUID AgentID = new UUID();
            public long KickFlags;
		/** Unsigned Byte */ 
		public byte[] Reason;

            @Override
			public int getLength()
            {
                                {
                    int length = 54;
                    if (Reason != null) { length += Reason.length; }
                    return length;
                }
            }

            public UserInfoBlock() { }
            public UserInfoBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    GodID.FromBytes(bytes, i[0]); i[0] += 16;
                    GodSessionID.FromBytes(bytes, i[0]); i[0] += 16;
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    KickFlags = Utils.bytesToUIntLit(bytes, i[0]); i[0] += 4;
                    length = Utils.bytesToUInt16Lit(bytes, i[0]); i[0] += 2;
                    Reason = new byte[length];
                    Utils.arraycopy(bytes, i[0], Reason, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                GodID.ToBytes(bytes, i[0]); i[0] += 16;
                GodSessionID.ToBytes(bytes, i[0]); i[0] += 16;
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.uintToBytesLit(KickFlags, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)(Reason.length % 256);
                bytes[i[0]++] = (byte)((Reason.length >> 8) % 256);
                Utils.arraycopy(Reason, 0, bytes, i[0], Reason.length); i[0] +=  Reason.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += UserInfo.getLength();
                return length;
            }
        }
        public UserInfoBlock UserInfo;

        public GodKickUserPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.GodKickUser;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 165;
            header.Reliable = true;
            UserInfo = new UserInfoBlock();
        }

        public GodKickUserPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            UserInfo.FromBytes(bytes, i);
        }

        public GodKickUserPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            UserInfo.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += UserInfo.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            UserInfo.ToBytes(bytes, i);
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
