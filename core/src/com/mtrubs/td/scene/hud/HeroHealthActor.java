package com.mtrubs.td.scene.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mtrubs.td.scene.hero.HeroActor;

public class HeroHealthActor extends CooldownActor {

  private final TextureRegion health;
  private final HealthInformer healthInformer;

  /**
   * Creates an actor with the given texture region at the given x,y coordinates.
   *
   * @param positionX     the x coordinate of this actor.
   * @param positionY     the y coordinate of this actor.
   * @param textureRegion the texture of this actor.
   * @param health        the texture for damage.
   * @param cooldown      the cooldown image.
   * @param unit          the unit of this thumbnail
   */
  public HeroHealthActor(float positionX, float positionY, TextureRegion textureRegion,
                         TextureRegion health, TextureRegion cooldown, HeroActor unit) {
    super(positionX, positionY, textureRegion, cooldown, unit);
    this.health = health;
    this.healthInformer = unit;
  }

  @Override
  public void draw(Batch batch, float alpha) {
    super.draw(batch, alpha);
    float x = getX() + 1.0F;
    float y = getY() + 1.0F;
    float width = (this.health.getRegionWidth() + 1.0F) * this.healthInformer.getPercentHealth();
    float height = this.health.getRegionHeight() + 2.0F;
    batch.draw(this.health, x, y, width, height);
  }
}
