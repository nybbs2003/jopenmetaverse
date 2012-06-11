package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.util.*;
import com.ngt.jopenmetaverse.shared.protocol.MalformedDataException;

public final class EconomyDataPacket extends Packet
    {
        public final class InfoBlock extends PacketBlock
        {
            public int ObjectCapacity;
            public int ObjectCount;
            public int PriceEnergyUnit;
            public int PriceObjectClaim;
            public int PricePublicObjectDecay;
            public int PricePublicObjectDelete;
            public int PriceParcelClaim;
            public float PriceParcelClaimFactor;
            public int PriceUpload;
            public int PriceRentLight;
            public int TeleportMinPrice;
            public float TeleportPriceExponent;
            public float EnergyEfficiency;
            public float PriceObjectRent;
            public float PriceObjectScaleFactor;
            public int PriceParcelRent;
            public int PriceGroupCreate;

            @Override
            public int getLength()
            {
                    return 68;
            }

            public InfoBlock() { }
            
            public InfoBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
            public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ObjectCapacity = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    ObjectCount = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    PriceEnergyUnit = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    PriceObjectClaim = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    PricePublicObjectDecay = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    PricePublicObjectDelete = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    PriceParcelClaim = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    PriceParcelClaimFactor = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    PriceUpload = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    PriceRentLight = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    TeleportMinPrice = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    TeleportPriceExponent = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    EnergyEfficiency = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    PriceObjectRent = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    PriceObjectScaleFactor = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    PriceParcelRent = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    PriceGroupCreate = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
            public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.intToBytes(ObjectCapacity, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(ObjectCount, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(PriceEnergyUnit, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(PriceObjectClaim, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(PricePublicObjectDecay, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(PricePublicObjectDelete, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(PriceParcelClaim, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(PriceParcelClaimFactor, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(PriceUpload, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(PriceRentLight, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(TeleportMinPrice, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(TeleportPriceExponent, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(EnergyEfficiency, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(PriceObjectRent, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(PriceObjectScaleFactor, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(PriceParcelRent, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(PriceGroupCreate, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
        public int getLength()
        {
                int length = 10;
                length += info.getLength();
                return length;
        }
        public InfoBlock info;

        public EconomyDataPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.EconomyData;
            header = new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 25;
            header.Reliable = true;
            header.Zerocoded = true;
            info = new InfoBlock();
        }

        public EconomyDataPacket(byte[] bytes, int[] i)
        {
        	this();
            int[] packetEnd = new int[1];
            packetEnd[0]= bytes.length - 1;
            FromBytes(bytes, i, packetEnd, null);
        }

        @Override
        public void FromBytes(byte[] bytes, int[] i, int[] packetEnd, byte[] zeroBuffer)
        {
            header.FromBytes(bytes, i, packetEnd);
            if (header.Zerocoded && zeroBuffer != null)
            {
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            try {
				info.FromBytes(bytes, i);
			} catch (MalformedDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        public EconomyDataPacket(Header head, byte[] bytes, int[] i)
        {
        	this();
        	int[] packetEnd = new int[1];
        	packetEnd[0] = bytes.length - 1;
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
        public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header = header;
            try {
				info.FromBytes(bytes, i);
			} catch (MalformedDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        @Override
        public byte[] ToBytes()
        {
            int length = 10;
            length += info.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[1];
            i[0]= 0;
            header.ToBytes(bytes, i);
            info.ToBytes(bytes, i);
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
        public byte[][] ToBytesMultiple()
        {
            return new byte[][] { ToBytes() };
        }
    }
