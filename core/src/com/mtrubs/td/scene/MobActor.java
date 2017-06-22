package com.mtrubs.td.scene;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mtrubs.td.graphics.Mob;
import com.mtrubs.td.graphics.ProjectileType;

/**
 * The actor that represents each mob.
 */
public class MobActor extends CombatActor {

  private final Vector2[] path;
  private final float startDelay;
  private final Mob type;

  private Timeline timeline;

  /**
   * Creates an actor with the given texture region at the given x,y coordinates.
   *
   * @param path          the path of this actor.
   * @param textureRegion the texture of this actor.
   */
  public MobActor(Vector2[] path, Mob type, float scale, float startDelay,
                  TextureRegion textureRegion) {
    super(path[0].x, path[0].y, textureRegion, type.getSpeed());
    setHitPoints((int) ((float) type.getHealth() * scale));

    this.path = path;
    this.startDelay = startDelay;
    this.type = type;
  }

  public float getProgress() {
    return this.timeline.getCurrentTime();
  }

  @Override
  protected void handleNoTarget(float delta) {
    super.handleNoTarget(delta);
    // otherwise if we are not moving we continue moving again
    startMoving();
  }

  @Override
  protected void handleTarget(float delta) {
    super.handleTarget(delta);
    // if we have a target we stop moving and attack it
    stopMoving();
    attackTarget(delta);
  }

  @Override
  protected float getAttackCoolDown() {
    return this.type.getAttackCoolDown();
  }

  @Override
  protected ProjectileType getProjectileType() {
    return this.type.getProjectileType();
  }

  @Override
  protected boolean canAttack() {
    return getProjectileType() != null;
  }

  @Override
  protected void handleDefeat() {
    // on defeat we:
    LevelStage stage = (LevelStage) getStage();
    // clear our target
    clearTarget();
    // add currency for this mob's value
    stage.getCurrencyManager().add(getWorth());
    // remove this mob from the wave manager
    stage.getWaveManager().remove(this);
    // remove this mob from any unit's target
    for (PcActor unit : stage.getUnitManager().getUnits()) {
      unit.clearTarget(this);
    }
    // remove this mob from the stage
    remove();
  }

  private void stopMoving() {
    if (!this.timeline.isPaused()) {
      this.timeline.pause();
    }
  }

  private void startMoving() {
    if (this.timeline.isPaused()) {
      this.timeline.resume();
    }
  }

  @Override
  protected Targetable checkForTarget() {
    LevelStage stage = (LevelStage) getStage();
    // mobs will only attack the unit if it is attacking them
    // TODO: change this for different types of mobs
    for (PcActor unit : stage.getUnitManager().getUnits()) {
      if (unit.isDamageable() && unit.isTargeting(this) && isInRange(unit)) {
        return unit;
      }
    }
    return super.checkForTarget();
  }

  @Override
  protected float getRange() {
    return this.type.getRange();
  }

  @Override
  public int getDamage() {
    return this.type.getDamage();
  }

  public void start() {
    if (this.timeline != null) {
      throw new RuntimeException("This mob has already been started"); // TODO: type the exception
    }
    this.timeline = Timeline.createSequence().delay(this.startDelay);
    for (int i = 1; i < this.path.length; i++) {
      float duration = getDuration(this.path[i - 1], this.path[i]);
      this.timeline.push(Tween.to(this, TextureRegionActorAccessor.POSITION_XY, duration).target(
        this.path[i].x, this.path[i].y).ease(TweenEquations.easeNone));
    }
    this.timeline.start(getTweenManager());
  }

  private TweenManager getTweenManager() {
    return ((LevelStage) getStage()).getTweenManager();
  }

  public int getWorth() {
    return this.type.getWorth();
  }
}
