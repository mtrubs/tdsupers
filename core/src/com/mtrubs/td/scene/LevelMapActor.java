package com.mtrubs.td.scene;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * The actor the represents the level map/background.
 *
 * @author mrubino
 * @since 2015-01-26
 */
public class LevelMapActor extends Actor {

  private final TextureRegion textureRegion;

  public LevelMapActor(TextureRegion textureRegion) {
    this.textureRegion = textureRegion;
    setBounds(0.0F, 0.0F, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
  }

  @Override
  public void draw(Batch batch, float alpha) {
    super.draw(batch, alpha);
    batch.draw(this.textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
      getScaleX(), getScaleY(), getRotation());
  }
}