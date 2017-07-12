package com.mtrubs.td.graphics.level;

public interface PlayerControlled extends Combatant {

  float getDeathCoolDown();

  int getHealth();

  int getHps();
}
