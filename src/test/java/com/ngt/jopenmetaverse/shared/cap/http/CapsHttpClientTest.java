package com.ngt.jopenmetaverse.shared.cap.http;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Observable;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.ngt.jopenmetaverse.shared.sim.events.EventObserver;
import com.ngt.jopenmetaverse.shared.structureddata.OSD;
import com.ngt.jopenmetaverse.shared.structureddata.OSDFormat;
import com.ngt.jopenmetaverse.shared.structureddata.llsd.BinaryLLSDOSDParser;
import com.ngt.jopenmetaverse.shared.structureddata.llsd.JsonLLSDOSDParser;
import com.ngt.jopenmetaverse.shared.structureddata.llsd.XmlLLSDOSDParser;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class CapsHttpClientTest {
 	public class DownloadProgressObserver extends EventObserver<CapsHttpRequestProgressArg>
 	{
		public void handleEvent(Observable arg0, CapsHttpRequestProgressArg arg1) {
			CapsHttpRequestProgressArg rcha = (CapsHttpRequestProgressArg) arg1;
			System.out.println("Download Progress, bytes recieved: " 
			+ rcha.bytesReceived + " total bytes revieved: " + rcha.totalBytesToReceive);
		}	
 	}	

 	public class RequestCompletedObserver extends EventObserver<CapsHttpRequestCompletedArg>
 	{
 		public boolean[] completed;
			public void handleEvent(Observable arg0, CapsHttpRequestCompletedArg arg1) {
			System.out.println("RequestCompletedObserver called ...");
			completed[0] = true;
			CapsHttpRequestCompletedArg rcha = (CapsHttpRequestCompletedArg) arg1;
			if(rcha.error != null)
			{
				rcha.error.printStackTrace();
				Assert.assertFalse(true);
			}
			else
			{
				OSD osd = rcha.getResult();
			}
		}	
 	}	
 	
	private FileServer fileServer;
 	
	@Before
	public void setup() throws Exception
	{
		 URL url = getClass().getClassLoader().getResource("data/");
		fileServer = new FileServer(9999, url.getPath());
		fileServer.start();
	}
	
	@After
	public void clean() throws Exception
	{
		fileServer.stop();
	}
	
 	
	@Test
	public void testGetRequestDefault() throws IOException, URISyntaxException {
//		 InputStream istream = getClass().getClassLoader().getResourceAsStream("data/files/test.txt");
//		 Assert.assertNotNull(istream);
//		 URL url = getClass().getClassLoader().getResource("data/");
//		 System.out.println(url.getPath());
//		downloadFile(new URI("http://www.owasp.org/images/5/56/OWASP_Testing_Guide_v3.pdf"));
		
//		 System.out.println(fileServer.createURI("/files/test.txt").toString());
//		 sleep(100000);
		
		
		String testSD1 = "[ \n" + 
				"{ \n" + 
				"\"region_id\": \"67153d5b-3659-afb4-8510-adda2c034649\", \n" + 
				"\"scale\": \"one minute\", \n" + 
				"\"simulator statistics\": { \n" + 
				"\"time dilation\": 0.9878624, \n" + 
				"\"sim fps\": 44.38898, \n" + 
//				"\"agent updates per second\": NaN, \n" + 
				"\"total task count\": 4.0, \n" + 
				"\"active task count\": 0.0, \n" + 
				"\"pending uploads\": 0.0001096525 \n" + 
				"}\n" + 
				"}\n" + 
				"]";		
		
		try{
			uploadFile(fileServer.createURI("/"), Utils.stringToBytes(testSD1), OSDFormat.Json, false);
			uploadFile(fileServer.createURI("/"), Utils.stringToBytes(testSD1), OSDFormat.Json, true);
		}
		catch(Exception e)
		{Assert.fail();}
	}
	
	@Test
	public void testPostRequestDefault() throws IOException, URISyntaxException 
	{	
		downloadFileAsync(fileServer.createURI("/files/json/ex1.txt"));
		downloadFileAsync(fileServer.createURI("/files/json/ex2.txt"));
		downloadFileAsync(fileServer.createURI("/files/json/ex3.txt"));
		
		try {
			downloadFile(fileServer.createURI("/files/json/ex1.txt"));
			downloadFile(fileServer.createURI("/files/json/ex2.txt"));
			downloadFile(fileServer.createURI("/files/json/ex3.txt"));
		} catch (InterruptedException e) {
			Assert.fail("Filed while downloading from server\n" + Utils.getExceptionStackTraceAsString(e));
		}
	}
	
	
	private void downloadFileAsync(URI url)
	{
		CapsHttpClient client = new CapsHttpClient(url);
		RequestCompletedObserver rco = new RequestCompletedObserver();
		rco.completed = new boolean[]{false};
		client.addRequestCompleteObserver(rco);
		client.addRequestProgressObservable(new DownloadProgressObserver());
		client.BeginGetResponse(1000000);	
		
		while(!rco.completed[0])
		{
			System.out.println("sleeping ...");			
			sleep(5000);
		}
	}

	private void downloadFile(URI url) throws InterruptedException
	{
		CapsHttpClient client = new CapsHttpClient(url);
		RequestCompletedObserver rco = new RequestCompletedObserver();
		rco.completed = new boolean[]{false};
		client.addRequestCompleteObserver(rco);
		client.addRequestProgressObservable(new DownloadProgressObserver());
		client.GetResponse(1000000);	
	}
	
	
	private void uploadFile(URI url, byte[] data, OSDFormat format, boolean async) throws Exception
	{
		OSD postData = null;
		switch (format)
		{
		case Xml:
			postData = XmlLLSDOSDParser.DeserializeLLSDXml(data);
			break;
		case Binary:
			postData = BinaryLLSDOSDParser.DeserializeLLSDBinary(data);
			break;
		case Json:
		default:
			postData = JsonLLSDOSDParser.DeserializeLLSDJson(data);
			break;
		}
		
		uploadFile(url, postData, format, async);
	}
	
	private void uploadFile(URI url, OSD data, OSDFormat format, boolean async) throws Exception
	{
		CapsHttpClient client = new CapsHttpClient(url);
		RequestCompletedObserver rco = new RequestCompletedObserver();
		rco.completed = new boolean[]{false};
		client.addRequestCompleteObserver(rco);
		client.addRequestProgressObservable(new DownloadProgressObserver());
		if(async)
		{
		client.BeginGetResponse(data, format, 1000000);	
		
		while(!rco.completed[0])
		{
			System.out.println("sleeping ...");			
			sleep(5000);
		}
		}
		else
		{
			client.GetResponse(data, format, 1000000);	
		}
	}
	
	
	private void sleep(int timeout)
	{
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
