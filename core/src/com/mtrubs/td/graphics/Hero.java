package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This enum represents all the heroes available.
 */
public enum Hero implements TextureReference {

  TestHero1("images/towers/menu/TestHero1.png",
    Tower.TestHero1_1, TowerEnhancement.TestHero1,
    new Tower[]{Tower.TestHero1A2, Tower.TestHero1A3, Tower.TestHero1A4}, TowerEnhancement.TestHero1A,
    new Tower[]{Tower.TestHero1B2, Tower.TestHero1B3, Tower.TestHero1B4}, TowerEnhancement.TestHero1B),
  TestHero2("images/towers/menu/TestHero2.png",
    Tower.TestHero2_1, TowerEnhancement.TestHero2,
    new Tower[]{Tower.TestHero2A2, Tower.TestHero2A3, Tower.TestHero2A4}, TowerEnhancement.TestHero2A,
    new Tower[]{Tower.TestHero2B2, Tower.TestHero2B3, Tower.TestHero2B4}, TowerEnhancement.TestHero2B),
  TestHero3("images/towers/menu/TestHero3.png",
    Tower.TestHero3_1, TowerEnhancement.TestHero3,
    new Tower[]{Tower.TestHero3A2, Tower.TestHero3A3, Tower.TestHero3A4}, TowerEnhancement.TestHero3A,
    new Tower[]{Tower.TestHero3B2, Tower.TestHero3B3, Tower.TestHero3B4}, TowerEnhancement.TestHero3B);

  private final String texturePath;
  private final String key;

  private final Tower base;
  private final TowerEnhancement[] enhancementBase;

  private final Tower[] pathA;
  private final TowerEnhancement[] enhancementA;

  private final Tower[] pathB;
  private final TowerEnhancement[] enhancementB;

  private Hero(String texturePath,
               @Nonnull Tower base, @Nonnull TowerEnhancement enhancementBase,
               @Nonnull Tower[] pathA, @Nonnull TowerEnhancement enhancementA,
               @Nonnull Tower[] pathB, @Nonnull TowerEnhancement enhancementB) {
    this.texturePath = texturePath;
    this.key = String.format("%s.%s", getClass().getSimpleName(), name());
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
      return this.pathA[level - 2];
    } else {
      return this.pathB[level - 2];
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

  @Nonnull
  public TowerEnhancement[] getEnhancements(int level, @Nullable TowerPath path) {
    // level 0 = empty plot
    // level 1 = pathless hero - entitled to hero enhancement
    // level 2 = path[0]
    // level 3 = path[1] - entitled to path enhancement
    // level 4 = path[2]
    if (level >= 3) {
      if (path == TowerPath.A) {
        return this.enhancementA;
      } else {
        return this.enhancementB;
      }
    } else if (level >= 1) {
      return this.enhancementBase;
    } else {
      return TowerEnhancement.noEnhancements();
    }
  }
}
