package com.mtrubs.td.scene.level.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.mtrubs.td.scene.TextureRegionActor;

public class CooldownActor extends TextureRegionActor {

  private final CooldownInformer cooldownInformer;
  private final Sprite cooldown;

  /**
   * Creates an actor with the given texture region at the given x,y coordinates.
   *
   * @param positionX        the x coordinate of this actor.
   * @param positionY        the y coordinate of this actor.
   * @param textureRegion    the texture of this actor.
   * @param cooldown         the cooldown image.
   * @param cooldownInformer manages the cooldown associated with this actor.
   */
  public CooldownActor(float positionX, float positionY,
                       TextureRegion textureRegion,
                       TextureRegion cooldown,
                       CooldownInformer cooldownInformer) {
    super(positionX, positionY, textureRegion);
    this.cooldown = new Sprite(cooldown);
    this.cooldown.setX(positionX);
    this.cooldown.setY(positionY);
    this.cooldownInformer = cooldownInformer;
  }

  @Override
  public void draw(Batch batch, float alpha) {
    super.draw(batch, alpha);
    float cooldown = this.cooldownInformer.getPercentCooldown();
    if (cooldown > 0.0F) {
      setTouchable(Touchable.disabled);
      this.cooldown.draw(batch, cooldown);
    } else {
      setTouchable(Touchable.enabled);
    }
  }
}
