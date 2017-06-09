package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;

import javax.annotation.Nonnull;

/**
 * This enum represents all the heroes available.
 */
public enum Hero implements TextureReference {

  TestHero1("images/towers/menu/TestHero1.png",
    Tower.TestHero1_1,
    new Tower[]{Tower.TestHero1A2, Tower.TestHero1A3, Tower.TestHero1A4},
    new Tower[]{Tower.TestHero1B2, Tower.TestHero1B3, Tower.TestHero1B4}),
  TestHero2("images/towers/menu/TestHero2.png",
    Tower.TestHero2_1,
    new Tower[]{Tower.TestHero2A2, Tower.TestHero2A3, Tower.TestHero2A4},
    new Tower[]{Tower.TestHero2B2, Tower.TestHero2B3, Tower.TestHero2B4}),
  TestHero3("images/towers/menu/TestHero3.png",
    Tower.TestHero3_1,
    new Tower[]{Tower.TestHero3A2, Tower.TestHero3A3, Tower.TestHero3A4},
    new Tower[]{Tower.TestHero3B2, Tower.TestHero3B3, Tower.TestHero3B4});

  private final String texturePath;
  private final String key;
  private final Tower base;
  private final Tower[] pathA;
  private final Tower[] pathB;

  private Hero(String texturePath, Tower base, @Nonnull Tower[] pathA, @Nonnull Tower[] pathB) {
    this.texturePath = texturePath;
    this.key = String.format("%s.%s", getClass().getSimpleName(), name());
    this.base = base;
    this.pathA = pathA;
    this.pathB = pathB;
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

  public Tower getTower(int level, TowerPath path) {
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
}
