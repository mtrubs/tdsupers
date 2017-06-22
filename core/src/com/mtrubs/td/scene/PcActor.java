package com.mtrubs.td.scene;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class PcActor extends CombatActor {

  /**
   * Creates an actor with the given texture region at the given x,y coordinates.
   *
   * @param positionX     the x coordinate of this actor.
   * @param positionY     the y coordinate of this actor.
   * @param textureRegion the texture of this actor.
   * @param speed         the speed of this actor.
   */
  public PcActor(float positionX, float positionY, TextureRegion textureRegion, float speed) {
    super(positionX, positionY, textureRegion, speed);
  }
}
