package com.mtrubs.td.graphics;

import com.mtrubs.td.scene.hero.TestHero1Actor;

/**
 * This enum represents all the heroes available.
 */
public enum Hero {

  TestHero1(HeroThumbnail.TestHero1, HeroSkill.TestHero1, HeroTower.TestHero1) {
    @Override
    public TestHero1Actor newActor(TextureRegionManager textureRegionManager) {
      return new TestHero1Actor(textureRegionManager, 400.0F, 200.0F);
    }
  },
  TestHero2(HeroThumbnail.TestHero2, HeroSkill.TestHero2, HeroTower.TestHero2) {
    @Override
    public TestHero1Actor newActor(TextureRegionManager textureRegionManager) {
      // TODO: TestHero2
      return new TestHero1Actor(textureRegionManager, 200.0F, 200.0F);
    }
  },
  TestHero3(HeroThumbnail.TestHero3, HeroSkill.TestHero3, HeroTower.TestHero3) {
    @Override
    public TestHero1Actor newActor(TextureRegionManager textureRegionManager) {
      // TODO: TestHero3
      return new TestHero1Actor(textureRegionManager, 300.0F, 300.0F);
    }
  };

  private final HeroThumbnail thumbnail;
  private final HeroSkill skill;
  private final HeroTower heroTower;

  private Hero(HeroThumbnail thumbnail, HeroSkill skill, HeroTower heroTower) {
    this.thumbnail = thumbnail;
    this.skill = skill;
    this.heroTower = heroTower;
  }

  public HeroTower getHeroTower() {
    return this.heroTower;
  }

  public HeroThumbnail getThumbnail() {
    return this.thumbnail;
  }

  public HeroSkill getSkill() {
    return this.skill;
  }

  public abstract TestHero1Actor newActor(TextureRegionManager textureRegionManager);
}
