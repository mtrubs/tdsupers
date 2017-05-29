package com.mtrubs.td.scene.hud;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mtrubs.td.graphics.HeadsUpDisplay;
import com.mtrubs.td.graphics.TextureRegionManager;
import com.mtrubs.td.scene.TextureRegionActor;

/**
 * @author mrubino
 * @since 2015-02-21
 */
public class HudStage extends Stage {

  private static final float PAD = 5.0F;

  private static final float NORMAL_SPEED = 1.0F;
  private static final float FAST_SPEED = 10.5F;
  private static final float PAUSE_SPEED = 0.0F;

  private float speedFactor = NORMAL_SPEED;

  public HudStage(float worldWidth, float worldHeight, TextureRegionManager textureRegionManager) {
    super(new ExtendViewport(worldWidth, worldHeight));

    addTopLeft(textureRegionManager);
    addTopRight(textureRegionManager);
    addBottomLeft(textureRegionManager);
    addBottomRight(textureRegionManager);
  }

  private void addTopLeft(TextureRegionManager textureRegionManager) {
    // This is the top left HUD portion; handles player information
    // TODO: player health - top left
    // TODO: player currency - next to health
    // TODO: wave status - under health
    // TODO: needs feedback hooks for current health/currency/wave
    TextureRegion currencyTexture = textureRegionManager.get(HeadsUpDisplay.Currency);
    TextureRegionActor currency = new TextureRegionActor(
        PAD, // 0
        getHeight() - currencyTexture.getRegionHeight() - PAD, // world minus size
        currencyTexture);
    addActor(currency);
  }

  private void addTopRight(TextureRegionManager textureRegionManager) {
    // This is the top right HUD portion; handles menu items
    final TextureRegion fastForwardTextureOff = textureRegionManager.get(HeadsUpDisplay.FastForwardOff);
    final TextureRegion fastForwardTextureOn = textureRegionManager.get(HeadsUpDisplay.FastForwardOn);
    final TextureRegionActor fastForward = new TextureRegionActor(
        getWidth() - fastForwardTextureOff.getRegionWidth() - PAD, // world minus size
        getHeight() - fastForwardTextureOff.getRegionHeight() - PAD, // world minus size
        fastForwardTextureOff);
    addActor(fastForward);

    final TextureRegion pauseTextureOff = textureRegionManager.get(HeadsUpDisplay.PauseOff);
    final TextureRegion pauseTextureOn = textureRegionManager.get(HeadsUpDisplay.PauseOn);
    final TextureRegionActor pause = new TextureRegionActor(
        fastForward.getX() - pauseTextureOff.getRegionWidth() - PAD, // offset from fast forward
        fastForward.getY(), // same height as fast forward
        pauseTextureOff);
    addActor(pause);

    // this is not the cleanest since pause and fast forward are a little intertwined
    final SpeedToggleListener pauseListener = new SpeedToggleListener(pause,
        pauseTextureOff, pauseTextureOn);
    final SpeedToggleListener fastForwardListener = new SpeedToggleListener(fastForward,
        fastForwardTextureOff, fastForwardTextureOn);
    pause.addListener(pauseListener);
    fastForward.addListener(fastForwardListener);

    GameStateHandler gameStateHandler = new GameStateHandler() {
      @Override
      protected void handle() {
        if (pauseListener.isOn()) {
          // TODO: launch menu
          HudStage.this.speedFactor = PAUSE_SPEED;
        } else if (fastForwardListener.isOn()) {
          HudStage.this.speedFactor = FAST_SPEED;
        } else {
          HudStage.this.speedFactor = NORMAL_SPEED;
        }
      }
    };
    pauseListener.gameStateHandler = gameStateHandler;
    fastForwardListener.gameStateHandler = gameStateHandler;
  }

  private void addBottomLeft(TextureRegionManager textureRegionManager) {
    // This is the bottom left HUD; handles hero info
    // TODO: hero icons - bottom left
  }

  private void addBottomRight(TextureRegionManager textureRegionManager) {
    // This is the bottom right HUD; handles skills
    // TODO: hero specials - bottom right
  }

  /**
   * Gets the speed factor as dictated by the HUD. This is used for deriving
   * normal vs fast forward vs paused.
   *
   * @return the currently set speed factor.
   */
  public float getSpeedFactor() {
    return this.speedFactor;
  }

  /**
   * Handles the on/off notion of certain actors.  This takes the
   * on/off textures as well as the actor.
   */
  private class SpeedToggleListener extends ClickListener {

    private boolean on = false;
    private GameStateHandler gameStateHandler;

    private final TextureRegionActor actor;
    private final TextureRegion offTexture;
    private final TextureRegion onTexture;

    private SpeedToggleListener(TextureRegionActor actor, TextureRegion offTexture, TextureRegion onTexture) {
      this.actor = actor;
      this.offTexture = offTexture;
      this.onTexture = onTexture;
    }

    public boolean isOn() {
      return this.on;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
      super.clicked(event, x, y);
      // turn things on/off
      this.on = !this.on;
      this.actor.setTextureRegion(this.on ? this.onTexture : this.offTexture);
      this.gameStateHandler.handle();
    }
  }

  /**
   * This handles the state of the game based on other factors.
   */
  private abstract class GameStateHandler {

    protected abstract void handle();
  }
}
