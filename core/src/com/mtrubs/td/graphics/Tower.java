package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;

/**
 * This enum represents all the towers (and upgrades) available.
 */
public enum Tower implements TextureReference {

  EmptyPlot("images/towers/plot.png", 0, null),

  // Towers for TestHero1
  TestHero1_1("images/towers/TestHero1/1.png", 21, TowerUnit.TestHero1Unit),
  TestHero1A2("images/towers/TestHero1/a/2.png", 31, TowerUnit.TestHero1Unit),
  TestHero1B2("images/towers/TestHero1/b/2.png", 32, TowerUnit.TestHero1Unit),
  TestHero1A3("images/towers/TestHero1/a/3.png", 41, TowerUnit.TestHero1Unit),
  TestHero1B3("images/towers/TestHero1/b/3.png", 42, TowerUnit.TestHero1Unit),
  TestHero1A4("images/towers/TestHero1/a/4.png", 51, TowerUnit.TestHero1Unit),
  TestHero1B4("images/towers/TestHero1/b/4.png", 52, TowerUnit.TestHero1Unit),

  // Towers for TestHero2
  TestHero2_1("images/towers/TestHero2/1.png", 23, TowerUnit.TestHero2Unit),
  TestHero2A2("images/towers/TestHero2/a/2.png", 33, TowerUnit.TestHero2Unit),
  TestHero2B2("images/towers/TestHero2/b/2.png", 34, TowerUnit.TestHero2Unit),
  TestHero2A3("images/towers/TestHero2/a/3.png", 43, TowerUnit.TestHero2Unit),
  TestHero2B3("images/towers/TestHero2/b/3.png", 44, TowerUnit.TestHero2Unit),
  TestHero2A4("images/towers/TestHero2/a/4.png", 53, TowerUnit.TestHero2Unit),
  TestHero2B4("images/towers/TestHero2/b/4.png", 54, TowerUnit.TestHero2Unit),

  // Towers for TestHero3
  TestHero3_1("images/towers/TestHero3/1.png", 25, TowerUnit.TestHero3Unit),
  TestHero3A2("images/towers/TestHero3/a/2.png", 35, TowerUnit.TestHero3Unit),
  TestHero3B2("images/towers/TestHero3/b/2.png", 36, TowerUnit.TestHero3Unit),
  TestHero3A3("images/towers/TestHero3/a/3.png", 45, TowerUnit.TestHero3Unit),
  TestHero3B3("images/towers/TestHero3/b/3.png", 46, TowerUnit.TestHero3Unit),
  TestHero3A4("images/towers/TestHero3/a/4.png", 55, TowerUnit.TestHero3Unit),
  TestHero3B4("images/towers/TestHero3/b/4.png", 56, TowerUnit.TestHero3Unit);

  private final String texturePath;
  private final String key;
  private final int cost;
  private final TowerUnit unit;

  /**
   * Creates the tower texture.
   *
   * @param texturePath the file path associated with the image of this tower.
   * @param unit        what the tower generates for a unit.
   */
  private Tower(String texturePath, int cost, TowerUnit unit) {
    this.texturePath = texturePath;
    this.cost = cost;
    this.unit = unit;
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
