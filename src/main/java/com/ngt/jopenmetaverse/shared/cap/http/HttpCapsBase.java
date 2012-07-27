package com.ngt.jopenmetaverse.shared.cap.http;

import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

public class HttpCapsBase {
	
//	private static PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
//	
//	static {
//		connectionManager = new PoolingClientConnectionManager();
//		connectionManager.setMaxTotal(100);
//		connectionManager.closeIdleConnections(1000 * 60, TimeUnit.MILLISECONDS);
//	}
//	
//	 public static class RequestState
//     {
//         public HttpRequestBase Request;
//         public byte[] UploadData;
//         public int MillisecondsTimeout;
////         public delegate void OpenWriteEventHandler(HttpWebRequest request);
////         public delegate void DownloadProgressEventHandler(HttpWebRequest request, HttpWebResponse response, int bytesReceived, int totalBytesToReceive);
////         public delegate void RequestCompletedEventHandler(HttpWebRequest request, HttpWebResponse response, byte[] responseData, Exception error);
//         public Observer OpenWriteCallback;
//         public Observer DownloadProgressCallback;
//         public Observer CompletedCallback;
//         
//         public RequestState(HttpRequestBase request, byte[] uploadData, int millisecondsTimeout, Observer openWriteCallback,
//        		 Observer downloadProgressCallback, Observer completedCallback)
//         {
//             Request = request;
//             UploadData = uploadData;
//             MillisecondsTimeout = millisecondsTimeout;
//             OpenWriteCallback = openWriteCallback;
//             DownloadProgressCallback = downloadProgressCallback;
//             CompletedCallback = completedCallback;
//         }
//     }
//	 
//     public static HttpRequestBase DownloadStringAsync(URI address, X509Certificate clientCert, int millisecondsTimeout,
//    		 Observer downloadProgressCallback, Observer completedCallback)
//         {
//             // Create the request
//    	 	HttpGet request = SetupGetRequest(address, clientCert);
//             DownloadDataAsync(request, millisecondsTimeout, downloadProgressCallback, completedCallback);
//             return request;
//         }
//
//         public static void DownloadDataAsync(HttpRequestBase request, int millisecondsTimeout,
//        		 Observer downloadProgressCallback, Observer completedCallback)
//         {
//             // Create an object to hold all of the state for this request
//             RequestState state = new RequestState(request, null, millisecondsTimeout, null, downloadProgressCallback,
//                 completedCallback);
//
//             // Start the request for the remote server response
//             IAsyncResult result = request.BeginGetResponse(GetResponse, state);
//             // Register a timeout for the request
//             ThreadPool.RegisterWaitForSingleObject(result.AsyncWaitHandle, TimeoutCallback, state, millisecondsTimeout, true);
//         }
//         
//         private static HttpGet SetupGetRequest(URI address, X509Certificate clientCert) throws Exception
//         {
//             if (address == null)
//                 throw new Exception("address can not be null");
//             
//             HttpParams httpparams = new  BasicHttpParams();
//             httpparams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000L);
//             httpparams.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
//             
//             // Create the request
//             HttpClient httpclient = new DefaultHttpClient(connectionManager, httpparams);
//             HttpGet request = new HttpGet(address);
//             
//             /*
//              * TODO Need to support client side certificates
//              */
////             // Add the client certificate to the request if one was given
////             if (clientCert != null)
////                 request.ClientCertificates.Add(clientCert);
//
//             //TODO Need to set all of the following parameters on a connection instance
////             // Leave idle connections to this endpoint open for up to 60 seconds
////             request.ServicePoint.MaxIdleTime = 1000 * 60;
////             // Disable stupid Expect-100: Continue header
////             request.ServicePoint.Expect100Continue = false;
////             // Crank up the max number of connections per endpoint (default is 2!)
////             request.ServicePoint.ConnectionLimit = Math.Max(request.ServicePoint.ConnectionLimit, 32);
////             // Caps requests are never sent as trickles of data, so Nagle's
////             // coalescing algorithm won't help us
////             request.ServicePoint.UseNagleAlgorithm = false;
//             
//             /*
//              * Need handle following
//              */
////             // If not on mono, set accept-encoding header that allows response compression
////             if (Type.GetType("Mono.Runtime") == null)
////             {
////                 request.AutomaticDecompression = DecompressionMethods.GZip | DecompressionMethods.Deflate;
////             }
//             
//             return request;
//         }

}
