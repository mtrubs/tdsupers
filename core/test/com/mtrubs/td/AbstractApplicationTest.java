package com.mtrubs.td;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockito.Mockito;

import java.lang.reflect.Field;

public abstract class AbstractApplicationTest {

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

  protected <T, R> R get(T instance, String fieldName, Class<R> returnType) {
    return get(instance, instance.getClass(), fieldName, returnType);
  }

  private <T, R> R get(T instance, Class<?> type, String fieldName, Class<R> returnType) {
    try {
      Field field = type.getDeclaredField(fieldName);
      field.setAccessible(true);
      Object object = field.get(instance);
      return returnType.cast(object);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (NoSuchFieldException e) {
      Class<?> parent = type.getSuperclass();
      if (parent == null) {
        throw new RuntimeException(e);
      } else {
        return get(instance, parent, fieldName, returnType);
      }
    }
  }

  protected <T> void set(T instance, String fieldName, Object value) {
    set(instance, instance.getClass(), fieldName, value);
  }

  protected <T> void set(T instance, Class<?> type, String fieldName, Object value) {
    try {
      Field field = type.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(instance, value);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (NoSuchFieldException e) {
      Class<?> parent = type.getSuperclass();
      if (parent == null) {
        throw new RuntimeException(e);
      } else {
        set(instance, parent, fieldName, value);
      }
    }
  }
}
