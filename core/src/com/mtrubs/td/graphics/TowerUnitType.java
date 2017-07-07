package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.mtrubs.util.NonNull;
import com.mtrubs.util.Nullable;

/**
 * This enum represents all the tower units.
 */
public enum TowerUnitType implements TowerUnit {

  TestHero1Unit("images/towers/TestHero1/towerUnit1.png", 30, 250.0F, 100.0F, 10.0F, 5.0F, 10,
    ProjectileType.YellowBullet),
  TestHero2Unit("images/towers/TestHero2/towerUnit1.png", 30, 225.0F, 150.0F, 6.0F, 5.0F, 10,
    ProjectileType.YellowBullet),
  TestHero3Unit("images/towers/TestHero3/towerUnit1.png", 30, 200.0F, 200.0F, 10.0F, 10.0F, 10,
    ProjectileType.YellowBullet);

  private final String texturePath;
  private final String key;
  private final float range;
  private final float engageRange;
  private final float attackCoolDown;
  private final float deathCoolDown;
  private final int damage;
  private final int health;
  private final ProjectileType projectileType;

  private TowerUnitType(String texturePath, int health, float range, float engageRange,
                        float deathCoolDown, float attackCoolDown, int damage,
                        ProjectileType projectileType) {
    this.texturePath = texturePath;
    this.range = range;
    this.engageRange = engageRange;
    this.attackCoolDown = attackCoolDown;
    this.deathCoolDown = deathCoolDown;
    this.projectileType = projectileType;
    this.damage = damage;
    this.health = health;
    this.key = String.format("%s.%s", getClass().getSimpleName(), name());
  }

  @Override
  public String getTexturePath() {
    return this.texturePath;
  }

  @Override
  public void setTextureFilter(Texture texture) {
    texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
  }

  @NonNull
  @Override
  public String getKey() {
    return this.key;
  }

  @Nullable
  @Override
  public ProjectileType getProjectileType() {
    return this.projectileType;
  }

  @Override
  public float getAttackCoolDown() {
    return this.attackCoolDown;
  }

  @Override
  public float getRange() {
    return this.range;
  }

  @Override
  public int getDamage() {
    return this.damage;
  }

  @Override
  public float getDeathCoolDown() {
    return this.deathCoolDown;
  }

  @Override
  public int getHealth() {
    return this.health;
  }

  @Override
  public float getSpeed() {
    return 25.0F;
  }
}
