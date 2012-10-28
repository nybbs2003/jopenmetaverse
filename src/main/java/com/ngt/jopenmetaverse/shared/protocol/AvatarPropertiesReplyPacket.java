/**
 * A library to interact with Virtual Worlds such as OpenSim
 * Copyright (C) 2012  Jitendra Chauhan, Email: jitendra.chauhan@gmail.com
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class AvatarPropertiesReplyPacket extends Packet
    {
        /// <exclude/>
        public static final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID = new UUID();
            public UUID AvatarID = new UUID();

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
                    AvatarID.FromBytes(bytes, i[0]); i[0] += 16;
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
                AvatarID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public static final class PropertiesDataBlock extends PacketBlock
        {
            public UUID ImageID = new UUID();
            public UUID FLImageID = new UUID();
            public UUID PartnerID = new UUID();
		/** Unsigned Byte */ 
		public byte[] AboutText;
		/** Unsigned Byte */ 
		public byte[] FLAboutText;
		/** Unsigned Byte */ 
		public byte[] BornOn;
		/** Unsigned Byte */ 
		public byte[] ProfileURL;
		/** Unsigned Byte */ 
		public byte[] CharterMember;
            public long Flags;

            @Override
			public int getLength()
            {
                                {
                    int length = 58;
                    if (AboutText != null) { length += AboutText.length; }
                    if (FLAboutText != null) { length += FLAboutText.length; }
                    if (BornOn != null) { length += BornOn.length; }
                    if (ProfileURL != null) { length += ProfileURL.length; }
                    if (CharterMember != null) { length += CharterMember.length; }
                    return length;
                }
            }

            public PropertiesDataBlock() { }
            public PropertiesDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    ImageID.FromBytes(bytes, i[0]); i[0] += 16;
                    FLImageID.FromBytes(bytes, i[0]); i[0] += 16;
                    PartnerID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = Utils.bytesToUInt16Lit(bytes, i[0]); i[0] += 2;
                    AboutText = new byte[length];
                    Utils.arraycopy(bytes, i[0], AboutText, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    FLAboutText = new byte[length];
                    Utils.arraycopy(bytes, i[0], FLAboutText, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    BornOn = new byte[length];
                    Utils.arraycopy(bytes, i[0], BornOn, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    ProfileURL = new byte[length];
                    Utils.arraycopy(bytes, i[0], ProfileURL, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    CharterMember = new byte[length];
                    Utils.arraycopy(bytes, i[0], CharterMember, 0, length); i[0] +=  length;
                    Flags = Utils.bytesToUIntLit(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ImageID.ToBytes(bytes, i[0]); i[0] += 16;
                FLImageID.ToBytes(bytes, i[0]); i[0] += 16;
                PartnerID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)(AboutText.length % 256);
                bytes[i[0]++] = (byte)((AboutText.length >> 8) % 256);
                Utils.arraycopy(AboutText, 0, bytes, i[0], AboutText.length); i[0] +=  AboutText.length;
                bytes[i[0]++] = (byte)FLAboutText.length;
                Utils.arraycopy(FLAboutText, 0, bytes, i[0], FLAboutText.length); i[0] +=  FLAboutText.length;
                bytes[i[0]++] = (byte)BornOn.length;
                Utils.arraycopy(BornOn, 0, bytes, i[0], BornOn.length); i[0] +=  BornOn.length;
                bytes[i[0]++] = (byte)ProfileURL.length;
                Utils.arraycopy(ProfileURL, 0, bytes, i[0], ProfileURL.length); i[0] +=  ProfileURL.length;
                bytes[i[0]++] = (byte)CharterMember.length;
                Utils.arraycopy(CharterMember, 0, bytes, i[0], CharterMember.length); i[0] +=  CharterMember.length;
                Utils.uintToBytesLit(Flags, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += PropertiesData.getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public PropertiesDataBlock PropertiesData;

        public AvatarPropertiesReplyPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.AvatarPropertiesReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 171;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            PropertiesData = new PropertiesDataBlock();
        }

        public AvatarPropertiesReplyPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            PropertiesData.FromBytes(bytes, i);
        }

        public AvatarPropertiesReplyPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            PropertiesData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += PropertiesData.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            PropertiesData.ToBytes(bytes, i);
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
