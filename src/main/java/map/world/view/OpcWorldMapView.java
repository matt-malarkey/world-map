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

  // Server configuration
  private static final String DEFAULT_SERVER = "127.0.0.1";
  private static final int DEFAULT_PORT = 7890;

  // Pixel configuration
  // TODO: read from config files?
  public static final int[] pixelsPerStrip = {63, 60, 64, 44, 60, 64, 52, 64};

  private final OpcClient server;
  private final PixelStrip[] strips = new PixelStrip[8];

  public OpcWorldMapView() {
    this(DEFAULT_PORT);
  }

  public OpcWorldMapView(int port) {
    server = new OpcClient(DEFAULT_SERVER, port);
    OpcDevice fadeCandy = server.addDevice();

    for (int i = 0; i < pixelsPerStrip.length; i++) {
      strips[i] = fadeCandy.addPixelStrip(i, pixelsPerStrip[i]);
    }
    //System.out.println(server.getConfig());
  }

  @Override public void clear() {
    for (int strip = 0; strip < pixelsPerStrip.length; strip++) {
      for (int pixel = 0; pixel < pixelsPerStrip[strip]; pixel++) {
        strips[strip].setPixelColor(pixel, 0);
      }
    }
    server.show();
  }

  // TODO: need maps from:
  //  1. pixel num to strip num
  //  2.

  // TODO: consider not copying across
  @Override public void update(WorldMapEffect effect) {
    // Given a WorldMapEffect representation, update the OPC pixel strips
    List<Integer> pixels = effect.getPixelList();
    int total = 0;
    for (int strip = 0; strip < pixelsPerStrip.length; strip++) {
      for (int pixel = 0; pixel < pixelsPerStrip[strip]; pixel++) {
        strips[strip].setPixelColor(pixel, pixels.get(pixel + total));
      }
      total += pixelsPerStrip[strip];
    }
    server.show();
  }
}
