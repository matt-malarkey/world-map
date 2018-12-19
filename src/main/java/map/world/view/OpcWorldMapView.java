package map.world.view;

import map.world.effect.WorldMapEffect;
import map.world.opc.OpcClient;
import map.world.opc.OpcDevice;
import map.world.opc.PixelStrip;

import java.util.List;

/*
 *
 *  A facade for the OPC library classes, this will handle everything needed.
 *  TODO: docs
 *
 */
public final class OpcWorldMapView implements WorldMapView {

  private static final int NUM_PIXELS = 3; //TODO: check
  private final PixelStrip[] strips = new PixelStrip[8];

  private final OpcClient server;

  // TODO: move to constants, no magic numbers
  public OpcWorldMapView() {
    // TODO: compose with port for different views? figure this out
    server = new OpcClient("127.0.0.1", 7890);
    OpcDevice fadeCandy = server.addDevice();

    // TODO: read from config
    for (int i = 0; i < 8; i++) {
      strips[i] = fadeCandy.addPixelStrip(0, 10);
    }
    //System.out.println(server.getConfig());
  }

  @Override public void clear() {
    // TODO: Set all pixels to black
  }

  // TODO
  private PixelStrip getStripForIndex(int i) {
    return strips[0];
  }

  @Override public void update(WorldMapEffect effect) {
    // TODO: write pixels from effect to display

    // Given a WorldMapEffect representation, update the OPC stuff accordingly
    List<Integer> pixels = effect.getPixelList();
    for (int pixel = 0; pixel < NUM_PIXELS; pixel++) {
      getStripForIndex(pixel).setPixelColor(pixel, pixels.get(pixel));
    }

    server.show();
  }
}
