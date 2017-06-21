package com.mtrubs.td.config;

import com.mtrubs.td.scene.UnitActor;

import java.util.Collection;
import java.util.HashSet;

public class UnitManager {

  private final Collection<UnitActor> units;

  public UnitManager() {
    this.units = new HashSet<UnitActor>();
  }

  public void register(UnitActor actor) {
    this.units.add(actor);
  }

  public Collection<UnitActor> getUnits() {
    return this.units;
  }
}
