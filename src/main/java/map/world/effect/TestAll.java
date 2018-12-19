package map.world.effect;

import map.world.view.WorldMapView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;


public class TestAll extends WorldMapEffect {

  private static final int FRAME_PAUSE = 1;

  private final Integer[] pixels = new Integer[NUM_PIXELS]; // TODO: move to abstract.
  private int currentFrame = 0;

  public TestAll(WorldMapView view) {
    super(view, FRAME_PAUSE);

    IntStream.range(0, NUM_PIXELS).forEach(i -> pixels[i] = 0);
  }

  // TODO: distinguish between circular effects and ones that end?
  @Override public void next_frame() {
    if (currentFrame < NUM_PIXELS) {
      pixels[currentFrame] = 255;
      currentFrame++;
    }
  }

  @Override public List<Integer> getPixelList() {
    return Arrays.asList(pixels);
  }
}
