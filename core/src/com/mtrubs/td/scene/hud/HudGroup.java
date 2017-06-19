package com.mtrubs.td.scene.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mtrubs.td.config.CurrencyManager;
import com.mtrubs.td.config.HeroManager;
import com.mtrubs.td.config.WaveManager;
import com.mtrubs.td.graphics.HeadsUpDisplay;
import com.mtrubs.td.graphics.Hero;
import com.mtrubs.td.graphics.TextureReference;
import com.mtrubs.td.scene.LevelStage;
import com.mtrubs.td.scene.TextureRegionActor;

import java.util.Locale;

/**
 * Stage to handle the heads up display (HUD).
 */
public class HudGroup extends Group {

  private static final float PAD = 5.0F;

  private static final float NORMAL_SPEED = 1.0F;
  private static final float FAST_SPEED = 10.5F;
  private static final float PAUSE_SPEED = 0.0F;

  private final WaveManager waveManager; // disposed elsewhere
  private final CurrencyManager currencyManager;
  private final HeroManager heroManager;

  private float speedFactor = NORMAL_SPEED;
  private int startHealth;
  private float timeToNextWave;

  private Label healthLabel;
  private Label currencyLabel;
  private Label waveLabel;
  private TextureRegionActor waveCaller;

  public HudGroup(CurrencyManager currencyManager, HeroManager heroManager, WaveManager waveManager) {
    this.currencyManager = currencyManager;
    this.heroManager = heroManager;
    this.waveManager = waveManager;
  }

  public void init(float worldWidth, float worldHeight, int startHealth) {
    setBounds(getX(), getY(), getStage().getWidth(), getStage().getHeight());
    setTouchable(Touchable.childrenOnly);

    this.startHealth = startHealth;

    addTopLeft();
    addTopRight();
    addBottomLeft();
    addBottomRight();

    TextureRegion waveCallerTexture = getTextureRegion(HeadsUpDisplay.WaveCaller);
    this.waveCaller = new TextureRegionActor(
      determineCoordinate(this.waveManager.getNextStartX(), waveCallerTexture.getRegionWidth(), worldWidth),
      determineCoordinate(this.waveManager.getNextStartY(), waveCallerTexture.getRegionHeight(), worldHeight),
      waveCallerTexture);
    this.waveCaller.addListener(new ClickListener() {

      @Override
      public void clicked(InputEvent event, float x, float y) {
        // TODO: bonus currency for early click
        startNextWave();
      }
    });
    addActor(this.waveCaller);
  }

  private TextureRegion getTextureRegion(TextureReference type) {
    return ((LevelStage) getStage()).getTextureRegion(type);
  }

  /**
   * Ensures that the start coordinate falls within acceptable bounds of the world.  This
   * takes into account the size of the image and applies a pad.
   *
   * @param start     the [x,y] coordinate for the image.
   * @param imageSize the [width,height] of the image.
   * @param worldSize the [width,height] of the world.
   * @return the adjusted [x,y] coordinate for the image.
   */
  private float determineCoordinate(float start, float imageSize, float worldSize) {
    float worldStart = PAD + PAD;
    float worldEnd = worldSize - imageSize - PAD - PAD;

    return Math.min(worldEnd, Math.max(worldStart, start));
  }

  private void startNextWave() {
    this.waveCaller.setTouchable(Touchable.disabled);
    this.waveCaller.setVisible(false);
    this.waveManager.startWave((LevelStage) getStage());
    updateWaveLabel();
    this.timeToNextWave = this.waveManager.getNextWaveDelay();
    // TODO: display wave announcements
  }

  public void setHealthValue(int healthValue) {
    this.healthLabel.setText(String.valueOf(healthValue));
    // TODO: if healthValue == 0 then GAME OVER!
  }

  public void updateWaveLabel() {
    this.waveLabel.setText(String.format((Locale) null, "%d / %d",
      this.waveManager.getCurrentWave(), this.waveManager.getTotalWaves()));
  }

  private void addTopLeft() {
    // This is the top left HUD portion; handles player information
    TextureRegion healthTexture = getTextureRegion(HeadsUpDisplay.Health);
    TextureRegionActor health = new TextureRegionActor(
      PAD, // left of the world
      getHeight() - healthTexture.getRegionHeight() - PAD, // top of the world
      healthTexture);
    addActor(health);

    Label.LabelStyle style = new Label.LabelStyle();
    style.font = new BitmapFont();
    style.fontColor = Color.RED;
    this.healthLabel = new Label("", style);
    setHealthValue(this.startHealth);
    this.healthLabel.setBounds(
      health.getX() + health.getWidth() + PAD, // offset from health
      health.getY() + (health.getHeight() / 2.0F), // same as health, middle aligned
      this.healthLabel.getWidth(), this.healthLabel.getHeight());
    addActor(this.healthLabel);

    TextureRegion currencyTexture = getTextureRegion(HeadsUpDisplay.Currency);
    TextureRegionActor currency = new TextureRegionActor(
      this.healthLabel.getX() + this.healthLabel.getPrefWidth() + PAD, // offset from health label
      health.getY(), // same as health
      currencyTexture);
    addActor(currency);

    this.currencyLabel = new Label("", style);
    this.currencyLabel.setBounds(
      currency.getX() + currency.getWidth() + PAD, // offset from currency
      currency.getY() + (currency.getHeight() / 2.0F), // same a currency; middle aligned
      this.currencyLabel.getWidth(), this.currencyLabel.getHeight());
    addActor(this.currencyLabel);

    TextureRegion waveStatusTexture = getTextureRegion(HeadsUpDisplay.WaveStatus);
    TextureRegionActor waveStatus = new TextureRegionActor(
      health.getX(), // same as health
      health.getY() - health.getHeight() - PAD, // below health
      waveStatusTexture);
    addActor(waveStatus);

    this.waveLabel = new Label("", style);
    this.waveLabel.setBounds(
      waveStatus.getX() + waveStatus.getWidth() + PAD,
      waveStatus.getY() + (waveStatus.getHeight() / 2.0F),
      this.waveLabel.getWidth(), this.waveLabel.getHeight());
    updateWaveLabel();
    addActor(this.waveLabel);
  }

