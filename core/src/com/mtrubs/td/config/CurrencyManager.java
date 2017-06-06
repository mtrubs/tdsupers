package com.mtrubs.td.config;

/**
 * @author mrubino
 * @since 2017-06-04
 */
public class CurrencyManager {

  private int currency;

  public CurrencyManager(int start) {
    this.currency = start;
  }

  public int getCurrency() {
    return this.currency;
  }

  public void add(int amount) {
    this.currency += amount;
  }

  public void subtract(int amount) {
    this.currency -= amount;
  }
}
