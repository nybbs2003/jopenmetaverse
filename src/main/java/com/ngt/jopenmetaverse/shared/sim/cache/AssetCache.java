package com.ngt.jopenmetaverse.shared.sim.cache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import com.ngt.jopenmetaverse.shared.sim.GridClient;
import com.ngt.jopenmetaverse.shared.sim.Settings;
import com.ngt.jopenmetaverse.shared.sim.events.EventObserver;
import com.ngt.jopenmetaverse.shared.sim.events.EventTimer;
import com.ngt.jopenmetaverse.shared.sim.events.MethodDelegate;
import com.ngt.jopenmetaverse.shared.sim.events.ThreadPool;
import com.ngt.jopenmetaverse.shared.sim.events.ThreadPoolFactory;
import com.ngt.jopenmetaverse.shared.sim.events.asm.ComputeAssetCacheFilenameEventArgs;
import com.ngt.jopenmetaverse.shared.sim.events.asm.ImageDownload;
import com.ngt.jopenmetaverse.shared.sim.events.nm.DisconnectedEventArgs;
import com.ngt.jopenmetaverse.shared.sim.login.LoginProgressEventArgs;
import com.ngt.jopenmetaverse.shared.sim.login.LoginStatus;
import com.ngt.jopenmetaverse.shared.types.Enums.AssetType;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.FileUtils;
import com.ngt.jopenmetaverse.shared.util.JLogger;
import com.ngt.jopenmetaverse.shared.util.Utils;

/// <summary>
/// Class that handles the local asset cache
/// </summary>
public class AssetCache implements IAssetCache
{
	// User can plug in a routine to compute the asset cache location
	//        public delegate String ComputeAssetCacheFilenameDelegate(String cacheDir, UUID assetID);

	//        public ComputeAssetCacheFilenameDelegate ComputeAssetCacheFilename = null;

	MethodDelegate<String, ComputeAssetCacheFilenameEventArgs> computeAssetCacheFilenameDelegate = null;

	public MethodDelegate<String, ComputeAssetCacheFilenameEventArgs> getComputeAssetCacheFilenameDelegate() {
		return computeAssetCacheFilenameDelegate;
	}
	public void setComputeAssetCacheFilenameDelegate(
			MethodDelegate<String, ComputeAssetCacheFilenameEventArgs> computeAssetCacheFilenameDelegate) {
		this.computeAssetCacheFilenameDelegate = computeAssetCacheFilenameDelegate;
	}

	private static ThreadPool threadPool = ThreadPoolFactory.getThreadPool();


	private GridClient Client;
	//	private Thread cleanerThread;
	private AtomicBoolean isCleanerThread = new AtomicBoolean(false); 
	private EventTimer cleanerTimer;
	private double pruneInterval = 1000 * 60 * 5;
	private boolean autoPruneEnabled = true;
	private AssetTagMap assetTagMap = new AssetTagMap();

	/// <summary>
	/// Allows setting weather to periodicale prune the cache if it grows too big
	/// Default is enabled, when caching is enabled
	/// </summary>
	public boolean getAutoPruneEnabled()
	{
		return autoPruneEnabled; 
	}

	public void setAutoPruneEnabled(boolean value)
	{
		autoPruneEnabled = value;

		if (autoPruneEnabled)
		{
			SetupTimer();
		}
		else
		{
			DestroyTimer();
		}
	}

	/// <summary>
	/// How long (in ms) between cache checks (default is 5 min.) 
	/// </summary>
	public void setAutoPruneInterval(double value)
	{
		pruneInterval = value;
		SetupTimer();
	}

	public double getAutoPruneInterval()
	{
		return pruneInterval;
	}

