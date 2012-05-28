package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.util.Utils;


  /// <summary>
    /// The header of a message template packet. Holds packet flags, sequence
    /// number, packet ID, and any ACKs that will be appended at the end of
    /// the packet
    /// </summary>
    public class Header
    {
        public boolean Reliable;
        public boolean Resent;
        public boolean Zerocoded;
        public boolean AppendedAcks;
        public long Sequence;
        public int ID;
        public PacketFrequency Frequency;
        public long[] AckList;

        public void ToBytes(byte[] bytes, int[] i)
        {
            byte flags = 0;
            if (Reliable) flags |= Helpers.MSG_RELIABLE;
            if (Resent) flags |= Helpers.MSG_RESENT;
            if (Zerocoded) flags |= Helpers.MSG_ZEROCODED;
            if (AppendedAcks) flags |= Helpers.MSG_APPENDED_ACKS;

            // Flags
            bytes[i[0]++] = flags;
            
            // Sequence number
            //TODO need to take only 4 bytes out of 8 bytes of long
            Utils.intToBytes((int)Sequence, bytes, i[0]);
            i[0] += 4;

            // Extra byte
            bytes[i[0]++] = 0;

            // Packet ID
            switch (Frequency)
            {
                case High:
                    // 1 byte ID
                    bytes[i[0]++] = (byte)ID;
                    break;
                case Medium:
                    // 2 byte ID
                    bytes[i[0]++] = (byte)0xFF;
                    bytes[i[0]++] = (byte)ID;
                    break;
                case Low:
                    // 4 byte ID
                    bytes[i[0]++] = (byte)0xFF;
                    bytes[i[0]++] = (byte)0xFF;
                    Utils.intToBytes(ID, bytes, i[0]);
                    i[0] += 2;
                    break;
            }
        }

        public void FromBytes(byte[] bytes, int pos[], int packetEnd[])
        {
            Header header = BuildHeader(bytes, pos, packetEnd);
            this.AppendedAcks = header.AppendedAcks;
            this.Reliable = header.Reliable;
            this.Resent = header.Resent;
            this.Zerocoded = header.Zerocoded;
            this.Sequence = header.Sequence;
        }

        /// <summary>
        /// Convert the AckList to a byte array, used for packet serializing
        /// </summary>
        /// <param name="bytes">Reference to the target byte array</param>
        /// <param name="i">Beginning position to start writing to in the byte
        /// array, will be updated with the ending position of the ACK list</param>
        public void AcksToBytes(byte[] bytes, int i[])
        {
        	
            for(int j=0; j < AckList.length; j++ )
            {
                Utils.intToBytes((int)AckList[j], bytes, i[0]);
                i[0] += 4;
            }
            if (AckList.length > 0) { bytes[i[0]++] = (byte)AckList.length; }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="bytes"></param>
        /// <param name="pos"></param>
        /// <param name="packetEnd"></param>
        /// <returns></returns>
        public static Header BuildHeader(byte[] bytes, int[] pos, int[] packetEnd)
        {
//        	int[] packetEnd = new int[] {packetEnd2};
            Header header = new Header();
            byte flags = bytes[pos[0]];

            header.AppendedAcks = (flags & Helpers.MSG_APPENDED_ACKS) != 0;
            header.Reliable = (flags & Helpers.MSG_RELIABLE) != 0;
            header.Resent = (flags & Helpers.MSG_RESENT) != 0;
            header.Zerocoded = (flags & Helpers.MSG_ZEROCODED) != 0;
            header.Sequence = (long)((bytes[pos[0] + 1] << 24) + (bytes[pos[0] + 2] << 16) + (bytes[pos[0] + 3] << 8) + bytes[pos[0] + 4]);

            // Set the frequency and packet ID number
            if (bytes[pos[0] + 6] == 0xFF)
            {
                if (bytes[pos[0] + 7] == 0xFF)
                {
                    header.Frequency = PacketFrequency.Low;
                    if (header.Zerocoded && bytes[pos[0] + 8] == 0)
                        header.ID = bytes[pos[0] + 10];
                    else
                        header.ID = (int)((bytes[pos[0] + 8] << 8) + bytes[pos[0] + 9]);
                    
                    pos[0] += 10;
                }
                else
                {
                    header.Frequency = PacketFrequency.Medium;
                    header.ID = bytes[pos[0] + 7];

                    pos[0] += 8;
                }
            }
            else
            {
                header.Frequency = PacketFrequency.High;
                header.ID = bytes[pos[0] + 6];

                pos[0] += 7;
            }

            header.AckList = null;
            CreateAckList(header, bytes, packetEnd);

            return header;
        }
        
        /// <summary>
        /// 
        /// </summary>
        /// <param name="header"></param>
        /// <param name="bytes"></param>
        /// <param name="packetEnd"></param>
        static void CreateAckList(Header header, byte[] bytes, int[] packetEnd)
        {
            if (header.AppendedAcks)
            {
                int count = bytes[packetEnd[0]--];
                header.AckList = new long[count];
                
                for (int i = 0; i < count; i++)
                {
                    header.AckList[i] = (long)(
                        (bytes[(packetEnd[0] - i * 4) - 3] << 24) |
                        (bytes[(packetEnd[0] - i * 4) - 2] << 16) |
                        (bytes[(packetEnd[0] - i * 4) - 1] <<  8) |
                        (bytes[(packetEnd[0] - i * 4)    ]));
                }

                packetEnd[0] -= (count * 4);
            }
        }
    }
    