package com.ngt.jopenmetaverse.shared.sim;

import com.ngt.jopenmetaverse.shared.sim.stats.UtilizationStatistics;

public class GridClient {
    /// <summary>Networking subsystem</summary>
    public NetworkManager network;
    /// <summary>Settings class including constant values and changeable
    /// parameters for everything</summary>
    public Settings settings;
    /// <summary>Parcel (subdivided simulator lots) subsystem</summary>
    public ParcelManager parcels;
    /// <summary>Our own avatars subsystem</summary>
    public AgentManager self;
    /// <summary>Other avatars subsystem</summary>
    public AvatarManager avatars;
    /// <summary>Estate subsystem</summary>
    public EstateTools estate;
    /// <summary>Friends list subsystem</summary>
    public FriendsManager friends;
    /// <summary>Grid (aka simulator group) subsystem</summary>
    public GridManager grid;
    /// <summary>Object subsystem</summary>
    public ObjectManager objects;
    /// <summary>Group subsystem</summary>
    public GroupManager groups;
    /// <summary>Asset subsystem</summary>
    public AssetManager assets;
    /// <summary>Appearance subsystem</summary>
    public AppearanceManager appearance;
    /// <summary>Inventory subsystem</summary>
    public InventoryManager inventory;
    /// <summary>Directory searches including classifieds, people, land 
    /// sales, etc</summary>
    public DirectoryManager directory;
    /// <summary>Handles land, wind, and cloud heightmaps</summary>
    public TerrainManager terrain;
    /// <summary>Handles sound-related networking</summary>
    public SoundManager sound;
    /// <summary>Throttling total bandwidth usage, or allocating bandwidth
    /// for specific data stream types</summary>
    public AgentThrottle throttle;

    public UtilizationStatistics stats;
    /// <summary>
    /// Default constructor
    /// </summary>
    public GridClient()
    {
        // These are order-dependant
        network = new NetworkManager(this);
        settings = new Settings(this);
        parcels = new ParcelManager(this);
        self = new AgentManager(this);
        avatars = new AvatarManager(this);
        estate = new EstateTools(this);
        friends = new FriendsManager(this);
        grid = new GridManager(this);
        objects = new ObjectManager(this);
        groups = new GroupManager(this);
        assets = new AssetManager(this);
        appearance = new AppearanceManager(this);
        inventory = new InventoryManager(this);
        directory = new DirectoryManager(this);
        terrain = new TerrainManager(this);
        sound = new SoundManager(this);
        throttle = new AgentThrottle(this);
        stats = new UtilizationStatistics();            
    }

    /// <summary>
    /// Return the full name of this instance
    /// </summary>
    /// <returns>Client avatars full name</returns>
    @Override
    public String toString()
    {
        return self.Name;
    }
}
