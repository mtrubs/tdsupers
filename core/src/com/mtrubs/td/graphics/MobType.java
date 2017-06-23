package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;

/**
 * This enum represents all the mob available.
 */
public enum MobType implements Mob {

  TestMob("images/mobs/TestMob.png", 20, 50.0F, 50.0F, 8.0F, 20, 10, 25.0F, ProjectileType.RedBullet);

  private final String texturePath;
  private final String key;
  private final float range;
  private final float engageRange;
  private final float attackCoolDown;
  private final int damage;
  private final int health;
  private final int worth;
  private final float speed;
  private final ProjectileType projectileType;

  private MobType(String texturePath, int health, float range, float engageRange, float attackCoolDown,
                  int damage, int worth, float speed, ProjectileType projectileType) {
    this.texturePath = texturePath;
    this.range = range;
    this.engageRange = engageRange;
    this.attackCoolDown = attackCoolDown;
    this.projectileType = projectileType;
    this.speed = speed;
    this.damage = damage;
    this.worth = worth;
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

  @Override
  public String getKey() {
    return this.key;
  }

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
  public int getHealth() {
    return this.health;
  }

  @Override
  public int getWorth() {
    return this.worth;
  }

  @Override
  public float getSpeed() {
    return this.speed;
  }
}
