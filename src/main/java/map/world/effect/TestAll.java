package map.world.effect;

import map.world.view.WorldMapView;

public class TestAll extends WorldMapEffect {

  private static final int FRAME_PAUSE = 100;

  public TestAll(WorldMapView view) {
    super(view, FRAME_PAUSE);
  }

  @Override void next_frame() {
    // TODO: frame to frame rule etc...
  }
}
