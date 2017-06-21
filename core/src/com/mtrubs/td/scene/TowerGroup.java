package com.mtrubs.td.scene;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mtrubs.td.graphics.TextureReference;
import com.mtrubs.td.graphics.Tower;
import com.mtrubs.td.graphics.TowerMenuItem;
import com.mtrubs.td.graphics.TowerPath;
import com.mtrubs.td.scene.hud.TowerMenuActor;

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
   * The units associated with this tower.
   */
  private final UnitGroup units;
  /**
   * The level of this current tower.
   */
  private TowerState state;

  public TowerGroup() {
    this.menuItems = new EnumMap<TowerMenuItem, TextureRegionActor>(TowerMenuItem.class);
    this.confirmClicks = new ArrayList<ConfirmClickListener>();
    this.menu = new Group();
    this.units = new UnitGroup();
  }

  /**
   * Create a new tower group with the given settings.
   *
   * @param positionX     the x coordinate of this tower group's tower.
   * @param positionY     the y coordinate of this tower group's tower.
   * @param startingState the starting state of this tower group.
   * @param unitPositionX the x coordinate for where the units of this tower are centered.
   * @param unitPositionY the y coordinate for where the units of this tower are centered.
   */
  public void init(float positionX, float positionY, TowerState startingState,
                   float unitPositionX, float unitPositionY) {
    this.state = startingState;
    // this is the tower image of this group
    Tower startingTower = startingState.getTower();
    final TextureRegionActor tower = new TextureRegionActor(positionX, positionY,
      getTextureRegion(startingTower));
    addActor(tower);

    addActor(this.units);
    this.units.setRally(unitPositionX, unitPositionY);
    this.units.setState(startingTower);

    // all the menu items
    final TextureRegionActor ring = addMenuRing(tower);
    final TowerMenuActor sell = addMenuItem(ring, 240.0F, TowerMenuItem.Sell);
    final TowerMenuActor setRally = addMenuItem(ring, 300.0F, TowerMenuItem.SetRally);
    final TowerMenuActor upgrade = addMenuItem(ring, 30.0F, TowerMenuItem.Upgrade);
    final TowerMenuActor enhanceHero = addMenuItem(ring, 90.0F, TowerMenuItem.EnhanceHero);
    final TowerMenuActor enhancePath = addMenuItem(ring, 150.0F, TowerMenuItem.EnhancePath);
    final TowerMenuActor heroA = addMenuItem(ring, 120.0F, TowerMenuItem.HeroA);
    final TowerMenuActor heroB = addMenuItem(ring, 60.0F, TowerMenuItem.HeroB);

    // for now this is hard capped at 3
    final TowerMenuActor hero1 = addHeroMenuItem(0, TowerMenuItem.Hero1, ring);
    final TowerMenuActor hero2 = addHeroMenuItem(1, TowerMenuItem.Hero2, ring);
    final TowerMenuActor hero3 = addHeroMenuItem(2, TowerMenuItem.Hero3, ring);

    // the menu itself
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
          hero1.setTextureRegion(getTextureRegion(TowerMenuItem.Confirm));
          super.handleConfirm(event, x, y);
        }

        @Override
        public void reset() {
          hero1.setTextureRegion(getTextureRegion(
            TowerGroup.this.state.getTextureReference(TowerMenuItem.Hero1)));
          super.reset();
        }

        @Override
        public void handleClick(InputEvent event, float x, float y) {
          TowerGroup.this.state.upgrade(0);
          updateCurrentState(tower);
          super.handleClick(event, x, y);
        }
      });
    }

    // when hero2 is clicked, if it exists, we want to upgrade the tower accordingly
    if (hero2 != null) {
      hero2.addListener(new ConfirmClickListener(this.confirmClicks) {

        @Override
        public void handleConfirm(InputEvent event, float x, float y) {
          resetOthers(TowerGroup.this.confirmClicks);
          hero2.setTextureRegion(getTextureRegion(TowerMenuItem.Confirm));
          super.handleConfirm(event, x, y);
        }

        @Override
        public void reset() {
          hero2.setTextureRegion(getTextureRegion(
            TowerGroup.this.state.getTextureReference(TowerMenuItem.Hero2)));
          super.reset();
        }

        @Override
        public void handleClick(InputEvent event, float x, float y) {
          TowerGroup.this.state.upgrade(1);
          updateCurrentState(tower);
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
          hero3.setTextureRegion(getTextureRegion(TowerMenuItem.Confirm));
          super.handleConfirm(event, x, y);
        }

        @Override
        public void reset() {
          hero3.setTextureRegion(getTextureRegion(
            TowerGroup.this.state.getTextureReference(TowerMenuItem.Hero3)));
          super.reset();
        }

        @Override
        public void handleClick(InputEvent event, float x, float y) {
          TowerGroup.this.state.upgrade(2);
          updateCurrentState(tower);
          super.handleClick(event, x, y);
        }
      });
    }

    // if path A is clicked, we want to select that path for the selected hero
    heroA.addListener(new ConfirmClickListener(this.confirmClicks) {

      @Override
      public void handleConfirm(InputEvent event, float x, float y) {
        resetOthers(TowerGroup.this.confirmClicks);
        heroA.setTextureRegion(getTextureRegion(TowerMenuItem.Confirm));
        super.handleConfirm(event, x, y);
      }

      @Override
      public void reset() {
        heroA.setTextureRegion(getTextureRegion(
          TowerGroup.this.state.getTextureReference(TowerMenuItem.HeroA)));
        super.reset();
      }

      @Override
      public void handleClick(InputEvent event, float x, float y) {
        TowerGroup.this.state.upgrade(TowerPath.A);
        updateCurrentState(tower);
        super.handleClick(event, x, y);
      }
    });

    // if path B is clicked, we want to select that path for the selected hero
    heroB.addListener(new ConfirmClickListener(this.confirmClicks) {

      @Override
      public void handleConfirm(InputEvent event, float x, float y) {
        resetOthers(TowerGroup.this.confirmClicks);
        heroB.setTextureRegion(getTextureRegion(TowerMenuItem.Confirm));
        super.handleConfirm(event, x, y);
      }

      @Override
      public void reset() {
        heroB.setTextureRegion(getTextureRegion(
          TowerGroup.this.state.getTextureReference(TowerMenuItem.HeroB)));
        super.reset();
      }

      @Override
      public void handleClick(InputEvent event, float x, float y) {
        TowerGroup.this.state.upgrade(TowerPath.B);
        updateCurrentState(tower);
        super.handleClick(event, x, y);
      }
    });

    // if upgrade is clicked, we want to upgrade the tower for the selected hero and path
    upgrade.addListener(new ConfirmClickListener(this.confirmClicks) {

      @Override
      public void handleConfirm(InputEvent event, float x, float y) {
        resetOthers(TowerGroup.this.confirmClicks);
        upgrade.setTextureRegion(getTextureRegion(TowerMenuItem.Confirm));
        super.handleConfirm(event, x, y);
      }

      @Override
      public void reset() {
        upgrade.setTextureRegion(getTextureRegion(
          TowerGroup.this.state.getTextureReference(TowerMenuItem.Upgrade)));
        super.reset();
      }

      @Override
      public void handleClick(InputEvent event, float x, float y) {
        TowerGroup.this.state.upgrade();
        updateCurrentState(tower);
        super.handleClick(event, x, y);
      }
    });

    // if enhance is clicked, we want to set that state on this tower
    enhanceHero.addListener(new ConfirmClickListener(this.confirmClicks) {

      @Override
      public void handleConfirm(InputEvent event, float x, float y) {
        resetOthers(TowerGroup.this.confirmClicks);
        enhanceHero.setTextureRegion(getTextureRegion(TowerMenuItem.Confirm));
        super.handleConfirm(event, x, y);
      }

      @Override
      public void reset() {
        enhanceHero.setTextureRegion(getTextureRegion(
          TowerGroup.this.state.getTextureReference(TowerMenuItem.EnhanceHero)));
        super.reset();
      }

      @Override
      public void handleClick(InputEvent event, float x, float y) {
        TowerGroup.this.state.enhanceHero();
        updateCurrentState(tower);
        super.handleClick(event, x, y);
      }
    });

    // if enhance is clicked, we want to set that state on this tower
    enhancePath.addListener(new ConfirmClickListener(this.confirmClicks) {

      @Override
      public void handleConfirm(InputEvent event, float x, float y) {
        resetOthers(TowerGroup.this.confirmClicks);
        enhancePath.setTextureRegion(getTextureRegion(TowerMenuItem.Confirm));
        super.handleConfirm(event, x, y);
      }

      @Override
      public void reset() {
        enhancePath.setTextureRegion(getTextureRegion(
          TowerGroup.this.state.getTextureReference(TowerMenuItem.EnhancePath)));
        super.reset();
      }

      @Override
      public void handleClick(InputEvent event, float x, float y) {
        TowerGroup.this.state.enhancePath();
        updateCurrentState(tower);
        super.handleClick(event, x, y);
      }
    });

    // if sell is clicked, we want to revert the tower back to the empty plot
    sell.addListener(new ConfirmClickListener(this.confirmClicks) {

      @Override
      public void handleConfirm(InputEvent event, float x, float y) {
        resetOthers(TowerGroup.this.confirmClicks);
        sell.setTextureRegion(getTextureRegion(TowerMenuItem.Confirm));
        super.handleConfirm(event, x, y);
      }

      @Override
      public void reset() {
        sell.setTextureRegion(getTextureRegion(
          TowerGroup.this.state.getTextureReference(TowerMenuItem.Sell)));
        super.reset();
      }

      @Override
      public void handleClick(InputEvent event, float x, float y) {
        TowerGroup.this.state.reset(isActive());
        updateCurrentState(tower);
        super.handleClick(event, x, y);
      }
    });

    // if set rally is clicked, we want to update the spawn point of the tower
    setRally.addListener(new ClickListener() {

      // TODO link into stage to have next click update the rally
      // see how the HeroActor works for movements
    });
  }

  private boolean isActive() {
    return ((LevelStage) getStage()).isActive();
  }

  /**
   * Updates the state of this tower group.
   *
   * @param towerActor the tower actor.
   */
  private void updateCurrentState(TextureRegionActor towerActor) {
    Tower tower = this.state.getTower();
    updateTower(tower, towerActor);
    this.units.setState(tower);
  }

  /**
   * Updates the given tower actor to the current state of this group.
   *
   * @param tower the tower representation of this group.
   * @param actor the tower actor to upgrade.
   */
  private void updateTower(Tower tower, TextureRegionActor actor) {
    actor.setTextureRegion(getTextureRegion(tower));
    updateMenuState();
  }

  /**
   * Updates the state of this group.  This will hide and show menu items based this objects current state.
   */
  private void updateMenuState() {
    deselect();
    this.menu.clearChildren();
    this.menu.addActor(this.menuItems.get(TowerMenuItem.Ring));
    for (TowerMenuItem item : this.state.getVisibleItems()) {
      // TODO: and is available
      TextureRegionActor actor = this.menuItems.get(item);
      if (actor != null) {
        this.menu.addActor(actor);
        if (item.hasCost() && actor instanceof TowerMenuActor) {
          ((TowerMenuActor) actor).setCost(item.getCost(this.state));
        }
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

  private TextureRegionActor addMenuRing(TextureRegionActor tower) {
    TextureRegion textureRegion = getTextureRegion(TowerMenuItem.Ring);
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
   * @param index the hero we are using in the hero list.
   * @param key   which tower menu item (generic version) we are associating with this hero.
   * @param ring  the tower menu ring.
   * @return the menu actor added.
   */
  private TowerMenuActor addHeroMenuItem(int index, TowerMenuItem key,
                                         TextureRegionActor ring) {
    int heroCount = this.state.activeHeroCount();
    if (heroCount == 1) {
      return index > 0 ? null : addMenuItem(ring, 90.0F, key);
    } else if (heroCount == 2) {
      return index > 1 ? null : addMenuItem(ring, 60.0F + 60.0F * (float) index, key);
    } else if (heroCount == 3) {
      return index > 2 ? null : addMenuItem(ring, 30.0F + 60.0F * (float) index, key);
    } else {
      throw new RuntimeException("unexpected hero count");
    }
  }

  /**
   * Adds a menu item to the tower menu group.
   *
   * @param ring    the tower menu ring.
   * @param degrees the degree on the ring we want to position the menu item.
   * @param item    the generic menu item we want to associate the item with.
   * @return the menu actor added.
   */
  private TowerMenuActor addMenuItem(TextureRegionActor ring, float degrees,
                                     TowerMenuItem item) {
    TextureRegion textureRegion = getTextureRegion(this.state.getTextureReference(item));
    float radius = ring.getWidth() / 2.0F;

    // circle math to figure out where this menu item belongs
    // we offset x and y by the radius of the ring
    // TODO: circle math might need minor tweaking based on width of ring (which I am not sure I can calculate dynamically)
    float circleX = (float) (radius * Math.cos(degrees * Math.PI / 180.0F)) + ring.getX() + radius;
    float circleY = (float) (radius * Math.sin(degrees * Math.PI / 180.0F)) + ring.getY() + radius;
    TowerMenuActor actor = new TowerMenuActor(
      // we offset the final results by half the image size
      circleX - textureRegion.getRegionWidth() / 2.0F,
      circleY - textureRegion.getRegionHeight() / 2.0F,
      textureRegion,
      item.hasCost() ? getTextureRegion(TowerMenuItem.CostPlaque) : null
    );
    this.menuItems.put(item, actor);
    return actor;
  }

  private TextureRegion getTextureRegion(TextureReference type) {
    return ((LevelStage) getStage()).getTextureRegion(type);
  }

  /**
   * Handles a currency change event.  In the case of this tower group that means,
   * enabling/disabling tower menu items.
   *
   * @param currency the current currency level.
   */
  public void currencyChangeEvent(int currency) {
    for (TowerMenuItem menuItem : this.state.getVisibleItems()) {
      if (menuItem.hasCost()) {
        int cost = menuItem.getCost(this.state);
        TextureRegionActor actor = this.menuItems.get(menuItem);
        if (actor != null) {
          if (cost > currency) {
            // this means we cannot afford it
            // only disable it if it is enabled
            if (actor.getTouchable() == Touchable.enabled) {
              actor.setTouchable(Touchable.disabled);
            }
          } else if (cost <= currency) {
            // this means we can afford it
            // only enable it if it is disabled
            if (actor.getTouchable() == Touchable.disabled) {
              actor.setTouchable(Touchable.enabled);
            }
          }
        }
      }
    }
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
