package com.mtrubs.td.config;

/**
 * This implementation just maintains the state for the current instance,
 * it has not connection to the device or any means of persistence.
 * <p>
 * TODO: actual implementation for android/ios devices
 */
public class InMemorySettingsManager implements SettingsManager {

  private boolean soundEnabled = true;
  private boolean musicEnabled = true;
  private boolean vibrateEnabled = true;

  @Override
  public boolean isSoundEnabled() {
    return this.soundEnabled;
  }

  @Override
  public void toggleSoundEnabled() {
    this.soundEnabled = !this.soundEnabled;
  }

  @Override
  public boolean isMusicEnabled() {
    return this.musicEnabled;
  }

  @Override
  public void toggleMusicEnabled() {
    this.musicEnabled = !this.musicEnabled;
  }

  @Override
  public boolean isVibrateEnabled() {
    return this.vibrateEnabled;
  }

  @Override
  public void toggleVibrateEnabled() {
    this.vibrateEnabled = !this.vibrateEnabled;
  }
}
