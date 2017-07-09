package com.mtrubs.td.scene;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mtrubs.td.config.*;
import com.mtrubs.td.graphics.LevelMap;
import com.mtrubs.td.graphics.TextureReference;
import com.mtrubs.td.graphics.TextureRegionManager;
import com.mtrubs.td.scene.hero.SelectableMover;
import com.mtrubs.td.scene.hud.HudGroup;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This stage represents a given level of the game.
 */
public class LevelStage extends Stage implements CurrencyWatcher {

  public static final boolean DEBUG = false;
  /**
   * The closest we are allowed to zoom in.
   */
  private static final float MIN_ZOOM = 0.35F;
  /**
   * The farthest we are allowed to zoom out.
   */
  private static final float MAX_ZOOM = 1.0F;
  /**
   * Modifier controlling how fast we pan.
   */
  private static final float PAN_RATE = 0.6F;

  private final ShapeRenderer shapeRenderer;
  private final TweenManager tweenManager;
  private final TextureRegionManager textureRegionManager;
  private final WaveManager waveManager;
  private final CurrencyManager currencyManager;
  private final HeroManager heroManager;
  private final UnitManager unitManager;

  private final HudGroup hud;

  /**
   * List of all tower plots.
   */
  private final Collection<TowerGroup> towers;

  private SelectableMover selectable;

  /**
   * Creates a new level stage for game play.  We are given the level map, the list of active heroes
   * configurations and the configuration of all the towers.
   */
  public LevelStage(float worldWidth, float worldHeight,
                    LevelMap levelMap, HeroManager heroManager, TowerLevelConfig[] towers,
                    TextureRegionManager textureRegionManager, CurrencyManager currencyManager,
                    WaveManager waveManager) {
    super(new ExtendViewport(worldWidth, worldHeight));
    this.textureRegionManager = textureRegionManager;
    this.tweenManager = new TweenManager();
    this.shapeRenderer = new ShapeRenderer();
    this.heroManager = heroManager;
    this.unitManager = new UnitManager();

    this.waveManager = waveManager;

    this.currencyManager = currencyManager;
    this.currencyManager.setWatcher(this);

    // add the background image
    final LevelMapActor levelMapActor = new LevelMapActor(this.textureRegionManager.get(levelMap));
    addActor(levelMapActor);

    // handle moving around the level and zooming in and out
    levelMapActor.addListener(new ActorGestureListener() {

      @Override
      public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
        super.pan(event, x, y, deltaX, deltaY);
        // update the camera x,y based on the motion
        OrthographicCamera camera = (OrthographicCamera) event.getStage().getCamera();
        camera.translate(-deltaX * PAN_RATE, -deltaY * PAN_RATE);
        clampCamera(event.getStage(), levelMapActor, camera);
        camera.update();
      }

      @Override
      public void zoom(InputEvent event, float initialDistance, float distance) {
        super.zoom(event, initialDistance, distance);
        // update the zoom based on the motion
        OrthographicCamera camera = (OrthographicCamera) event.getStage().getCamera();
        camera.zoom = MathUtils.clamp(camera.zoom * initialDistance / distance, MIN_ZOOM, MAX_ZOOM);
        clampCamera(event.getStage(), levelMapActor, camera);
        camera.update();
      }
    });

    // add each tower plot
    this.towers = new ArrayList<TowerGroup>(towers.length);
    for (TowerLevelConfig tower : towers) {
      TowerGroup towerGroup = new TowerGroup();
      addActor(towerGroup);
      towerGroup.init(tower.getX(), tower.getY(),
        new TowerState(heroManager.getActiveTowers(), this.currencyManager), tower.getUnitX(), tower.getUnitY());
      this.towers.add(towerGroup);
    }

    heroManager.createActors(this, textureRegionManager);

    /*
    TODO
    Tower
    on click (if tower present)
     - shows range on unit
     -- green for ranged distance
     -- blue for melee distance
    on click of upgrade
     - some kind of informational note
     - (if range changes) distance changes to grey with unit upgrade distance in blue/green
    on drag from tower
     - unit range indicator becomes grey
     - unit range indicator (blue/green) shows from finger
     - rally becomes end point
    */

