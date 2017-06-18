package com.mtrubs.td.scene.hero;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mtrubs.td.graphics.HeroUnit;
import com.mtrubs.td.graphics.ProjectileType;
import com.mtrubs.td.graphics.TextureRegionManager;
import com.mtrubs.td.scene.CombatActor;
import com.mtrubs.td.scene.LevelStage;
import com.mtrubs.td.scene.TextureRegionActorAccessor;

public class TestHero1Actor extends CombatActor implements SelectableMover {

  private Timeline timeline;
  private float speed = 25.0F;
  private boolean selected;

  public TestHero1Actor(TextureRegionManager textureRegionManager, float startX, float startY) {
    super(startX, startY, textureRegionManager.get(HeroUnit.TestHero1));

    addListener(new ClickListener() {

      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        super.touchDown(event, x, y, pointer, button);
        return true; // to intercept for the touch up event
      }

      @Override
      public void touchUp(InputEvent event, float xDelta, float yDelta, int pointer, int button) {
        super.touchUp(event, xDelta, yDelta, pointer, button);
        if (!inTapSquare()) {
          // as long as we get the touchUp and we are outside the tap square then we move
          moveTo(event);
        }
      }

      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        ((LevelStage) TestHero1Actor.this.getStage()).setSelected(TestHero1Actor.this);
      }
    });
  }

  @Override
  public void moveTo(InputEvent event) {
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

  private void moveTo(Vector2 from, float toX, float toY) {
    // adjust to be the center
    Vector2 to = new Vector2(toX - getWidth() / 2.0F, toY - getHeight() / 2.0F);

    // stop other timelines associated with this
    getTweenManager().killTarget(this);
    // move to the position
    this.timeline = Timeline.createSequence().delay(0.0F);
    float duration = getDuration(from, to);
    this.timeline.push(Tween.to(this,
      TextureRegionActorAccessor.POSITION_XY, duration).target(to.x, to.y).ease(TweenEquations.easeNone));
    this.timeline.start(getTweenManager());
  }

  // TODO: this logic (and thus for MobActors too) doesnt seem quite right
  private float getDuration(Vector2 a, Vector2 b) {
    return a.dst(b) / this.speed;
  }

  private TweenManager getTweenManager() {
    return ((LevelStage) getStage()).getTweenManager();
  }

  @Override
  public int getDamage() {
    return 0; // TODO
  }

  @Override
  protected float getRange() {
    return 0; // TODO
  }

  @Override
  protected float getAttackCoolDown() {
    return 0; // TODO
  }

  @Override
  protected ProjectileType getProjectileType() {
    return null; // TODO
  }

  @Override
  protected boolean canAttack() {
    return false; // TODO
  }

  @Override
  protected void handleDefeat() {
    // TODO
  }

  @Override
  public void draw(Batch batch, float alpha) {
    super.draw(batch, alpha);
    if (this.selected) {
      // TODO: selection notification
    }
  }
}
