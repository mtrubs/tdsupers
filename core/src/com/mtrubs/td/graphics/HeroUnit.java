package com.mtrubs.td.graphics;

public interface HeroUnit extends TextureReference {

  float getRange();

  float getAttackCoolDown();

  int getHealth();

  ProjectileType getProjectileType();

  float getDeathCoolDown();

  int getDamage();

  float getSpeed();
}
