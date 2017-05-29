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

  private static final float NORMAL_SPEED = 1.0F;
  private static final float FAST_SPEED = 1.5F;
  private static final float PAUSE_SPEED = 0.0F;

  private float speedFactor = NORMAL_SPEED;

  public HudStage(float worldWidth, float worldHeight, TextureRegionManager textureRegionManager) {
    super(new ExtendViewport(worldWidth, worldHeight));

    addTopLeft(textureRegionManager);
    addTopRight(textureRegionManager);
    addBottomLeft(textureRegionManager);
    addBottomRight(textureRegionManager);
  }

  private static final float PAD = 5.0F;

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

    // TODO: click doesnt seem to register
    SpeedClickListener pauseClickListener = new SpeedClickListener(pause,
        pauseTextureOff, pauseTextureOn, NORMAL_SPEED, PAUSE_SPEED);
    SpeedClickListener fastForwardClickListener = new SpeedClickListener(fastForward,
        fastForwardTextureOff, fastForwardTextureOn, NORMAL_SPEED, FAST_SPEED);

    pauseClickListener.setCounterpart(fastForwardClickListener);
    fastForwardClickListener.setCounterpart(pauseClickListener);

    pause.addListener(pauseClickListener);
    fastForward.addListener(fastForwardClickListener);
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

  private void setSpeedFactor(float speedFactor) {
    this.speedFactor = speedFactor;
  }

  private class SpeedClickListener extends ClickListener {

    private final TextureRegionActor actor;
    private final TextureRegion offTexture;
    private final TextureRegion onTexture;
    private final float offSpeed;
    private final float onSpeed;

    private boolean on = false;
    private SpeedClickListener counterpart;

    private SpeedClickListener(TextureRegionActor actor, TextureRegion offTexture, TextureRegion onTexture,
                               float offSpeed, float onSpeed) {
      this.actor = actor;
      this.offTexture = offTexture;
      this.onTexture = onTexture;
      this.offSpeed = offSpeed;
      this.onSpeed = onSpeed;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
      super.clicked(event, x, y);
      if (this.on) {
        // turn off
        System.out.println("On/Off - turn off");
        this.on = false;
        this.actor.setTextureRegion(this.offTexture);
        setSpeedFactor(this.offSpeed);
      } else {
        // turn on
        System.out.println("On/Off - turn on");
        this.actor.setTextureRegion(this.onTexture);
        this.counterpart.forceOff();
        this.on = true;
        setSpeedFactor(this.onSpeed);
      }
    }

    public void forceOff() {
      this.on = false;
      this.actor.setTextureRegion(this.offTexture);
    }

    public void setCounterpart(SpeedClickListener counterpart) {
      this.counterpart = counterpart;
    }
  }
}
