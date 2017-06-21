package com.mtrubs.td.scene;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.SnapshotArray;
import com.mtrubs.td.graphics.Tower;
import com.mtrubs.td.graphics.TowerUnit;
import com.mtrubs.td.scene.hero.SelectableMover;

public class UnitGroup extends Group implements SelectableMover {

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
  private boolean selected;

  public UnitGroup() {
    setTouchable(Touchable.childrenOnly);
  }

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
      // TODO: dynamic unit count
      if (this.hasChildren()) {
        // if the actors are already created then we just need to update them
        SnapshotArray<Actor> actors = getChildren();
        Actor[] units = actors.begin();
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
          TowerGroup parent = (TowerGroup) getParent();
          Vector2 position = determinePosition(count, MAX);
          UnitActor unit = new UnitActor(parent.getCenterX(), parent.getCenterY(), type, getTextureRegion(type));
          addActor(unit);
          ((LevelStage) getStage()).getUnitManager().register(unit);
          unit.setHome(position);

          unit.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
              super.touchDown(event, x, y, pointer, button);
              return true; // to intercept for the touch up event
            }

            @Override
            public void touchUp(InputEvent event, float xDelta, float yDelta, int pointer, int button) {
              super.touchUp(event, xDelta, yDelta, pointer, button);
              if (!inTapSquare()) {
                // as long as we get the touchUp and we are outside the tap square then we move
                moveTo(event);
              }
            }
          });
        }
      }
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

  @Override
  public void moveTo(InputEvent event) {
    // TODO: bound to the tower in some fashion
    setRally(event.getStageX(), event.getStageY());
  }

  @Override
  public void select() {
    this.selected = true;
  }

  @Override
  public void deselect() {
    this.selected = false;
  }

  @Override
  public boolean isSelected() {
    return this.selected;
  }
}
