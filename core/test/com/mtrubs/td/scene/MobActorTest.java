package com.mtrubs.td.scene;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mtrubs.td.AbstractApplicationTest;
import com.mtrubs.td.config.CurrencyManager;
import com.mtrubs.td.config.UnitManager;
import com.mtrubs.td.config.WaveManager;
import com.mtrubs.td.graphics.level.Mob;
import com.mtrubs.td.scene.level.mob.MobActor;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class MobActorTest extends AbstractApplicationTest {

  @Test
  public void spawn() {
    CurrencyManager currencyManager = mock(CurrencyManager.class);

    WaveManager waveManager = mock(WaveManager.class);

    com.mtrubs.td.scene.level.LevelStage stage = Mockito.mock(com.mtrubs.td.scene.level.LevelStage.class);
    when(stage.getUnitManager()).thenReturn(mock(UnitManager.class));
    when(stage.getCurrencyManager()).thenReturn(currencyManager);
    when(stage.getWaveManager()).thenReturn(waveManager);

    Group parent = mock(Group.class);

    Mob mob = mock(Mob.class);
    when(mob.getHealth()).thenReturn(2);
    when(mob.getWorth()).thenReturn(50);

    MobActor actor = new MobActor(new Vector2[]{new Vector2(10.0F, 20.0F)}, mob,
      1.0F, 0.0F, mock(TextureRegion.class));
    set(actor, "parent", parent);
    set(actor, "stage", stage);

    // when the actor is created
    assertTrue("then it is present", actor.isVisible());
    assertEquals("and it has health", 2, getHitPoints(actor));
    verifyNoMoreInteractions(currencyManager, waveManager);

    // when it takes non-mortal damage
    actor.damage(1);
    actor.act(0.5F);
    assertEquals("then the damage is recorded", 1, getHitPoints(actor));
    assertTrue("and it is still present", actor.isVisible());
    verifyNoMoreInteractions(currencyManager, waveManager);

    // when it takes mortal damage
    actor.damage(1);
    actor.act(0.5F);
    assertEquals("then the damage is recorded", 0, getHitPoints(actor));
    verify(currencyManager, description("and credit is given")).add(50);
    verify(waveManager, description("and it unregisters itself")).remove(actor);
    verify(parent, description("and it is no longer present")).removeActor(actor, true);
    verifyNoMoreInteractions(currencyManager, waveManager, parent);
  }

  private int getHitPoints(MobActor actor) {
    return get(actor, "hitPoints", Integer.class);
  }
}
