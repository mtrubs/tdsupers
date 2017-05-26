package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;

/**
 * @author mrubino
 * @since 2015-02-11
 */
public enum TowerUnit implements TextureReference {

    TestHeroUnit("images/towers/TestHero/Unit1.png", 30, 100.0F, 100.0F, 10.0F, 5.0F, 10, ProjectileType.YellowBullet);

    private final String texturePath;
    private final String key;
    private final float range;
    private final float engageRange;
    private final float attackCoolDown;
    private final float deathCoolDown;
    private final int damage;
    private final int health;
    private final ProjectileType projectileType;

    private TowerUnit(String texturePath, int health, float range, float engageRange,
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

    @Override
    public String getKey() {
        return this.key;
    }

    public ProjectileType getProjectileType() {
        return this.projectileType;
    }

    public float getAttackCoolDown() {
        return this.attackCoolDown;
    }

    public float getRange() {
        return this.range;
    }

    public int getDamage() {
        return this.damage;
    }

    public float getDeathCoolDown() {
        return this.deathCoolDown;
    }

    public int getHealth() {
        return this.health;
    }
}
