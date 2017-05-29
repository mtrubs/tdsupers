package com.mtrubs.td.scene;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mtrubs.td.config.HeroConfig;
import com.mtrubs.td.config.TowerLevelConfig;
import com.mtrubs.td.graphics.LevelMap;
import com.mtrubs.td.graphics.Mob;
import com.mtrubs.td.graphics.TextureReference;
import com.mtrubs.td.graphics.TextureRegionManager;

import java.util.*;

/**
 * This stage represents a given level of the game.
 *
 * @author mrubino
 * @since 2015-01-26
 */
public class LevelStage extends Stage {

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
  /**
   * A comparator to sort the mobs by most progress to least.
   */
  private static final Comparator<MobActor> MOB_COMPARATOR = new Comparator<MobActor>() {

    @Override
    public int compare(MobActor o1, MobActor o2) {
      return Float.compare(o2.getProgress(), o1.getProgress());
    }
  };

  private final TextureRegionManager textureRegionManager;
  private final TweenManager tweenManager;

  /**
   * List of active mobs.
   */
  private List<MobActor> mobs;
  /**
   * List of all tower plots.
   */
  private Collection<TowerGroup> towers;

  /**
   * Creates a new level stage for game play.  We are given the level map, the list of active heroes
   * configurations and the configuration of all the towers.
   *
   * @param levelMap image of the map.
   * @param heroes   the active hero configurations.
   * @param towers   the tower configurations.
   */
  public LevelStage(float worldWidth, float worldHeight,
                    LevelMap levelMap, HeroConfig heroes, TowerLevelConfig[] towers,
                    TextureRegionManager textureRegionManager) {
    super(new ExtendViewport(worldWidth, worldHeight));
    this.textureRegionManager = textureRegionManager;
    this.tweenManager = new TweenManager();

    // add the background image
    final LevelMapActor levelMapActor = new LevelMapActor(this.textureRegionManager.get(levelMap));
    addActor(levelMapActor);

    // handle moving around the level and zooming in and out
    levelMapActor.addListener(new ActorGestureListener() {

      @Override
      public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
        super.touchDown(event, x, y, pointer, button);
        // TODO: remove debug
        System.out.println(String.format("%f,%f", x, y));
      }

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
      TowerGroup towerGroup = new TowerGroup(tower.getX(), tower.getY(), tower.getStartingState(),
          heroes.getHeroTowers(), this.textureRegionManager, tower.getUnitX(), tower.getUnitY());
      addActor(towerGroup);
      this.towers.add(towerGroup);
    }

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
        on click of rally
         - next click in range becomes rally point
         - unit range indicator moves to rally
        on drag from tower
         - unit range indicator becomes grey
         - unit range indicator (blue/green) shows from finger
         - rally becomes end point
        */

    // deselects every tower that we have not clicked on
    addListener(new ClickListener() {

      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        // this seems too tied to the setup
        Actor hit = hit(x, y, true).getParent().getParent();
        for (TowerGroup tower : LevelStage.this.towers) {
          if (tower != hit) {
            tower.deselect();
          }
        }
      }
    });

    Vector2[] path = new Vector2[]{
        new Vector2(-100.0F, 289.0F),
        new Vector2(3.0F, 289.0F),
        new Vector2(100.0F, 260.0F),
        new Vector2(157.0F, 233.0F),
        new Vector2(238.0F, 227.0F),
        new Vector2(301.0F, 218.0F),
        new Vector2(413.0F, 233.0F),
        new Vector2(476.0F, 233.0F),
        new Vector2(532.0F, 255.0F),
        new Vector2(550.0F, 300.0F),
        new Vector2(613.0F, 340.0F),
        new Vector2(675.0F, 348.0F),
        new Vector2(757.0F, 345.0F),
        new Vector2(1210.0F, 257.0F)
    };

    this.mobs = new ArrayList<MobActor>(5); // TODO: right size
    addMob(Mob.TestMob, path, 25.0F, 0.0F);
    addMob(Mob.TestMob, path, 25.0F, 1.5F);
    addMob(Mob.TestMob, path, 25.0F, 3.0F);
    addMob(Mob.TestMob, path, 25.0F, 4.5F);
    addMob(Mob.TestMob, path, 25.0F, 6.0F);
  }

  private void addMob(Mob type, Vector2[] path, float speed, float delay) {
    MobActor mob = new MobActor(path[0].x, path[0].y, type, 1.0F, this.textureRegionManager.get(type));
    addActor(mob);
    this.mobs.add(mob);

    Timeline timeline = Timeline.createSequence().delay(delay);
    for (int i = 1; i < path.length; i++) {
      timeline.push(Tween.to(mob, TextureRegionActorAccessor.POSITION_XY,
          getDuration(path[i - 1], path[i], speed))
          .target(path[i].x, path[i].y).ease(TweenEquations.easeNone));
    }
    mob.setTimeline(timeline);
    timeline.start(this.tweenManager);
  }

  private float getDuration(Vector2 a, Vector2 b, float speed) {
    return a.dst(b) / speed;
  }

  public Collection<TowerGroup> getTowers() {
    return this.towers;
  }

  public Collection<MobActor> getMobs() {
    return this.mobs;
  }

  public void deselect(UnitActor actor) {
    for (MobActor mob : this.mobs) {
      if (mob.getTarget() == actor) {
        mob.clearTarget();
      }
    }
  }

  public void deselect(MobActor actor) {
    for (TowerGroup tower : this.towers) {
      if (tower.getTarget() == actor) {
        tower.clearTarget();
      }
    }
  }

  public void remove(MobActor actor) {
    deselect(actor);
    this.mobs.remove(actor);
    actor.remove();
  }

  @Override
  public void act(float delta) {
    Collections.sort(this.mobs, MOB_COMPARATOR);

    super.act(delta);
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
    super.dispose();
  }

  public TextureRegion getTextureRegion(TextureReference type) {
    return this.textureRegionManager.get(type);
  }
}
