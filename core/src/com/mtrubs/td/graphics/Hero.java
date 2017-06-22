package com.mtrubs.td.graphics;

import com.mtrubs.td.scene.hero.HeroActor;

/**
 * This enum represents all the heroes available.
 */
public enum Hero {

  TestHero1(HeroThumbnail.TestHero1, HeroUnit.TestHero1,
    HeroSkill.TestHero1, HeroTower.TestHero1),
  TestHero2(HeroThumbnail.TestHero2, HeroUnit.TestHero2,
    HeroSkill.TestHero2, HeroTower.TestHero2),
  TestHero3(HeroThumbnail.TestHero3, HeroUnit.TestHero3,
    HeroSkill.TestHero3, HeroTower.TestHero3);

  private final HeroThumbnail thumbnail;
  private final HeroUnit unit;
  private final HeroSkill skill;
  private final HeroTower tower;

  private Hero(HeroThumbnail thumbnail, HeroUnit unit,
               HeroSkill skill, HeroTower tower) {
    this.thumbnail = thumbnail;
    this.unit = unit;
    this.skill = skill;
    this.tower = tower;
  }

  public HeroTower getTower() {
    return this.tower;
  }

  public HeroThumbnail getThumbnail() {
    return this.thumbnail;
  }

  public HeroSkill getSkill() {
    return this.skill;
  }

  public HeroActor newActor(TextureRegionManager textureRegionManager, float x, float y) {
    return new HeroActor(this.unit, textureRegionManager.get(this.unit), x, y);
  }
}
