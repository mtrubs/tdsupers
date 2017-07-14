package com.mtrubs.td.scene.level.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mtrubs.td.config.SettingsManager;
import com.mtrubs.td.graphics.level.LevelMenu;
import com.mtrubs.td.scene.TextureRegionActor;
import com.mtrubs.td.scene.level.LevelStage;
import com.mtrubs.util.NonNull;
import com.mtrubs.util.Nullable;

public class LevelMenuGroup extends Group {

  private static final float PAD = 40.0F;

  @Nullable
  private ClickListener pauseListener;

  public LevelMenuGroup(@NonNull Group parent) {
    setBounds(parent.getX(), parent.getY(), parent.getWidth(), parent.getHeight());
    setTouchable(Touchable.enabled);
  }

  public void init() {
    // placard
    // TODO: level info
    TextureRegion backgroundTexture = getStage().getTextureRegion(LevelMenu.Background);
    TextureRegionActor menu = new TextureRegionActor((getWidth() - backgroundTexture.getRegionWidth()) / 2.0F,
      (getHeight() - backgroundTexture.getRegionHeight()) / 2.0F, backgroundTexture);
    addActor(menu);

    // first row
    TextureRegion quitTexture = getStage().getTextureRegion(LevelMenu.Quit);
    TextureRegionActor quit = new TextureRegionActor(menu.getX() + PAD,
      menu.getY() + menu.getHeight() - PAD - quitTexture.getRegionHeight(),
      quitTexture);
    addActor(quit);
    TextureRegionActor resume = new TextureRegionActor(
      menu.getX() + menu.getWidth() - PAD - quitTexture.getRegionWidth(),
      quit.getY(), getStage().getTextureRegion(LevelMenu.Resume));
    addActor(resume);
    TextureRegionActor restart = new TextureRegionActor(menu.getCenterX() - quitTexture.getRegionWidth() / 2.0F,
      quit.getY(), getStage().getTextureRegion(LevelMenu.Restart));
    addActor(restart);

    // second row
    TextureRegion musicTexture = getStage().getTextureRegion(LevelMenu.Music);
    TextureRegionActor music = new TextureRegionActor(quit.getX(),
      menu.getY() + PAD, musicTexture);
    addActor(music);
    TextureRegionActor sound = new TextureRegionActor(restart.getX(), music.getY(),
      getStage().getTextureRegion(LevelMenu.Sound));
    addActor(sound);
    TextureRegionActor vibrate = new TextureRegionActor(resume.getX(), music.getY(),
      getStage().getTextureRegion(LevelMenu.Vibrate));
    addActor(vibrate);

    // enabled/disabled state cue for the setting toggles
    final SettingsManager settingsManager = getStage().getSettingsManager();
    TextureRegion disabled = getStage().getTextureRegion(LevelMenu.Disabled);
    float offsetX = disabled.getRegionWidth() * 0.25F;
    float offsetY = disabled.getRegionHeight() * 0.25F;
    final TextureRegionActor musicDisabled = new TextureRegionActor(music.getX() - offsetX,
      music.getY() - offsetY, disabled);
    musicDisabled.setVisible(!settingsManager.isMusicEnabled());
    musicDisabled.setTouchable(Touchable.disabled);
    addActor(musicDisabled);
    final TextureRegionActor soundDisabled = new TextureRegionActor(sound.getX() - offsetX,
      sound.getY() - offsetY, disabled);
    soundDisabled.setVisible(!settingsManager.isSoundEnabled());
    soundDisabled.setTouchable(Touchable.disabled);
    addActor(soundDisabled);
    final TextureRegionActor vibrateDisabled = new TextureRegionActor(vibrate.getX() - offsetX,
      vibrate.getY() - offsetY, disabled);
    vibrateDisabled.setVisible(!settingsManager.isVibrateEnabled());
    vibrateDisabled.setTouchable(Touchable.disabled);
    addActor(vibrateDisabled);

    // TODO: Screen shot

    // add a click event to quit the level
    quit.addListener(new ClickListener() {

      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        // TODO confirm
        getStage().quit();
      }
    });
    // add a click event to restart the level
    restart.addListener(new ClickListener() {

      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        // TODO: confirm
        getStage().restart();
      }
    });
    // add a click event to unpause the level
    resume.addListener(new ClickListener() {

      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        if (LevelMenuGroup.this.pauseListener != null) {
          LevelMenuGroup.this.pauseListener.clicked(event, x, y);
        }
      }
    });
    // add a click event to enable/disable the music
    music.addListener(new MusicToggleClickListener(settingsManager, musicDisabled));
    // add a click event to enable/disable the sound effects
    sound.addListener(new SoundToggleClickListener(settingsManager, soundDisabled));
    // add a click event to enable/disable the vibration
    vibrate.addListener(new VibrateToggleClickListener(settingsManager, vibrateDisabled));
  }

  protected void setPauseListener(@NonNull ClickListener pauseListener) {
    this.pauseListener = pauseListener;
  }

  @Override
  public void draw(Batch batch, float alpha) {
    batch.end();
    Gdx.gl.glEnable(GL20.GL_BLEND);

    ShapeRenderer shapeRenderer = getStage().getShapeRenderer();
    shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
    shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

    // gray out the entire field of view
    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    shapeRenderer.setColor(0.0F, 0.0F, 0.0F, 0.5F);
    shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
    shapeRenderer.end();

    Gdx.gl.glDisable(GL20.GL_BLEND);
    batch.begin();
    super.draw(batch, alpha);
  }

  @Override
  public LevelStage getStage() {
    return (LevelStage) super.getStage();
  }

  private static class MusicToggleClickListener extends ToggleClickListener {

    public MusicToggleClickListener(SettingsManager settings, TextureRegionActor actor) {
      super(settings, actor);
    }

    @Override
    protected boolean isEnabled(SettingsManager settings) {
      return settings.isMusicEnabled();
    }

    @Override
    protected void toggle(SettingsManager settings) {
      settings.toggleMusicEnabled();
    }
  }

  private static class SoundToggleClickListener extends ToggleClickListener {

    public SoundToggleClickListener(SettingsManager settings, TextureRegionActor actor) {
      super(settings, actor);
    }

    @Override
    protected boolean isEnabled(SettingsManager settings) {
      return settings.isSoundEnabled();
    }

    @Override
    protected void toggle(SettingsManager settings) {
      settings.toggleSoundEnabled();
    }
  }

  private static class VibrateToggleClickListener extends ToggleClickListener {

    public VibrateToggleClickListener(SettingsManager settings, TextureRegionActor actor) {
      super(settings, actor);
    }

    @Override
    protected boolean isEnabled(SettingsManager settings) {
      return settings.isVibrateEnabled();
    }

    @Override
    protected void toggle(SettingsManager settings) {
      settings.toggleVibrateEnabled();
    }
  }

  private abstract static class ToggleClickListener extends ClickListener {

    private final SettingsManager settings;
    private final TextureRegionActor actor;

    public ToggleClickListener(SettingsManager settings, TextureRegionActor actor) {
      this.settings = settings;
      this.actor = actor;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
      super.clicked(event, x, y);
      toggle(this.settings);
      this.actor.setVisible(!isEnabled(this.settings));
    }

    protected abstract boolean isEnabled(SettingsManager settings);

    protected abstract void toggle(SettingsManager settings);
  }
}
