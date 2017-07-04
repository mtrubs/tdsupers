package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;

/**
 * This enum represents all the HUD textures available.
 */
public enum HeadsUpDisplay implements TextureReference {

  Cooldown("images/hud/cooldown.png"),
  Currency("images/hud/influence.png"),
  FastForwardOff("images/hud/fastForwardOff.png"),
  FastForwardOn("images/hud/fastForwardOn.png"),
  Health("images/hud/health.png"),
  PauseOff("images/hud/pauseOff.png"),
  PauseOn("images/hud/pauseOn.png"),
  WaveCaller("images/hud/waveCaller.png"),
  WaveStatus("images/hud/waveStatus.png");

  private final String texturePath;
  private final String key;

  private HeadsUpDisplay(String texturePath) {
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
