package map.world.effect;

import map.world.view.WorldMapView;

public class TestAll extends WorldMapEffect {

  private static final int FRAME_PAUSE = 1;

  public TestAll(WorldMapView view) {
    super(view, FRAME_PAUSE);
  }

  @Override public void calculateNextFrame(int frame) {
    if (frame < NUM_PIXELS) {
      setPixel(frame, 255);
    }
  }
}
