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
   * Where the 'call wave' button shows up. X coordinate.
   */
  private final float callX;
  /**
   * Where the 'call wave' button shows up. Y coordinate.
   */
  private final float callY;
  /**
   * How much time to delay this way from the next.
   */
  private final float delay;

  /**
   * The list of mobs associated with this wave.
   */
  private List<MobActor> mobs;

  public Wave(float delay, TextureRegionManager textureRegionManager) {
    // TODO: path moves to be configurable
    Vector2[] path = new Vector2[]{
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

    this.callX = Math.min(Math.max(path[0].x, 0.0F), 800.0F); // TODO: cleaner safe bounds with world
    this.callY = Math.min(Math.max(path[0].y, 0.0F), 600.0F); // TODO: cleaner safe bounds with world
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
}
