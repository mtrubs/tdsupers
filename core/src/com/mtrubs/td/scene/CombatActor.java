package com.mtrubs.td.scene;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mtrubs.td.graphics.ProjectileType;

/**
 * Base actor that represents anything capable of engaging in combat.
 */
public abstract class CombatActor extends TextureRegionActor implements Targetable {

  private Targetable target;
  private float attackCoolDown;
  private int hitPoints;

  /**
   * Creates an actor with the given texture region at the given x,y coordinates.
   *
   * @param positionX     the x coordinate of this actor.
   * @param positionY     the y coordinate of this actor.
   * @param textureRegion the texture of this actor.
   */
  public CombatActor(float positionX, float positionY, TextureRegion textureRegion) {
    super(positionX, positionY, textureRegion);
  }

  public void clearTarget(Targetable targetable) {
    if (isTargeting(targetable)) {
      clearTarget();
    }
  }

  protected void clearTarget() {
    this.target = null;
  }

  public boolean isTargeting(Targetable targetable) {
    return this.target == targetable;
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    if (!isVisible()) {
      resetCoolDown();
      return;
    }

    // make sure the current target (if there is one) is in range
    if (this.target != null && !isInRange(this.target)) {
      this.target = null;
    }

    // if we do not have a target then acquire one
    if (this.target == null) {
      this.target = checkForTarget();
    }

    // depending on if we have a target act on that
    if (this.target == null) {
      handleNoTarget(delta);
    } else {
      handleTarget(delta);
    }
  }

  @Override
  public void damage(int amount) {
    if (this.hitPoints > 0) {
      this.hitPoints -= amount;
      if (this.hitPoints <= 0) {
        handleDefeat();
      }
    }
  }

  public void setHitPoints(int amount) {
    this.hitPoints = amount;
  }

  protected Targetable checkForTarget() {
    return null;
  }

  protected void handleNoTarget(float delta) {
    this.attackCoolDown = Math.max(0.0F, this.attackCoolDown - delta);
  }

  protected void handleTarget(float delta) {
  }

  private void resetCoolDown() {
    this.attackCoolDown = 0.0F;
  }

  protected void attackTarget(float delta) {
    this.attackCoolDown -= delta;
    if (!canAttack()) {
      return;
    }
    if (this.attackCoolDown <= 0.0F) {
      LevelStage stage = (LevelStage) getStage();
      ProjectileActor projectile = new ProjectileActor(getCenterX(), getCenterY(),
        this, this.target, stage.getTextureRegion(getProjectileType()));
      getStage().addActor(projectile);
      this.attackCoolDown += getAttackCoolDown();
    }
  }

  protected boolean isInRange(Targetable target) {
    if (target != null) {
      Vector2 unitLoc = getCenter();
      Vector2 mobLoc = target.getCenter();
      if (unitLoc.dst(mobLoc) < getRange()) {
        return true;
      }
    }
    return false;
  }

  protected abstract float getRange();

  protected abstract float getAttackCoolDown();

  protected abstract ProjectileType getProjectileType();

  protected abstract boolean canAttack();

  protected abstract void handleDefeat();
}
