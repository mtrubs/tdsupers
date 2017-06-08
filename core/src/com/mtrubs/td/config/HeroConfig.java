package com.mtrubs.td.config;

import com.mtrubs.td.graphics.Hero;

import java.util.ArrayList;
import java.util.List;

public class HeroConfig {

  public List<Hero> getHeroTowers() {
    List<Hero> heroTowers = new ArrayList<Hero>();
    heroTowers.add(Hero.TestHero1);
    heroTowers.add(Hero.TestHero2);
    heroTowers.add(Hero.TestHero3);
    return heroTowers;
  }
}
