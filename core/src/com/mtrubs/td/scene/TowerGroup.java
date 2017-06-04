package com.mtrubs.td.scene;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mtrubs.td.graphics.*;

import java.util.*;

/**
 * This represents a tower, its menu items and units.
 */
public class TowerGroup extends Group {

  /**
   * This is a map of all menu items for the tower.
   */
  private final Map<TowerMenuItem, TextureRegionActor> menuItems;
  /**
   * This is a list of all confirmation click listeners associated with the menu items.
   */
  private final List<ConfirmClickListener> confirmClicks;
  /**
   * The menu group.
   */
  private final Group menu;
  /**
   * The texture of the tower at this time.
   */
  private Tower currentState;
  /**
   * The active hero (upgrade) of this tower.
   */
  private Hero selectedHero;
  /**
   * The active path (upgrade) of this tower.
   */
  private TowerPath selectedPath;
  /**
   * Whether or not this tower has been enhanced (upgrade).
   */
  private boolean enhanced;

  private UnitActor unit;

  /**
   * Create a new tower group with the given settings.
   *
   * @param positionX            the x coordinate of this tower group's tower.
   * @param positionY            the y coordinate of this tower group's tower.
   * @param startingState        the starting state of this tower group.
   * @param activeHeroes         the list of active heroes for the current level.
   * @param textureRegionManager the texture region manager.
   */
  public TowerGroup(float positionX, float positionY, Tower startingState,
                    final List<Hero> activeHeroes,
                    final TextureRegionManager textureRegionManager,
                    float unitPositionX, float unitPositionY) {
    this.menuItems = new EnumMap<TowerMenuItem, TextureRegionActor>(TowerMenuItem.class);
    this.confirmClicks = new ArrayList<ConfirmClickListener>();
    this.currentState = startingState;
    this.selectedHero = startingState.getHero();
    this.selectedPath = startingState.getPath();
    // this is the tower image of this group
    final TextureRegionActor tower = new TextureRegionActor(positionX, positionY,
      textureRegionManager.get(startingState));
    addActor(tower);

    if (startingState.hasUnit()) {
      this.unit = new UnitActor(unitPositionX, unitPositionY, startingState.getUnit(),
        textureRegionManager.get(startingState.getUnit()));
    } else {
      this.unit = new UnitActor(unitPositionX, unitPositionY, null, null);
    }
    addActor(this.unit);

    // all the menu items
    final TextureRegionActor ring = addMenuRing(tower, textureRegionManager);
    final TextureRegionActor sell = addMenuItem(ring, 240.0F, TowerMenuItem.Sell, textureRegionManager);
    // TODO: set rally
    //final TextureRegionActor setRally = addMenuItem(ring, 300.0F, TowerMenuItem.SetRally, textureRegionManager);
    final TextureRegionActor upgrade = addMenuItem(ring, 120.0F, TowerMenuItem.Upgrade, textureRegionManager);
    final TextureRegionActor enhance = addMenuItem(ring, 60.0F, TowerMenuItem.Enhance, textureRegionManager);
    final TextureRegionActor heroA = addMenuItem(ring, 120.0F, TowerMenuItem.HeroA, textureRegionManager);
    final TextureRegionActor heroB = addMenuItem(ring, 60.0F, TowerMenuItem.HeroB, textureRegionManager);

    // for now this is hard capped at 3
    final TextureRegionActor hero1 = addHeroMenuItem(0, activeHeroes, TowerMenuItem.Hero1, ring, textureRegionManager);
    final TextureRegionActor hero2 = addHeroMenuItem(1, activeHeroes, TowerMenuItem.Hero2, ring, textureRegionManager);
    final TextureRegionActor hero3 = addHeroMenuItem(2, activeHeroes, TowerMenuItem.Hero3, ring, textureRegionManager);

    // the menu itself
    this.menu = new Group();
    addActor(this.menu);
    updateMenuState();

    // show the menu when we click on the tower
    tower.addListener(new ClickListener() {

      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        setMenuVisibility(true);
      }
    });

