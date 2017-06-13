package com.mtrubs.td.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

/**
 * This region manager loads each texture region on use and caches it for subsequent calls.
 */
public class ActiveTextureRegionManager implements TextureRegionManager {

  private final Map<String, TextureRegion> loadedTextures;

  public ActiveTextureRegionManager() {
    this.loadedTextures = new HashMap<String, TextureRegion>();
  }

  @Override
  public TextureRegion get(TextureReference type) {
    if (type == null) {
      return null;
    }
    String texturePath = type.getTexturePath();
    if (texturePath == null) {
      return null;
    }
    TextureRegion result = this.loadedTextures.get(type.getKey());
    if (result == null) {
      // lazy load
      Texture texture = new Texture(Gdx.files.internal(texturePath));
      type.setTextureFilter(texture);
      result = new TextureRegion(texture);
      this.loadedTextures.put(type.getKey(), result);
    }
    return result;
  }

  @Override
  public void dispose() {
    for (TextureRegion textureRegion : this.loadedTextures.values()) {
      textureRegion.getTexture().dispose();
    }
  }
}
