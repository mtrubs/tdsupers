package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author mrubino
 * @since 2015-01-26
 */
public enum Tower implements TextureReference {

  EmptyPlot("images/towers/plot.png", 0, null, null, null),

  // Towers for TestHero
  TestHero_1("images/towers/TestHero/1.png", 1, Hero.TestHero, null, TowerUnit.TestHeroUnit),
  TestHeroA2("images/towers/TestHero/a/2.png", 2, Hero.TestHero, TowerPath.A, TowerUnit.TestHeroUnit),
  TestHeroB2("images/towers/TestHero/b/2.png", 2, Hero.TestHero, TowerPath.B, TowerUnit.TestHeroUnit),
  TestHeroA3("images/towers/TestHero/a/3.png", 3, Hero.TestHero, TowerPath.A, TowerUnit.TestHeroUnit),
  TestHeroB3("images/towers/TestHero/b/3.png", 3, Hero.TestHero, TowerPath.B, TowerUnit.TestHeroUnit),
  TestHeroA4("images/towers/TestHero/a/4.png", 4, Hero.TestHero, TowerPath.A, TowerUnit.TestHeroUnit),
  TestHeroB4("images/towers/TestHero/b/4.png", 4, Hero.TestHero, TowerPath.B, TowerUnit.TestHeroUnit);

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
  private Tower(String texturePath, int level, Hero hero, TowerPath path, TowerUnit unit) {
    this.texturePath = texturePath;
    this.level = level;
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
}
