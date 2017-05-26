package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mrubino
 * @since 2015-01-26
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

  private static final List<TowerMenuItem[]> TOWER_LEVEL_MENUS;

  static {
    TowerMenuItem[] norm = new TowerMenuItem[]{
        TowerMenuItem.Sell, TowerMenuItem.SetRally,
        TowerMenuItem.Upgrade, TowerMenuItem.Enhance
    };
    TOWER_LEVEL_MENUS = new ArrayList<TowerMenuItem[]>();
    // level 0 - select a hero
    TOWER_LEVEL_MENUS.add(new TowerMenuItem[]{
        TowerMenuItem.Hero1, TowerMenuItem.Hero2, TowerMenuItem.Hero3
    });
    // level 1 - choose a path
    TOWER_LEVEL_MENUS.add(new TowerMenuItem[]{
        TowerMenuItem.Sell, TowerMenuItem.SetRally,
        TowerMenuItem.HeroA, TowerMenuItem.HeroB
    });
    // level 2 - upgrade/enhance
    TOWER_LEVEL_MENUS.add(norm);
    // level 3 - upgrade/enhance
    TOWER_LEVEL_MENUS.add(norm);
    // level 4 - final
    TOWER_LEVEL_MENUS.add(new TowerMenuItem[]{
        TowerMenuItem.Sell, TowerMenuItem.SetRally, TowerMenuItem.Enhance
    });
  }

  private final String texturePath;
  private final String key;

  private TowerMenuItem(String texturePath) {
    this.texturePath = texturePath;
    this.key = String.format("%s.%s", getClass().getSimpleName(), name());
  }

  /**
   * Gets an array of visible menu items that are available for a given tower level.
   *
   * @param level the level of the tower.
   * @return the visible menu items for the level.
   */
  public static TowerMenuItem[] getVisibleItems(int level) {
    return TOWER_LEVEL_MENUS.get(level);
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
