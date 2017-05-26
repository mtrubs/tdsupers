package com.mtrubs.td.scene;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author mrubino
 * @since 2015-02-18
 */
public class ProjectileActor extends TextureRegionActor {

  private final Targetable source;
  private final Targetable target;

  /**
   * Creates an actor with the given texture region at the given x,y coordinates.
   * The projectile will originate at the source and travel to the target.
   *
   * @param positionX     the starting X coordinate.
   * @param positionY     the starting Y coordinate.
   * @param source        the originator of this projectile.
   * @param target        the target of this projectile.
   * @param textureRegion the texture of this actor.
   */
  public ProjectileActor(float positionX, float positionY, Targetable source, Targetable target, TextureRegion textureRegion) {
    super(positionX, positionY, textureRegion);
    this.source = source;
    this.target = target;
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    // move towards the target
    Vector2 start = getCenter();
    Vector2 end = this.target.getCenter();
    float speed = 30.0F;

    float distance = start.dst(end);
    float dx = (end.x - start.x) / distance;
    float dy = (end.y - start.y) / distance;
    setX(getX() + dx * speed * delta);
    setY(getY() + dy * speed * delta);

    // if we have moved to or past then we are done
    Vector2 current = getCenter();
    if (current.dst(start) >= distance) {
      // TODO damage
      this.target.damage(this.source.getDamage());
      remove();
    }
  }
}
