package com.mtrubs.td.scene;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mtrubs.td.graphics.TextureReference;
import com.mtrubs.util.NonNull;

/**
 * The actor that represents a particular texture region in the world.
 */
public class TextureRegionActor extends Actor {

  private TextureRegion textureRegion;

  /**
   * Creates an actor with the given texture region at the given x,y coordinates.
   *
   * @param positionX     the x coordinate of this actor.
   * @param positionY     the y coordinate of this actor.
   * @param textureRegion the texture of this actor.
   */
  public TextureRegionActor(float positionX, float positionY, final TextureRegion textureRegion) {
    this.textureRegion = textureRegion;
    if (textureRegion == null) {
      setBounds(positionX, positionY, 0.0F, 0.0F);
      setVisible(false);
    } else {
      setBounds(positionX, positionY, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    }
  }

  /**
   * Updates the texture region of this actor to that given.
   *
   * @param textureRegion the desired texture region of this actor.
   */
  public void setTextureRegion(TextureRegion textureRegion) {
    this.textureRegion = textureRegion;
    if (textureRegion == null) {
      setBounds(getX(), getY(), 0.0F, 0.0F);
      setVisible(false);
    } else {
      setBounds(getX(), getY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
      setVisible(true);
    }
  }

  @Override
  public void draw(Batch batch, float alpha) {
    super.draw(batch, alpha);
    if (this.textureRegion != null) {
      batch.draw(this.textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
        getScaleX(), getScaleY(), getRotation());
    }
  }

  public float getCenterX() {
    return getX() + getWidth() / 2.0F;
  }

  public float getCenterY() {
    return getY() + getHeight() / 2.0F;
  }

  @NonNull
  public Vector2 getCenter() {
    return new Vector2(getCenterX(), getCenterY());
  }

  protected TextureRegion getTextureRegion(TextureReference type) {
    LevelStage stage = (LevelStage) getStage();
    return stage == null ? null : stage.getTextureRegion(type);
  }
}
