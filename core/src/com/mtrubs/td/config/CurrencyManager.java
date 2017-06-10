package com.mtrubs.td.config;

public class CurrencyManager {

  private CurrencyWatcher watcher;
  private int currency;

  public CurrencyManager(int start) {
    this.currency = start;
  }

  public void setWatcher(CurrencyWatcher watcher) {
    this.watcher = watcher;
  }

  public int getCurrency() {
    return this.currency;
  }

  public void add(int amount) {
    this.currency += amount;
    currencyChangeEvent();
  }

  public void subtract(int amount) {
    this.currency -= amount;
    currencyChangeEvent();
  }

  private void currencyChangeEvent() {
    if (this.watcher != null) {
      this.watcher.currencyChangeEvent();
    }
  }
}
