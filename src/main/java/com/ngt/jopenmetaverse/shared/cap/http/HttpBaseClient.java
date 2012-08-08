package com.ngt.jopenmetaverse.shared.cap.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Observable;
import javax.security.cert.X509Certificate;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ngt.jopenmetaverse.shared.sim.events.EventObservable;
import com.ngt.jopenmetaverse.shared.sim.events.ThreadPool;
import com.ngt.jopenmetaverse.shared.sim.events.ThreadPoolFactory;
import com.ngt.jopenmetaverse.shared.util.Utils;


/*
 * TODO
 *  * Handle client side certificates
 *  * Handle timeout
 *  * Handle HTTP error codes in getting and posting
 */
public class HttpBaseClient {

	private static HttpClient httpclient;

	private static ThreadPool threadPool = ThreadPoolFactory.getThreadPool();

	static 
	{
		//TODO manage httpclient level resources 
		httpclient = new DefaultHttpClient();

		// When HttpClient instance is no longer needed,
		// shut down the connection manager to ensure
		// immediate deallocation of all system resources
		//httpclient.getConnectionManager().shutdown();

	}

	public static HttpRequestBase UploadDataAsync(URI _Address,
			java.security.cert.X509Certificate _ClientCert, String contentType,
			byte[] postData, int millisecondsTimeout, Object object,
			final EventObservable downloadProgressObservable,
			final EventObservable requestCompletedObservable) 
	{
		   HttpPost httppost = new HttpPost(_Address);
		   
		   if(!Utils.isNullOrEmpty(contentType))
			   httppost.setHeader("Content-Type", contentType);

	        httppost.setEntity(new ByteArrayEntity(postData));

			HttpRequestStringThreadPoolTaskAsyc(httppost, millisecondsTimeout, 
					downloadProgressObservable, requestCompletedObservable);
		    return httppost;
	}


	public static HttpRequestBase DownloadStringAsync(URI _Address, 
			java.security.cert.X509Certificate _ClientCert,
			final int millisecondsTimeout,
			final EventObservable downloadProgressObservable,
			final EventObservable requestCompletedObservable)
			{
		// Prepare a request object
		final HttpGet httpget = new HttpGet(_Address);

		HttpRequestStringThreadPoolTaskAsyc(httpget, millisecondsTimeout, 
				downloadProgressObservable, requestCompletedObservable);
		
		return httpget;

			}
	
	private static void HttpRequestStringThreadPoolTaskAsyc(final HttpRequestBase httprequest, final int millisecondsTimeout,
			final EventObservable downloadProgressObservable,
			final EventObservable requestCompletedObservable)
	{
		threadPool.execute(new Runnable(){
			public void run()
			{
				HttpRequestStringThreadPoolTask(httprequest, millisecondsTimeout, 
						downloadProgressObservable, requestCompletedObservable);
			}
		});
	}
	
	private static void HttpRequestStringThreadPoolTask(HttpRequestBase httpget, int millisecondsTimeout,
			final EventObservable downloadProgressObservable,
			final EventObservable requestCompletedObservable)
	{
		// Execute the request
		HttpResponse response = null;
		int statusCode;
		try {
			response = httpclient.execute(httpget);

			// Examine the response status
//			System.out.println(response.getStatusLine());
			statusCode = response.getStatusLine().getStatusCode();

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();
				try {

					byte[] bytes = readBytes(httpget, response, instream, downloadProgressObservable);
					HttpBaseRequestCompletedArg r = new HttpBaseRequestCompletedArg(httpget, response, bytes, null);
					requestCompletedObservable.raiseEvent(r);
				} catch (IOException ex) {

					// In case of an IOException the connection will be released
					// back to the connection manager automatically
					throw ex;

				} catch (RuntimeException ex) {

					// In case of an unexpected exception you may want to abort
					// the HTTP request in order to shut down the underlying
					// connection and release it back to the connection manager.
					httpget.abort();
					throw ex;

				} finally {

					// Closing the input stream will trigger connection release
					instream.close();
				}
			}
			else
			{
				HttpBaseRequestCompletedArg r = new HttpBaseRequestCompletedArg(httpget, response, new byte[0], null);
				requestCompletedObservable.raiseEvent(r);
			}
		} 
		catch (Exception e) {
			HttpBaseRequestCompletedArg r = new HttpBaseRequestCompletedArg(httpget, null, null, e);
			requestCompletedObservable.raiseEvent(r);
		}
	}
	
	
	private static byte[] readBytes(HttpRequestBase request, HttpResponse response, InputStream input, 
			final EventObservable downloadProgressObservable) throws IOException
	{
		int totalBytesRead = 0;
		int bytesRead = 0;
		//TODO Need to review following LIMITs and Performance Parameters
		final int maxByteRead = 10000;
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream(2048);
		byte[] bytes = new byte[maxByteRead];

		//TODO byteBuffer should not grow forever. There should be some upper limit
		while(bytesRead >= 0){
//			System.out.println("Starting Reading...");
			bytesRead = input.read(bytes, 0, maxByteRead); 
			if (bytesRead > 0){
				totalBytesRead = totalBytesRead + bytesRead;
				byteBuffer.write(bytes, 0, bytesRead);
//				System.out.println("Read: " + bytesRead);
				HttpBaseDownloadProgressArg r = new HttpBaseDownloadProgressArg(request, response, bytesRead, totalBytesRead);
				downloadProgressObservable.raiseEvent(r);
			}
		}
		return byteBuffer.toByteArray();
	}


}

