package com.mtrubs.td.graphics;

public interface Mob extends TextureReference {

  float getSpeed();

  int getHealth();

  float getAttackCoolDown();

  ProjectileType getProjectileType();

  int getDamage();

  float getRange();

  int getWorth();
}