	/// <summary>
	/// Default constructor
	/// </summary>
	/// <param name="client">A reference to the GridClient object</param>
	public AssetCache(GridClient client)
	{
		Client = client;          
		Client.network.RegisterLoginProgressCallback(new EventObserver<LoginProgressEventArgs>()
				{
			@Override
			public void handleEvent(Observable o, LoginProgressEventArgs e) {
				if (e.getStatus() == LoginStatus.Success)
				{
					SetupTimer();
				}}});

		Client.network.RegisterOnDisconnectedCallback(new EventObserver<DisconnectedEventArgs>()
				{
			@Override
			public void handleEvent(Observable o, DisconnectedEventArgs e) {
				DestroyTimer();
			}
				});

		//            Client.network.LoginProgress += delegate(object sender, LoginProgressEventArgs e)
		//            {
		//                if (e.Status == LoginStatus.Success)
		//                {
		//                    SetupTimer();
		//                }
		//            };
		//
		//            Client.Network.Disconnected += delegate(object sender, DisconnectedEventArgs e) { DestroyTimer(); };
	}


	/// <summary>
	/// Disposes cleanup timer
	/// </summary>
	private void DestroyTimer()
	{
		if (cleanerTimer != null)
		{
			cleanerTimer.cancel();
			cleanerTimer = null;
		}
	}

	/// <summary>
	/// Only create timer when needed
	/// </summary>
	private void SetupTimer()
	{
		if (Operational() && autoPruneEnabled && Client.network.getConnected())
		{
			if (cleanerTimer == null)
			{
				cleanerTimer = new EventTimer(new TimerTask()
				{
					@Override
					public void run() {
						cleanerTimer_Elapsed();
					}                		
				});

				//                    cleanerTimer = new System.Timers.Timer(pruneInterval);
				//                    cleanerTimer.Elapsed += new System.Timers.ElapsedEventHandler(cleanerTimer_Elapsed);
			}
			cleanerTimer.schedule((long)pruneInterval, (int)pruneInterval);

			//                cleanerTimer.Interval = pruneInterval;
			//                cleanerTimer.Enabled = true;
		}
	}

	/// <summary>
	/// Return bytes read from the local asset cache, null if it does not exist
	/// </summary>
	/// <param name="assetID">UUID of the asset we want to get</param>
	/// <returns>Raw bytes of the asset, or null on failure</returns>
	public byte[] getCachedAssetBytes(UUID assetID)
	{
		if (!Operational())
		{
			return null;
		}
		try
		{
			byte[] data = null;
			File f = new File(FileName(assetID));
			if (f.exists())
			{
				JLogger.debug("Reading " + FileName(assetID) + " from asset cache.");
				FileInputStream fs = new FileInputStream(f); 
				//                    data = File.ReadAllBytes(FileName(assetID));
				data = FileUtils.readBytes(fs);
				fs.close();
			}
			else
			{
				JLogger.debug("Reading " + StaticFileName(assetID) + " from static asset cache.");
				//                    data = File.ReadAllBytes(StaticFileName(assetID));
				f = new File(StaticFileName(assetID));
				if(f.exists())
				{
					FileInputStream fs = new FileInputStream(f); 
					data = FileUtils.readBytes(fs);
				}
			}
			assetTagMap.assetAccessed(assetID);
			return data;
		}
		catch (Exception ex)
		{
			JLogger.warn("Failed reading asset from cache (" + ex.getMessage() + ")");
			return null;
		}
	}


	/// <summary>
	/// Returns ImageDownload object of the
	/// image from the local image cache, null if it does not exist
	/// </summary>
	/// <param name="imageID">UUID of the image we want to get</param>
	/// <returns>ImageDownload object containing the image, or null on failure</returns>
	public ImageDownload getCachedImage(UUID imageID)
	{
		if (!Operational())
			return null;

		byte[] imageData = getCachedAssetBytes(imageID);
		if (imageData == null)
			return null;
		ImageDownload transfer = new ImageDownload();
		transfer.AssetType = AssetType.Texture;
		transfer.ID = imageID;
		transfer.Simulator = Client.network.getCurrentSim();
		transfer.Size = imageData.length;
		transfer.Success = true;
		transfer.Transferred = imageData.length;
		transfer.AssetData = imageData;
		return transfer;
	}

