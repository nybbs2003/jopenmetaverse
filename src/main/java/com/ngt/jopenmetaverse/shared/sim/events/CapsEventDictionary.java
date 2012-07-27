package com.ngt.jopenmetaverse.shared.sim.events;

import java.util.logging.Logger;

import com.ngt.jopenmetaverse.shared.sim.GridClient;
import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.Caps;

public class CapsEventDictionary {

	//TODO Need to implement
//	/// <summary>
//		/// Object that is passed to worker threads in the ThreadPool for
//		/// firing CAPS callbacks
//		/// </summary>
//		private class CapsCallbackWrapper
//		{
//			/// <summary>Callback to fire for this packet</summary>
//			public Caps.EventQueueCallback Callback;
//			/// <summary>Name of the CAPS event</summary>
//			public String CapsEvent;
//			/// <summary>Strongly typed decoded data</summary>
//			public IMessage Message;
//			/// <summary>Reference to the simulator that generated this event</summary>
//			public Simulator Simulator;
//		}
//
//		/// <summary>Reference to the GridClient object</summary>
//		public GridClient Client;
//
//		private Dictionary<String, Caps.EventQueueCallback> _EventTable =
//				new Dictionary<String, Caps.EventQueueCallback>();
//		private WaitCallback _ThreadPoolCallback;
//
//		/// <summary>
//		/// Default constructor
//		/// </summary>
//		/// <param name="client">Reference to the GridClient object</param>
//		public CapsEventDictionary(GridClient client)
//		{
//			Client = client;
//			_ThreadPoolCallback = new WaitCallback(ThreadPoolDelegate);
//		}
//
//		/// <summary>
//		/// Register an new event handler for a capabilities event sent via the EventQueue
//		/// </summary>
//		/// <remarks>Use String.Empty to fire this event on every CAPS event</remarks>
//		/// <param name="capsEvent">Capability event name to register the 
//		/// handler for</param>
//		/// <param name="eventHandler">Callback to fire</param>
//		public void RegisterEvent(String capsEvent, Caps.EventQueueCallback eventHandler)
//		{
//			// TODO: Should we add support for synchronous CAPS handlers?
//			synchronized (_EventTable)
//			{
//				if (_EventTable.ContainsKey(capsEvent))
//					_EventTable[capsEvent] += eventHandler;
//				else
//					_EventTable[capsEvent] = eventHandler;
//			}
//		}
//
//		/// <summary>
//		/// Unregister a previously registered capabilities handler 
//		/// </summary>
//		/// <param name="capsEvent">Capability event name unregister the 
//		/// handler for</param>
//		/// <param name="eventHandler">Callback to unregister</param>
//		public void UnregisterEvent(String capsEvent, Caps.EventQueueCallback eventHandler)
//		{
//			synchronized (_EventTable)
//			{
//				if (_EventTable.ContainsKey(capsEvent) && _EventTable[capsEvent] != null)
//					_EventTable[capsEvent] -= eventHandler;
//			}
//		}
//
//		/// <summary>
//		/// Fire the events registered for this event type synchronously
//		/// </summary>
//		/// <param name="capsEvent">Capability name</param>
//		/// <param name="message">Decoded event body</param>
//		/// <param name="simulator">Reference to the simulator that 
//		/// generated this event</param>
//		private void RaiseEvent(String capsEvent, IMessage message, Simulator simulator)
//		{
//			boolean specialHandler = false;
//			Caps.EventQueueCallback callback;
//
//			// Default handler first, if one exists
//			if (_EventTable.TryGetValue(capsEvent, out callback))
//			{
//				if (callback != null)
//				{
//					try { callback(capsEvent, message, simulator); }
//					catch (Exception ex) { Logger.Log("CAPS Event Handler: " + ex.ToString(), Helpers.LogLevel.Error, Client); }
//				}
//			}
//
//			// Explicit handler next
//			if (_EventTable.TryGetValue(capsEvent, out callback) && callback != null)
//			{
//				try { callback(capsEvent, message, simulator); }
//				catch (Exception ex) { Logger.Log("CAPS Event Handler: " + ex.ToString(), Helpers.LogLevel.Error, Client); }
//
//				specialHandler = true;
//			}
//
//			if (!specialHandler)
//				logger.Log("Unhandled CAPS event " + capsEvent, Helpers.LogLevel.Warning, Client);
//		}
//
//		/// <summary>
//		/// Fire the events registered for this event type asynchronously
//		/// </summary>
//		/// <param name="capsEvent">Capability name</param>
//		/// <param name="message">Decoded event body</param>
//		/// <param name="simulator">Reference to the simulator that 
//		/// generated this event</param>
//		private void BeginRaiseEvent(String capsEvent, IMessage message, Simulator simulator)
//		{
//			boolean specialHandler = false;
//			Caps.EventQueueCallback callback;
//
//			// Default handler first, if one exists
//			if (_EventTable.TryGetValue(String.Empty, out callback))
//			{
//				if (callback != null)
//				{
//					CapsCallbackWrapper wrapper;
//					wrapper.Callback = callback;
//					wrapper.CapsEvent = capsEvent;
//					wrapper.Message = message;
//					wrapper.Simulator = simulator;
//					ThreadPool.QueueUserWorkItem(_ThreadPoolCallback, wrapper);
//				}
//			}
//
//			// Explicit handler next
//			if (_EventTable.TryGetValue(capsEvent, out callback) && callback != null)
//			{
//				CapsCallbackWrapper wrapper;
//				wrapper.Callback = callback;
//				wrapper.CapsEvent = capsEvent;
//				wrapper.Message = message;
//				wrapper.Simulator = simulator;
//				ThreadPool.QueueUserWorkItem(_ThreadPoolCallback, wrapper);
//
//				specialHandler = true;
//			}
//
//			if (!specialHandler)
//				Logger.Log("Unhandled CAPS event " + capsEvent, Helpers.LogLevel.Warning, Client);
//		}
//
//		private void ThreadPoolDelegate(Object state)
//		{
//			CapsCallbackWrapper wrapper = (CapsCallbackWrapper)state;
//			try
//			{
//				wrapper.Callback(wrapper.CapsEvent, wrapper.Message, wrapper.Simulator);
//			}
//			catch (Exception ex)
//			{
//				Logger.Log("Async CAPS Event Handler: " + ex.ToString(), Helpers.LogLevel.Error, Client);
//			}
//		}
}
