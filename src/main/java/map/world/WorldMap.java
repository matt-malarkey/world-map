package map.world;

import map.world.controller.SingleEffectTest;
import map.world.controller.WorldMapController;
import map.world.effect.OneByOne;
import map.world.effect.Scroller;
import map.world.effect.WorldMapEffect;
import map.world.view.OpcWorldMapView;
import map.world.view.WorldMapView;

import java.util.concurrent.atomic.AtomicBoolean;

// TODO: make singleton?
public class WorldMap {

  private final WorldMapController controller;
  private final WorldMapView view;
  private WorldMapEffect effect;

  public WorldMap(WorldMapController controller, WorldMapView view) {
    this.controller = controller;
    this.view = view;

    // Set startup screen effect
    effect = new Scroller(view);

    run();
  }

  private void setEffectFromController() {
    effect = controller.getEffectToRun(view);
  }

  // Main run loop
  private void run() {
    // TODO: setup comms method from server to controller
    // Set up control server thread
    // Set up basic server (for command line run args)

    // Start the effect
    effect.start();

    // Run the main loop
    final AtomicBoolean shouldExit = new AtomicBoolean(false);
    // TODO: use callbacks as in MVC model
    while (!shouldExit.get()) {
      // Handle controller input, if any
      controller.getCommand().ifPresent(cmd ->
      {
        switch (cmd) {
          case PLAY:
            effect.play();
            break;
          case PAUSE:
            effect.pause();
            break;
          case RUN:
            setEffectFromController();
            break;
          case EXIT:
            shouldExit.set(true);
            break;
        }
      });

      // Pause before checking again for a command
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    // Stop running the current effect and exit
    effect.kill();
  }

  public static void main(String[] args) {
    new WorldMap(new SingleEffectTest(), new OpcWorldMapView());
  }
}