	/// <summary>
	/// Saves an asset to the local cache
	/// </summary>
	/// <param name="assetID">UUID of the asset</param>
	/// <param name="assetData">Raw bytes the asset consists of</param>
	/// <returns>Weather the operation was successfull</returns>
	public boolean saveAssetToCache(UUID assetID, byte[] assetData)
	{
		if (!Operational())
		{
			return false;
		}

		try
		{
			JLogger.debug("Saving " + FileName(assetID) + " to asset cache.");
			File dir = new File(Client.settings.ASSET_CACHE_DIR);
			if (!dir.exists())
			{
				//                    Directory.CreateDirectory(Client.settings.ASSET_CACHE_DIR);
				new File(Client.settings.ASSET_CACHE_DIR).mkdirs();
			}

			//                File.WriteAllBytes(FileName(assetID), assetData);
			File f = new File(FileName(assetID));
			FileOutputStream fos = new FileOutputStream(f);
			FileUtils.writeBytes(fos, assetData);
			assetTagMap.assetAdded(assetID);
			fos.close();
		}
		catch (Exception ex)
		{
			JLogger.warn("Failed saving asset to cache (" + ex.getMessage() + ")" + "\n" + Utils.getExceptionStackTraceAsString(ex));
			return false;
		}

		return true;
	}

	/// <summary>
	/// Get the file name of the asset stored with gived UUID
	/// </summary>
	/// <param name="assetID">UUID of the asset</param>
	/// <returns>Null if we don't have that UUID cached on disk, file name if found in the cache folder</returns>
	public String AssetFileName(UUID assetID)
	{
		if (!Operational())
		{
			return null;
		}

		String fileName = FileName(assetID);
		File f = new File(fileName);
		if (f.exists())
			return fileName;
		else
			return null;
	}

	public File getAssetFile(UUID assetID)
	{
		if (!Operational())
		{
			return null;
		}

		String fileName = FileName(assetID);
		File f = new File(fileName);
		if (f.exists())
			return f;
		else
			return null;
	}


	/// <summary>
	/// Checks if the asset exists in the local cache
	/// </summary>
	/// <param name="assetID">UUID of the asset</param>
	/// <returns>True is the asset is stored in the cache, otherwise false</returns>
	public boolean hasAsset(UUID assetID)
	{
		if (!Operational())
			return false;
		else
		{
			String fileName = FileName(assetID);
			File f = new File(fileName);
			if (f.exists())
				return true;
			else
			{
				fileName = StaticFileName(assetID);
				return new File(fileName).exists();
			}
		}
	}

	/// <summary>
	/// Wipes out entire cache
	/// </summary>
	public void clear()
	{
		String cacheDir = Client.settings.ASSET_CACHE_DIR;
		File dir = new File(cacheDir);
		assetTagMap.clear();
		if (!dir.exists())
		{
			return;
		}

		//            DirectoryInfo di = new DirectoryInfo(cacheDir);
		//            // We save file with UUID as file name, only delete those
		//            FileInfo[] files = di.GetFiles("????????-????-????-????-????????????", SearchOption.TopDirectoryOnly);

//		File[] files = getFileList(cacheDir, "????????-????-????-????-????????????", true);
		File[] files = FileUtils.getFileList(cacheDir, "\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}", true);

		
		int num = 0;
		for (File file : files)
		{
			file.delete();
			++num;
		}

		JLogger.debug("Wiped out " + num + " files from the cache directory.");
	}

	/// <summary>
	/// Brings cache size to the 90% of the max size
	/// </summary>
	public void prune()
	{
		String cacheDir = Client.settings.ASSET_CACHE_DIR;
		File dir = new File(cacheDir);

		if (!dir.exists())
		{
			return;
		}

		//            DirectoryInfo di = new DirectoryInfo(cacheDir);
		//            // We save file with UUID as file name, only count those
		//            FileInfo[] files = di.GetFiles("????????-????-????-????-????????????", SearchOption.TopDirectoryOnly);

		File[] files = FileUtils.getFileList(cacheDir, "\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}", true);
		long size = GetFileSize(files);

		if (size > Client.settings.ASSET_CACHE_MAX_SIZE)
		{
			//                Array.Sort(files, new SortFilesByAccesTimeHelper());
			List<UUID> assetIDs = assetTagMap.getAssets();
			long targetSize = (long)(Client.settings.ASSET_CACHE_MAX_SIZE * 0.9);
			int num = 0;
			File file = null;
			for (UUID assetID : assetIDs)
			{
				if((file = getAssetFile(assetID))!=null)
				{
					++num;
					size -= file.length();
					file.delete();
					if (size < targetSize)
					{
						break;
					}
				}
			}
			JLogger.debug(num + " files deleted from the cache, cache size now: " + NiceFileSize(size));
		}
		else
		{
			JLogger.debug("Cache size is " + NiceFileSize(size) + ", file deletion not needed");
		}

	}

