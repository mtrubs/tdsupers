package com.mtrubs.td.scene;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author mrubino
 * @since 2017-06-02
 */
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

  private final TweenManager tweenManager;
  private final List<Wave> waves;

  private int wave = -1;

  /**
   * List of active mobs.
   */
  private List<MobActor> mobs;
  private Stage stage;

  public WaveManager(List<Wave> waves) {
    this.waves = waves;
    this.tweenManager = new TweenManager();
    this.mobs = new ArrayList<MobActor>(); // TODO: right size?
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void remove(MobActor actor) {
    this.mobs.remove(actor);
    actor.remove();
  }

  public List<MobActor> getActiveMobs() {
    return this.mobs;
  }

  public void startWave() {
    this.wave++;
    for (MobActor mob : this.waves.get(this.wave).getMobs()) {
      this.stage.addActor(mob);
      this.mobs.add(mob);
      mob.start(this.tweenManager);
    }
  }

  public int getTotalWaves() {
    return this.waves.size();
  }

  public int getCurrentWave() {
    // this is + 1 due to array index starting at 0
    return this.wave + 1;
  }

  public void update(float delta) {
    if (this.wave >= 0) {
      Collections.sort(this.mobs, MOB_COMPARATOR);
      this.tweenManager.update(delta);
    }
  }

  public void dispose() {
    this.tweenManager.killAll();
  }
}