  private void addTopRight() {
    // This is the top right HUD portion; handles menu items
    final TextureRegion fastForwardTextureOff = getTextureRegion(HeadsUpDisplay.FastForwardOff);
    final TextureRegion fastForwardTextureOn = getTextureRegion(HeadsUpDisplay.FastForwardOn);
    final TextureRegionActor fastForward = new TextureRegionActor(
      getWidth() - fastForwardTextureOff.getRegionWidth() - PAD, // top of the world
      getHeight() - fastForwardTextureOff.getRegionHeight() - PAD, // right of the world
      fastForwardTextureOff);
    addActor(fastForward);

    final TextureRegion pauseTextureOff = getTextureRegion(HeadsUpDisplay.PauseOff);
    final TextureRegion pauseTextureOn = getTextureRegion(HeadsUpDisplay.PauseOn);
    final TextureRegionActor pause = new TextureRegionActor(
      fastForward.getX() - pauseTextureOff.getRegionWidth() - PAD, // left of fast forward
      fastForward.getY(), // same as fast forward
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
          HudGroup.this.speedFactor = PAUSE_SPEED;
        } else if (fastForwardListener.isOn()) {
          HudGroup.this.speedFactor = FAST_SPEED;
        } else {
          HudGroup.this.speedFactor = NORMAL_SPEED;
        }
      }
    };
    pauseListener.gameStateHandler = gameStateHandler;
    fastForwardListener.gameStateHandler = gameStateHandler;
  }

  private void addBottomLeft() {
    // This is the bottom left HUD; handles hero info
    float x = 0.0F;
    for (Hero hero : this.heroManager.getActiveHeroes()) {
      TextureRegion textureRegion = getTextureRegion(hero.getThumbnail());
      TextureRegionActor actor = new TextureRegionActor(
        x + PAD, PAD, textureRegion);
      x = actor.getX() + actor.getWidth(); // sets up the next to be beside it
      addActor(actor);
      actor.addListener(new ClickListener() {

        public void clicked(InputEvent event, float x, float y) {
          super.clicked(event, x, y);
          // TODO: select hero
        }
      });

      // TODO: add health indicator
      // TODO: cooldown indicator on death
    }
  }

  private void addBottomRight() {
    // This is the bottom right HUD; handles skills
    float x = getWidth();
    for (Hero hero : this.heroManager.getActiveHeroes()) {
      TextureRegion textureRegion = getTextureRegion(hero.getSkill());
      TextureRegionActor actor = new TextureRegionActor(
        x - PAD - textureRegion.getRegionWidth(), PAD, textureRegion);
      x = actor.getX();
      addActor(actor);

      // TODO: add click event to handle skill action
      // TODO: cooldown indicator
    }
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    this.currencyLabel.setText(String.valueOf(this.currencyManager.getCurrency()));
    // this is how we know if we have started/finished and thus need to track waves
    if (this.waveManager.isActive()) {
      this.timeToNextWave -= delta;
      // if the delay hits zero then we force the wave to start
      // otherwise, when we get close to the start we show the wave caller
      if (this.timeToNextWave <= 0.0F) {
        startNextWave();
      } else if (!this.waveCaller.isVisible() && this.timeToNextWave <= 10.0F) {
        this.waveCaller.setX(determineCoordinate(
          this.waveManager.getNextStartX(),
          this.waveCaller.getWidth(),
          getWidth()
        ));
        this.waveCaller.setY(determineCoordinate(
          this.waveManager.getNextStartY(),
          this.waveCaller.getHeight(),
          getHeight()
        ));
        this.waveCaller.setTouchable(Touchable.enabled);
        this.waveCaller.setVisible(true);
      }
    }
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
  private static class SpeedToggleListener extends ClickListener {

    private final TextureRegionActor actor;
    private final TextureRegion offTexture;
    private final TextureRegion onTexture;
    private boolean on;
    private GameStateHandler gameStateHandler;

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
  private abstract static class GameStateHandler {

    protected abstract void handle();
  }
}
