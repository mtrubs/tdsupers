package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;

/**
 * This enum represents all the heroes available.
 */
public enum Hero implements TextureReference {

  TestHero1("images/towers/menu/TestHero1.png"),
  TestHero2("images/towers/menu/TestHero2.png"),
  TestHero3("images/towers/menu/TestHero3.png");

  private final String texturePath;
  private final String key;

  private Hero(String texturePath) {
    this.texturePath = texturePath;
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

  @Override
  public String getKey() {
    return this.key;
  }
}
