package map.world.controller;

import map.world.effect.StripTest;
import map.world.effect.WorldMapEffect;
import map.world.view.WorldMapView;

import java.util.Optional;

public class TestController implements WorldMapController {

  private int count = 0;

  // Do nothing, then pause, then play, then exit.
  @Override public Optional<WorldMapCommand> getCommand() {
    if (count < 1) {
      count++;
      return Optional.empty();
    }

    if (count < 10) {
      count++;
      return Optional.of(WorldMapCommand.PAUSE);
    }

    if (count < 11) {
      count++;
      return Optional.of(WorldMapCommand.PLAY);
    }

    return Optional.of(WorldMapCommand.EXIT);
  }

  @Override public WorldMapEffect getEffectToRun(WorldMapView view) {
    return new StripTest(view);
  }
}
