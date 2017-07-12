package com.mtrubs.td.graphics.level;

import com.badlogic.gdx.graphics.Texture;
import com.mtrubs.td.graphics.TextureReference;
import com.mtrubs.util.NonNull;

/**
 * This enum represents all the projectiles available.
 */
public enum ProjectileType implements TextureReference {

  RedBullet("images/projectiles/red.png"),
  YellowBullet("images/projectiles/yellow.png");

  private final String texturePath;
  private final String key;

  private ProjectileType(String texturePath) {
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
