package com.mtrubs.td.config;

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
