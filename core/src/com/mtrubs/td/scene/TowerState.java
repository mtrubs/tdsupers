package com.mtrubs.td.scene;

import com.mtrubs.td.config.CurrencyManager;
import com.mtrubs.td.graphics.Hero;
import com.mtrubs.td.graphics.Tower;
import com.mtrubs.td.graphics.TowerMenuItem;
import com.mtrubs.td.graphics.TowerPath;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TowerState {

  /**
   * How much we discount the tower value by when we sell it.
   */
  private static final float SELL_FACTOR = 0.75F;
  private static final List<TowerMenuItem[]> TOWER_LEVEL_MENUS;

  static {
    TowerMenuItem[] norm = {
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

  private final CurrencyManager currencyManager;
  private int level;
  private Hero hero;
  private TowerPath path;
  private int costs;

  // TODO: allow setting hero, path, level
  public TowerState(CurrencyManager currencyManager) {
    this.currencyManager = currencyManager;
  }

  public void upgrade() {
    this.level++;
    Tower upgrade = getTower();
    int costs = upgrade.getCost();
    this.currencyManager.subtract(costs);
    this.costs += costs;
  }

  public void upgrade(@Nonnull Hero hero) {
    this.hero = hero;
    upgrade();
  }

  public void upgrade(@Nonnull TowerPath path) {
    this.path = path;
    upgrade();
  }

  public Tower getTower() {
    return this.hero == null ? Tower.EmptyPlot : this.hero.getTower(this.level, this.path);
  }

  public void reset(boolean active) {
    // if we have not started the first wave yet then sell at full cost
    this.currencyManager.add(active ? Math.round(((float) this.costs) * SELL_FACTOR) : this.costs);
    this.level = 0;
    this.costs = 0;
    this.hero = null;
    this.path = null;
  }

  /**
   * Gets an array of visible menu items that are available for the current state.
   *
   * @return the visible menu items for this state.
   */
  public TowerMenuItem[] getVisibleItems() {
    return TOWER_LEVEL_MENUS.get(this.level);
  }
}
