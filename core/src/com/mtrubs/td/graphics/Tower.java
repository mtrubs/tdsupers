package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This enum represents all the towers (and upgrades) available.
 */
public enum Tower implements TextureReference {

  EmptyPlot("images/towers/plot.png", 0, 0, null, null, null),

  // Towers for TestHero1
  TestHero1_1("images/towers/TestHero1/1.png", 1, 25, Hero.TestHero1, null, TowerUnit.TestHeroUnit1),
  TestHero1A2("images/towers/TestHero1/a/2.png", 2, 30, Hero.TestHero1, TowerPath.A, TowerUnit.TestHeroUnit1),
  TestHero1B2("images/towers/TestHero1/b/2.png", 2, 30, Hero.TestHero1, TowerPath.B, TowerUnit.TestHeroUnit1),
  TestHero1A3("images/towers/TestHero1/a/3.png", 3, 40, Hero.TestHero1, TowerPath.A, TowerUnit.TestHeroUnit1),
  TestHero1B3("images/towers/TestHero1/b/3.png", 3, 40, Hero.TestHero1, TowerPath.B, TowerUnit.TestHeroUnit1),
  TestHero1A4("images/towers/TestHero1/a/4.png", 4, 50, Hero.TestHero1, TowerPath.A, TowerUnit.TestHeroUnit1),
  TestHero1B4("images/towers/TestHero1/b/4.png", 4, 50, Hero.TestHero1, TowerPath.B, TowerUnit.TestHeroUnit1),

  // Towers for TestHero2
  TestHero2_1("images/towers/TestHero2/1.png", 1, 25, Hero.TestHero2, null, TowerUnit.TestHeroUnit2),
  TestHero2A2("images/towers/TestHero2/a/2.png", 2, 30, Hero.TestHero2, TowerPath.A, TowerUnit.TestHeroUnit2),
  TestHero2B2("images/towers/TestHero2/b/2.png", 2, 30, Hero.TestHero2, TowerPath.B, TowerUnit.TestHeroUnit2),
  TestHero2A3("images/towers/TestHero2/a/3.png", 3, 40, Hero.TestHero2, TowerPath.A, TowerUnit.TestHeroUnit2),
  TestHero2B3("images/towers/TestHero2/b/3.png", 3, 40, Hero.TestHero2, TowerPath.B, TowerUnit.TestHeroUnit2),
  TestHero2A4("images/towers/TestHero2/a/4.png", 4, 50, Hero.TestHero2, TowerPath.A, TowerUnit.TestHeroUnit2),
  TestHero2B4("images/towers/TestHero2/b/4.png", 4, 50, Hero.TestHero2, TowerPath.B, TowerUnit.TestHeroUnit2),

  // Towers for TestHero3
  TestHero3_1("images/towers/TestHero3/1.png", 1, 25, Hero.TestHero3, null, TowerUnit.TestHeroUnit3),
  TestHero3A2("images/towers/TestHero3/a/2.png", 2, 30, Hero.TestHero3, TowerPath.A, TowerUnit.TestHeroUnit3),
  TestHero3B2("images/towers/TestHero3/b/2.png", 2, 30, Hero.TestHero3, TowerPath.B, TowerUnit.TestHeroUnit3),
  TestHero3A3("images/towers/TestHero3/a/3.png", 3, 40, Hero.TestHero3, TowerPath.A, TowerUnit.TestHeroUnit3),
  TestHero3B3("images/towers/TestHero3/b/3.png", 3, 40, Hero.TestHero3, TowerPath.B, TowerUnit.TestHeroUnit3),
  TestHero3A4("images/towers/TestHero3/a/4.png", 4, 50, Hero.TestHero3, TowerPath.A, TowerUnit.TestHeroUnit3),
  TestHero3B4("images/towers/TestHero3/b/4.png", 4, 50, Hero.TestHero3, TowerPath.B, TowerUnit.TestHeroUnit3);

  private static final Map<String, Tower> NAME_MAP;

  static {
    NAME_MAP = new HashMap<String, Tower>(values().length);
    for (Tower tower : values()) {
      NAME_MAP.put(tower.name(), tower);
    }
  }

  private final String texturePath;
  private final String key;
  private final int level;
  private final int cost;
  private final Hero hero;
  private final TowerPath path;
  private final TowerMenuItem[] visibleItems;
  private final TowerUnit unit;

  /**
   * Creates the tower texture.
   *
   * @param texturePath the file path associated with the image of this tower.
   * @param level       the level of this tower.
   * @param hero        the hero associated with this tower (null for no hero).
   * @param path        the path associated with this tower (null for no path).
   * @param unit        what the tower generates for a unit.
   */
  private Tower(String texturePath, int level, int cost, Hero hero, TowerPath path, TowerUnit unit) {
    this.texturePath = texturePath;
    this.level = level;
    this.cost = cost;
    this.hero = hero;
    this.unit = unit;
    this.path = path;
    this.key = String.format("%s.%s", getClass().getSimpleName(), name());
    this.visibleItems = TowerMenuItem.getVisibleItems(level);
  }

  /**
   * Gets the Tower by the given name.
   *
   * @param name the name of the tower texture to lookup.
   * @return the named tower texture or null.
   */
  private static Tower getByName(String name) {
    return NAME_MAP.get(name);
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

  /**
   * Gets an array of visible menu items for this tower.
   *
   * @return the visible menu items.
   */
  public TowerMenuItem[] getVisibleItems() {
    return this.visibleItems;
  }

  /**
   * Gets the next tower available for the given hero and path.  If we do not get a hero,
   * we assume we are selling.  If we do not get a path we assume one has not been chosen yet.
   *
   * @param hero the desired hero.
   * @param path the desired path.
   * @return the that that comes after upgrading this tower.
   */
  public Tower upgrade(Hero hero, TowerPath path) {
    if (hero == null) {
      return EmptyPlot;
    }
    // can this be better?
    String name = String.format((Locale) null, "%s%s%d",
      hero.name(), path == null ? "_" : path.name(), this.level + 1);
    return getByName(name);
  }

  /**
   * The hero associated with this tower.
   *
   * @return The hero associated with this tower.
   */
  public Hero getHero() {
    return this.hero;
  }

  /**
   * The path associated with this tower.
   *
   * @return The path associated with this tower.
   */
  public TowerPath getPath() {
    return this.path;
  }

  /**
   * Whether or not this tower type has a unit associated with it or not.
   *
   * @return true if this tower has a unit; false otherwise.
   */
  public boolean hasUnit() {
    return this.unit != null;
  }

  /**
   * The type of unit this tower has or null if it does not have one.
   *
   * @return the unit of this tower or null.
   */
  public TowerUnit getUnit() {
    return this.unit;
  }

  /**
   * @return the amount of currency it costs to do this upgrade.
   */
  public int getCost() {
    return this.cost;
  }
}
