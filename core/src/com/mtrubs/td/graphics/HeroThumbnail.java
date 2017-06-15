package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;

public enum HeroThumbnail implements TextureReference {

  TestHero1("images/towers/TestHero1/thumbnail.png"),
  TestHero2("images/towers/TestHero2/thumbnail.png"),
  TestHero3("images/towers/TestHero3/thumbnail.png");

  private final String texturePath;
  private final String key;

  private HeroThumbnail(String texturePath) {
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
