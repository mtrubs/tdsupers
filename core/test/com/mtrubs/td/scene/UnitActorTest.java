package com.mtrubs.td.scene;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mtrubs.td.AbstractApplicationTest;
import com.mtrubs.td.config.UnitManager;
import com.mtrubs.td.config.WaveManager;
import com.mtrubs.td.graphics.TowerUnit;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UnitActorTest extends AbstractApplicationTest {

  @Test
  public void spawning() {
    UnitManager unitManager = Mockito.mock(UnitManager.class);

    LevelStage stage = Mockito.mock(LevelStage.class);
    when(stage.getUnitManager()).thenReturn(unitManager);
    when(stage.getWaveManager()).thenReturn(mock(WaveManager.class));
    when(stage.getTweenManager()).thenReturn(mock(TweenManager.class));

    TowerUnit towerUnit = mock(TowerUnit.class);
    when(towerUnit.getHealth()).thenReturn(2);
    when(towerUnit.getDeathCoolDown()).thenReturn(10.0F);

    UnitActor actor = new UnitActor(10.0F, 20.0F, towerUnit, mock(TextureRegion.class));
    set(actor, "parent", mock(Group.class));
    set(actor, "stage", stage);

    // when the actor is created
    clearInvocations(unitManager);
    assertFalse("then it is not present", actor.isVisible());
    assertEquals("and its health is set", 2, getHitPoints(actor));
    verifyNoMoreInteractions(unitManager);

    // when enough time passes that is spawns
    clearInvocations(unitManager);
    actor.act(0.5F);
    assertTrue("then it is present", actor.isVisible());
    assertEquals("and it has health", 2, getHitPoints(actor));
    verify(unitManager, description("and it is registered")).register(actor);
    verifyNoMoreInteractions(unitManager);

    // when it takes non-mortal damage
    clearInvocations(unitManager);
    actor.damage(1);
    actor.act(0.5F);
    assertEquals("then the damage is recorded", 1, getHitPoints(actor));
    assertTrue("and it is present", actor.isVisible());
    verifyNoMoreInteractions(unitManager);

    // when it takes mortal damage
    clearInvocations(unitManager);
    actor.damage(1);
    actor.act(0.5F);
    assertEquals("then the damage is recorded", 0, getHitPoints(actor));
    assertFalse("and it is not present", actor.isVisible());
    verify(unitManager, description("and it is unregistered")).unregister(actor);
    verifyNoMoreInteractions(unitManager);

    // when not enough time for a respawn passes
    clearInvocations(unitManager);
    actor.act(6.0F);
    assertFalse("then it is still not present", actor.isVisible());
    assertEquals("and has no health", 0, getHitPoints(actor));
    verifyNoMoreInteractions(unitManager);

    // when enough time passes for a respawn
    clearInvocations(unitManager);
    actor.act(6.0F);
    assertTrue("then it is present", actor.isVisible());
    assertEquals("and has health again", 2, getHitPoints(actor));
    verify(unitManager, description("and it is registered again")).register(actor);
    verifyNoMoreInteractions(unitManager);
  }

  @Test
  public void target() {
    // a MOB that is in range and damageable
    MobActor first = mock(MobActor.class);
    when(first.isVisible()).thenReturn(true);
    when(first.getCenter()).thenReturn(new Vector2(11.0F, 21.0F));

    // a MOB that is in range and damageable
    MobActor second = mock(MobActor.class);
    when(second.isVisible()).thenReturn(true);
    when(second.getCenter()).thenReturn(new Vector2(12.0F, 22.0F));

    WaveManager waveManager = mock(WaveManager.class);
    when(waveManager.getActiveMobs()).thenReturn(Arrays.asList(first, second));

    LevelStage stage = mock(LevelStage.class);
    when(stage.getWaveManager()).thenReturn(waveManager);
    when(stage.getUnitManager()).thenReturn(mock(UnitManager.class));
    when(stage.getTweenManager()).thenReturn(mock(TweenManager.class));

    TowerUnit type = mock(TowerUnit.class);
    when(type.getRange()).thenReturn(15.0F);
    when(type.getAttackCoolDown()).thenReturn(5.0F);

    UnitActor actor = new UnitActor(10.0F, 20.0F, type, mock(TextureRegion.class));
    set(actor, "stage", stage);

    // when the actor is created
    clearInvocations(waveManager, first, second);
    assertFalse("then the actor has no target", actor.hasTarget());
    verifyNoMoreInteractions(waveManager, first, second);
    reset();

    // when enough time has passes to find a target
    clearInvocations(waveManager, first, second);
    actor.act(0.5F);
    //noinspection ResultOfMethodCallIgnored
    verify(waveManager, description("then the actor checks the manager for a target")).getActiveMobs();
    verify(first, description("and the actor checks the first MOB is targetable")).isVisible();
    verify(first, description("and the actor checks the first MOB is in range")).getCenter();
    assertTrue("then the actor has a target", actor.hasTarget());
    assertTrue("and the actor is targeting the first MOB", actor.isTargeting(first));
    verifyNoMoreInteractions(waveManager, first, second);

    reset(first);
    when(first.isVisible()).thenReturn(false);

    // when the first mob is no longer damageable
    clearInvocations(waveManager, first, second);
    actor.act(0.5F);
    //noinspection ResultOfMethodCallIgnored
    verify(waveManager, description("then the actor checks the manager for a target")).getActiveMobs();
    verify(first, times(2).description("and the actor checks the first MOB is still valid and targetable")).isVisible();
    verify(second, description("and the actor checks the second MOB is targetable")).isVisible();
    verify(second, description("and the actor checks the second MOB is in range")).getCenter();
    assertTrue("then the actor has a target", actor.hasTarget());
    assertTrue("and the actor is targeting the second MOB", actor.isTargeting(second));
    verifyNoMoreInteractions(waveManager, first, second);

    reset(second);
    when(second.isVisible()).thenReturn(true);
    when(second.getCenter()).thenReturn(new Vector2(100.0F, 200.0F));

    // when the second mob is no longer in range
    clearInvocations(waveManager, first, second);
    actor.act(0.5F);
    //noinspection ResultOfMethodCallIgnored
    verify(waveManager, description("then the actor checks the manager for a target")).getActiveMobs();
    verify(first, description("and the actor checks the first MOB is still targetable")).isVisible();
    verify(second, times(2).description("and the actor checks the second MOB is valid and targetable")).isVisible();
    verify(second, times(2).description("and the actor checks the second MOB is in range")).getCenter();
    assertFalse("then the actor has a target", actor.hasTarget());
    verifyNoMoreInteractions(waveManager, first, second);
  }

  private int getHitPoints(UnitActor actor) {
    return get(actor, "hitPoints", Integer.class);
  }
}
