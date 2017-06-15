package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;

import javax.annotation.Nonnull;

public enum HeroPath implements TextureReference {

  TestHero1A("images/towers/TestHero1/menu/upgradeA.png",
    new Tower[]{Tower.TestHero1A2, Tower.TestHero1A3, Tower.TestHero1A4}),
  TestHero1B("images/towers/TestHero1/menu/upgradeB.png",
    new Tower[]{Tower.TestHero1B2, Tower.TestHero1B3, Tower.TestHero1B4}),
  TestHero2A("images/towers/TestHero2/menu/upgradeA.png",
    new Tower[]{Tower.TestHero2A2, Tower.TestHero2A3, Tower.TestHero2A4}),
  TestHero2B("images/towers/TestHero2/menu/upgradeB.png",
    new Tower[]{Tower.TestHero2B2, Tower.TestHero2B3, Tower.TestHero2B4}),
  TestHero3A("images/towers/TestHero3/menu/upgradeA.png",
    new Tower[]{Tower.TestHero3A2, Tower.TestHero3A3, Tower.TestHero3A4}),
  TestHero3B("images/towers/TestHero3/menu/upgradeB.png",
    new Tower[]{Tower.TestHero3B2, Tower.TestHero3B3, Tower.TestHero3B4});

  private final String texturePath;
  private final String key;
  private final Tower[] path;

  private HeroPath(String texturePath, Tower[] path) {
    this.texturePath = texturePath;
    this.key = String.format("%s.%s", getClass().getSimpleName(), name());
    this.path = path;
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
  public Tower getTower(int level) {
    return this.path[level - 2];
  }
}
