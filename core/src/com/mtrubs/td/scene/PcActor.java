package com.mtrubs.td.scene;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class PcActor extends CombatActor {

  private float deathCoolDown;

  /**
   * Creates an actor with the given texture region at the given x,y coordinates.
   *
   * @param positionX     the x coordinate of this actor.
   * @param positionY     the y coordinate of this actor.
   * @param textureRegion the texture of this actor.
   * @param speed         the speed of this actor.
   */
  public PcActor(float positionX, float positionY, TextureRegion textureRegion, float speed) {
    super(positionX, positionY, textureRegion, speed);
    // this makes the units spawn at the start
    setVisible(false);
    this.deathCoolDown = 0.01F;
  }

  @Override
  public void act(float delta) {
    respawn(delta);
    super.act(delta);
  }

  @Override
  public void setVisible(boolean visible) {
    if (isVisible() && !visible) {
      untarget();
    }
    super.setVisible(visible);
  }

  private void untarget() {
    clearTarget();
    LevelStage stage = (LevelStage) getStage();
    if (stage != null) {
      stage.getWaveManager().clearTarget(this);
    }
  }

  @Override
  protected void handleTarget(float delta) {
    super.handleTarget(delta);
    attackTarget(delta);
  }

  protected void respawn(float delta) {
    if (hasUnit() && !isVisible()) {
      if (this.deathCoolDown > 0.0F) {
        this.deathCoolDown -= delta;
        if (this.deathCoolDown <= 0.0F) {
          setVisible(true);
          ((LevelStage) getStage()).getUnitManager().register(this);
        }
      }
    }
  }

  protected abstract boolean hasUnit();

  protected abstract float getDeathCoolDown();

  @Override
  protected void handleDefeat() {
    despawn();
    setVisible(false);
    this.deathCoolDown = getDeathCoolDown();
  }

  protected void despawn() {
    LevelStage stage = ((LevelStage) getStage());
    // if the stage is null we could not have registered it yet
    if (stage != null) {
      stage.getUnitManager().unregister(this);
    }
  }

  @Override
  protected Targetable checkForTarget() {
    LevelStage stage = (LevelStage) getStage();
    // if able, towers will attack the first unit they can
    for (MobActor mob : stage.getWaveManager().getActiveMobs()) {
      if (mob.isDamageable() && isInRange(mob)) {
        return mob;
      }
    }
    return super.checkForTarget();
  }
}
