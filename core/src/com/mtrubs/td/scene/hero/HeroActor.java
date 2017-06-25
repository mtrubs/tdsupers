package com.mtrubs.td.scene.hero;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mtrubs.td.graphics.HeroUnit;
import com.mtrubs.td.scene.PcActor;
import com.mtrubs.td.scene.TextureRegionActorAccessor;

public class HeroActor extends PcActor<HeroUnit> implements SelectableMover {

  private boolean selected;

  public HeroActor(HeroUnit type, TextureRegion textureRegion, float startX, float startY) {
    super(startX, startY, textureRegion, type);
    setHitPoints(type.getHealth());

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
        getStage().setSelected(HeroActor.this);
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
    // render this first if necessary so it ends up behind the image, gives illusion of depth.
    if (this.selected) {
      batch.end();

      ShapeRenderer shapeRenderer = getStage().getShapeRenderer();

      shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
      shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

      // Green outlined ellipse
      shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
      shapeRenderer.setColor(0.0F, 1.0F, 0.0F, 0.75F);
      shapeRenderer.ellipse(getX() - 3.0F, getY() - 3.0F,
        getWidth() + 3.0F, 15.0F); // TODO: factor of height?
      shapeRenderer.end();

      batch.begin();
    }

    super.draw(batch, alpha);
  }

  @Override
  protected boolean hasUnit() {
    return true;
  }
}
