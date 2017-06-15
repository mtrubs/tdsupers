package com.mtrubs.td.config;

import com.mtrubs.td.graphics.Hero;

import java.util.List;

public class HeroManager {


  private final List<Hero> activeHeroes;

  public HeroManager(List<Hero> activeHeroes) {
    this.activeHeroes = activeHeroes;
  }

  public List<Hero> getActiveHeroes() {
    return this.activeHeroes;
  }
}
