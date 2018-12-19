package map.world.controller;

import java.util.Optional;

public class DemoController implements WorldMapController {

  private int count = 0;

  @Override public Optional<WorldMapCommand> getCommand() {
    if (count < 100) {
      count++;
      return Optional.empty();
    }
    return Optional.of(WorldMapCommand.EXIT);
  }
}
