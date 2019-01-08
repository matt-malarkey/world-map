package map.world.effect;

import map.world.view.OpcPixel;
import map.world.view.WorldMapView;

import java.time.Duration;
import java.time.LocalTime;

public class Sunrise extends WorldMapEffect {

  private static final int FRAME_DELAY = 20;

  private LocalTime internalTime = LocalTime.of(0, 0, 0);
  private final Duration advanceTime = Duration.ofMinutes(40);

  public Sunrise(WorldMapView view) {
    super(view, FRAME_DELAY, true);

    // TODO: Read in config file here
  }

  // TODO: lookup pixel in config map and scale white value
  private int calculateSunlight(int pixel) {
    return OpcPixel.makePixel(100, 100, 100);
  }

  /* How it works - internal time is mapped to by frame number, pixels are
     turned on if internal time is within the sun up time for that pixel */
  @Override void calculateNextFrame(int frame) {
    // Advance internal world time by a fixed amount each frame to model daytime
    internalTime = internalTime.plus(advanceTime);

    // Update pixel lists based on new internal world time
    for (int pixel = 0; pixel < NUM_PIXELS; pixel++) {
      setPixel(pixel, calculateSunlight(pixel));
    }
  }
}
