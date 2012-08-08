package com.ngt.jopenmetaverse.shared.sim.buffers;

import java.net.InetSocketAddress;

public class PacketBufferPool  
{
//
//	 private InetSocketAddress EndPoint;
//
//     /// <summary>
//     /// Initialize the object pool in client mode
//     /// </summary>
//     /// <param name="endPoint">Server to connect to</param>
//     /// <param name="itemsPerSegment"></param>
//     /// <param name="minSegments"></param>
//     public PacketBufferPool(InetSocketAddress endPoint, int itemsPerSegment, int minSegments)
//         : base()
//     {
//         EndPoint = endPoint;
//         Initialize(itemsPerSegment, minSegments, true, 1000 * 60 * 5);
//     }
//
//     /// <summary>
//     /// Initialize the object pool in server mode
//     /// </summary>
//     /// <param name="itemsPerSegment"></param>
//     /// <param name="minSegments"></param>
//     public PacketBufferPool(int itemsPerSegment, int minSegments)
//         : base()
//     {
//         EndPoint = null;
//         Initialize(itemsPerSegment, minSegments, true, 1000 * 60 * 5);
//     }
//
//     /// <summary>
//     /// Returns a packet buffer with EndPoint set if the buffer is in
//     /// client mode, or with EndPoint set to null in server mode
//     /// </summary>
//     /// <returns>Initialized UDPPacketBuffer object</returns>
//     @Override
//     protected  UDPPacketBuffer GetObjectInstance()
//     {
//         if (EndPoint != null)
//             // Client mode
//             return new UDPPacketBuffer(EndPoint);
//         else
//             // Server mode
//             return new UDPPacketBuffer();
//     }
}
