package com.mtrubs.td.scene.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mtrubs.td.graphics.ActiveTextureRegionManager;
import com.mtrubs.td.graphics.TextureRegionManager;
import com.mtrubs.td.scene.GameState;
import com.mtrubs.util.NonNull;

public class WorldStage extends Stage {

  @NonNull
  private final GameState gameState;
  @NonNull
  private final TextureRegionManager textureRegionManager;

  public WorldStage(float worldWidth, float worldHeight, Batch batch, @NonNull GameState gameState) {
    super(new ExtendViewport(worldWidth, worldHeight), batch);
    this.gameState = gameState;
    this.textureRegionManager = new ActiveTextureRegionManager();

    Label.LabelStyle style = new Label.LabelStyle();
    style.font = new BitmapFont();
    style.fontColor = Color.RED;
    Label label = new Label("Level 1", style);
    label.setX(300.0F);
    label.setY(300.0F);
    label.addListener(new ClickListener() {

      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        WorldStage.this.gameState.switchToLevel();
      }
    });

    addActor(label);
  }

  @Override
  public void dispose() {
    this.textureRegionManager.dispose();
    super.dispose();
  }
}
