package com.ngt.jopenmetaverse.shared.sim.events.terrain;

import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

 //region EventArgs classes
  // <summary>Provides data for LandPatchReceived</summary>
  public class LandPatchReceivedEventArgs extends EventArgs
  {
      private Simulator m_Simulator;
      private int m_X;
      private int m_Y;
      private int m_PatchSize;
      private float[] m_HeightMap;

      /// <summary>Simulator from that sent tha data</summary>
      public Simulator getSimulator() {return m_Simulator;} 
      /// <summary>Sim coordinate of the patch</summary>
      public int getX() {return m_X;} 
      /// <summary>Sim coordinate of the patch</summary>
      public int getY() {return m_Y;} 
      /// <summary>Size of tha patch</summary>
      public int getPatchSize() {return m_PatchSize;} 
      /// <summary>Heightmap for the patch</summary>
      public float[] getHeightMap() {return m_HeightMap;} 

      public LandPatchReceivedEventArgs(Simulator simulator, int x, int y, int patchSize, float[] heightMap)
      {
          this.m_Simulator = simulator;
          this.m_X = x;
          this.m_Y = y;
          this.m_PatchSize = patchSize;
          this.m_HeightMap = heightMap;
      }
  }