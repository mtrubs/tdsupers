package com.mtrubs.td.scene;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;
import com.mtrubs.td.graphics.Tower;
import com.mtrubs.td.graphics.TowerUnit;

public class UnitGroup extends Group {

  private static final int MAX = 3; // TODO: sizing

  /**
   * The current tower state of this unit to derive properties.
   */
  private Tower state;
  /**
   * x coordinate for the center of the rally point.
   */
  private float x;
  /**
   * y coordinate for the center of the rally point.
   */
  private float y;

  public void setRally(float x, float y) {
    this.x = x;
    this.y = y;

    // move the actors to the right place
    if (hasChildren()) {
      SnapshotArray<Actor> actors = getChildren();
      Actor[] units = actors.begin();
      int count = 0;
      for (Actor actor : units) {
        if (actor != null) {
          UnitActor unit = (UnitActor) actor;
          if (unit.isVisible()) {
            Vector2 position = determinePosition(count, MAX);
            unit.setHome(position);
            count++;
          }
        }
      }
      actors.end();
    }
  }

  public void setState(Tower state) {
    if (this.state != state) {
      TowerUnit type = state.getUnit();
      // update the tower
      if (this.hasChildren()) {
        // if the actors are already created then we just need to update them
        SnapshotArray<Actor> actors = getChildren();
        Actor[] units = actors.begin();
        //int count = 0;
        for (Actor actor : units) {
          if (actor != null) {
            UnitActor unit = (UnitActor) actor;
            unit.setType(type);
            unit.setTextureRegion(getTextureRegion(type));
          }
        }
        actors.end();
      } else {
        // otherwise we create them
        for (int count = 0; count < MAX; count++) {
          Vector2 position = determinePosition(count, MAX);
          UnitActor unit = new UnitActor(position.x, position.y, type, getTextureRegion(type));
          addActor(unit);
          ((LevelStage) getStage()).getUnitManager().register(unit);
          unit.setHome(position);
        }
      }

      // TODO: dynamic unit count
//      if (this.hasChildren()) {
//        // otherwise we are updating them
//        SnapshotArray<Actor> actors = getChildren();
//        UnitActor[] units = actors.begin();
//        int count = 0;
//        for (UnitActor unit : units) {
//          if (count + 1 < state.getUnitCount()) {
//            // these are the actors we care about
//            TowerUnit type = state.getUnit();
//            Vector2 position = determinePosition(count, state.getUnitCount());
//            unit.setType(type);
//            unit.setTextureRegion(getTextureRegion(type));
//            unit.moveTo(position);
//          } else {
//            // these are inactive
//            unit.setType(null);
//            unit.setTextureRegion(null);
//            unit.setX(getParent().getX());
//            unit.setY(getParent().getY());
//          }
//          count++;
//        }
//        actors.end();
//      } else {
//        // initializing things
//        for (int count = 0; count < MAX; count++) {
//          TowerUnit type = state.getUnit();
//          Group parent = getParent();
//          if (count + 1 < state.getUnitCount()) {
//            UnitActor unit = new UnitActor(parent.getX(), parent.getY(), type, getTextureRegion(type));
//            addActor(unit);
//            Vector2 position = determinePosition(count, state.getUnitCount());
//            unit.moveTo(position);
//          } else {
//            UnitActor unit = new UnitActor(parent.getX(), parent.getY(), null, null);
//            addActor(unit);
//          }
//        }
//      }
    }
    this.state = state;
  }

  private Vector2 determinePosition(int count, int total) {
    return new Vector2(this.x + ((float) count / (float) total * 50.0F),
      this.y + ((float) count / (float) total * 50.0F));
  }

  private TextureRegion getTextureRegion(TowerUnit type) {
    return ((LevelStage) getStage()).getTextureRegion(type);
  }
}
