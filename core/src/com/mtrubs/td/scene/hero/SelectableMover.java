package com.mtrubs.td.scene.hero;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

/**
 * @author mrubino
 * @since 2017-06-18
 */
public interface SelectableMover {

  void moveTo(InputEvent event);

  void select();

  void deselect();

  boolean isSelected();
}
