package com.mtrubs.td.scene;

import com.mtrubs.td.config.CurrencyManager;
import com.mtrubs.td.graphics.*;
import com.mtrubs.util.NonNull;

import java.util.ArrayList;
import java.util.List;

public class TowerState {

  /**
   * How much we discount the tower value by when we sell it.
   */
  private static final float SELL_FACTOR = 0.75F;
  private static final List<TowerMenuItem[]> TOWER_LEVEL_MENUS_ALL_ENH;
  private static final List<TowerMenuItem[]> TOWER_LEVEL_MENUS_HERO_ENH;
  private static final List<TowerMenuItem[]> TOWER_LEVEL_MENUS_PATH_ENH;
  private static final List<TowerMenuItem[]> TOWER_LEVEL_MENUS_NO_ENH;

  static {
    TOWER_LEVEL_MENUS_ALL_ENH = new ArrayList<TowerMenuItem[]>();
    TOWER_LEVEL_MENUS_HERO_ENH = new ArrayList<TowerMenuItem[]>();
    TOWER_LEVEL_MENUS_PATH_ENH = new ArrayList<TowerMenuItem[]>();
    TOWER_LEVEL_MENUS_NO_ENH = new ArrayList<TowerMenuItem[]>();

    // level 0 - select a hero
    TOWER_LEVEL_MENUS_ALL_ENH.add(new TowerMenuItem[]{TowerMenuItem.Hero1, TowerMenuItem.Hero2, TowerMenuItem.Hero3});
    TOWER_LEVEL_MENUS_HERO_ENH.add(new TowerMenuItem[]{TowerMenuItem.Hero1, TowerMenuItem.Hero2, TowerMenuItem.Hero3});
    TOWER_LEVEL_MENUS_PATH_ENH.add(new TowerMenuItem[]{TowerMenuItem.Hero1, TowerMenuItem.Hero2, TowerMenuItem.Hero3});
    TOWER_LEVEL_MENUS_NO_ENH.add(new TowerMenuItem[]{TowerMenuItem.Hero1, TowerMenuItem.Hero2, TowerMenuItem.Hero3});

    // level 1 - choose a path
    TOWER_LEVEL_MENUS_ALL_ENH.add(new TowerMenuItem[]{TowerMenuItem.Sell, TowerMenuItem.SetRally, TowerMenuItem.HeroA, TowerMenuItem.HeroB});
    TOWER_LEVEL_MENUS_HERO_ENH.add(new TowerMenuItem[]{TowerMenuItem.Sell, TowerMenuItem.SetRally, TowerMenuItem.HeroA, TowerMenuItem.HeroB});
    TOWER_LEVEL_MENUS_PATH_ENH.add(new TowerMenuItem[]{TowerMenuItem.Sell, TowerMenuItem.SetRally, TowerMenuItem.HeroA, TowerMenuItem.HeroB});
    TOWER_LEVEL_MENUS_NO_ENH.add(new TowerMenuItem[]{TowerMenuItem.Sell, TowerMenuItem.SetRally, TowerMenuItem.HeroA, TowerMenuItem.HeroB});

    // level 2 - upgrade + hero enhance
    TOWER_LEVEL_MENUS_ALL_ENH.add(new TowerMenuItem[]{TowerMenuItem.Sell, TowerMenuItem.SetRally, TowerMenuItem.Upgrade});
    TOWER_LEVEL_MENUS_HERO_ENH.add(new TowerMenuItem[]{TowerMenuItem.Sell, TowerMenuItem.SetRally, TowerMenuItem.Upgrade});
    TOWER_LEVEL_MENUS_PATH_ENH.add(new TowerMenuItem[]{TowerMenuItem.Sell, TowerMenuItem.SetRally, TowerMenuItem.Upgrade, TowerMenuItem.EnhanceHero});
    TOWER_LEVEL_MENUS_NO_ENH.add(new TowerMenuItem[]{TowerMenuItem.Sell, TowerMenuItem.SetRally, TowerMenuItem.Upgrade, TowerMenuItem.EnhanceHero});

    // level 3 - upgrade + path enhance
    TOWER_LEVEL_MENUS_ALL_ENH.add(new TowerMenuItem[]{TowerMenuItem.Sell, TowerMenuItem.SetRally, TowerMenuItem.Upgrade});
    TOWER_LEVEL_MENUS_HERO_ENH.add(new TowerMenuItem[]{TowerMenuItem.Sell, TowerMenuItem.SetRally, TowerMenuItem.Upgrade, TowerMenuItem.EnhancePath});
    TOWER_LEVEL_MENUS_PATH_ENH.add(new TowerMenuItem[]{TowerMenuItem.Sell, TowerMenuItem.SetRally, TowerMenuItem.Upgrade, TowerMenuItem.EnhanceHero});
    TOWER_LEVEL_MENUS_NO_ENH.add(new TowerMenuItem[]{TowerMenuItem.Sell, TowerMenuItem.SetRally, TowerMenuItem.Upgrade, TowerMenuItem.EnhanceHero, TowerMenuItem.EnhancePath});

    // level 4 - final
    TOWER_LEVEL_MENUS_ALL_ENH.add(new TowerMenuItem[]{TowerMenuItem.Sell, TowerMenuItem.SetRally});
    TOWER_LEVEL_MENUS_HERO_ENH.add(new TowerMenuItem[]{TowerMenuItem.Sell, TowerMenuItem.SetRally, TowerMenuItem.EnhancePath});
    TOWER_LEVEL_MENUS_PATH_ENH.add(new TowerMenuItem[]{TowerMenuItem.Sell, TowerMenuItem.SetRally, TowerMenuItem.EnhanceHero});
    TOWER_LEVEL_MENUS_NO_ENH.add(new TowerMenuItem[]{TowerMenuItem.Sell, TowerMenuItem.SetRally, TowerMenuItem.EnhanceHero, TowerMenuItem.EnhancePath});
  }