    // hide the menu if we click on things outside the menu items
    ring.addListener(new ClickListener() {

      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        deselect();
      }
    });

    if (hero1 != null) {
      // when hero1 is clicked, we want to upgrade the tower accordingly
      hero1.addListener(new ConfirmClickListener(this.confirmClicks) {

        @Override
        public void handleConfirm(InputEvent event, float x, float y) {
          resetOthers(TowerGroup.this.confirmClicks);
          hero1.setTextureRegion(textureRegionManager.get(TowerMenuItem.Confirm));
          super.handleConfirm(event, x, y);
        }

        @Override
        public void reset() {
          hero1.setTextureRegion(textureRegionManager.get(activeHeroes.get(0)));
          super.reset();
        }

        @Override
        public void handleClick(InputEvent event, float x, float y) {
          updateCurrentState(activeHeroes.get(0), null,
            TowerGroup.this.enhanced, tower, TowerGroup.this.unit, textureRegionManager);
          super.handleClick(event, x, y);
        }
      });
    }

    // when hero1 is clicked, if it exists, we want to upgrade the tower accordingly
    if (hero2 != null) {
      hero2.addListener(new ConfirmClickListener(this.confirmClicks) {

        @Override
        public void handleConfirm(InputEvent event, float x, float y) {
          resetOthers(TowerGroup.this.confirmClicks);
          hero2.setTextureRegion(textureRegionManager.get(TowerMenuItem.Confirm));
          super.handleConfirm(event, x, y);
        }

        @Override
        public void reset() {
          hero2.setTextureRegion(textureRegionManager.get(activeHeroes.get(1)));
          super.reset();
        }

        @Override
        public void handleClick(InputEvent event, float x, float y) {
          updateCurrentState(activeHeroes.get(1), null,
            TowerGroup.this.enhanced, tower, TowerGroup.this.unit, textureRegionManager);
          super.handleClick(event, x, y);
        }
      });
    }

    // when hero3 is clicked, if it exists, we want to upgrade the tower accordingly
    if (hero3 != null) {
      hero3.addListener(new ConfirmClickListener(this.confirmClicks) {

        @Override
        public void handleConfirm(InputEvent event, float x, float y) {
          resetOthers(TowerGroup.this.confirmClicks);
          hero3.setTextureRegion(textureRegionManager.get(TowerMenuItem.Confirm));
          super.handleConfirm(event, x, y);
        }

        @Override
        public void reset() {
          hero3.setTextureRegion(textureRegionManager.get(activeHeroes.get(3)));
          super.reset();
        }

        @Override
        public void handleClick(InputEvent event, float x, float y) {
          updateCurrentState(activeHeroes.get(2), null,
            TowerGroup.this.enhanced, tower, TowerGroup.this.unit, textureRegionManager);
          super.handleClick(event, x, y);
        }
      });
    }

    // if path a is clicked, we want to select that path for the selected hero
    heroA.addListener(new ConfirmClickListener(this.confirmClicks) {

      @Override
      public void handleConfirm(InputEvent event, float x, float y) {
        resetOthers(TowerGroup.this.confirmClicks);
        heroA.setTextureRegion(textureRegionManager.get(TowerMenuItem.Confirm));
        super.handleConfirm(event, x, y);
      }

      @Override
      public void reset() {
        heroA.setTextureRegion(textureRegionManager.get(TowerMenuItem.HeroA));
        super.reset();
      }

      @Override
      public void handleClick(InputEvent event, float x, float y) {
        updateCurrentState(TowerGroup.this.selectedHero, TowerPath.A,
          TowerGroup.this.enhanced, tower, TowerGroup.this.unit, textureRegionManager);
        super.handleClick(event, x, y);
      }
    });

    // if path b is clicked, we want to select that path for the selected hero
    heroB.addListener(new ConfirmClickListener(this.confirmClicks) {

      @Override
      public void handleConfirm(InputEvent event, float x, float y) {
        resetOthers(TowerGroup.this.confirmClicks);
        heroB.setTextureRegion(textureRegionManager.get(TowerMenuItem.Confirm));
        super.handleConfirm(event, x, y);
      }

      @Override
      public void reset() {
        heroB.setTextureRegion(textureRegionManager.get(TowerMenuItem.HeroB));
        super.reset();
      }

      @Override
      public void handleClick(InputEvent event, float x, float y) {
        updateCurrentState(TowerGroup.this.selectedHero, TowerPath.B,
          TowerGroup.this.enhanced, tower, TowerGroup.this.unit, textureRegionManager);
        super.handleClick(event, x, y);
      }
    });

    // if upgrade is clicked, we want to upgrade the tower for the selected hero and path
    upgrade.addListener(new ConfirmClickListener(this.confirmClicks) {

      @Override
      public void handleConfirm(InputEvent event, float x, float y) {
        resetOthers(TowerGroup.this.confirmClicks);
        upgrade.setTextureRegion(textureRegionManager.get(TowerMenuItem.Confirm));
        super.handleConfirm(event, x, y);
      }

      @Override
      public void reset() {
        upgrade.setTextureRegion(textureRegionManager.get(TowerMenuItem.Upgrade));
        super.reset();
      }

      @Override
      public void handleClick(InputEvent event, float x, float y) {
        updateCurrentState(TowerGroup.this.selectedHero, TowerGroup.this.selectedPath,
          TowerGroup.this.enhanced, tower, TowerGroup.this.unit, textureRegionManager);
        super.handleClick(event, x, y);
      }
    });

    // if enhance is clicked, we want to set that state on this tower
    enhance.addListener(new ConfirmClickListener(this.confirmClicks) {

      @Override
      public void handleConfirm(InputEvent event, float x, float y) {
        resetOthers(TowerGroup.this.confirmClicks);
        enhance.setTextureRegion(textureRegionManager.get(TowerMenuItem.Confirm));
        super.handleConfirm(event, x, y);
      }

      @Override
      public void reset() {
        enhance.setTextureRegion(textureRegionManager.get(TowerMenuItem.Enhance));
        super.reset();
      }

      @Override
      public void handleClick(InputEvent event, float x, float y) {
        TowerGroup.this.enhanced = true;
        updateTower(tower, textureRegionManager);
        super.handleClick(event, x, y);
      }
    });

    // if sell is clicked, we want to revert the tower back to the empty plot
    sell.addListener(new ConfirmClickListener(this.confirmClicks) {

      @Override
      public void handleConfirm(InputEvent event, float x, float y) {
        resetOthers(TowerGroup.this.confirmClicks);
        sell.setTextureRegion(textureRegionManager.get(TowerMenuItem.Confirm));
        super.handleConfirm(event, x, y);
      }

      @Override
      public void reset() {
        sell.setTextureRegion(textureRegionManager.get(TowerMenuItem.Sell));
        super.reset();
      }

      @Override
      public void handleClick(InputEvent event, float x, float y) {
        updateCurrentState(null, null, false, tower, TowerGroup.this.unit, textureRegionManager);
        super.handleClick(event, x, y);
      }
    });
  }

  /**
   * Updates the state of this tower group.
   *
   * @param selectedHero         the selected hero we have upgraded with.
   * @param selectedPath         the selected path we have upgraded with.
   * @param enhanced             whether or not this tower has been enhanced.
   * @param tower                the tower actor.
   * @param unit                 the unit actor.
   * @param textureRegionManager the texture region manager.
   */
  private void updateCurrentState(Hero selectedHero, TowerPath selectedPath, boolean enhanced,
                                  TextureRegionActor tower, UnitActor unit,
                                  TextureRegionManager textureRegionManager) {
    this.currentState = this.currentState.upgrade(selectedHero, selectedPath);
    this.selectedHero = this.currentState.getHero();
    this.selectedPath = this.currentState.getPath();
    this.enhanced = enhanced;
    updateTower(tower, textureRegionManager);
    updateUnit(unit, textureRegionManager);
  }

  /**
   * Updates the given tower actor to the current state of this group.
   *
   * @param tower                the tower actor to upgrade.
   * @param textureRegionManager the texture region manager.
   */
  private void updateTower(TextureRegionActor tower, TextureRegionManager textureRegionManager) {
    tower.setTextureRegion(textureRegionManager.get(this.currentState));
    updateMenuState();
  }

  /**
   * Updates the given unit actor to the current state of this group.
   *
   * @param unit                 the unit actor to upgrade.
   * @param textureRegionManager the texture region manager.
   */
  private void updateUnit(UnitActor unit, TextureRegionManager textureRegionManager) {
    unit.setTextureRegion(textureRegionManager.get(this.currentState.getUnit()));
    unit.setType(this.currentState.getUnit());
  }

  /**
   * Updates the state of this group.  This will hide and show menu items based this objects current state.
   */
  private void updateMenuState() {
    deselect();
    this.menu.clearChildren();
    this.menu.addActor(this.menuItems.get(TowerMenuItem.Ring));
    for (TowerMenuItem item : this.currentState.getVisibleItems()) {
      if (item == TowerMenuItem.Enhance && this.enhanced) {
        // no need to show the enhance option if it has already been done
        continue;
      }
      // TODO: and is available
      TextureRegionActor actor = this.menuItems.get(item);
      if (actor != null) {
        this.menu.addActor(actor);
      }
    }
  }

  /**
   * Deselects this tower.  This will hide the menu and reset any settings.
   */
  public void deselect() {
    setMenuVisibility(false);
    for (ConfirmClickListener listener : this.confirmClicks) {
      listener.reset();
    }
  }

  /**
   * Sets the visibility of the menu of this tower group.
   *
   * @param visible true to show the menu; false to hide it.
   */
  private void setMenuVisibility(boolean visible) {
    this.menu.setVisible(visible);
  }

  public void clearTarget() {
    this.unit.clearTarget();
  }

  public Targetable getTarget() {
    return this.unit.getTarget();
  }

  public UnitActor getUnit() {
    return this.unit;
  }

  private TextureRegionActor addMenuRing(TextureRegionActor tower, TextureRegionManager textureRegionManager) {
    TextureRegion textureRegion = textureRegionManager.get(TowerMenuItem.Ring);
    TextureRegionActor actor = new TextureRegionActor(
      // offset X and Y by half the size of this region plus half the size of the tower
      // this way it ends up centered over the tower
      tower.getX() - textureRegion.getRegionWidth() / 2.0F + tower.getWidth() / 2.0F,
      tower.getY() - textureRegion.getRegionHeight() / 2.0F + tower.getHeight() / 2.0F,
      textureRegion
    );
    this.menuItems.put(TowerMenuItem.Ring, actor);
    return actor;
  }

  /**
   * Adds a hero menu item to the menu of this tower group.
   *
   * @param index                the hero we are using in the hero list.
   * @param activeHeroes         the list of active heroes.
   * @param key                  which tower menu item (generic version) we are associating with this hero.
   * @param ring                 the tower menu ring.
   * @param textureRegionManager the texture region manager.
   * @return the menu actor added.
   */
  private TextureRegionActor addHeroMenuItem(int index, List<Hero> activeHeroes, TowerMenuItem key,
                                             TextureRegionActor ring, TextureRegionManager textureRegionManager) {
    if (activeHeroes.size() == 1) {
      return index > 0 ? null : addMenuItem(ring, 90.0F, activeHeroes.get(index), key, textureRegionManager);
    } else if (activeHeroes.size() == 2) {
      return index > 1 ? null : addMenuItem(ring, 60.0F + 60.0F * (float) index, activeHeroes.get(index), key, textureRegionManager);
    } else if (activeHeroes.size() == 3) {
      return index > 2 ? null : addMenuItem(ring, 30.0F + 60.0F * (float) index, activeHeroes.get(index), key, textureRegionManager);
    } else {
      throw new RuntimeException("unexpected hero count");
    }
  }

  /**
   * Adds a menu item to the tower menu group.
   *
   * @param ring                 the tower menu ring.
   * @param degrees              the degree on the ring we want to position the menu item.
   * @param item                 the item we are adding.
   * @param textureRegionManager the texture region manager.
   * @return the menu actor added.
   */
  private TextureRegionActor addMenuItem(TextureRegionActor ring, float degrees, TowerMenuItem item,
                                         TextureRegionManager textureRegionManager) {
    return addMenuItem(ring, degrees, item, item, textureRegionManager);
  }

  /**
   * Adds a menu item to the tower menu group.
   *
   * @param ring                 the tower menu ring.
   * @param degrees              the degree on the ring we want to position the menu item.
   * @param item                 the item we are adding.
   * @param key                  the generic menu item we want to associate the item with.
   * @param textureRegionManager the texture region manager.
   * @return the menu actor added.
   */
  private TextureRegionActor addMenuItem(TextureRegionActor ring, float degrees, TextureReference item,
                                         TowerMenuItem key, TextureRegionManager textureRegionManager) {
    TextureRegion textureRegion = textureRegionManager.get(item);
    float radius = ring.getWidth() / 2.0F;

    // circle math to figure out where this menu item belongs
    // we offset x and y bu the radius of the ring
    // TODO: circle math might need minor tweaking based on width of ring (which I am not sure I can calculate dynamically)
    float circleX = (float) (radius * Math.cos(degrees * Math.PI / 180.0F)) + ring.getX() + radius;
    float circleY = (float) (radius * Math.sin(degrees * Math.PI / 180.0F)) + ring.getY() + radius;
    TextureRegionActor actor = new TextureRegionActor(
      // we offset the final results by half the image size
      circleX - textureRegion.getRegionWidth() / 2.0F,
      circleY - textureRegion.getRegionHeight() / 2.0F,
      textureRegion
    );
    this.menuItems.put(key, actor);
    return actor;
  }

  /**
   * This class handles any sort of confirmation click we might need for the menu items.
   */
  private abstract static class ConfirmClickListener extends ClickListener {

    private boolean inConfirm;

    private ConfirmClickListener(Collection<ConfirmClickListener> list) {
      list.add(this);
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
      super.clicked(event, x, y);
      if (!this.inConfirm) {
        handleConfirm(event, x, y);
        this.inConfirm = true;
      } else {
        handleClick(event, x, y);
        reset();
      }
    }

    /**
     * Resets all other menu click listeners.
     *
     * @param list the list of existing click listeners.
     */
    public void resetOthers(Collection<ConfirmClickListener> list) {
      for (ConfirmClickListener listener : list) {
        if (listener != this) {
          listener.reset();
        }
      }
    }

    public void reset() {
      this.inConfirm = false;
    }

    /**
     * This is what is called when the menu item is first clicks on and
     * we want to confirm the user's action.
     *
     * @param event the associated event data.
     * @param x     the x coordinate of the event.
     * @param y     the y coordinate of the event.
     */
    public void handleConfirm(InputEvent event, float x, float y) {
    }

    /**
     * This is what is called on the second click of the menu item. It has already been clicked
     * once and asked for confirmation.
     *
     * @param event the associated event data.
     * @param x     the x coordinate of the event.
     * @param y     the y coordinate of the event.
     */
    public void handleClick(InputEvent event, float x, float y) {
    }
  }

// TODO this is for drawing a transparent circle (tower range)
//    private class CircleActor extends Actor {
//        // TODO: this needs to be disposed
//        private ShapeRenderer shapeRenderer;
//
//        private CircleActor(float xPosition, float yPosition) {
//            this.shapeRenderer = new ShapeRenderer();
//            setBounds(xPosition, yPosition, 125.0F, 125.0F);
//        }
//
//        public void draw(Batch batch, float alpha) {
//            super.draw(batch, alpha);
//            batch.end();
//
//            // TODO: what is the right place to declare this?
//            // enables alpha for shape rendering inside actors
//            Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
//
//            this.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
//            this.shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
//
//            // Green filled transparent circle
//            this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//            this.shapeRenderer.setColor(0.0F, 1.0F, 0.0F, 0.25F);
//            this.shapeRenderer.circle(getX() + 32, getY() + 32, 85.0F);
//            this.shapeRenderer.end();
//
//            // Green outlined circle
//            this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//            this.shapeRenderer.setColor(0.0F, 1.0F, 0.0F, 0.75F);
//            this.shapeRenderer.circle(getX() + 32, getY() + 32, 85.0F);
//            this.shapeRenderer.end();
//
//            batch.begin();
//        }
//    }
}
