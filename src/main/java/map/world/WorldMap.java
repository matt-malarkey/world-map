package map.world;

import map.world.controller.DemoController;
import map.world.controller.WorldMapController;
import map.world.effect.TestAll;
import map.world.effect.WorldMapEffect;
import map.world.view.OpcWorldMapView;
import map.world.view.WorldMapView;

import java.util.concurrent.atomic.AtomicBoolean;

// TODO: make singleton?
public class WorldMap {

  private final WorldMapController controller;
  private final WorldMapView view;

  public WorldMap(WorldMapController controller, WorldMapView view) {
    this.controller = controller;
    this.view = view;
    run();
  }

  // Main run loop
  private void run() {
    // TODO: setup comms method from server to controller
    // Set up control server thread
    // Set up basic server (for command line run args)

    // Set startup screen effect
    WorldMapEffect effectRunner = new TestAll(view);

    // Start the effect
    effectRunner.start();

    // TODO: use callbacks as in MVC model
    // Run the main loop
    final AtomicBoolean shouldExit = new AtomicBoolean(false);
    while (!shouldExit.get()) {
      // Handle controller input, if any
      controller.getCommand().ifPresent(cmd ->
      {
        switch (cmd) {
          case PLAY:
            System.out.println("Play");
            break;
          case PAUSE:
            System.out.println("Pause");
            break;
          case RUN:
            System.out.println("Running effect ?");
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
    effectRunner.close();
  }

  public static void main(String[] args) {
    new WorldMap(new DemoController(), new OpcWorldMapView());
  }
}
