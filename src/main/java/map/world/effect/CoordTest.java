package map.world.effect;

import map.world.view.OpcPixel;
import map.world.view.WorldMapView;

public class CoordTest extends WorldMapEffect {

  private static final int FRAME_DELAY = 10;

  private int currX = 0;
  private int currY = 0;

  public CoordTest(WorldMapView view) {
    super(view, FRAME_DELAY, false);
  }

  // Find next x, y, that is mapped to, then turn on that pixel
  @Override void calculateNextFrame(int frame) {
    // Search to end of current row
    for (int x = currX; x < MAP_WIDTH; x++) {
      if (setPixel(x, currY, OpcPixel.makePixel(255, 0, 255))) {
        currX = x + 1;
        return;
      }
    }

    // Start search again from next row
    for (int y = currY + 1; y < MAP_HEIGHT; y++) {
      for (int x = 0; x < MAP_WIDTH; x++) {
        if (setPixel(x, y, OpcPixel.makePixel(255, 0, 255))) {
          currY = y;
          currX = x + 1;
          return;
        }
      }
    }
  }
}
