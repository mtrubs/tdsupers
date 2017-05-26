package com.mtrubs.td.config;

import com.mtrubs.td.graphics.Hero;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mrubino
 * @since 2015-01-26
 */
public class HeroConfig {

    public List<Hero> getHeroTowers() {
        List<Hero> heroTowers = new ArrayList<Hero>();
        heroTowers.add(Hero.TestHero);
//        heroTowers.add(Hero.TestHero);
//        heroTowers.add(Hero.TestHero);
        return heroTowers;
    }
}
