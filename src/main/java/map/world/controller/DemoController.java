package map.world.controller;

import map.world.effect.TestAll;
import map.world.effect.WorldMapEffect;
import map.world.view.WorldMapView;

import java.util.Optional;

public class DemoController implements WorldMapController {

  // Time between effects to run in milliseconds
  private final int TIME_BETWEEN_EFFECTS = 20 * 1000;
  private long nextEffectAt = System.currentTimeMillis() + TIME_BETWEEN_EFFECTS;

  private DemoEffect nextEffectToRun;

  // TODO: need to find a way better way to do this, use method references??
  private enum DemoEffect {
    TEST_ALL,
    RAVER_PLAID,
    LAVA_LAMP;

    public DemoEffect next() {
      return values()[(this.ordinal() + 1) % values().length];
    }
  }

  // Return command to play next effect if more than TIME_BETWEEN_EFFECTS has
  // elapsed since the last effect started
  @Override public Optional<WorldMapCommand> getCommand() {
    if (System.currentTimeMillis() > nextEffectAt) {
      nextEffectAt = System.currentTimeMillis() + TIME_BETWEEN_EFFECTS;
      nextEffectToRun = nextEffectToRun.next();
      return Optional.of(WorldMapCommand.RUN);
    }
    return Optional.empty();
  }

  @Override public WorldMapEffect getEffectToRun(WorldMapView view) {
    switch (nextEffectToRun) {
      case TEST_ALL:
        return new TestAll(view);
      case RAVER_PLAID:
        return null;
      case LAVA_LAMP:
        return null;
      default:
        return null;
    }
  }
}
