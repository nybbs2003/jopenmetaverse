package com.ngt.jopenmetaverse.shared.sim.events.nm;

import com.ngt.jopenmetaverse.shared.sim.NetworkManager;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

public class DisconnectedEventArgs extends EventArgs 
{
	  private  NetworkManager.DisconnectType m_Reason;
      private  String m_Message;

      public NetworkManager.DisconnectType getReason() { return m_Reason; } 
      public String getMessage() { return m_Message; } 

      public DisconnectedEventArgs(NetworkManager.DisconnectType reason, String message)
      {
          this.m_Reason = reason;
          this.m_Message = message;
      }
}
