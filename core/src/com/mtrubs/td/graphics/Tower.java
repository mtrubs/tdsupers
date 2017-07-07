package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.mtrubs.util.NonNull;

/**
 * This enum represents all the towers (and upgrades) available.
 */
public enum Tower implements TextureReference {

  EmptyPlot("images/towers/plot.png", 0, null, 0),

  // Towers for TestHero1
  TestHero1_1("images/towers/TestHero1/1.png", 21, TowerUnitType.TestHero1Unit, 3),
  TestHero1A2("images/towers/TestHero1/2A.png", 31, TowerUnitType.TestHero1Unit, 3),
  TestHero1B2("images/towers/TestHero1/2B.png", 32, TowerUnitType.TestHero1Unit, 3),
  TestHero1A3("images/towers/TestHero1/3A.png", 41, TowerUnitType.TestHero1Unit, 3),
  TestHero1B3("images/towers/TestHero1/3B.png", 42, TowerUnitType.TestHero1Unit, 3),
  TestHero1A4("images/towers/TestHero1/4A.png", 51, TowerUnitType.TestHero1Unit, 3),
  TestHero1B4("images/towers/TestHero1/4B.png", 52, TowerUnitType.TestHero1Unit, 3),

  // Towers for TestHero2
  TestHero2_1("images/towers/TestHero2/1.png", 23, TowerUnitType.TestHero2Unit, 3),
  TestHero2A2("images/towers/TestHero2/2A.png", 33, TowerUnitType.TestHero2Unit, 3),
  TestHero2B2("images/towers/TestHero2/2B.png", 34, TowerUnitType.TestHero2Unit, 2),
  TestHero2A3("images/towers/TestHero2/3A.png", 43, TowerUnitType.TestHero2Unit, 4),
  TestHero2B3("images/towers/TestHero2/3B.png", 44, TowerUnitType.TestHero2Unit, 2),
  TestHero2A4("images/towers/TestHero2/4A.png", 53, TowerUnitType.TestHero2Unit, 5),
  TestHero2B4("images/towers/TestHero2/4B.png", 54, TowerUnitType.TestHero2Unit, 2),

  // Towers for TestHero3
  TestHero3_1("images/towers/TestHero3/1.png", 25, TowerUnitType.TestHero3Unit, 2),
  TestHero3A2("images/towers/TestHero3/2A.png", 35, TowerUnitType.TestHero3Unit, 2),
  TestHero3B2("images/towers/TestHero3/2B.png", 36, TowerUnitType.TestHero3Unit, 3),
  TestHero3A3("images/towers/TestHero3/3A.png", 45, TowerUnitType.TestHero3Unit, 2),
  TestHero3B3("images/towers/TestHero3/3B.png", 46, TowerUnitType.TestHero3Unit, 3),
  TestHero3A4("images/towers/TestHero3/4A.png", 55, TowerUnitType.TestHero3Unit, 1),
  TestHero3B4("images/towers/TestHero3/4B.png", 56, TowerUnitType.TestHero3Unit, 3);

  private final String texturePath;
  private final String key;
  private final int cost;
  private final TowerUnit unit;
  private final int unitCount;

  /**
   * Creates the tower texture.
   *
   * @param texturePath the file path associated with the image of this tower.
   * @param unit        what the tower generates for a unit.
   */
  private Tower(String texturePath, int cost, TowerUnit unit, int unitCount) {
    this.texturePath = texturePath;
    this.cost = cost;
    this.unit = unit;
    this.unitCount = unitCount;
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

  @NonNull
  @Override
  public String getKey() {
    return this.key;
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
   * How many units are spawned for this tower.
   */
  public int getUnitCount() {
    return this.unitCount;
  }

  /**
   * @return the amount of currency it costs to do this upgrade.
   */
  public int getCost() {
    return this.cost;
  }
}
