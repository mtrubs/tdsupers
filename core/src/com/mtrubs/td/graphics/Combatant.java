package com.mtrubs.td.graphics;

import com.mtrubs.util.Nullable;

public interface Combatant extends TextureReference {

  int getDamage();

  float getSpeed();

  float getAttackCoolDown();

  float getRange();

  @Nullable
  ProjectileType getProjectileType();
}
