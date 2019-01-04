package map.world.effect;

import map.world.view.OpcPixel;
import map.world.view.WorldMapView;

public class Sunrise extends WorldMapEffect {

  private static final int FRAME_DELAY = 20;

  public Sunrise(WorldMapView view) {
    super(view, FRAME_DELAY);

    // TODO: Read in config file here
  }

  // TODO: map to internal time class, from frame number
  private int calculateWorldTime() {
    return 0;
  }

  // TODO: create abstract classes for looping and non-looping effects
  /* How it works - internal time is mapped to by frame number, pixels are
     turned on if internal time is within the sun up time for that pixel */
  @Override void calculateNextFrame(int frame) {
    for (int pixel = 0; pixel < NUM_PIXELS; pixel++) {
      int r = 0, g = 0, b = 0;
      setPixel(pixel, OpcPixel.makePixel(r, g, b));
    }
  }
}
