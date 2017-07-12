package com.mtrubs.td.scene.level.mob;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mtrubs.util.NonNull;

public interface SelectableMover {

  void moveTo(@NonNull InputEvent event);

  void select();

  void deselect();

  boolean isSelected();
}
