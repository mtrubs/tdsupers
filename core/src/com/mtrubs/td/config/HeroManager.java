package com.mtrubs.td.config;

import com.mtrubs.td.graphics.Hero;
import com.mtrubs.td.graphics.TextureRegionManager;
import com.mtrubs.td.scene.LevelStage;
import com.mtrubs.td.scene.hero.TestHero1Actor;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class HeroManager {


  private final List<Hero> activeHeroes;
  private final Map<Hero, TestHero1Actor> actors;

  public HeroManager(List<Hero> activeHeroes) {
    this.activeHeroes = activeHeroes;
    this.actors = new EnumMap<Hero, TestHero1Actor>(Hero.class);
  }

  public List<Hero> getActiveHeroes() {
    return this.activeHeroes;
  }

  public void createActors(LevelStage stage, TextureRegionManager textureRegionManager) {
    for (Hero hero : this.activeHeroes) {
      TestHero1Actor actor = hero.newActor(textureRegionManager);
      stage.addActor(actor);
      this.actors.put(hero, actor);
    }
  }

  public TestHero1Actor getActor(Hero hero) {
    return this.actors.get(hero);
  }
}
