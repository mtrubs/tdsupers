package com.mtrubs.td;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockito.Mockito;

/**
 * @author mrubino
 * @since 2017-05-30
 */
public abstract class AbstractBaseTest {

  private static Application application;

  @BeforeClass
  public static void init() {
    // Note that we don't need to implement any of the listener's methods as it is headless
    application = new HeadlessApplication(new ApplicationListener() {
      @Override
      public void create() {
      }

      @Override
      public void resize(int width, int height) {
      }

      @Override
      public void render() {
      }

      @Override
      public void pause() {
      }

      @Override
      public void resume() {
      }

      @Override
      public void dispose() {
      }
    });

    // Use Mockito to mock the OpenGL methods since we are running headless
    Gdx.gl20 = Mockito.mock(GL20.class);
    Gdx.gl = Gdx.gl20;
  }

  @AfterClass
  public static void cleanUp() {
    // Exit the application first
    application.exit();
    application = null;
  }
}