	/// <summary>
	/// Asynchronously brings cache size to the 90% of the max size
	/// </summary>
	public void beginPrune()
	{
		//TODO need to verify 
		//            // Check if the background cache cleaning thread is active first
		//            if (cleanerThread != null && cleanerThread.isAlive())
		//            {
		//                return;
		//            }

		if(!isCleanerThread.get())
		{
			isCleanerThread.set(true);
			threadPool.execute(new Runnable() {
				public void run() {
					isCleanerThread.set(true);
					synchronized (this)
					{
						//                cleanerThread = new Thread(new ThreadStart(this.Prune));
						//                cleanerThread.IsBackground = true;
						//                cleanerThread.Start();
						prune();
					}
					isCleanerThread.set(false);
				}
			});
		}
	}

	/// <summary>
	/// Constructs a file name of the cached asset
	/// </summary>
	/// <param name="assetID">UUID of the asset</param>
	/// <returns>String with the file name of the cahced asset</returns>
	private String FileName(UUID assetID)
	{
		if (computeAssetCacheFilenameDelegate != null)
		{
			return computeAssetCacheFilenameDelegate.execute(new ComputeAssetCacheFilenameEventArgs(Client.settings.ASSET_CACHE_DIR, assetID));
		}
		return Client.settings.ASSET_CACHE_DIR + "/" + assetID.toString();
	}

	/// <summary>
	/// Constructs a file name of the static cached asset
	/// </summary>
	/// <param name="assetID">UUID of the asset</param>
	/// <returns>String with the file name of the static cached asset</returns>
	private String StaticFileName(UUID assetID)
	{
		return Settings.RESOURCE_DIR + "/" + "static_assets" + "/" + assetID.toString();
	}


	/// <summary>
	/// Adds up file sizes passes in a FileInfo array
	/// </summary>
	private long GetFileSize(File[] files)
	{
		long ret = 0;
		for (File file : files)
		{
			ret += file.length();
		}
		return ret;
	}

	/// <summary>
	/// Checks whether caching is enabled
	/// </summary>
	private boolean Operational()
	{
		return Client.settings.USE_ASSET_CACHE;
	}

	/// <summary>
	/// Periodically prune the cache
	/// </summary>
	private void cleanerTimer_Elapsed()
	{
		beginPrune();
	}

	/// <summary>
	/// Nicely formats file sizes
	/// </summary>
	/// <param name="byteCount">Byte size we want to output</param>
	/// <returns>String with humanly readable file size</returns>
	private String NiceFileSize(long byteCount)
	{
		String size = "0 Bytes";
		if (byteCount >= 1073741824)
			size = String.format("%f", byteCount / 1073741824.0) + " GB";
		else if (byteCount >= 1048576)
			size = String.format("%f", byteCount / 1048576.0) + " MB";
		else if (byteCount >= 1024)
			size = String.format("%f", byteCount / 1024.0) + " KB";
		else if (byteCount > 0 && byteCount < 1024)
			size = byteCount + " Bytes";

		return size;
	}

	//        /// <summary>
	//        /// Helper class for sorting files by their last accessed time
	//        /// </summary>
	//        private class SortFilesByAccesTimeHelper : IComparer<FileInfo>
	//        {
	//            int IComparer<FileInfo>.Compare(File f1, File f2)
	//            {
	//                if (f1.LastAccessTime > f2.LastAccessTime)
	//                    return 1;
	//                if (f1.LastAccessTime < f2.LastAccessTime)
	//                    return -1;
	//                else
	//                    return 0;
	//            }
	//        }

}