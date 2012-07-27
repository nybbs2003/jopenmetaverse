package com.ngt.jopenmetaverse.shared.sim.events;

import com.ngt.jopenmetaverse.shared.structureddata.OSDMap;

	    /// <summary>
	    /// Capabilities is the name of the bi-directional HTTP REST protocol
	    /// used to communicate non real-time transactions such as teleporting or
	    /// group messaging
	    /// </summary>
	    public class Caps
	    {
	    	//TODO Need to implement
//	        /// <summary>
//	        /// Triggered when an event is received via the EventQueueGet 
//	        /// capability
//	        /// </summary>
//	        /// <param name="capsKey">Event name</param>
//	        /// <param name="message">Decoded event data</param>
//	        /// <param name="simulator">The simulator that generated the event</param>
//	        //public delegate void EventQueueCallback(String message, StructuredData.OSD body, Simulator simulator);
//
//	        public delegate void EventQueueCallback(String capsKey, IMessage message, Simulator simulator);
//
//	        /// <summary>Reference to the simulator this system is connected to</summary>
//	        public Simulator Simulator;
//
//	        public String _SeedCapsURI;
//	        public Dictionary<String, Uri> _Caps = new Dictionary<String, Uri>();
//
//	        private CapsClient _SeedRequest;
//	        private EventQueueClient _EventQueueCap = null;
//
//	        /// <summary>Capabilities URI this system was initialized with</summary>
//	        public String SeedCapsURI { get { return _SeedCapsURI; } }
//
//	        /// <summary>Whether the capabilities event queue is connected and
//	        /// listening for incoming events</summary>
//	        public boolean IsEventQueueRunning
//	        {
//	            get
//	            {
//	                if (_EventQueueCap != null)
//	                    return _EventQueueCap.Running;
//	                else
//	                    return false;
//	            }
//	        }
//
//	        /// <summary>
//	        /// Default constructor
//	        /// </summary>
//	        /// <param name="simulator"></param>
//	        /// <param name="seedcaps"></param>
//	        internal Caps(Simulator simulator, String seedcaps)
//	        {
//	            Simulator = simulator;
//	            _SeedCapsURI = seedcaps;
//
//	            MakeSeedRequest();
//	        }
//
//	        public void Disconnect(boolean immediate)
//	        {
//	            Logger.Log(String.Format("Caps system for {0} is {1}", Simulator,
//	                (immediate ? "aborting" : "disconnecting")), Helpers.LogLevel.Info, Simulator.Client);
//
//	            if (_SeedRequest != null)
//	                _SeedRequest.Cancel();
//
//	            if (_EventQueueCap != null)
//	                _EventQueueCap.Stop(immediate);
//	        }
//
//	        /// <summary>
//	        /// Request the URI of a named capability
//	        /// </summary>
//	        /// <param name="capability">Name of the capability to request</param>
//	        /// <returns>The URI of the requested capability, or String.Empty if
//	        /// the capability does not exist</returns>
//	        public Uri CapabilityURI(String capability)
//	        {
//	            Uri cap;
//
//	            if (_Caps.TryGetValue(capability, out cap))
//	                return cap;
//	            else
//	                return null;
//	        }
//
//	        private void MakeSeedRequest()
//	        {
//	            if (Simulator == null || !Simulator.Client.Network.Connected)
//	                return;
//
//	            // Create a request list
//	            OSDArray req = new OSDArray();
//	            // This list can be updated by using the following command to obtain a current list of capabilities the official linden viewer supports:
//	            // wget -q -O - http://bitbucket.org/lindenlab/viewer-development/raw/default/indra/newview/llviewerregion.cpp | grep 'capabilityNames.append'  | sed 's/^[ \t]*//;s/capabilityNames.append("/req.Add("/'
//	            req.Add("AttachmentResources");
//	            req.Add("AvatarPickerSearch");
//	            req.Add("ChatSessionRequest");
//	            req.Add("CopyInventoryFromNotecard");
//	            req.Add("DispatchRegionInfo");
//	            req.Add("EstateChangeInfo");
//	            req.Add("EventQueueGet");
//	            req.Add("ObjectMedia");
//	            req.Add("ObjectMediaNavigate");
//	            req.Add("FetchLib2");
//	            req.Add("FetchLibDescendents2");
//	            req.Add("FetchInventory2");
//	            req.Add("FetchInventoryDescendents2");
//	            req.Add("GetDisplayNames");
//	            req.Add("GetTexture");
//	            req.Add("GetMesh");
//	            req.Add("GetObjectCost");
//	            req.Add("GetObjectPhysicsData");
//	            req.Add("GroupProposalBallot");
//	            req.Add("HomeLocation");
//	            req.Add("LandResources");
//	            req.Add("MapLayer");
//	            req.Add("MapLayerGod");
//	            req.Add("NewFileAgentInventory");
//	            req.Add("NewFileAgentInventoryVariablePrice");
//	            req.Add("ObjectAdd");
//	            req.Add("ParcelPropertiesUpdate");
//	            req.Add("ParcelMediaURLFilterList");
//	            req.Add("ParcelNavigateMedia");
//	            req.Add("ParcelVoiceInfoRequest");
//	            req.Add("ProductInfoRequest");
//	            req.Add("ProvisionVoiceAccountRequest");
//	            req.Add("RemoteParcelRequest");
//	            req.Add("RequestTextureDownload");
//	            req.Add("SearchStatRequest");
//	            req.Add("SearchStatTracking");
//	            req.Add("SendPostcard");
//	            req.Add("SendUserReport");
//	            req.Add("SendUserReportWithScreenshot");
//	            req.Add("ServerReleaseNotes");
//	            req.Add("SimConsole");
//	            req.Add("SimulatorFeatures");
//	            req.Add("SetDisplayName");
//	            req.Add("SimConsoleAsync");
//	            req.Add("StartGroupProposal");
//	            req.Add("TextureStats");
//	            req.Add("UntrustedSimulatorMessage");
//	            req.Add("UpdateAgentInformation");
//	            req.Add("UpdateAgentLanguage");
//	            req.Add("UpdateGestureAgentInventory");
//	            req.Add("UpdateNotecardAgentInventory");
//	            req.Add("UpdateScriptAgent");
//	            req.Add("UpdateGestureTaskInventory");
//	            req.Add("UpdateNotecardTaskInventory");
//	            req.Add("UpdateScriptTask");
//	            req.Add("UploadBakedTexture");
//	            req.Add("UploadObjectAsset");
//	            req.Add("ViewerMetrics");
//	            req.Add("ViewerStartAuction");
//	            req.Add("ViewerStats");
//
//	            _SeedRequest = new CapsClient(new Uri(_SeedCapsURI));
//	            _SeedRequest.OnComplete += new CapsClient.CompleteCallback(SeedRequestCompleteHandler);
//	            _SeedRequest.BeginGetResponse(req, OSDFormat.Xml, Simulator.Client.Settings.CAPS_TIMEOUT);
//	        }
//
//	        private void SeedRequestCompleteHandler(CapsClient client, OSD result, Exception error)
//	        {
//	            if (result != null && result.Type == OSDType.Map)
//	            {
//	                OSDMap respTable = (OSDMap)result;
//
//	                foreach (String cap in respTable.Keys)
//	                {
//	                    _Caps[cap] = respTable[cap].AsUri();
//	                }
//
//	                if (_Caps.ContainsKey("EventQueueGet"))
//	                {
//	                    Logger.DebugLog("Starting event queue for " + Simulator.ToString(), Simulator.Client);
//
//	                    _EventQueueCap = new EventQueueClient(_Caps["EventQueueGet"]);
//	                    _EventQueueCap.OnConnected += EventQueueConnectedHandler;
//	                    _EventQueueCap.OnEvent += EventQueueEventHandler;
//	                    _EventQueueCap.Start();
//	                }
//	            }
//	            else if (
//	                error != null &&
//	                error is WebException &&
//	                ((WebException)error).Response != null &&
//	                ((HttpWebResponse)((WebException)error).Response).StatusCode == HttpStatusCode.NotFound)
//	            {
//	                // 404 error
//	                Logger.Log("Seed capability returned a 404, capability system is aborting", Helpers.LogLevel.Error);
//	            }
//	            else
//	            {
//	                // The initial CAPS connection failed, try again
//	                MakeSeedRequest();
//	            }
//	        }
//
//	        private void EventQueueConnectedHandler()
//	        {
//	            Simulator.Client.Network.RaiseConnectedEvent(Simulator);
//	        }
//
//	        /// <summary>
//	        /// Process any incoming events, check to see if we have a message created for the event, 
//	        /// </summary>
//	        /// <param name="eventName"></param>
//	        /// <param name="body"></param>
//	        private void EventQueueEventHandler(String eventName, OSDMap body)
//	        {
//	            IMessage message = Messages.MessageUtils.DecodeEvent(eventName, body);
//	            if (message != null)
//	            {
//	                Simulator.Client.Network.CapsEvents.BeginRaiseEvent(eventName, message, Simulator);
//
//	                #region Stats Tracking
//	                if (Simulator.Client.Settings.TRACK_UTILIZATION)
//	                {
//	                    Simulator.Client.Stats.Update(eventName, OpenMetaverse.Stats.Type.Message, 0, body.ToString().Length);
//	                }
//	                #endregion
//	            }
//	            else
//	            {
//	                Logger.Log("No Message handler exists for event " + eventName + ". Unable to decode. Will try Generic Handler next", Helpers.LogLevel.Warning);
//	                Logger.Log("Please report this information to http://jira.openmv.org/: \n" + body, Helpers.LogLevel.Debug);
//
//	                // try generic decoder next which takes a caps event and tries to match it to an existing packet
//	                if (body.Type == OSDType.Map)
//	                {
//	                    OSDMap map = (OSDMap)body;
//	                    Packet packet = Packet.BuildPacket(eventName, map);
//	                    if (packet != null)
//	                    {
//	                        NetworkManager.IncomingPacket incomingPacket;
//	                        incomingPacket.Simulator = Simulator;
//	                        incomingPacket.Packet = packet;
//
//	                        Logger.DebugLog("Serializing " + packet.Type.ToString() + " capability with generic handler", Simulator.Client);
//
//	                        Simulator.Client.Network.PacketInbox.Enqueue(incomingPacket);
//	                    }
//	                    else
//	                    {
//	                        Logger.Log("No Packet or Message handler exists for " + eventName, Helpers.LogLevel.Warning);
//	                    }
//	                }
//	            }
//	        }        
	    }
