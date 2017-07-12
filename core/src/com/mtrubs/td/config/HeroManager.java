package com.mtrubs.td.config;

import com.mtrubs.td.graphics.Hero;
import com.mtrubs.td.graphics.HeroTower;
import com.mtrubs.td.graphics.TextureRegionManager;
import com.mtrubs.td.scene.level.LevelStage;
import com.mtrubs.td.scene.level.mob.HeroActor;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class HeroManager {

  // TODO: config driven
  private static final float[] xs = {400.0F, 200.0F, 300.0F};
  private static final float[] ys = {200.0F, 200.0F, 300.0F};
  private final List<Hero> activeHeroes;
  private final Map<Hero, HeroActor> actors;

  public HeroManager(List<Hero> activeHeroes) {
    this.activeHeroes = activeHeroes;
    this.actors = new EnumMap<Hero, HeroActor>(Hero.class);
  }

  public List<Hero> getActiveHeroes() {
    return this.activeHeroes;
  }

  public List<HeroTower> getActiveTowers() {
    List<HeroTower> towers = new ArrayList<HeroTower>(this.activeHeroes.size());
    for (Hero hero : this.activeHeroes) {
      towers.add(hero.getTower());
    }
    return towers;
  }

  public void createActors(LevelStage stage, TextureRegionManager textureRegionManager) {
    for (int i = 0; i < this.activeHeroes.size(); i++) {
      Hero hero = this.activeHeroes.get(i);
      HeroActor actor = hero.newActor(textureRegionManager, xs[i], ys[i]);
      stage.addActor(actor);
      this.actors.put(hero, actor);
    }
  }

  public HeroActor getActor(Hero hero) {
    return this.actors.get(hero);
  }
}
