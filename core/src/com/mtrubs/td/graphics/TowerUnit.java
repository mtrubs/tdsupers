package com.mtrubs.td.graphics;

public interface TowerUnit extends TextureReference {

  float getRange();

  float getAttackCoolDown();

  ProjectileType getProjectileType();

  float getDeathCoolDown();

  int getDamage();

  int getHealth();
}
