package com.mtrubs.td.config;

import com.mtrubs.td.scene.level.mob.PcActor;

import java.util.Collection;
import java.util.HashSet;

public class UnitManager {

  private final Collection<PcActor> units;

  public UnitManager() {
    this.units = new HashSet<PcActor>();
  }

  public void register(PcActor actor) {
    this.units.add(actor);
  }

  public void unregister(PcActor actor) {
    this.units.remove(actor);
  }

  public Collection<PcActor> getUnits() {
    return this.units;
  }
}
