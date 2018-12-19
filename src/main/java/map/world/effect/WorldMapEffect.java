package map.world.effect;

import map.world.view.WorldMapView;

public abstract class WorldMapEffect implements Runnable {

  private final WorldMapView view;
  private final int framePause; // TODO: think of a better name

  public WorldMapEffect(WorldMapView view, int framePause) {
    this.view = view;
    this.framePause = framePause;

    // Setup pixel_info
    // TODO: what to store in base class
  }

  private void sleepBetweenFrames() {
    try {
      Thread.sleep(framePause);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  abstract void next_frame();

  @Override public void run() {
    view.clear();

    // Run this effect until interrupted by controller.
    while (!Thread.currentThread().isInterrupted()) {
      // Update pixels
      next_frame();

      // Update view
      view.update(this); // TODO: provide public method to view to call to get pixel list or whatever it needs.

      // Sleep
      sleepBetweenFrames();
    }

    view.clear();
  }
}
