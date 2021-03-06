package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.mtrubs.util.NonNull;

import java.io.Serializable;

public interface TextureReference extends Serializable {

  /**
   * Gets the path to this texture reference on the file system.
   *
   * @return the path to this texture reference.
   */
  String getTexturePath();

  /**
   * Sets the texture filter of the given texture based on this reference.
   *
   * @param texture the texture to have its filter set.
   */
  void setTextureFilter(Texture texture);

  /**
   * A unique key for this texture reference.  This key should be unique across
   * all implementations.
   *
   * @return the unique key of this reference.
   */
  @NonNull
  String getKey();
}
