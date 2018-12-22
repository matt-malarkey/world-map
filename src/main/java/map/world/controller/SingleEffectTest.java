package map.world.controller;

import map.world.effect.WorldMapEffect;
import map.world.view.WorldMapView;

import java.util.Optional;

public class SingleEffectTest implements WorldMapController {

  // Run an effect for 3 seconds before exiting
  private final long END_TIME = System.currentTimeMillis() + (1000 * 10);

  @Override public Optional<WorldMapCommand> getCommand() {
    if (System.currentTimeMillis() > END_TIME) {
      return Optional.of(WorldMapCommand.EXIT);
    }
    return Optional.empty();
  }

  @Override public WorldMapEffect getEffectToRun(WorldMapView view) {
    return null;
  }
}
