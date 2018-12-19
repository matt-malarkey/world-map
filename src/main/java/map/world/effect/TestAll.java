package map.world.effect;

import map.world.view.WorldMapView;

import java.util.Arrays;
import java.util.List;

public class TestAll extends WorldMapEffect {

  private static final int FRAME_PAUSE = 100;

  public TestAll(WorldMapView view) {
    super(view, FRAME_PAUSE);
  }

  @Override public void next_frame() {
    // TODO: frame to frame rule etc...
  }

  // TODO: implement
  @Override public List<Integer> getPixelList() {
    return Arrays.asList(255,255,255,255);
  }
}
