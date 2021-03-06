package com.mtrubs.td.scene.level.mob;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mtrubs.td.graphics.level.TowerUnit;
import com.mtrubs.td.scene.TextureRegionActorAccessor;

/**
 * Actor to represent each tower unit.
 */
public class UnitActor extends PcActor<TowerUnit> {

  /**
   * Where this unit will spawn from.
   */
  private final Vector2 spawn;

  /**
   * The rally point of this unit.
   */
  private Vector2 home;

  /**
   * Creates an actor with the given texture region at the given x,y coordinates.
   *
   * @param positionX     the x coordinate of this actor.
   * @param positionY     the y coordinate of this actor.
   * @param textureRegion the texture of this actor.
   */
  public UnitActor(float positionX, float positionY, TowerUnit type, TextureRegion textureRegion) {
    super(positionX, positionY, textureRegion, type);
    this.spawn = new Vector2(positionX, positionY);
    this.home = this.spawn;
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    goHome();
  }

  private void goHome() {
    TweenManager tweenManager = getStage().getTweenManager();
    if (isVisible() && !hasTarget()) {
      Timeline timeline = Timeline.createSequence();

      timeline.push(Tween.to(this, TextureRegionActorAccessor.POSITION_XY,
        getDuration(new Vector2(getX(), getY()), this.home)).target(
        this.home.x, this.home.y).ease(TweenEquations.easeNone));
      timeline.start(tweenManager);
    } else {
      tweenManager.killTarget(this);
    }
  }

  @Override
  protected void despawn() {
    super.despawn();
    if (this.spawn != null) {
      setX(this.spawn.x);
      setY(this.spawn.y);
    }
  }

  @Override
  public void setType(TowerUnit type) {
    if (type == null) {
      despawn();
    } else {
      setHitPoints(type.getHealth());
      setTextureRegion(getTextureRegion(type));
    }
    super.setType(type);
  }

  public void setHome(Vector2 home) {
    this.home = home;
  }

  @Override
  protected boolean hasUnit() {
    return getType() != null;
  }

  @Override
  protected boolean canAttack() {
    return hasUnit() && super.canAttack();
  }
}
