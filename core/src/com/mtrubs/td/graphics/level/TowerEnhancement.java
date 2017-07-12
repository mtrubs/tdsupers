package com.mtrubs.td.graphics.level;

import com.badlogic.gdx.graphics.Texture;
import com.mtrubs.td.graphics.TextureReference;
import com.mtrubs.util.NonNull;

public enum TowerEnhancement implements TextureReference {

  TestHero1("images/towers/TestHero1/menu/enhance.png", 100),
  TestHero1A("images/towers/TestHero1/menu/enhanceA.png", 151),
  TestHero1B("images/towers/TestHero1/menu/enhanceB.png", 152),
  TestHero2("images/towers/TestHero2/menu/enhance.png", 103),
  TestHero2A("images/towers/TestHero2/menu/enhanceA.png", 153),
  TestHero2B("images/towers/TestHero2/menu/enhanceB.png", 154),
  TestHero3("images/towers/TestHero3/menu/enhance.png", 105),
  TestHero3A("images/towers/TestHero3/menu/enhanceA.png", 155),
  TestHero3B("images/towers/TestHero3/menu/enhanceB.png", 156);

  private final String texturePath;
  private final String key;
  private final int cost;

  private TowerEnhancement(String texturePath, int cost) {
    this.texturePath = texturePath;
    this.cost = cost;
    this.key = String.format("%s.%s", getClass().getSimpleName(), name());
  }

  @Override
  public String getTexturePath() {
    return this.texturePath;
  }

  @Override
  public void setTextureFilter(Texture texture) {
    texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
  }

  @NonNull
  @Override
  public String getKey() {
    return this.key;
  }

  /**
   * @return the amount of currency it costs to do this upgrade.
   */
  public int getCost() {
    return this.cost;
  }
}
