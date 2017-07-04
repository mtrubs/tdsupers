package com.mtrubs.td.scene.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.mtrubs.td.scene.TextureRegionActor;

public class CooldownActor extends TextureRegionActor {

  private final CooldownManager cooldownManager;
  private final Sprite cooldown;

  /**
   * Creates an actor with the given texture region at the given x,y coordinates.
   *
   * @param positionX       the x coordinate of this actor.
   * @param positionY       the y coordinate of this actor.
   * @param textureRegion   the texture of this actor.
   * @param cooldown        the cooldown image.
   * @param cooldownManager manages the cooldown associated with this actor.
   */
  public CooldownActor(float positionX, float positionY,
                       TextureRegion textureRegion,
                       TextureRegion cooldown,
                       CooldownManager cooldownManager) {
    super(positionX, positionY, textureRegion);
    this.cooldown = new Sprite(cooldown);
    this.cooldown.setX(positionX);
    this.cooldown.setY(positionY);
    this.cooldownManager = cooldownManager;
  }

  @Override
  public void draw(Batch batch, float alpha) {
    super.draw(batch, alpha);
    float cooldown = this.cooldownManager.getPercentCooldown();
    if (cooldown > 0.0F) {
      setTouchable(Touchable.disabled);
      this.cooldown.draw(batch, cooldown);
    } else {
      setTouchable(Touchable.enabled);
    }
  }
}
