package map.world;

import map.world.effect.TestAll;
import map.world.view.OpcWorldMapView;
import map.world.view.WorldMapView;

// TODO: make singleton?
public class WorldMapController {

  // View to update when pixels change
  private final WorldMapView view;

  public WorldMapController(WorldMapView view) {
    this.view = view;
    run();
  }

  // Main run loop
  private void run() {
    // TODO: setup comms method from server to controller
    // Set up control server thread
    // Set up basic server (for command line run args)

    // Set startup screen effect
    Thread effectRunner = new Thread(new TestAll(view));

    // Start the effect
    effectRunner.start();

    // Run effect for 5 seconds before exiting
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // TODO: act on server input
//    while (!(sa.interrupted)) {
//      nanosleep(&effect_runner->effect->time_delta, NULL);
//      effect_runner->effect->run(effect_runner);
//      effect_runner->frame_no++;
//
//      // Handle server input
//      pthread_mutex_lock((pthread_mutex_t *) &sa.mutex);
//      if (sa.shared_cmd > 0) {
//        if (sa.shared_cmd < sizeof(commands) / sizeof(char *)) {
//          puts("Cleaning up current effect...");
//          effect_runner->effect->remove(effect_runner->effect);
//          CLEAR_PIXELS;
//
//          printf("Running effect: %s\n", commands[sa.shared_cmd]);
//          effect_runner->effect = init_effect(commands[sa.shared_cmd], pixel_info, sink);
//          effect_runner->frame_no = 0;
//          sa.shared_cmd = -1;
//        }
//      }
//      pthread_mutex_unlock((pthread_mutex_t *) &sa.mutex);
//    }

    // TODO: make effects extend Thread class and add public method to do the below operations to end.
    // Stop running the current effect and exit
    effectRunner.interrupt();
    try {
      effectRunner.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new WorldMapController(new OpcWorldMapView());
  }
}
