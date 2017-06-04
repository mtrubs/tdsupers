package com.mtrubs.td.scene;

import com.badlogic.gdx.math.Vector2;
import com.mtrubs.td.graphics.Mob;
import com.mtrubs.td.graphics.TextureRegionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mrubino
 * @since 2017-06-02
 */
public class Wave {

  // TODO: make this a list of X,Y's for more complex maps
  /**
   * Where the wave starts. X coordinate.
   */
  private final float startX;
  /**
   * Where the wave starts. Y coordinate.
   */
  private final float startY;
  /**
   * How much time, in seconds, until this wave would start.
   */
  private float delay;

  /**
   * The list of mobs associated with this wave.
   */
  private List<MobActor> mobs;

  private static int count = 0; // TODO: just for test

  public Wave(float delay, TextureRegionManager textureRegionManager) {
    // TODO: path moves to be configurable
    Vector2[] values = new Vector2[]{
      new Vector2(-100.0F, 289.0F),
      new Vector2(3.0F, 289.0F),
      new Vector2(100.0F, 260.0F),
      new Vector2(157.0F, 233.0F),
      new Vector2(238.0F, 227.0F),
      new Vector2(301.0F, 218.0F),
      new Vector2(413.0F, 233.0F),
      new Vector2(476.0F, 233.0F),
      new Vector2(532.0F, 255.0F),
      new Vector2(550.0F, 300.0F),
      new Vector2(613.0F, 340.0F),
      new Vector2(675.0F, 348.0F),
      new Vector2(757.0F, 345.0F),
      new Vector2(1210.0F, 257.0F)
    };

    // TODO: just for test
    float y2x = 800.0F / 600.0F;
    float x2y = 600.0F / 800.0F;
    Vector2[] path = new Vector2[values.length];
    for (int i = 0; i < values.length; i++) {
      switch (count) {
        case 0:
          // left to right
          path[i] = new Vector2(values[i].x, values[i].y);
          break;
        case 1:
          // right to left
          path[i] = new Vector2(800.0F + -values[i].x, 600.0F + -values[i].y);
          break;
        case 2:
          // bottom to top
          path[i] = new Vector2(values[i].y * y2x, values[i].x * x2y);
          break;
        default:
          // top to bottom
          path[i] = new Vector2(800.0F + -values[i].y * y2x, 600.0F + -values[i].x * x2y);
      }
    }
    count++;

    this.startX = path[0].x;
    this.startY = path[0].y;
    this.delay = delay;

    this.mobs = new ArrayList<MobActor>(5); // TODO: right size

    // TODO: mobs move to be configurable
    this.mobs.add(new MobActor(path, Mob.TestMob, 1.0F, 25.0F, 0.0F,
      textureRegionManager.get(Mob.TestMob)));
    this.mobs.add(new MobActor(path, Mob.TestMob, 1.0F, 25.0F, 1.5F,
      textureRegionManager.get(Mob.TestMob)));
    this.mobs.add(new MobActor(path, Mob.TestMob, 1.0F, 25.0F, 3.0F,
      textureRegionManager.get(Mob.TestMob)));
    this.mobs.add(new MobActor(path, Mob.TestMob, 1.0F, 25.0F, 4.5F,
      textureRegionManager.get(Mob.TestMob)));
    this.mobs.add(new MobActor(path, Mob.TestMob, 1.0F, 25.0F, 6.0F,
      textureRegionManager.get(Mob.TestMob)));
  }

  public List<MobActor> getMobs() {
    return this.mobs;
  }

  public float getStartX() {
    return this.startX;
  }

  public float getStartY() {
    return this.startY;
  }

  public float getDelay() {
    return this.delay;
  }
}
