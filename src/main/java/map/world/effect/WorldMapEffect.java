package map.world.effect;

import map.world.view.WorldMapView;

import java.util.List;

public abstract class WorldMapEffect implements Runnable {

  private final WorldMapView view;
  private final int framePause; // TODO: think of a better name

  public WorldMapEffect(WorldMapView view, int framePause) {
    this.view = view;
    this.framePause = framePause;

    // Setup pixel_info
    // TODO: what to store in base class
  }

  abstract public void next_frame();

  abstract public List<Integer> getPixelList();

  @Override public void run() {
    view.clear();

    // Run this effect until interrupted by controller
    while (!Thread.currentThread().isInterrupted()) {
      try {
        // Update pixels
        next_frame();

        // Update view
        view.update(this); // TODO: provide public method to view to call to get pixel list or whatever it needs.

        // Sleep between frames
        Thread.sleep(framePause);

      // Must catch and re-interrupt in case the thread sleep was interrupted
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }

    view.clear();
  }
}
