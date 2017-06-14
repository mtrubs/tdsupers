package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This enum represents all the heroes available.
 */
public enum Hero implements TextureReference {

  TestHero1("images/towers/TestHero1/menu/icon.png", HeroThumbnail.TestHero1,
    Tower.TestHero1_1, TowerEnhancement.TestHero1,
    HeroPath.TestHero1A, TowerEnhancement.TestHero1A,
    HeroPath.TestHero1B, TowerEnhancement.TestHero1B),
  TestHero2("images/towers/TestHero2/menu/icon.png", HeroThumbnail.TestHero2,
    Tower.TestHero2_1, TowerEnhancement.TestHero2,
    HeroPath.TestHero2A, TowerEnhancement.TestHero2A,
    HeroPath.TestHero2B, TowerEnhancement.TestHero2B),
  TestHero3("images/towers/TestHero3/menu/icon.png", HeroThumbnail.TestHero3,
    Tower.TestHero3_1, TowerEnhancement.TestHero3,
    HeroPath.TestHero3A, TowerEnhancement.TestHero3A,
    HeroPath.TestHero3B, TowerEnhancement.TestHero3B);

  private final String texturePath;
  private final String key;

  private final HeroThumbnail thumbnail;
  private final Tower base;
  private final TowerEnhancement[] enhancementBase;
  private final HeroPath pathA;
  private final HeroPath pathB;
  private final TowerEnhancement[] enhancementA;
  private final TowerEnhancement[] enhancementB;

  private Hero(String texturePath, HeroThumbnail thumbnail,
               @Nonnull Tower base, @Nonnull TowerEnhancement enhancementBase,
               @Nonnull HeroPath pathA, @Nonnull TowerEnhancement enhancementA,
               @Nonnull HeroPath pathB, @Nonnull TowerEnhancement enhancementB) {
    this.texturePath = texturePath;
    this.key = String.format("%s.%s", getClass().getSimpleName(), name());
    this.thumbnail = thumbnail;
    this.base = base;
    this.enhancementBase = new TowerEnhancement[]{enhancementBase};
    this.pathA = pathA;
    this.enhancementA = new TowerEnhancement[]{enhancementBase, enhancementA};
    this.pathB = pathB;
    this.enhancementB = new TowerEnhancement[]{enhancementBase, enhancementB};
  }

  @Override
  public String getTexturePath() {
    return this.texturePath;
  }

  @Override
  public void setTextureFilter(Texture texture) {
    texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
  }

  @Override
  public String getKey() {
    return this.key;
  }

  @Nonnull
  public Tower getTower(int level, @Nullable TowerPath path) {
    // level 0 = empty plot
    // level 1 = pathless hero
    // level 2 = path[0]
    // level 3 = path[1]
    // level 4 = path[2]
    if (level == 0) {
      return Tower.EmptyPlot;
    } else if (level == 1 || path == null) {
      return this.base;
    } else if (path == TowerPath.A) {
      return this.pathA.getTower(level);
    } else {
      return this.pathB.getTower(level);
    }
  }

  public TowerEnhancement getEnhancement(@Nullable TowerPath path) {
    if (path == null) {
      return this.enhancementBase[0];
    } else if (path == TowerPath.A) {
      return this.enhancementA[1];
    } else {
      return this.enhancementB[1];
    }
  }

  public HeroPath getPathA() {
    return this.pathA;
  }

  public HeroPath getPathB() {
    return this.pathB;
  }
}
