package map.world.view;

import map.world.effect.WorldMapEffect;

// TODO: docs
public interface WorldMapView {
  void clear();
  void update(WorldMapEffect effect);
}
