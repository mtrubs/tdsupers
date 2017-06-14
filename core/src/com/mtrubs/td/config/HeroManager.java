package com.mtrubs.td.config;

import com.mtrubs.td.graphics.Hero;

import java.util.List;

/**
 * @author mrubino
 * @since 2017-06-14
 */
public class HeroManager {

  private final List<Hero> activeHeroes;

  public HeroManager(List<Hero> activeHeroes) {
    this.activeHeroes = activeHeroes;
  }
}
