package com.mtrubs.td.scene.level.mob;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mtrubs.td.graphics.PlayerControlled;
import com.mtrubs.td.scene.level.LevelStage;
import com.mtrubs.td.scene.level.hud.CooldownInformer;

public abstract class PcActor<T extends PlayerControlled> extends CombatActor<T> implements CooldownInformer {

  private static final float OUT_OF_COMBAT = 5.0F;
  private static final float HEAL_TICK = 1.0F;

  private float deathCoolDown;
  private float outOfCombatTimer;
  private float healTimer;

  /**
   * Creates an actor with the given texture region at the given x,y coordinates.
   *
   * @param positionX     the x coordinate of this actor.
   * @param positionY     the y coordinate of this actor.
   * @param textureRegion the texture of this actor.
   * @param type          the type of this actor.
   */
  public PcActor(float positionX, float positionY, TextureRegion textureRegion, T type) {
    super(positionX, positionY, textureRegion, type);
  }

  @Override
  public void act(float delta) {
    respawn(delta);
    super.act(delta);
  }

  @Override
  protected void handleNoTarget(float delta) {
    super.handleNoTarget(delta);
    this.outOfCombatTimer += delta;
    // TODO: and is hurt
    if (this.outOfCombatTimer >= OUT_OF_COMBAT) { // once a PC has been OOC for 5 seconds it can heal
      this.healTimer += delta;
      if (this.healTimer > HEAL_TICK) {
        this.healTimer -= HEAL_TICK;
        T type = getType();
        heal(type == null ? 0 : type.getHps());
      }
    }
  }

  @Override
  protected void handleTarget(float delta) {
    super.handleTarget(delta);
    this.outOfCombatTimer = 0.0F;
    this.healTimer = 0.0F;
  }

  @Override
  protected void setType(T type) {
    super.setType(type);
    // this makes the units spawn at the start
    setVisible(false);
    this.deathCoolDown = 0.1F;
  }

  @Override
  public void setVisible(boolean visible) {
    if (isVisible() && !visible) {
      despawn();
    }
    super.setVisible(visible);
  }

  protected void respawn(float delta) {
    if (hasUnit() && !isVisible()) {
      if (this.deathCoolDown > 0.0F) {
        this.deathCoolDown -= delta;
        if (this.deathCoolDown <= 0.0F) {
          setHitPoints(getType().getHealth());
          setVisible(true);
          getStage().getUnitManager().register(this);
        }
      }
    }
  }

  protected abstract boolean hasUnit();

  @Override
  protected void handleDefeat() {
    setVisible(false);
    this.deathCoolDown = getType().getDeathCoolDown();
  }

  protected void despawn() {
    clearTarget();
    LevelStage stage = getStage();
    // if the stage is null we could not have registered it yet
    if (stage != null) {
      stage.getUnitManager().unregister(this);
      stage.getWaveManager().clearTarget(this);
    }
  }

  @Override
  protected Targetable checkForTarget() {
    LevelStage stage = getStage();
    // if able, towers will attack the first unit they can
    // TODO: change this for different types
    for (MobActor mob : stage.getWaveManager().getActiveMobs()) {
      if (isTargetable(mob)) {
        return mob;
      }
    }
    return super.checkForTarget();
  }

  @Override
  public float getPercentCooldown() {
    T type = getType();
    return type == null ? 0.0F : this.deathCoolDown / type.getDeathCoolDown() * 0.75F;
  }

  @Override
  protected int getMaxHealth() {
    T type = getType();
    return type == null ? 0 : this.getType().getHealth();
  }
}
