package map.world.effect;

import map.world.view.OpcPixel;
import map.world.view.WorldMapView;

public class StripTest extends WorldMapEffect {

  private static final int FRAME_DELAY = 1;

  public StripTest(WorldMapView view) {
    super(view, FRAME_DELAY, false);
  }

  @Override public void calculateNextFrame(int frame) {
    if (frame < NUM_PIXELS) {
      setPixel(frame, OpcPixel.makePixel(0, 255, 0));
    }
  }
}
