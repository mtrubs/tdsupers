package com.mtrubs.td.scene.level.mob;

import com.badlogic.gdx.math.Vector2;
import com.mtrubs.util.NonNull;

public interface Targetable {

  @NonNull
  Vector2 getCenter();

  void damage(int amount);

  int getDamage();

  boolean isVisible();
}
