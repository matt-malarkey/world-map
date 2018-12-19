package map.world.controller;

import java.util.Optional;

public interface WorldMapController {
  Optional<WorldMapCommand> getCommand();
}
