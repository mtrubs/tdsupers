package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;

public enum HeroUnitType implements HeroUnit {

  TestHero1("images/towers/TestHero1/heroUnit.png", 10, 250.0F, 20.0F, 10.0F, 50.0F, 20, 25.0F, ProjectileType.RedBullet),
  TestHero2("images/towers/TestHero2/heroUnit.png", 20, 225.0F, 20.0F, 5.0F, 50.0F, 15, 30.0F, ProjectileType.RedBullet),
  TestHero3("images/towers/TestHero3/heroUnit.png", 30, 200.0F, 20.0F, 3.0F, 50.0F, 5, 35.0F, ProjectileType.RedBullet);

  private final String texturePath;
  private final String key;
  private final float range;
  private final float engageRange;
  private final float attackCoolDown;
  private final float deathCoolDown;
  private final int damage;
  private final int health;
  private final ProjectileType projectileType;
  private final float speed;

  private HeroUnitType(String texturePath, int health, float range, float engageRange, float attackCoolDown,
                       float deathCoolDown, int damage, float speed, ProjectileType projectileType) {
    this.texturePath = texturePath;
    this.key = String.format("%s.%s", getClass().getSimpleName(), name());

    this.range = range;
    this.engageRange = engageRange;
    this.attackCoolDown = attackCoolDown;
    this.deathCoolDown = deathCoolDown;
    this.damage = damage;
    this.health = health;
    this.speed = speed;
    this.projectileType = projectileType;
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
  public int getHealth() {
    return this.health;
  }

  @Override
  public ProjectileType getProjectileType() {
    return this.projectileType;
  }

  @Override
  public int getDamage() {
    return this.damage;
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
  public float getSpeed() {
    return this.speed;
  }

  @Override
  public float getDeathCoolDown() {
    return this.deathCoolDown;
  }
}
