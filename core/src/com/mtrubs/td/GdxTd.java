package com.mtrubs.td;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.mtrubs.td.scene.GameStateInputProcessor;
import com.mtrubs.td.scene.TextureRegionActor;
import com.mtrubs.td.scene.TextureRegionActorAccessor;
import com.mtrubs.util.Nullable;

public class GdxTd extends ApplicationAdapter {

  /**
   * Keeps the acting in step with the desired frame rate.
   */
  private static final float GAME_TICK = 1.0F / 60.0F;
  private static final FPSLogger FPS = new FPSLogger(); // FIXME: for debug
  /**
   * The current unhandled delta amount based on the graphics.
   */
  private float delta;
  /**
   * The currently active and running stage.
   */
  @Nullable
  private GameStateInputProcessor processor;

  @Override
  public void create() {
    super.create();

    // not sure where these should live exactly
    Tween.registerAccessor(TextureRegionActor.class, new TextureRegionActorAccessor());
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    this.processor = new GameStateInputProcessor();
    Gdx.input.setInputProcessor(this.processor);
    this.processor.switchToWorld();
  }

  @Override
  public void dispose() {
    if (this.processor != null) {
      this.processor.dispose();
    }
    super.dispose();
  }

  @Override
  public void render() {
    FPS.log();
    super.render();

    this.delta += Gdx.graphics.getDeltaTime();
    while (this.delta >= GAME_TICK) {
      Gdx.gl.glClearColor(1, 1, 1, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      this.delta -= GAME_TICK;
      if (this.processor != null) {
        this.processor.act(GAME_TICK);
        this.processor.draw();
      }
    }
  }
}
