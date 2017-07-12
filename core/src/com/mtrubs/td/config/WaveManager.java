package com.mtrubs.td.config;

import com.mtrubs.td.scene.level.LevelStage;
import com.mtrubs.td.scene.level.mob.MobActor;
import com.mtrubs.td.scene.level.mob.Targetable;

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
  /**
   * List of active mobs.
   */
  private final List<MobActor> mobs;
  private int wave = -1;

  public WaveManager(List<Wave> waves) {
    this.waves = waves;
    this.mobs = new ArrayList<MobActor>(); // FIXME: right size?
  }

  public void remove(MobActor actor) {
    this.mobs.remove(actor);
    actor.remove();
  }

  public void clearTarget(Targetable target) {
    for (MobActor mob : this.mobs) {
      mob.clearTarget(target);
    }
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

  /**
   * Returns the current wave as a person would think of it [1,...,n] or
   * the next wave as an array would think of it [0,...,n].
   *
   * @return number of the current wave; index of the next wave.
   */
  public int getCurrentWave() {
    // this is + 1 due to array index starting at 0
    return this.wave + 1;
  }

  // for readability
  private int getNextWaveIndex() {
    return getCurrentWave();
  }

  /**
   * @return the location of the next wave's start point. X coordinate.
   */
  public float getNextStartX() {
    int next = getNextWaveIndex();
    return next < this.waves.size() ? this.waves.get(next).getStartX() : 0.0F;
  }

  /**
   * @return the location of the next wave's start point. Y coordinate.
   */
  public float getNextStartY() {
    int next = getNextWaveIndex();
    return next < this.waves.size() ? this.waves.get(next).getStartY() : 0.0F;
  }

  /**
   * @return the delay until the next wave should start
   */
  public float getNextWaveDelay() {
    int next = getNextWaveIndex();
    return next < this.waves.size() ? this.waves.get(next).getDelay() : 100.0F;
  }

  /**
   * A wave manager is considered active if its first wave has started and its last wave
   * has not started.
   *
   * @return true if we are running the firth through last waves.
   */
  public boolean isActive() {
    int next = getNextWaveIndex(); // 0 if we have not started yet
    return next > 0 && next < this.getTotalWaves();
  }

  public void sort() {
    if (this.wave >= 0) {
      Collections.sort(this.mobs, MOB_COMPARATOR);
    }
  }

  /**
   * Gets the maximum amount of bonus currency that can be awarded for calling this wave early.
   *
   * @return the amount of bonus currency to be awarded (max).
   */
  public int getNextWaveBonus() {
    int next = getNextWaveIndex();
    return next > 0 && next < this.getTotalWaves() ? this.waves.get(next).getBonusCurrency() : 0;
  }
}
