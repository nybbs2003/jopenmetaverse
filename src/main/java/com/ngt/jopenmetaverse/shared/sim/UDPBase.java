package com.ngt.jopenmetaverse.shared.sim;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import com.ngt.jopenmetaverse.shared.sim.buffers.UDPPacketBuffer;
import com.ngt.jopenmetaverse.shared.sim.events.ThreadPool;
import com.ngt.jopenmetaverse.shared.sim.events.ThreadPoolFactory;
import com.ngt.jopenmetaverse.shared.util.JLogger;
import com.ngt.jopenmetaverse.shared.util.Utils;

public abstract class UDPBase {
	// these abstract methods must be implemented in a derived class to actually do
	// something with the packets that are sent and received.
	protected abstract void PacketReceived(UDPPacketBuffer buffer);
	protected abstract void PacketSent(UDPPacketBuffer buffer, int bytesSent);
	private static ThreadPool threadPool = ThreadPoolFactory.getThreadPool();


	// the port to listen on
	protected int udpPort;

	// the remote endpoint to communicate with
	protected InetSocketAddress remoteEndPoint = null;

	// the UDP socket
	private DatagramSocket udpSocket;

	// the all important shutdownFlag.
	private volatile boolean shutdownFlag = true;

	/// <summary>
	/// Initialize the UDP packet handler in server mode
	/// </summary>
	/// <param name="port">Port to listening for incoming UDP packets on</param>
	public UDPBase(int port)
	{
		udpPort = port;
	}

	/// <summary>
	/// Initialize the UDP packet handler in client mode
	/// </summary>
	/// <param name="endPoint">Remote UDP server to connect to</param>
	public UDPBase(InetSocketAddress endPoint)
	{
		remoteEndPoint = endPoint;
		udpPort = 0;
	}

	/// <summary>
	/// 
	/// </summary>
	public void Start() throws SocketException
	{
		if (shutdownFlag)
		{
			//            final int SIO_UDP_CONNRESET = -1744830452;

			SocketAddress ipep = new InetSocketAddress(Settings.BIND_ADDR, udpPort);
			udpSocket = new DatagramSocket(ipep);
			//            try
			//            {
			//                // this udp socket flag is not supported under mono, 
			//                // so we'll catch the exception and continue
			//                udpSocket.IOControl(SIO_UDP_CONNRESET, new byte[] { 0 }, null);
			//            }
			//            catch (SocketException)
			//            {
			//                Logger.DebugLog("UDP SIO_UDP_CONNRESET flag not supported on this platform");
			//            }
			//            udpSocket.bind(ipep);

			// we're not shutting down, we're starting up
			shutdownFlag = false;

			// kick off an async receive.  The Start() method will return, the
			// actual receives will occur asynchronously and will be caught in
			// AsyncEndRecieve().
			AsyncBeginReceive();
		}
	}

	/// <summary>
	/// 
	/// </summary>
	public void Stop()
	{
		if (!shutdownFlag)
		{
			// wait indefinitely for a writer lock.  Once this is called, the .NET runtime
			// will deny any more reader locks, in effect blocking all other send/receive
			// threads.  Once we have the lock, we set shutdownFlag to inform the other
			// threads that the socket is closed.
			shutdownFlag = true;
			udpSocket.close();
		}
	}

	/// <summary>
	/// 
	/// </summary>
	public boolean isRunning()
	{
		return !shutdownFlag; 
	}

