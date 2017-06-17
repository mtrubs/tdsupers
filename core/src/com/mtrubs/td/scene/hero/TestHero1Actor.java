package com.mtrubs.td.scene.hero;

import com.mtrubs.td.graphics.HeroUnit;
import com.mtrubs.td.graphics.ProjectileType;
import com.mtrubs.td.graphics.TextureRegionManager;
import com.mtrubs.td.scene.CombatActor;

public class TestHero1Actor extends CombatActor {

  public TestHero1Actor(TextureRegionManager textureRegionManager, float startX, float startY) {
    super(startX, startY, textureRegionManager.get(HeroUnit.TestHero1));
  }

  @Override
  public int getDamage() {
    return 0; // TODO
  }

  @Override
  protected float getRange() {
    return 0; // TODO
  }

  @Override
  protected float getAttackCoolDown() {
    return 0; // TODO
  }

  @Override
  protected ProjectileType getProjectileType() {
    return null; // TODO
  }

  @Override
  protected boolean canAttack() {
    return false; // TODO
  }

  @Override
  protected void handleDefeat() {
    // TODO
  }
}
