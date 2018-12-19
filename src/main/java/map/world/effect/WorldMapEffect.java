package map.world.effect;

import map.world.view.WorldMapView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public abstract class WorldMapEffect extends Thread {

  static final int NUM_PIXELS = 471;

  private final WorldMapView view;
  private final int frameDelay;
  private final Integer[] pixels = new Integer[NUM_PIXELS];

  private volatile boolean isDead = false;
  private int currentFrame = 0;

  public WorldMapEffect(WorldMapView view, int frameDelay) {
    this.view = view;
    this.frameDelay = frameDelay;

    // Set up pixel list
    IntStream.range(0, NUM_PIXELS).forEach(i -> pixels[i] = 0);
  }

  abstract void calculate_next_frame(int frame);

  private void advance_frame() {
    calculate_next_frame(currentFrame);
    currentFrame++;
  }

  public final void setPixel(int pixelNum, int colour) {
    pixels[pixelNum] = colour;
  }

  public final List<Integer> getPixelList() {
    return Arrays.asList(pixels);
  }

  public final void close() {
    isDead = true;
  }

  private void frameSleepBetween() {
    try {
      Thread.sleep(frameDelay);
    } catch (InterruptedException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  @Override public final void run() {
    view.clear();

    // Run this effect until interrupted by controller
    while (!isDead) {
      // Update pixels for next frame then update display
      advance_frame();
      view.update(this);

      // Sleep between frames
      frameSleepBetween();
    }

    view.clear();
  }
}