    addListener(new ClickListener() {

      @Override
      public boolean scrolled(InputEvent event, float x, float y, int amount) {
        super.scrolled(event, x, y, amount);
        // handles mouse wheel scroll zooming
        OrthographicCamera camera = (OrthographicCamera) event.getStage().getCamera();
        camera.zoom = MathUtils.clamp(camera.zoom - 0.1F * (float) amount, MIN_ZOOM, MAX_ZOOM);
        clampCamera(event.getStage(), levelMapActor, camera);
        camera.update();
        return true;
      }

      @Override
      public void clicked(InputEvent event, float x, float y) {
        // deselects every tower that we have not clicked on
        super.clicked(event, x, y);
        // this seems too tied to the setup
        Actor hit = hit(x, y, true).getParent().getParent();
        for (TowerGroup tower : LevelStage.this.towers) {
          if (tower != hit) {
            tower.deselect();
          }
        }
        hit = hit(x, y, true);
        // get related actor is a slight hack to handle the actor disappearing on click
        if (LevelStage.this.selectable != null && event.getRelatedActor() == null) {
          if (hit == levelMapActor) {
            moveSelected(event);
          } else if (hit != LevelStage.this.selectable &&
            hit.getParent() != LevelStage.this.hud) {
            deselect();
          }
        }
      }
    });

    int startHealth = 10;
    // the HUD for the level
    this.hud = new HudGroup();
    addActor(this.hud);
    this.hud.init(worldWidth, worldHeight, startHealth);
  }

  public float getSpeedFactor() {
    return this.hud.getSpeedFactor();
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    this.waveManager.sort();
    this.tweenManager.update(delta);
  }

  /**
   * Ensures that the given camera stays within the bounds of this stage.  The bounds actor is what
   * we keep in view always.
   *
   * @param stage  the stage this camera is working with.
   * @param bounds the actor we always want to keep in view.
   * @param camera the camera we ensure stays on the actor.
   */
  private void clampCamera(Stage stage, Actor bounds, OrthographicCamera camera) {
    float zoom = camera.zoom;

    float minX = stage.getWidth() / 2.0F * zoom; // half screen width (taking zoom into account)
    float minY = stage.getHeight() / 2.0F * zoom; // half screen height (taking zoom into account)
    float maxX = bounds.getWidth() - minX; // texture width minus half screen width
    float maxY = bounds.getHeight() - minY; // texture height minus half screen height

    camera.position.x = MathUtils.clamp(camera.position.x, minX, maxX);
    camera.position.y = MathUtils.clamp(camera.position.y, minY, maxY);
  }

  @Override
  public void dispose() {
    this.tweenManager.killAll();
    this.shapeRenderer.dispose();
    super.dispose();
  }

  public TextureRegion getTextureRegion(TextureReference type) {
    return this.textureRegionManager.get(type);
  }

  public boolean isActive() {
    return this.waveManager.isActive();
  }

  public TweenManager getTweenManager() {
    return this.tweenManager;
  }

  /**
   * Sets the selected moveable so that on next click we move it.
   *
   * @param selectable the selected movable.
   */
  public void setSelected(SelectableMover selectable) {
    deselect();
    this.selectable = selectable;
    if (selectable != null) {
      selectable.select();
    }
  }

  /**
   * Moves the selected movable based on the InputEvent and deselects it.
   *
   * @param event the click event triggering the move.
   */
  private void moveSelected(InputEvent event) {
    this.selectable.moveTo(event);
    deselect();
  }

  /**
   * Deselects the currently selected movable.
   */
  private void deselect() {
    if (this.selectable != null) {
      this.selectable.deselect();
      this.selectable = null;
    }
  }

  public ShapeRenderer getShapeRenderer() {
    return this.shapeRenderer;
  }

  @Override
  public void currencyChangeEvent() {
    for (TowerGroup tower : this.towers) {
      tower.currencyChangeEvent(this.currencyManager.getCurrency());
    }
  }

  public WaveManager getWaveManager() {
    return this.waveManager;
  }

  public HeroManager getHeroManager() {
    return this.heroManager;
  }

  public CurrencyManager getCurrencyManager() {
    return this.currencyManager;
  }

  public UnitManager getUnitManager() {
    return this.unitManager;
  }
}
