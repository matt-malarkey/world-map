package map.world.view;

import map.world.effect.WorldMapEffect;
import map.world.opc.OpcClient;
import map.world.opc.OpcDevice;
import map.world.opc.PixelStrip;

/*
 *
 *  A facade for the OPC library classes, this will handle everything needed.
 *  TODO: docs
 *
 */
public abstract class OpcWorldMapView implements WorldMapView {

  // TODO: move to constants, no magic numbers
  public OpcWorldMapView() {
    // TODO: compose with port for different views? figure this out
    OpcClient server = new OpcClient("127.0.0.1", 7890);
    OpcDevice fadeCandy = server.addDevice();

    // TODO: read from config
    PixelStrip strip1 = fadeCandy.addPixelStrip(0, 10);
    PixelStrip strip2 = fadeCandy.addPixelStrip(0, 10);
    PixelStrip strip3 = fadeCandy.addPixelStrip(0, 10);
    PixelStrip strip4 = fadeCandy.addPixelStrip(0, 10);
    PixelStrip strip5 = fadeCandy.addPixelStrip(0, 10);
    PixelStrip strip6 = fadeCandy.addPixelStrip(0, 10);
    PixelStrip strip7 = fadeCandy.addPixelStrip(0, 10);
    PixelStrip strip8 = fadeCandy.addPixelStrip(0, 10);
    System.out.println(server.getConfig());

    // Set animations

    // Animate

  }

  @Override public void clear() {
    // TODO: Set all pixels to black
  }

  @Override public void update(WorldMapEffect effect) {
    // TODO: write pixels from effect to display
  }
}
