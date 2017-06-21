package com.mtrubs.td.scene;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mtrubs.td.graphics.ProjectileType;
import com.mtrubs.td.graphics.TowerUnit;

/**
 * Actor to represent each tower unit.
 */
public class UnitActor extends CombatActor {

  private TowerUnit type;
  private float deathCoolDown;
  private Vector2 home;

  /**
   * Creates an actor with the given texture region at the given x,y coordinates.
   *
   * @param positionX     the x coordinate of this actor.
   * @param positionY     the y coordinate of this actor.
   * @param textureRegion the texture of this actor.
   */
  public UnitActor(float positionX, float positionY, TowerUnit type, TextureRegion textureRegion) {
    super(positionX, positionY, textureRegion);
    setType(type);
  }

  @Override
  public void act(float delta) {
    respawn(delta);
    super.act(delta);
  }

  private void respawn(float delta) {
    if (hasUnit() && !isVisible()) {
      this.deathCoolDown -= delta;
      if (this.deathCoolDown <= 0.0F) {
        setVisible(true);
      }
    }
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
    untarget();
  }

  private void untarget() {
    clearTarget();
    LevelStage stage = (LevelStage) getStage();
    if (stage != null) {
      for (MobActor mob : stage.getWaveManager().getActiveMobs()) {
        mob.clearTarget(this);
      }
    }
  }

  @Override
  protected void handleTarget(float delta) {
    super.handleTarget(delta);
    attackTarget(delta);
  }

  @Override
  protected Targetable checkForTarget() {
    LevelStage stage = (LevelStage) getStage();
    // if able, towers will attack the first unit they can
    for (MobActor mob : stage.getWaveManager().getActiveMobs()) {
      if (isInRange(mob)) {
        return mob;
      }
    }
    return super.checkForTarget();
  }

  @Override
  protected float getRange() {
    return this.type.getRange();
  }

  public void setType(TowerUnit type) {
    if (type != null) {
      setHitPoints(type.getHealth());
    }
    this.type = type;
  }

  public void setHome(Vector2 home) {
    this.home = home;
  }

  @Override
  protected float getAttackCoolDown() {
    return this.type.getAttackCoolDown();
  }

  @Override
  protected ProjectileType getProjectileType() {
    return this.type.getProjectileType();
  }

  private boolean hasUnit() {
    return this.type != null;
  }

  @Override
  protected boolean canAttack() {
    return hasUnit();
  }

  @Override
  protected void handleDefeat() {
    setVisible(false);
    this.deathCoolDown = this.type.getDeathCoolDown();
  }

  @Override
  public int getDamage() {
    return this.type.getDamage();
  }
}
