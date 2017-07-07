package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.mtrubs.util.NonNull;

/**
 * This enum represents the various level textures available.
 */
public enum LevelMap implements TextureReference {

  TestLevel("tiles/maps/TestMap.png");

  private final String texturePath;
  private final String key;

  private LevelMap(String texturePath) {
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

  @NonNull
  @Override
  public String getKey() {
    return this.key;
  }
}
