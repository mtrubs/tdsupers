package com.mtrubs.td;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mtrubs.td.config.CurrencyManager;
import com.mtrubs.td.config.HeroConfig;
import com.mtrubs.td.config.TowerLevelConfig;
import com.mtrubs.td.config.Wave;
import com.mtrubs.td.graphics.ActiveTextureRegionManager;
import com.mtrubs.td.graphics.LevelMap;
import com.mtrubs.td.graphics.TextureRegionManager;
import com.mtrubs.td.scene.LevelStage;
import com.mtrubs.td.scene.TextureRegionActor;
import com.mtrubs.td.scene.TextureRegionActorAccessor;
import com.mtrubs.td.scene.hud.HudStage;

import java.util.ArrayList;
import java.util.List;

public class GdxTd extends ApplicationAdapter {

  /**
   * Width of the game world for scaling purposes on different screen sizes.
   */
  private static final float WORLD_WIDTH = 800.0F;
  /**
   * Height of the game world for scaling purposes on different screen sizes.
   */
  private static final float WORLD_HEIGHT = 600.0F;

  private static final FPSLogger FPS = new FPSLogger();

  private TextureRegionManager textureRegionManager;
  private Stage levelStage;
  private HudStage hudStage;

  @Override
  public void create() {
    super.create();

    Tween.registerAccessor(TextureRegionActor.class, new TextureRegionActorAccessor());

    HeroConfig heroes = new HeroConfig();

    int startHealth = 10;

    this.textureRegionManager = new ActiveTextureRegionManager();

    // Tower plots
    List<TowerLevelConfig> towers = new ArrayList<TowerLevelConfig>();
    towers.add(new TowerLevelConfig(160.0F, 300.0F, 160.0F, 250.0F));
    towers.add(new TowerLevelConfig(440.0F, 300.0F, 440.0F, 250.0F));
    towers.add(new TowerLevelConfig(300.0F, 140.0F, 300.0F, 190.0F));
    //towers.add(new TowerLevelConfig(500.0f, 55.0F));

    // Currency Setup
    int startCurrency = 250;
    CurrencyManager currencyManager = new CurrencyManager(startCurrency);

    // Wave Setup
    List<Wave> waves = new ArrayList<Wave>();
    waves.add(new Wave(0.0F, this.textureRegionManager));
    waves.add(new Wave(30.0F, this.textureRegionManager));
    waves.add(new Wave(30.0F, this.textureRegionManager));
    waves.add(new Wave(30.0F, this.textureRegionManager));
    com.mtrubs.td.config.WaveManager waveManager = new com.mtrubs.td.config.WaveManager(waves);

    // TODO: I wonder if these should be one stage and hud is more of a group...
    // the HUD for the level
    this.hudStage = new HudStage(WORLD_WIDTH, WORLD_HEIGHT,
      this.textureRegionManager, startHealth, currencyManager, waveManager);
    // the current level
    this.levelStage = new LevelStage(WORLD_WIDTH, WORLD_HEIGHT,
      LevelMap.TestLevel, heroes, towers.toArray(new TowerLevelConfig[towers.size()]),
      this.textureRegionManager, currencyManager, waveManager);

    // order here is important because once a stage 'handles' an event it stops
    InputMultiplexer multiplexer = new InputMultiplexer();
    // the HUD goes first so that it gets the first click
    multiplexer.addProcessor(this.hudStage);
    multiplexer.addProcessor(this.levelStage);

    Gdx.input.setInputProcessor(multiplexer);
  }

  @Override
  public void dispose() {
    this.levelStage.dispose();
    this.hudStage.dispose();
    this.textureRegionManager.dispose();
    super.dispose();
  }

  @Override
  public void render() {
    FPS.log(); // TODO: for debug
    super.render();
    Gdx.gl.glClearColor(1, 1, 1, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    float delta = Gdx.graphics.getDeltaTime() * this.hudStage.getSpeedFactor();
    this.levelStage.act(delta);
    this.hudStage.act(delta);
    this.levelStage.draw();
    this.hudStage.draw();
  }
}
