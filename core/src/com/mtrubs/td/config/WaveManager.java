package com.mtrubs.td.config;

import com.mtrubs.td.scene.LevelStage;
import com.mtrubs.td.scene.MobActor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WaveManager {

  /**
   * A comparator to sort the mobs by most progress to least.
   */
  private static final Comparator<MobActor> MOB_COMPARATOR = new Comparator<MobActor>() {

    @Override
    public int compare(MobActor o1, MobActor o2) {
      return Float.compare(o2.getProgress(), o1.getProgress());
    }
  };

  private final List<Wave> waves;

  private int wave = -1;

  /**
   * List of active mobs.
   */
  private List<MobActor> mobs;

  public WaveManager(List<Wave> waves) {
    this.waves = waves;
    this.mobs = new ArrayList<MobActor>(); // TODO: right size?
  }

  public void remove(MobActor actor) {
    this.mobs.remove(actor);
    actor.remove();
  }

  public List<MobActor> getActiveMobs() {
    return this.mobs;
  }

  public void startWave(LevelStage stage) {
    this.wave++;
    for (MobActor mob : this.waves.get(this.wave).getMobs()) {
      stage.addActor(mob);
      this.mobs.add(mob);
      mob.start();
    }
  }

  public int getTotalWaves() {
    return this.waves.size();
  }

  public int getCurrentWave() {
    // this is + 1 due to array index starting at 0
    return this.wave + 1;
  }

  /**
   * @return the location of the next wave's start point. X coordinate.
   */
  public float getNextStartX() {
    int current = getCurrentWave();
    return current < this.waves.size() ? this.waves.get(current).getStartX() : 0.0F;
  }

  /**
   * @return the location of the next wave's start point. Y coordinate.
   */
  public float getNextStartY() {
    int current = getCurrentWave();
    return current < this.waves.size() ? this.waves.get(current).getStartY() : 0.0F;
  }

  /**
   * @return the delay until the next wave should start
   */
  public float getNextWaveDelay() {
    int current = getCurrentWave();
    return current < this.waves.size() ? this.waves.get(current).getDelay() : 100.0F;
  }

  /**
   * A wave manager is considered active if its first wave has started and its last wave
   * has not started.
   *
   * @return true if we are running the firth through last waves.
   */
  public boolean isActive() {
    int current = getCurrentWave();
    return current > 0 && current < this.getTotalWaves();
  }

  public void sort() {
    if (this.wave >= 0) {
      Collections.sort(this.mobs, MOB_COMPARATOR);
    }
  }
}
