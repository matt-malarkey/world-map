package map.world.controller;

import map.world.effect.WorldMapEffect;
import map.world.view.WorldMapView;

import java.util.Optional;

public interface WorldMapController {
  Optional<WorldMapCommand> getCommand();
  WorldMapEffect getEffectToRun(WorldMapView view);
}
