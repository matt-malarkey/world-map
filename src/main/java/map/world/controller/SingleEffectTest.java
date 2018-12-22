package map.world.controller;

import map.world.effect.WorldMapEffect;
import map.world.view.WorldMapView;

import java.util.Optional;

public class SingleEffectTest implements WorldMapController {

  @Override public Optional<WorldMapCommand> getCommand() {
    return Optional.empty();
  }

  @Override public WorldMapEffect getEffectToRun(WorldMapView view) {
    return null;
  }
}
