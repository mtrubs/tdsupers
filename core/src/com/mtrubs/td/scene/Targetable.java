package com.mtrubs.td.scene;

import com.badlogic.gdx.math.Vector2;

public interface Targetable {

  Vector2 getCenter();

  void damage(int amount);

  int getDamage();

  boolean isVisible();
}
