package com.mtrubs.td;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mtrubs.td.scene.TextureRegionActor;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author mrubino
 * @since 2017-05-30
 */
public class TextureRegionActorTest extends AbstractBaseTest {

  @Test
  public void createNull() {
    float x = 5.0F;
    float y = 15.0F;

    TextureRegionActor actor = new TextureRegionActor(x, y, null);
    assertFalse("Actor without texture should not be visible", actor.isVisible());
    assertEquals("X value should match", x, actor.getX(), 0.0F);
    assertEquals("Y value should match", y, actor.getY(), 0.0F);
    assertEquals("Without a texture, width should be zero",
        0.0F, actor.getWidth(), 0.0F);
    assertEquals("Without a texture, height should be zero",
        0.0F, actor.getHeight(), 0.0F);
  }

  @Test
  public void createWithTexture() {
    float x = 5.0F;
    float y = 15.0F;

    TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal("assets/images/texture.png")));
    assertNotNull("Failed to load texture", texture);
    TextureRegionActor actor = new TextureRegionActor(x, y, texture);
    assertTrue("Actor with texture should be visible", actor.isVisible());
    assertEquals("X value should match", x, actor.getX(), 0.0F);
    assertEquals("Y value should match", y, actor.getY(), 0.0F);
    assertEquals("Width should be inherited from texture",
        texture.getRegionWidth(), actor.getWidth(), 0.0F);
    assertEquals("Height should be inherited from texture",
        texture.getRegionHeight(), actor.getHeight(), 0.0F);
  }
}
