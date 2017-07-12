package com.mtrubs.td.scene.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class WorldStage extends Stage {

  public WorldStage(float worldWidth, float worldHeight, Batch batch) {
    super(new ExtendViewport(worldWidth, worldHeight), batch);
  }
}
