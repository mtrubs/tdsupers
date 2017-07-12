package com.mtrubs.td.scene.level.mob;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mtrubs.td.graphics.level.Combatant;
import com.mtrubs.td.scene.TextureRegionActor;
import com.mtrubs.td.scene.TextureRegionActorAccessor;
import com.mtrubs.td.scene.level.LevelStage;
import com.mtrubs.td.scene.level.hud.HealthInformer;
import com.mtrubs.util.NonNull;

/**
 * Base actor that represents anything capable of engaging in combat.
 */
public abstract class CombatActor<T extends Combatant> extends TextureRegionActor
  implements Targetable, SelectableMover, HealthInformer {

  private static final float HEIGHT_SCALE = 0.5F;
  /**
   * The range of this unit. Used to determine what we are able to attack.
   * TODO: probably need one per skill...?
   */
  private final Ellipse range = new Ellipse();
  private Targetable target;
  private float attackCoolDown;
  private int hitPoints;
  private boolean selected;
  private T type;

  /**
   * Creates an actor with the given texture region at the given x,y coordinates.
   *
   * @param positionX     the x coordinate of this actor.
   * @param positionY     the y coordinate of this actor.
   * @param textureRegion the texture of this actor.
   * @param type          the type of this actor.
   */
  public CombatActor(float positionX, float positionY, TextureRegion textureRegion, T type) {
    super(positionX, positionY, textureRegion);
    setType(type);
    updateRange();
  }

  protected T getType() {
    return this.type;
  }

  protected void setType(T type) {
    this.type = type;
  }

  public void clearTarget(Targetable targetable) {
    if (isTargeting(targetable)) {
      clearTarget();
    }
  }

  protected void clearTarget() {
    this.target = null;
  }

  public boolean hasTarget() {
    return this.target != null;
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
    if (!isTargetable(this.target)) {
      clearTarget();
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

  protected boolean isTargetable(Targetable target) {
    return target != null && target.isVisible() && this.range.contains(target.getCenter());
  }

  @Override
  public void damage(int amount) {
    if (this.hitPoints > 0) {
      // ensure that we do not exceed 0
      this.hitPoints = Math.max(0, this.hitPoints - amount);
      if (this.hitPoints == 0) {
        handleDefeat();
      }
    }
  }

  public void heal(int amount) {
    int max = getMaxHealth();
    if (this.hitPoints > 0 && this.hitPoints < max) {
      System.out.println(this + " wants to heal " + amount);
      // ensure that we do not exceed the max
      this.hitPoints = Math.min(max, this.hitPoints + amount);
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
    attackTarget(delta);
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
      ProjectileActor projectile = new ProjectileActor(getCenterX(), getCenterY(),
        this, this.target, getTextureRegion(this.type.getProjectileType()));
      getStage().addActor(projectile);
      this.attackCoolDown += this.type.getAttackCoolDown();
    }
  }

  protected boolean canAttack() {
    return this.type.getProjectileType() != null;
  }

  protected abstract void handleDefeat();

  // FIXME: this logic doesnt seem quite right
  protected float getDuration(Vector2 a, Vector2 b) {
    return a.dst(b) / this.type.getSpeed();
  }

  @Override
  public int getDamage() {
    return this.type.getDamage();
  }

  @Override
  public LevelStage getStage() {
    return (LevelStage) super.getStage();
  }

  @Override
  public void moveTo(@NonNull InputEvent event) {
    moveTo(getCenter(), event.getStageX(), event.getStageY());
  }

  @Override
  public void select() {
    this.selected = true;
  }

  @Override
  public void deselect() {
    this.selected = false;
  }

  @Override
  public boolean isSelected() {
    return this.selected;
  }

  private void moveTo(Vector2 from, float toX, float toY) {
    // adjust to be the center
    Vector2 to = new Vector2(toX - getWidth() / 2.0F, toY - getHeight() / 2.0F);

    // stop other time-lines associated with this
    getTweenManager().killTarget(this);
    // move to the position
    Timeline timeline = Timeline.createSequence().delay(0.0F);
    float duration = getDuration(from, to);
    timeline.push(Tween.to(this,
      TextureRegionActorAccessor.POSITION_XY, duration).target(to.x, to.y).ease(TweenEquations.easeNone));
    timeline.start(getTweenManager());
  }

  private TweenManager getTweenManager() {
    return getStage().getTweenManager();
  }

  @Override
  public void draw(Batch batch, float alpha) {
    updateRange();

    // render this first if necessary so it ends up behind the image, gives illusion of depth.
    if (this.selected || LevelStage.DEBUG) {
      batch.end();
      Gdx.gl.glEnable(GL20.GL_BLEND);

      ShapeRenderer shapeRenderer = getStage().getShapeRenderer();
      shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
      shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

      if (this.selected) {
        float selectWidth = getWidth() + 5.0F;
        float selectHeight = getWidth() * HEIGHT_SCALE;
        float selectX = getCenterX() - selectWidth * 0.5F;
        float selectY = getY() - selectHeight * 0.5F;

        // Green outlined ellipse
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.0F, 1.0F, 0.0F, alpha * 0.5F);
        shapeRenderer.ellipse(selectX, selectY, selectWidth, selectHeight);
        shapeRenderer.end();
      }

      // why is this translation is needed vs ellipse.x, ellipse.y?
      // the x,y seem to be the center for intersections but bottom left for drawing
      float rangeX = this.range.x - this.range.width * 0.5F;
      float rangeY = getY() - this.range.height * 0.5F;

      // blue filled transparent circle
      shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
      shapeRenderer.setColor(0.0F, 0.0F, 1.0F, alpha * 0.1F);
      shapeRenderer.ellipse(rangeX, rangeY, this.range.width, this.range.height);
      shapeRenderer.end();

      // blue outlined circle
      shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
      shapeRenderer.setColor(0.0F, 0.0F, 1.0F, alpha * 0.5F);
      shapeRenderer.ellipse(rangeX, rangeY, this.range.width, this.range.height);
      shapeRenderer.end();

      Gdx.gl.glDisable(GL20.GL_BLEND);
      batch.begin();
    }

    super.draw(batch, alpha);
  }

  private void updateRange() {
    T type = getType();
    float rangeWidth = type == null ? 0.0F : type.getRange();
    float rangeHeight = type == null ? 0.0F : type.getRange() * HEIGHT_SCALE;
    float rangeX = getCenterX();
    float rangeY = getCenterY();
    this.range.set(rangeX, rangeY, rangeWidth, rangeHeight);
  }

  @Override
  public float getPercentHealth() {
    float health = this.hitPoints;
    float max = getMaxHealth();
    return Math.max(0.0F, health / max);
  }

  protected abstract int getMaxHealth();
}
