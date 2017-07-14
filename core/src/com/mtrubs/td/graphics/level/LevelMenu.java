package com.mtrubs.td.graphics.level;

import com.badlogic.gdx.graphics.Texture;
import com.mtrubs.td.graphics.TextureReference;
import com.mtrubs.util.NonNull;

public enum LevelMenu implements TextureReference {

  Background("images/hud/menu/background.png"),
  Cancel("images/hud/menu/cancel.png"),
  Confirm("images/hud/menu/confirm.png"),
  Disabled("images/hud/menu/disabled.png"),
  Music("images/hud/menu/music.png"),
  Quit("images/hud/menu/quit.png"),
  Restart("images/hud/menu/restart.png"),
  Resume("images/hud/menu/resume.png"),
  Sound("images/hud/menu/sound.png"),
  Vibrate("images/hud/menu/vibrate.png");

  @NonNull
  private final String texturePath;
  @NonNull
  private final String key;

  private LevelMenu(@NonNull String texturePath) {
    this.texturePath = texturePath;
    this.key = String.format("%s.%s", getClass().getSimpleName(), name());
  }

  @NonNull
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