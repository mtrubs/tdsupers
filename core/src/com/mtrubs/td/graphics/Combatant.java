package com.mtrubs.td.graphics;

public interface Combatant extends TextureReference {

  int getDamage();

  float getSpeed();

  float getAttackCoolDown();

  float getRange();

  ProjectileType getProjectileType();
}
