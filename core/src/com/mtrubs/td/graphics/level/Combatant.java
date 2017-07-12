package com.mtrubs.td.graphics.level;

import com.mtrubs.td.graphics.TextureReference;
import com.mtrubs.util.Nullable;

public interface Combatant extends TextureReference {

  int getDamage();

  float getSpeed();

  float getAttackCoolDown();

  float getRange();

  @Nullable
  ProjectileType getProjectileType();
}
