package com.ngt.jopenmetaverse.shared.cap.http;

import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ngt.jopenmetaverse.shared.cap.http.DownloadManager.DownloadRequest;
import com.ngt.jopenmetaverse.shared.sim.events.AutoResetEvent;
import com.ngt.jopenmetaverse.shared.sim.events.MethodDelegate;
import com.ngt.jopenmetaverse.shared.util.PlatformUtils;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class DownloadManagerTest 
{
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
	public void queueDownladMultipleConcurentFilesTests()
	{
		try
		{
			final int max = 3000;
			final int p = 50;
			final AutoResetEvent event = new AutoResetEvent(false);
			DownloadManager downloadManager = new DownloadManager();
			final AtomicInteger noItemsDownloaded = new AtomicInteger(0);
			for(int j =0; j< max/p; j++)
			{
			for(int i = 0; i< p; i++)
			{
				final int itemno = i;
				DownloadRequest request = new DownloadRequest(fileServer.createURI("/files/json/ex1.txt"), 5000, 
						"*/*", new MethodDelegate<Void,HttpBaseDownloadProgressArg>()
						{
					public Void execute(HttpBaseDownloadProgressArg e) {
						return null;
					}

						}, new MethodDelegate<Void,HttpBaseRequestCompletedArg>()
						{
							public Void execute(HttpBaseRequestCompletedArg e) {
								if(e.getError() != null)
								{
									System.out.println(Utils.getExceptionStackTraceAsString(e.getError()));
									event.set();
								}
								else if(e.getResponseData().length <= 0)
								{
									event.set();
								}
								Assert.assertNull(e.getError());
								Assert.assertTrue(e.getResponseData().length > 0);
								noItemsDownloaded.incrementAndGet();
								System.out.println("Downloaded item: " + noItemsDownloaded.intValue() + " of size " + e.getResponseData().length);
								if(max <= noItemsDownloaded.intValue())
									event.set();
								return null;
							}
						});
				downloadManager.QueueDownlad(request);
			}
				PlatformUtils.sleep(500);
			}
			System.out.println("Main Thread is going to sleep...");
			event.waitOne();
		}
		catch(Exception e)
		{Assert.fail(Utils.getExceptionStackTraceAsString(e));}
	}


}
