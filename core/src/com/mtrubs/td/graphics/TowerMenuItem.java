package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.mtrubs.td.scene.TowerState;

/**
 * This enum represents all the tower menu items available.
 */
public enum TowerMenuItem implements TextureReference {

  // Static Menu Items
  Sell(false, "images/towers/menu/sell.png"),
  Ring(false, "images/towers/menu/ring.png"),
  SetRally(false, "images/towers/menu/rally.png"),
  CostPlaque(false, "images/towers/menu/costPlaque.png"),
  Upgrade(true, "images/towers/menu/upgrade.png") {
    public int getCost(TowerState state) {
      return state.getCost();
    }
  },
  Enhance(true, "images/towers/menu/enhance.png") {
    public int getCost(TowerState state) {
      // TODO: implement
      return super.getCost(state);
    }
  },
  Confirm(false, "images/towers/menu/confirm.png"),

  // Dynamic Menu Items
  Hero1(true, null) { // dynamic see Hero

    public int getCost(TowerState state) {
      return state.getCost(0);
    }
  },
  Hero2(true, null) { // dynamic see Hero

    public int getCost(TowerState state) {
      return state.getCost(1);
    }
  },
  Hero3(true, null) { // dynamic see Hero

    public int getCost(TowerState state) {
      return state.getCost(2);
    }
  },
  // TODO: make dynamic
  HeroA(true, "images/towers/menu/pathA.png") {
    public int getCost(TowerState state) {
      return state.getCost(TowerPath.A);
    }
  },
  HeroB(true, "images/towers/menu/pathB.png") {
    public int getCost(TowerState state) {
      return state.getCost(TowerPath.B);
    }
  };

  public static final int NO_COST = -1;

  private final String texturePath;
  private final boolean cost;
  private final String key;

  private TowerMenuItem(boolean cost, String texturePath) {
    this.cost = cost;
    this.texturePath = texturePath;
    this.key = String.format("%s.%s", getClass().getSimpleName(), name());
  }

  public boolean hasCost() {
    return this.cost;
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

  public int getCost(TowerState state) {
    return NO_COST;
  }
}