	private void AsyncBeginReceive()
	{
		// allocate a packet buffer
		//WrappedObject<UDPPacketBuffer> wrappedBuffer = Pool.CheckOut();
		final UDPPacketBuffer buf = new UDPPacketBuffer();

		if (!shutdownFlag)
		{
			threadPool.execute(new Runnable(){
				public void run()
				{
					DatagramPacket receivePacket = new DatagramPacket(buf.getData(), buf.getData().length);
					try {
						udpSocket.receive(receivePacket);
						buf.setDataLength(receivePacket.getLength());
						buf.setRemoteEndPoint(receivePacket.getSocketAddress());
						//Start another receiving, it will keep the server going
						AsyncBeginReceive();
						
						PacketReceived(buf);
					}

					//                	
					//                    // kick off an async read
					//                    udpSocket.BeginReceiveFrom(
					//                        //wrappedBuffer.Instance.Data,
					//                        buf.Data,
					//                        0,
					//                        UDPPacketBuffer.BUFFER_SIZE,
					//                        SocketFlags.None,
					//                        ref buf.RemoteEndPoint,
					//                        AsyncEndReceive,
					//                        //wrappedBuffer);
					//                        buf);

						catch (Exception e) {
							//TODO handle error in a better way
							JLogger.error("Error while recieving packet\n" + Utils.getExceptionStackTraceAsString(e));
						
					}
					//                catch (SocketException e)
					//                {
					//                    if (e.SocketErrorCode == SocketError.ConnectionReset)
					//                    {
					//                        Logger.Log("SIO_UDP_CONNRESET was ignored, attempting to salvage the UDP listener on port " + udpPort, Helpers.LogLevel.Error);
					//                        bool salvaged = false;
					//                        while (!salvaged)
					//                        {
					//                            try
					//                            {
					//                                udpSocket.BeginReceiveFrom(
					//                                    //wrappedBuffer.Instance.Data,
					//                                    buf.Data,
					//                                    0,
					//                                    UDPPacketBuffer.BUFFER_SIZE,
					//                                    SocketFlags.None,
					//                                    ref buf.RemoteEndPoint,
					//                                    AsyncEndReceive,
					//                                    //wrappedBuffer);
					//                                    buf);
					//                                salvaged = true;
					//                            }
					//                            catch (SocketException) { }
					//                            catch (ObjectDisposedException) { return; }
					//                        }
					//
					//                        Logger.Log("Salvaged the UDP listener on port " + udpPort, Helpers.LogLevel.Info);
					//                    }
					//                }
					//                catch (ObjectDisposedException) { }
				}
			});


		}
	}

	//    private void AsyncEndReceive(IAsyncResult iar)
	//    {
	//        // Asynchronous receive operations will complete here through the call
	//        // to AsyncBeginReceive
	//        if (!shutdownFlag)
	//        {
	//            // start another receive - this keeps the server going!
	//            AsyncBeginReceive();
	//
	//            // get the buffer that was created in AsyncBeginReceive
	//            // this is the received data
	//            //WrappedObject<UDPPacketBuffer> wrappedBuffer = (WrappedObject<UDPPacketBuffer>)iar.AsyncState;
	//            //UDPPacketBuffer buffer = wrappedBuffer.Instance;
	//            UDPPacketBuffer buffer = (UDPPacketBuffer)iar.AsyncState;
	//
	//            try
	//            {
	//                // get the length of data actually read from the socket, store it with the
	//                // buffer
	//                buffer.DataLength = udpSocket.EndReceiveFrom(iar, ref buffer.RemoteEndPoint);
	//
	//                // call the abstract method PacketReceived(), passing the buffer that
	//                // has just been filled from the socket read.
	//                PacketReceived(buffer);
	//            }
	//            catch (SocketException) { }
	//            catch (ObjectDisposedException) { }
	//            //finally { wrappedBuffer.Dispose(); }
	//        }
	//    }

	public void AsyncBeginSend(final UDPPacketBuffer buf)
	{
		if (!shutdownFlag)
		{
			threadPool.execute(new Runnable(){
				public void run()
				{
					try {
						DatagramPacket sendPacket = new DatagramPacket(buf.getData(), buf.getDataLength(), buf.getRemoteEndPoint());
						JLogger.debug(String.format("Data sending to server of length %d \n%s ", buf.getDataLength(), Utils.bytesToHexDebugString(buf.getData(), "")));
						udpSocket.send(sendPacket);
						PacketSent(buf, buf.getDataLength());
					}
					catch (Exception e) {
						//TODO handle error in a better way
						JLogger.error("Error while sending packet\n" + Utils.getExceptionStackTraceAsString(e));
					}
				}
			});

			//            try
			//            {
			//                // Profiling heavily loaded clients was showing better performance with 
			//                // synchronous UDP packet sending
			//                udpSocket.SendTo(
			//                    buf.Data,
			//                    0,
			//                    buf.DataLength,
			//                    SocketFlags.None,
			//                    buf.RemoteEndPoint);
			//
			//                //udpSocket.BeginSendTo(
			//                //    buf.Data,
			//                //    0,
			//                //    buf.DataLength,
			//                //    SocketFlags.None,
			//                //    buf.RemoteEndPoint,
			//                //    AsyncEndSend,
			//                //    buf);
			//            }
			//            catch (SocketException) { }
			//            catch (ObjectDisposedException) { }
		}
	}

	//void AsyncEndSend(IAsyncResult result)
	//{
	//    try
	//    {
	//        UDPPacketBuffer buf = (UDPPacketBuffer)result.AsyncState;
	//        if (!udpSocket.Connected) return;
	//        int bytesSent = udpSocket.EndSendTo(result);

	//        PacketSent(buf, bytesSent);
	//    }
	//    catch (SocketException) { }
	//    catch (ObjectDisposedException) { }
	//}
}

