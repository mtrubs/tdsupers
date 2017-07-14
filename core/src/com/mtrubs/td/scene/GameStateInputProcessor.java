package com.mtrubs.td.scene;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mtrubs.td.config.*;
import com.mtrubs.td.graphics.ActiveTextureRegionManager;
import com.mtrubs.td.graphics.TextureRegionManager;
import com.mtrubs.td.graphics.level.Hero;
import com.mtrubs.td.graphics.level.LevelMap;
import com.mtrubs.td.scene.level.LevelStage;
import com.mtrubs.td.scene.world.WorldStage;
import com.mtrubs.util.NonNull;
import com.mtrubs.util.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * This InputProcessor is used to switch between various stages.
 */
public class GameStateInputProcessor implements InputProcessor, GameState {

  /**
   * Width of the game world for scaling purposes on different screen sizes.
   */
  private static final float WORLD_WIDTH = 800.0F;
  /**
   * Height of the game world for scaling purposes on different screen sizes.
   */
  private static final float WORLD_HEIGHT = 600.0F;

  @Nullable
  private Stage stage;
  @NonNull
  private Batch batch = new SpriteBatch();

  @Override
  public void switchToLevel() {
    List<Hero> heroes = new ArrayList<Hero>();
    heroes.add(Hero.TestHero1);
    heroes.add(Hero.TestHero2);
    heroes.add(Hero.TestHero3);

    HeroManager heroManager = new HeroManager(heroes);

    // TODO: this goes away after the config is more ironed out
    TextureRegionManager textureRegionManager = new ActiveTextureRegionManager();

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
    waves.add(new Wave(0.0F, 0, textureRegionManager));
    waves.add(new Wave(30.0F, 50, textureRegionManager));
    waves.add(new Wave(30.0F, 51, textureRegionManager));
    waves.add(new Wave(30.0F, 52, textureRegionManager));
    WaveManager waveManager = new WaveManager(waves);

    disposeStage();
    // the current level
    this.stage = new LevelStage(WORLD_WIDTH, WORLD_HEIGHT, this.batch, this,
      LevelMap.TestLevel, heroManager, towers.toArray(new TowerLevelConfig[towers.size()]),
      currencyManager, waveManager);
  }

  @Override
  public void switchToWorld() {
    disposeStage();
    // the world map
    this.stage = new WorldStage(WORLD_WIDTH, WORLD_HEIGHT, this.batch, this);
  }

  @Override
  public boolean keyDown(int keycode) {
    return this.stage != null && this.stage.keyDown(keycode);
  }

  @Override
  public boolean keyUp(int keycode) {
    return this.stage != null && this.stage.keyUp(keycode);
  }

  @Override
  public boolean keyTyped(char character) {
    return this.stage != null && this.stage.keyTyped(character);
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return this.stage != null && this.stage.touchDown(screenX, screenY, pointer, button);
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    return this.stage != null && this.stage.touchUp(screenX, screenY, pointer, button);
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return this.stage != null && this.stage.touchDragged(screenX, screenY, pointer);
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    return this.stage != null && this.stage.mouseMoved(screenX, screenY);
  }

  @Override
  public boolean scrolled(int amount) {
    return this.stage != null && this.stage.scrolled(amount);
  }

  private void disposeStage() {
    if (this.stage != null) {
      this.stage.dispose();
    }
  }

  public void dispose() {
    disposeStage();
    this.batch.dispose();
  }

  public void act(float delta) {
    if (this.stage != null) {
      this.stage.act(delta);
    }
  }

  public void draw() {
    if (this.stage != null) {
      this.stage.draw();
    }
  }
}
