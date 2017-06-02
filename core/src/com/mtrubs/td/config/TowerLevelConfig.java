package com.mtrubs.td.config;

import com.mtrubs.td.graphics.Tower;

/**
 * @author mrubino
 * @since 2015-01-26
 */
public class TowerLevelConfig {

  private final float x;
  private final float y;
  private final float unitX;
  private final float unitY;
  private final Tower startingState;

  public TowerLevelConfig(float x, float y, float unitX, float unitY) {
    this.x = x;
    this.y = y;
    this.unitX = unitX;
    this.unitY = unitY;
    this.startingState = Tower.EmptyPlot;
  }

  public float getX() {
    return this.x;
  }

  public float getY() {
    return this.y;
  }

  public float getUnitX() {
    return this.unitX;
  }

  public float getUnitY() {
    return this.unitY;
  }

  public Tower getStartingState() {
    return this.startingState;
  }
}
