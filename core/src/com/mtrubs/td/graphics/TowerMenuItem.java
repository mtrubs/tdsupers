package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.mtrubs.td.scene.level.mob.TowerState;
import com.mtrubs.util.NonNull;

/**
 * This enum represents all the tower menu items available.
 */
public enum TowerMenuItem implements TextureReference {

  // Static Menu Items
  Sell(false, "images/towers/menu/sell.png"),
  Ring(false, "images/towers/menu/ring.png"),
  SetRally(false, "images/towers/menu/rally.png"),
  CostPlaque(false, "images/towers/menu/costPlaque.png"),
  Confirm(false, "images/towers/menu/confirm.png"),
  Disabled(false, "images/towers/menu/disabled.png"),
  Upgrade(true, "images/towers/menu/upgrade.png") {
    @Override
    public int getCost(TowerState state) {
      return state.getUpgradeCost();
    }
  },

  // Dynamic Menu Items
  Hero1(true, null) { // dynamic see Hero

    @Override
    public int getCost(TowerState state) {
      return state.getUpgradeCost(0);
    }
  },
  Hero2(true, null) { // dynamic see Hero

    @Override
    public int getCost(TowerState state) {
      return state.getUpgradeCost(1);
    }
  },
  Hero3(true, null) { // dynamic see Hero

    @Override
    public int getCost(TowerState state) {
      return state.getUpgradeCost(2);
    }
  },
  HeroA(true, null) { // dynamic see HeroPath

    @Override
    public int getCost(TowerState state) {
      return state.getUpgradeCost(TowerPath.A);
    }
  },
  HeroB(true, null) { // dynamic see HeroPath

    @Override
    public int getCost(TowerState state) {
      return state.getUpgradeCost(TowerPath.B);
    }
  },
  EnhanceHero(true, null) { // dynamic see TowerEnhancement

    @Override
    public int getCost(TowerState state) {
      return state.getEnhanceHeroCost();
    }
  },
  EnhancePath(true, null) { // dynamic see TowerEnhancement

    @Override
    public int getCost(TowerState state) {
      return state.getEnhancePathCost();
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

  @NonNull
  @Override
  public String getKey() {
    return this.key;
  }

  public int getCost(TowerState state) {
    return NO_COST;
  }
}