  private final CurrencyManager currencyManager;
  /**
   * List of active heroes for this instance.
   */
  private final List<HeroTower> activeTowers;
  /**
   * Current level of this tower.
   */
  private int level;
  /**
   * Hero enhance state.
   */
  private boolean heroEnhanced;
  /**
   * Hero path enhance state.
   */
  private boolean pathEnhanced;
  /**
   * The chosen hero of this tower.
   */
  private HeroTower tower;
  /**
   * The upgrade path of this tower.
   */
  private TowerPath path;
  /**
   * The total amount of cost invested in this tower.
   */
  private int costs;

  // TODO: allow setting tower, path, level
  public TowerState(@NonNull List<HeroTower> activeTowers, @NonNull CurrencyManager currencyManager) {
    this.currencyManager = currencyManager;
    this.activeTowers = activeTowers;
  }

  private static Tower getTower(HeroTower hero, TowerPath path, int level) {
    return hero == null ? Tower.EmptyPlot : hero.getTower(level, path);
  }

  public int activeHeroCount() {
    return this.activeTowers.size();
  }

  public int getUpgradeCost(int index) {
    return getTower(this.activeTowers.get(index), null, this.level + 1).getCost();
  }

  public int getUpgradeCost(TowerPath path) {
    return getTower(this.tower, path, this.level + 1).getCost();
  }

  public int getUpgradeCost() {
    return getTower(this.tower, this.path, this.level + 1).getCost();
  }

  public int getEnhanceHeroCost() {
    return this.tower.getEnhancement(null).getCost();
  }

  public int getEnhancePathCost() {
    return this.tower.getEnhancement(this.path).getCost();
  }

  public void enhanceHero() {
    this.heroEnhanced = true;
    int costs = getEnhanceHeroCost();
    this.currencyManager.subtract(costs);
    this.costs += costs;
  }

  public void enhancePath() {
    this.pathEnhanced = true;
    int costs = getEnhancePathCost();
    this.currencyManager.subtract(costs);
    this.costs += costs;
  }

  public void upgrade() {
    this.level++;
    Tower upgrade = getTower();
    int costs = upgrade.getCost();
    this.currencyManager.subtract(costs);
    this.costs += costs;
  }

  public void upgrade(int index) {
    this.tower = this.activeTowers.get(index);
    upgrade();
  }

  public void upgrade(@NonNull TowerPath path) {
    this.path = path;
    upgrade();
  }

  public Tower getTower() {
    return getTower(this.tower, this.path, this.level);
  }

  public void reset(boolean active) {
    // if we have not started the first wave yet then sell at full cost
    this.currencyManager.add(active ? Math.round(((float) this.costs) * SELL_FACTOR) : this.costs);
    this.level = 0;
    this.heroEnhanced = false;
    this.pathEnhanced = false;
    this.costs = 0;
    this.tower = null;
    this.path = null;
  }

  public TextureReference getTextureReference(TextureReference item) {
    // TODO: more dynamic
    if (item == TowerMenuItem.Hero1) {
      return this.activeTowers.get(0);
    } else if (item == TowerMenuItem.Hero2) {
      return this.activeTowers.get(1);
    } else if (item == TowerMenuItem.Hero3) {
      return this.activeTowers.get(2);
    } else if (item == TowerMenuItem.EnhanceHero) {
      return this.tower == null ? TowerMenuItem.Confirm : this.tower.getEnhancement(null);
    } else if (item == TowerMenuItem.EnhancePath) {
      return this.tower == null ? TowerMenuItem.Confirm : this.tower.getEnhancement(this.path);
    } else if (item == TowerMenuItem.HeroA) {
      return this.tower == null ? TowerMenuItem.Confirm : this.tower.getPathA();
    } else if (item == TowerMenuItem.HeroB) {
      return this.tower == null ? TowerMenuItem.Confirm : this.tower.getPathB();
    } else {
      return item;
    }
  }

  /**
   * Gets an array of visible menu items that are available for the current state.
   *
   * @return the visible menu items for this state.
   */
  public TowerMenuItem[] getVisibleItems() {
    if (this.heroEnhanced && this.pathEnhanced) {
      return TOWER_LEVEL_MENUS_ALL_ENH.get(this.level);
    } else if (this.heroEnhanced) {
      return TOWER_LEVEL_MENUS_HERO_ENH.get(this.level);
    } else if (this.pathEnhanced) {
      return TOWER_LEVEL_MENUS_PATH_ENH.get(this.level);
    } else {
      return TOWER_LEVEL_MENUS_NO_ENH.get(this.level);
    }
  }
}
