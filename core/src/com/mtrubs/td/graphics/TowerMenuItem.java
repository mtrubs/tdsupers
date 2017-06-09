package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;

/**
 * This enum represents all the tower menu items available.
 */
public enum TowerMenuItem implements TextureReference {

  // Static Menu Items
  Sell("images/towers/menu/sell.png"),
  Ring("images/towers/menu/ring.png"),
  SetRally("images/towers/menu/rally.png"),
  Upgrade("images/towers/menu/upgrade.png"),
  Enhance("images/towers/menu/enhance.png"),
  Confirm("images/towers/menu/confirm.png"),
  // Dynamic Menu Items
  Hero1(null), // dynamic see Hero
  Hero2(null), // dynamic see Hero
  Hero3(null), // dynamic see Hero
  // TODO: make dynamic
  HeroA("images/towers/menu/pathA.png"),
  HeroB("images/towers/menu/pathB.png");

  private final String texturePath;
  private final String key;

  private TowerMenuItem(String texturePath) {
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
