package com.mtrubs.td.scene.hero;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mtrubs.td.AbstractApplicationTest;
import com.mtrubs.td.config.UnitManager;
import com.mtrubs.td.config.WaveManager;
import com.mtrubs.td.graphics.HeroUnit;
import com.mtrubs.td.scene.LevelStage;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HeroActorTest extends AbstractApplicationTest {

  @Test
  public void spawnRespawn() {
    UnitManager unitManager = Mockito.mock(UnitManager.class);

    LevelStage stage = Mockito.mock(LevelStage.class);
    when(stage.getUnitManager()).thenReturn(unitManager);
    when(stage.getWaveManager()).thenReturn(Mockito.mock(WaveManager.class));

    HeroUnit heroUnit = mock(HeroUnit.class);
    when(heroUnit.getHealth()).thenReturn(2);
    when(heroUnit.getDeathCoolDown()).thenReturn(10.0F);

    HeroActor actor = new HeroActor(heroUnit, mock(TextureRegion.class), 10.0F, 20.0F);
    set(actor, "parent", mock(Group.class));
    set(actor, "stage", stage);

    // when the actor is created
    assertFalse("then it is not present", actor.isVisible());
    assertEquals("and it has health", 2, getHitPoints(actor));
    verifyNoMoreInteractions(unitManager);

    // when enough time has passed to spawn
    actor.act(0.5F);
    assertTrue("then it is present", actor.isVisible());
    assertEquals("and it has health", 2, getHitPoints(actor));
    verify(unitManager, description("and it registers itself")).register(actor);
    verifyNoMoreInteractions(unitManager);

    // when it takes non-mortal damage
    actor.damage(1);
    actor.act(0.5F);
    assertEquals("then the damage is recorded", 1, getHitPoints(actor));
    assertTrue("and it is still present", actor.isVisible());
    verifyNoMoreInteractions(unitManager);

    // when it takes mortal damage
    actor.damage(1);
    actor.act(0.5F);
    assertEquals("then the damage is recorded", 0, getHitPoints(actor));
    assertFalse("and it is no longer present", actor.isVisible());
    verify(unitManager, description("and it unregisters itself")).unregister(actor);
    // TODO: assert waveManager.clearTarget
    verifyNoMoreInteractions(unitManager);

    // when not enough time for a respawn passes
    actor.act(6.0F);
    assertFalse("then it is still not present", actor.isVisible());
    assertEquals("and has not health", 0, getHitPoints(actor));
    verifyNoMoreInteractions(unitManager);

    // when enough time passes for a respawn
    actor.act(6.0F);
    assertTrue("then it is present", actor.isVisible());
    assertEquals("and has health again", 2, getHitPoints(actor));
    verify(unitManager, times(2).description("and it is registered again")).register(actor);
    verifyNoMoreInteractions(unitManager);
  }

  private int getHitPoints(HeroActor actor) {
    return get(actor, "hitPoints", Integer.class);
  }
}
