package com.mtrubs.td.scene.hero;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mtrubs.td.AbstractApplicationTest;
import com.mtrubs.td.config.UnitManager;
import com.mtrubs.td.config.WaveManager;
import com.mtrubs.td.graphics.HeroUnit;
import com.mtrubs.td.scene.LevelStage;
import com.mtrubs.td.scene.MobActor;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

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

  @Test
  public void target() {
    // a MOB that is in range and damageable
    MobActor first = mock(MobActor.class);
    when(first.isDamageable()).thenReturn(true);
    when(first.getCenter()).thenReturn(new Vector2(11.0F, 21.0F));

    // a MOB that is in range and damageable
    MobActor second = mock(MobActor.class);
    when(second.isDamageable()).thenReturn(true);
    when(second.getCenter()).thenReturn(new Vector2(12.0F, 22.0F));

    WaveManager waveManager = mock(WaveManager.class);
    when(waveManager.getActiveMobs()).thenReturn(Arrays.asList(first, second));

    LevelStage stage = mock(LevelStage.class);
    when(stage.getWaveManager()).thenReturn(waveManager);
    when(stage.getUnitManager()).thenReturn(mock(UnitManager.class));
    when(stage.getTweenManager()).thenReturn(mock(TweenManager.class));

    HeroUnit type = mock(HeroUnit.class);
    when(type.getRange()).thenReturn(15.0F);
    when(type.getAttackCoolDown()).thenReturn(5.0F);

    HeroActor actor = new HeroActor(type, mock(TextureRegion.class), 10.0F, 20.0F);
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
    verify(first, description("and the actor checks the first MOB is targetable")).isDamageable();
    verify(first, description("and the actor checks the first MOB is in range")).getCenter();
    assertTrue("then the actor has a target", actor.hasTarget());
    assertTrue("and the actor is targeting the first MOB", actor.isTargeting(first));
    verifyNoMoreInteractions(waveManager, first, second);

    reset(first);
    when(first.isDamageable()).thenReturn(false);

    // when the first mob is no longer damageable
    clearInvocations(waveManager, first, second);
    actor.act(0.5F);
    //noinspection ResultOfMethodCallIgnored
    verify(waveManager, description("then the actor checks the manager for a target")).getActiveMobs();
    verify(first, times(2).description("and the actor checks the first MOB is still valid and targetable")).isDamageable();
    verify(second, description("and the actor checks the second MOB is targetable")).isDamageable();
    verify(second, description("and the actor checks the second MOB is in range")).getCenter();
    assertTrue("then the actor has a target", actor.hasTarget());
    assertTrue("and the actor is targeting the second MOB", actor.isTargeting(second));
    verifyNoMoreInteractions(waveManager, first, second);

    reset(second);
    when(second.isDamageable()).thenReturn(true);
    when(second.getCenter()).thenReturn(new Vector2(100.0F, 200.0F));

    // when the second mob is no longer in range
    clearInvocations(waveManager, first, second);
    actor.act(0.5F);
    //noinspection ResultOfMethodCallIgnored
    verify(waveManager, description("then the actor checks the manager for a target")).getActiveMobs();
    verify(first, description("and the actor checks the first MOB is still targetable")).isDamageable();
    verify(second, times(2).description("and the actor checks the second MOB is valid and targetable")).isDamageable();
    verify(second, times(2).description("and the actor checks the second MOB is in range")).getCenter();
    assertFalse("then the actor has a target", actor.hasTarget());
    verifyNoMoreInteractions(waveManager, first, second);
  }

  private int getHitPoints(HeroActor actor) {
    return get(actor, "hitPoints", Integer.class);
  }
}
