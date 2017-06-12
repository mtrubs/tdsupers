package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;

/**
 * @author mrubino
 * @since 2017-06-12
 */
public enum TowerEnhancement implements TextureReference {

  TestHero1("images/towers/menu/enhance.png", 100),
  TestHero1A("images/towers/menu/enhance.png", 151),
  TestHero1B("images/towers/menu/enhance.png", 152),
  TestHero2("images/towers/menu/enhance.png", 103),
  TestHero2A("images/towers/menu/enhance.png", 153),
  TestHero2B("images/towers/menu/enhance.png", 154),
  TestHero3("images/towers/menu/enhance.png", 105),
  TestHero3A("images/towers/menu/enhance.png", 155),
  TestHero3B("images/towers/menu/enhance.png", 156);

  private static final TowerEnhancement[] NONE = {};
  private final String texturePath;
  private final String key;
  private final int cost;

  private TowerEnhancement(String texturePath, int cost) {
    this.texturePath = texturePath;
    this.cost = cost;
    this.key = String.format("%s.%s", getClass().getSimpleName(), name());
  }

  public static TowerEnhancement[] noEnhancements() {
    return NONE;
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

  /**
   * @return the amount of currency it costs to do this upgrade.
   */
  public int getCost() {
    return this.cost;
  }
}
