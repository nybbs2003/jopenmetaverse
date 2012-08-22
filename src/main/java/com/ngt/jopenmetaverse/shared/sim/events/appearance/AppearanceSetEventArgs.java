package com.ngt.jopenmetaverse.shared.sim.events.appearance;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

/// <summary>Contains the Event data returned from an AppearanceSetRequest</summary>
    public class AppearanceSetEventArgs extends EventArgs
    {
        private  boolean m_success;

        /// <summary>Indicates whether appearance setting was successful</summary>
        public boolean getSuccess() { return m_success; } 
        /// <summary>
        /// Triggered when appearance data is sent to the sim and
        /// the main appearance thread is done.</summary>
        /// <param name="success">Indicates whether appearance setting was successful</param>
        public AppearanceSetEventArgs(boolean success)
        {
            this.m_success = success;
        }
    }