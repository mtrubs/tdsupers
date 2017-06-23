package com.mtrubs.td.scene;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mtrubs.td.AbstractApplicationTest;
import com.mtrubs.td.config.UnitManager;
import com.mtrubs.td.config.WaveManager;
import com.mtrubs.td.graphics.TowerUnit;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UnitActorTest extends AbstractApplicationTest {

  @Test
  public void spawnRespawn() {
    UnitManager unitManager = Mockito.mock(UnitManager.class);

    LevelStage stage = Mockito.mock(LevelStage.class);
    when(stage.getUnitManager()).thenReturn(unitManager);
    when(stage.getWaveManager()).thenReturn(mock(WaveManager.class));
    when(stage.getTweenManager()).thenReturn(mock(TweenManager.class));

    TowerUnit towerUnit = mock(TowerUnit.class);
    when(towerUnit.getHealth()).thenReturn(2);
    when(towerUnit.getDeathCoolDown()).thenReturn(10.0F);

    UnitActor actor = new UnitActor(10.0F, 20.0F, towerUnit, mock(TextureRegion.class));
    invoke(actor, Actor.class, "setStage", stage, Stage.class);

    // when the actor is create it is invisible
    assertFalse(actor.isVisible());
    assertEquals(2, (int) get(actor, "hitPoints", Integer.class));
    verifyNoMoreInteractions(unitManager);

    // after a short time it spawn, registering itself
    actor.act(0.5F);
    assertTrue(actor.isVisible());
    assertEquals(2, (int) get(actor, "hitPoints", Integer.class));
    verify(unitManager, times(1)).register(actor);
    verifyNoMoreInteractions(unitManager);

    // it takes some damage, but is still alive
    actor.damage(1);
    actor.act(0.5F);
    assertTrue(actor.isVisible());
    assertEquals(1, (int) get(actor, "hitPoints", Integer.class));
    verifyNoMoreInteractions(unitManager);

    // it takes enough damage to kill it, so it unregisters itself
    actor.damage(1);
    actor.act(0.5F);
    assertFalse(actor.isVisible());
    assertEquals(0, (int) get(actor, "hitPoints", Integer.class));
    verify(unitManager, times(1)).unregister(actor);
    verifyNoMoreInteractions(unitManager);

    // some time passes, it is still dead
    actor.act(6.0F);
    assertFalse(actor.isVisible());
    assertEquals(0, (int) get(actor, "hitPoints", Integer.class));
    verifyNoMoreInteractions(unitManager);

    // enough time passes for respawn so it comes back with full health, registering itself again
    actor.act(6.0F);
    assertTrue(actor.isVisible());
    // assertEquals(2, (int) get(actor, "hitPoints", Integer.class)); // FIXME: reset health
    verify(unitManager, times(2)).register(actor);
    verifyNoMoreInteractions(unitManager);
  }
}
